package org.kiwitcms.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestCaseRun;
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
    public static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    public static final String RUN_FILTER = "TestRun.filter";
    public static final String ADD_TC_TO_PLAN_METHOD = "TestPlan.add_case";
    public static final String CREATE_PLAN_METHOD = "TestPlan.create";
    public static final String TEST_PLAN_FILTER = "TestPlan.filter";
    public static final String TEST_CASE_RUN_FILTER = "TestCaseRun.filter";
    public static final String CREATE_TC_RUN_METHOD = "TestCaseRun.create";
    public static final String UPDATE_TC_RUN_METHOD = "TestCaseRun.update";
    public static final String TEST_CASE_STATUS_FILTER = "TestCaseStatus.filter";

    TestCase createNewTC(int categoryId, int priorityId, int caseStatusId, String summary) {
        Map<String, Object> params = new HashMap<>();
        params.put("category", categoryId);
        params.put("summary", summary);
        params.put("is_automated", "true");
        params.put("priority", priorityId);
        params.put("case_status", caseStatusId);

        JSONObject json = (JSONObject) callPosParamService(CREATE_TC_METHOD, Arrays.asList((Object) params));
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
        JSONObject json = (JSONObject) callPosParamService(CREATE_RUN_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    TestCase[] getRunIdTestCases(int runId) {
        JSONArray jsonArray = (JSONArray) callPosParamService(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TestCase[0];
        }
    }

    TestCaseRun addTestCaseToRunId(int runId, int caseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("run_id", runId);
        params.put("case_id", caseId);

        JSONObject json = (JSONObject) callNameParamService(ADD_TC_TO_RUN_METHOD, params);
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestCaseRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    boolean planExists(int planId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", planId);
        JSONArray jsonArray = (JSONArray) callPosParamService(TEST_PLAN_FILTER, Arrays.asList((Object) filter));
        return !jsonArray.isEmpty();
    }


    TestRun getRun(int runId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("run_id", runId);

        JSONArray jsonArray = (JSONArray) callPosParamService(RUN_FILTER, Arrays.asList((Object) filter));
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
        // TODO: move this in the constructor
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Unit");  // todo: this will need to change for TestNG
        JSONArray jsonArray = (JSONArray) callPosParamService("PlanType.filter", Arrays.asList((Object) params));
        Object type = ((JSONObject) jsonArray.get(0)).get("id");

        params = new HashMap<>();
        params.put("product", productId);
        params.put("type", type);
        params.put("default_product_version", 0);
        params.put("product_version", versionId);
        params.put("text", "WIP");
        params.put("is_active", true);
        params.put("name", name);
        JSONObject json = (JSONObject) callPosParamService(CREATE_PLAN_METHOD, Arrays.asList((Object) params));
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

        callNameParamService(ADD_TC_TO_PLAN_METHOD, params);
    }

    TestCaseRun getTestCaseRun(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) callPosParamService(TEST_CASE_RUN_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return null;
        } else {
            System.out.println(jsonArray.toJSONString());
            try {
                TestCaseRun[] tcRun = new ObjectMapper().readValue(jsonArray.toJSONString(), TestCaseRun[].class);
                return tcRun[0];
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    TestCaseRun createTestCaseRun(int runId, int caseId, int build, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("run", runId);
        params.put("case", caseId);
        params.put("build", build);
        params.put("status", status);

        JSONObject json = (JSONObject) callPosParamService(CREATE_TC_RUN_METHOD, Arrays.asList((Object) params));
        try {
            System.out.println(json.toJSONString());
            return new ObjectMapper().readValue(json.toJSONString(), TestCaseRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    TestCaseRun updateTestCaseRun(int tcRunId, int status) {
        Map<String, Object> values = new HashMap<>();
        values.put("status", status);

        Map<String, Object> params = new HashMap<>();
        params.put("case_run_id", tcRunId);
        params.put("values", values);
        JSONObject json = (JSONObject) callNameParamService(UPDATE_TC_RUN_METHOD, params);
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestCaseRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // TODO: Create TestCaseStatus class
    JSONArray getTestCaseStatus(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) callPosParamService(TEST_CASE_STATUS_FILTER, Arrays.asList((Object) filter));
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
