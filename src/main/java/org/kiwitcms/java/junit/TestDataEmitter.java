package org.kiwitcms.java.junit;

import org.kiwitcms.java.api.KiwiJsonRpcClient;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestCaseRun;
import org.kiwitcms.java.model.TestMethod;
import org.kiwitcms.java.model.TestRun;

import java.time.LocalDate;
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
        if (productId == null) {
            productId = client.getProductId(config.getProduct());
            return productId;
        } else {
            return productId;
        }
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
            planId = client.createNewTP(getProductId(), String.format("Auto Test Plan [%tF]", LocalDate.now()), 1).getId();

        }
        return planId;
    }

    public int getTestRunId() {
        if (runId == null) {
            Integer testRun = config.getKiwiRunId();
            if (testRun != null && client.getRun(testRun) != null) {
                runId = testRun;
            } else {
                runId = client.createNewRun(1, Config.getInstance().getKiwiUsername(),
                        getPlanId(), String.format("Auto Test Run [%tF]", LocalDate.now())).getId();
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
                    client.createTestCaseRun(runId, matchingCaseId, 1, test.getKiwiStatus());
                }
            } else {
                TestCase addition = client.createNewTC(76, getProductId(), test.getKiwiSummary());
                client.addTestCaseToPlan(getPlanId(), addition.getCaseId());
                TestCaseRun tcr = client.addTestCaseToRunId(runId, addition.getCaseId());
                client.updateTestCaseRun(tcr.getTcRunId(), test.getKiwiStatus());
            }
        }


    }
}
