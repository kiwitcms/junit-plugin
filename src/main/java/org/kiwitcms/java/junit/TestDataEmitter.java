// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.junit;

import org.kiwitcms.java.api.KiwiJsonRpcClient;
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
        String product_name = config.getProduct();

        if (productId == null) {
            productId = client.getProductId(product_name);
        }

        if (productId == null) {
            productId = client.createNewProduct(product_name).getId();
        }

        return productId;
    }

    public void emitNewTestCase(String summary) {
        client.createNewTC(76, getProductId(), summary);
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
                }
            }
            int productId = getProductId();
            int versionId = getVersion(productId);
            String name = String.format("[JUnit] Plan for %s (%s)",
                                        config.getProduct(),
                                        config.getProductVersion());
            planId = client.createNewTP(productId, name, 1, versionId).getId();

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
        for (TestMethod test : tests) {
            Integer matchingCaseId = TestCase.nameExists(test.getKiwiSummary(), existingTests);
            if (matchingCaseId != null) {
                Map<String, Object> filter = new HashMap<>();
                filter.put("case_id", matchingCaseId);
                filter.put("run_id", runId);
                TestCaseRun testCaseRun = client.getTestCaseRun(filter);
                if (testCaseRun != null) {
                    client.updateTestCaseRun(testCaseRun.getTcRunId(), test.getKiwiStatus());
                } else {
                    int buildId = getBuild(getProductId());
                    client.createTestCaseRun(runId, matchingCaseId, buildId, test.getKiwiStatus());
                }
            } else {
                //TODO: get category via filter
                TestCase addition = client.createNewTC(475, getProductId(), test.getKiwiSummary());
                client.addTestCaseToPlan(getPlanId(), addition.getCaseId());
                TestCaseRun tcr = client.addTestCaseToRunId(runId, addition.getCaseId());
                client.updateTestCaseRun(tcr.getTcRunId(), test.getKiwiStatus());
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
}
