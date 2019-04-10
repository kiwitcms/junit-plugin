// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.junit;

import net.minidev.json.JSONObject;
import org.kiwitcms.java.api.KiwiJsonRpcClient;
import org.kiwitcms.java.api.KiwiProductJsonRpcClient;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.*;

import java.util.*;

public class TestDataEmitter {

    private Integer productId;
    private Integer planId;
    private Integer runId;

    private KiwiJsonRpcClient client;
    Config config;

    public TestDataEmitter() {
        config = Config.getInstance();
        client = new KiwiJsonRpcClient();
        client.login();
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
            Integer confRunId = config.getKiwiRunId();
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
            Integer testRun = config.getKiwiRunId();
            if (testRun != null && client.getRun(testRun) != null) {
                runId = testRun;
            } else {
                runId = client.createNewRun(getBuild(getProductId()),
                        config.getKiwiUsername(),
                        getPlanId(),
                        String.format("[JUnit] Results for %s, %s, %s",
                                config.getProduct(),
                                config.getProductVersion(),
                                config.getKiwiBuild())).getId();
            }
        }
        return runId;
    }

    public void addTestResultsToRun(int runId, List<TestMethod> tests) {
        TestCase[] existingTests = client.getRunIdTestCases(runId);
        if (existingTests.length == 0){
            existingTests = client.getPlanIdTestCases(getPlanId());
        }
        for (TestMethod test : tests) {
            Integer matchingCaseId = TestCase.nameExists(test.getKiwiSummary(), existingTests);
            if (matchingCaseId != null) {
                Map<String, Object> filter = new HashMap<>();
                filter.put("case_id", matchingCaseId);
                filter.put("run_id", runId);
                TestExecution testExecution = client.getTestExecution(filter);
                if (testExecution != null) {
                    client.updateTestExecution(testExecution.getTcRunId(), test.getTestExecutionStatus());
                } else {
                    int buildId = getBuild(getProductId());
                    client.createTestExecution(runId, matchingCaseId, buildId, test.getTestExecutionStatus());
                }
            } else {
                TestCase addition = client.createNewConfirmedTC(getProductId(), getAvailableCategoryId(getProductId()),
                        getAvailablePriorityId(), test.getKiwiSummary());
                client.addTestCaseToPlan(getPlanId(), addition.getCaseId());
                TestExecution tcr = client.addTestCaseToRunId(runId, addition.getCaseId());
                client.updateTestExecution(tcr.getTcRunId(), test.getTestExecutionStatus());
            }
        }
    }

    public int getBuild(int productId) {
        String confBuild = config.getKiwiBuild();
        Map<String, Object> filter = new HashMap<>();
        filter.put("product", productId);
        if (confBuild != null) {
            filter.put("name", confBuild);
            Build[] existingBuilds = client.getBuilds(filter);
            if (existingBuilds.length > 0) {
                return existingBuilds[0].getId();
            } else {
                Build newBuild = client.createBuild(confBuild, productId);
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
