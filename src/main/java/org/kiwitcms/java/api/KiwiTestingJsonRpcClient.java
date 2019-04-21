package org.kiwitcms.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestExecution;
import org.kiwitcms.java.model.TestPlan;
import org.kiwitcms.java.model.TestRun;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KiwiTestingJsonRpcClient extends BaseRpcClient {
    public static final String GET_RUN_TCS_METHOD = "TestRun.get_cases";
    public static final String CREATE_RUN_METHOD = "TestRun.create";
    public static final String CREATE_TC_METHOD = "TestCase.create";
    public static final String TEST_CASE_FILTER = "TestCase.filter";
    public static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    public static final String RUN_FILTER = "TestRun.filter";
    public static final String ADD_TC_TO_PLAN_METHOD = "TestPlan.add_case";
    public static final String CREATE_PLAN_METHOD = "TestPlan.create";
    public static final String TEST_PLAN_FILTER = "TestPlan.filter";
    public static final String TEST_EXECUTION_FILTER = "TestExecution.filter";
    public static final String TEST_EXECUTION_CREATE = "TestExecution.create";
    public static final String TEST_EXECUTION_UPDATE = "TestExecution.update";
    public static final String TEST_CASE_STATUS_FILTER = "TestCaseStatus.filter";

    TestCase createNewTC(int productId, int categoryId, int priorityId, int caseStatusId, String summary) {
        Map<String, Object> params = new HashMap<>();
        params.put("product", productId);
        params.put("category", categoryId);
        params.put("summary", summary);
        params.put("is_automated", "true");
        params.put("priority", priorityId);
        params.put("case_status", caseStatusId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_TC_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestCase.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    TestRun createNewRun(int build, String manager, int plan, String summary) {
        Map<String, Object> params = new HashMap<>();
        params.put("build", build);
        params.put("manager", manager);
        params.put("plan", plan);
        params.put("summary", summary);
        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_RUN_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    TestCase[] getRunIdTestCases(int runId) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TestCase[0];
        }
    }

    TestCase[] getPlanIdTestCases(int planId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("plan", planId);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_FILTER, Arrays.asList((Object)filter));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TestCase[0];
        }
    }

    TestExecution addTestCaseToRunId(int runId, int caseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("run_id", runId);
        params.put("case_id", caseId);

        JSONObject json = (JSONObject) executeViaNamedParams(ADD_TC_TO_RUN_METHOD, params);
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestExecution.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean planExists(int planId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", planId);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_PLAN_FILTER, Arrays.asList((Object) filter));
        return !jsonArray.isEmpty();
    }

    int getTestPlanId(String name, int productId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        filter.put("product", productId);

        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_PLAN_FILTER, Arrays.asList((Object) filter));
        if (jsonArray == null || jsonArray.isEmpty()) {
            return -1;
        } else {
            try {
                TestPlan[] plans = new ObjectMapper().readValue(jsonArray.toJSONString(), TestPlan[].class);
                return plans[0].getId();
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
    }


    TestRun getRun(int runId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("run_id", runId);

        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(RUN_FILTER, Arrays.asList((Object) filter));
        if (jsonArray == null || jsonArray.isEmpty()) {
            return null;
        } else {
            try {
                TestRun[] run = new ObjectMapper().readValue(jsonArray.toJSONString(), TestRun[].class);
                return run[0];
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    TestPlan createNewTP(int productId, String name, int versionId) {
        // TODO: remove call for plan type
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Unit");
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("PlanType.filter", Arrays.asList((Object) params));
        Object type = ((JSONObject) jsonArray.get(0)).get("id");

        params = new HashMap<>();
        params.put("product", productId);
        params.put("type", type);
        params.put("default_product_version", 0);
        params.put("product_version", versionId);
        params.put("text", "WIP");
        params.put("is_active", true);
        params.put("name", name);
        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_PLAN_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestPlan.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void addTestCaseToPlan(int planId, int caseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("plan_id", planId);
        params.put("case_id", caseId);

        executeViaNamedParams(ADD_TC_TO_PLAN_METHOD, params);
    }

    TestExecution getTestExecution(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_EXECUTION_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return null;
        } else {
            try {
                TestExecution[] tcRun = new ObjectMapper().readValue(jsonArray.toJSONString(), TestExecution[].class);
                return tcRun[0];
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    TestExecution createTestExecution(int runId, int caseId, int build, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("run", runId);
        params.put("case", caseId);
        params.put("build", build);
        params.put("status", status);

        JSONObject json = (JSONObject) executeViaPositionalParams(TEST_EXECUTION_CREATE, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestExecution.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    TestExecution updateTestExecution(int tcRunId, int status) {
        Map<String, Object> values = new HashMap<>();
        values.put("status", status);

        Map<String, Object> params = new HashMap<>();
        params.put("case_run_id", tcRunId);
        params.put("values", values);
        JSONObject json = (JSONObject) executeViaNamedParams(TEST_EXECUTION_UPDATE, params);
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestExecution.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO: Create TestCaseStatus class
    JSONArray getTestCaseStatus(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_STATUS_FILTER, Arrays.asList((Object) filter));
        return jsonArray;
    }

    //Get first available
    int getConfirmedTCStatusId() {
        Map<String, Object> confirmed_params = new HashMap<>();
        confirmed_params.put("name", "CONFIRMED");
        Object id = ((JSONObject) getTestCaseStatus(confirmed_params).get(0)).get("id");
        return Integer.parseInt(String.valueOf(id));
    }
}
