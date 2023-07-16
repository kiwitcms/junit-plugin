// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2022 Alexander Todorov <atodorov@otb.bg>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.junit;

import net.minidev.json.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.kiwitcms.java.api.RpcClient;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.*;

import java.util.*;

public class TestDataEmitter {

    private static Integer productId;
    private static Integer planId;
    private static Integer runId;

    private RpcClient client;
    private TestCase[] casesInTestRun;
    Config config;

    public TestDataEmitter() {
        config = Config.getInstance();
        client = new RpcClient();
        client.login(config.getUsername(), config.getPassword());
    }

    public int getProductId() {
        String productName = config.getProduct();

        if (productId == null) {
            productId = client.getProductId(productName);
        }

        if (productId == null) {
            productId = client.createNewProduct(productName).getId();
        }

        return productId;
    }

    public void closeSession() {
        client.logout();
    }

    public int getPlanId() {
        if (planId == null) {
            Integer confRunId = config.getRunId();
            if (confRunId != null) {
                TestRun run = client.getRun(confRunId);
                if (run != null) {
                    planId = run.getPlan();
                    return planId;
                }
            }
            int productId = getProductId();
            int versionId = getVersion(productId);
            String name = String.format("[JUnit] Plan for %s (%s)",
                    config.getProduct(),
                    config.getProductVersion());
            // check for name duplication
            planId = client.getTestPlanId(name, productId);
            if (planId < 0) {
                planId = client.createNewTP(productId, name, versionId).getId();
            }
        }
        return planId;
    }

    public int getTestRunId() {
        if (runId == null) {
            Integer testRun = config.getRunId();
            if (testRun != null) {
                runId = client.getRun(testRun).getId();
            }

//TODO: this must check if this TR is already available in the DB
// and not create a new TR for the same product, version and build
            if (runId == null) {
                runId = client.createNewRun(getBuild(getVersion(getProductId())),
                        config.getUsername(),
                        getPlanId(),
                        String.format("[JUnit] Results for %s, %s, %s",
                                config.getProduct(),
                                config.getProductVersion(),
                                config.getBuild())).getId();
            }
        }
        casesInTestRun = client.getRunIdTestCases(runId);
        return runId;
    }

    public void addTestResultsToRun(int runId, List<TestMethod> tests) {
        int productId  = getProductId();
        int categoryId = getAvailableCategoryId(productId);
        int priorityId = getAvailablePriorityId();
        int testPlanId = getPlanId();

        for (TestMethod test : tests) {
            TestCase testCase = null;
            if (test.id != 0) {
                testCase = client.getTestCaseById(test.id);
            }
            if (ObjectUtils.isEmpty(testCase)) {
                testCase = client.getOrCreateTestCase(productId, categoryId, priorityId, test.getSummary());
            }
            client.addTestCaseToPlan(testPlanId, testCase.getCaseId());
    
            TestExecution[] executions = client.addTestCaseToRunId(runId, testCase.getCaseId());
            for (TestExecution testExecution : executions) {
                client.updateTestExecution(testExecution.getTcRunId(), getTestExecutionStatusId(test.result));
            }
        }
    }

    // TODO: this method is called for every execution.
    // Results can be cached
    private int getTestExecutionStatusId(String result) {
        TestExecutionStatus status = null;

        switch (result) {
            case "PASS":
                status = client.getTestExecutionStatus("PASSED", "__gt");
                break;
            case "FAIL":
                status = client.getTestExecutionStatus("FAILED", "__lt");
                break;
            default:
                //IDLE
                status = client.getTestExecutionStatus(result, "");
        }

        return status.getId();
    }

    public int getBuild(int versionId) {
        String confBuild = config.getBuild();
        Map<String, Object> filter = new HashMap<>();
        filter.put("version", versionId);
        if (confBuild != null) {
            filter.put("name", confBuild);
            Build[] existingBuilds = client.getBuilds(filter);
            if (existingBuilds.length > 0) {
                return existingBuilds[0].getId();
            } else {
                Build newBuild = client.createBuild(confBuild, versionId);
                if (newBuild != null) {
                    return newBuild.getId();
                }
            }
        }
        return client.getBuilds(filter)[0].getId();
    }

    public int getVersion(int productId) {
        String confVersion = config.getProductVersion();
        Map<String, Object> filter = new HashMap<>();
        filter.put("product", productId);
        if (confVersion != null) {
            filter.put("value", confVersion);
            Version[] existingVersions = client.getVersions(filter);
            if (existingVersions.length > 0) {
                return existingVersions[0].getId();
            } else {
                Version newVersion = client.createProductVersion(confVersion, productId);
                if (newVersion != null) {
                    return newVersion.getId();
                }
            }
        }
        return client.getVersions(filter)[0].getId();
    }

    public int getAvailablePriorityId(){
        Map<String, Object> filter = new HashMap<>();
        filter.put("is_active", "True");
        Priority[] existingPriorities = client.getPriority(filter);
        return existingPriorities[0].getId();
    }

    public int getAvailableCategoryId(int productId){
        Map<String, Object> filter = new HashMap<>();
        filter.put("product", productId);
        Object id = ((JSONObject) client.getCategory(filter).get(0)).get("id");
        return Integer.parseInt(String.valueOf(id));
    }
}
