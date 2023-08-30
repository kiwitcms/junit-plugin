// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2019-2022 Alexander Todorov <atodorov@MrSenko.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import org.kiwitcms.java.model.*;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;

import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Map;


public class RpcClient extends BaseRpcClient {

    public static final String LOGIN_METHOD = "Auth.login";
    public static final String LOGOUT_METHOD = "Auth.logout";

    private static final String PRODUCT_FILTER = "Product.filter";
    private static final String CREATE_PRODUCT_METHOD = "Product.create";
    private static final String BUILD_FILTER = "Build.filter";
    private static final String CREATE_BUILD_METHOD = "Build.create";
    private static final String CREATE_VERSION_METHOD = "Version.create";
    private static final String VERSION_FILTER = "Version.filter";
    private static final String PRIORITY_FILTER = "Priority.filter";
    private static final String CATEGORY_FILTER = "Category.filter";

    private static final String GET_RUN_TCS_METHOD = "TestRun.get_cases";
    private static final String CREATE_RUN_METHOD = "TestRun.create";
    public static final String CREATE_TC_METHOD = "TestCase.create";
    public static final String TEST_CASE_FILTER = "TestCase.filter";
    private static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    private static final String RUN_FILTER = "TestRun.filter";
    private static final String ADD_TC_TO_PLAN_METHOD = "TestPlan.add_case";
    private static final String CREATE_PLAN_METHOD = "TestPlan.create";
    private static final String TEST_PLAN_FILTER = "TestPlan.filter";
    private static final String TEST_EXECUTION_FILTER = "TestExecution.filter";
    private static final String TEST_EXECUTION_UPDATE = "TestExecution.update";
    public static final String TEST_CASE_STATUS_FILTER = "TestCaseStatus.filter";


    public String login(String username, String password) {
        sessionId = (String) executeViaPositionalParams(LOGIN_METHOD, Arrays.asList(username, password));
        return sessionId;
    }

    public void logout() {
        JSONRPC2Session mySession = prepareSession();

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(LOGOUT_METHOD, requestID);

        // Send request
        try {
            getResponse(mySession.send(request));
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
        }
    }

    public TestCase createTestCase(int categoryId, int priorityId, int caseStatusId, String summary) {
        Map<String, Object> params = new HashMap<>();
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

    //TODO: test coverage
    public TestCase getOrCreateTestCase(int productId, int categoryId, int priorityId, String summary) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("summary", summary);
        filter.put("category__product", productId);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_FILTER, Arrays.asList((Object)filter));
        if (jsonArray == null || jsonArray.isEmpty()) {
            int caseStatusId = getConfirmedTCStatusId();
            return createTestCase(categoryId, priorityId, caseStatusId, summary);
        }

        try {
            TestCase[] testCases = new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
            return testCases[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: test coverage
    public TestRun createNewRun(int build, String manager, int plan, String summary) {
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

    //TODO: test coverage
    public TestCase[] getRunIdTestCases(int runId) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TestCase[0];
        }
    }

    //TODO: test coverage
    public TestExecution[] addTestCaseToRunId(int runId, int caseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("run_id", runId);
        params.put("case_id", caseId);

        Object response = executeViaNamedParams(ADD_TC_TO_RUN_METHOD, params);
        try {
            if (response instanceof JSONObject) {
                // Kiwi TCMS v10.5 or earlier
                TestExecution execution = new ObjectMapper().readValue(((JSONObject)response).toJSONString(), TestExecution.class);
                TestExecution[] executions = {execution};
                return executions;
            } else {
                // Kiwi TCMS v11.0 or later
                return new ObjectMapper().readValue(((JSONArray)response).toJSONString(), TestExecution[].class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean planExists(int planId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", planId);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_PLAN_FILTER, Arrays.asList((Object) filter));
        return !jsonArray.isEmpty();
    }

    //TODO: test coverage
    public int getTestPlanId(String name, int productId) {
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

    public TestRun getRun(int runId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", runId);

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

    //TODO: test coverage
    public TestPlan createNewTP(int productId, String name, int versionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Unit");
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("PlanType.filter", Arrays.asList((Object) params));
        Object type = ((JSONObject) jsonArray.get(0)).get("id");

        params = new HashMap<>();
        params.put("product", productId);
        params.put("type", type);
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

    //TODO: test coverage
    public void addTestCaseToPlan(int planId, int caseId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", caseId);
        filter.put("plan", planId);

        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_FILTER, Arrays.asList((Object)filter));
        if (jsonArray == null || jsonArray.isEmpty()) {
            executeViaPositionalParams(ADD_TC_TO_PLAN_METHOD, Arrays.asList(planId, caseId));
        }
    }

    public TestExecution getTestExecution(Map<String, Object> filter) {
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

    //TODO: test coverage
    public TestExecution updateTestExecution(int tcRunId, int status) {
        Map<String, Object> values = new HashMap<>();
        values.put("status", status);

        Map<String, Object> params = new HashMap<>();
        params.put("execution_id", tcRunId);
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
    //TODO: test coverage
    public JSONArray getTestCaseStatus(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_STATUS_FILTER, Arrays.asList((Object) filter));
        return jsonArray;
    }

    //TODO: test coverage
    //Get first available
    public int getConfirmedTCStatusId() {
        Map<String, Object> confirmed_params = new HashMap<>();
        confirmed_params.put("name", "CONFIRMED");
        Object id = ((JSONObject) getTestCaseStatus(confirmed_params).get(0)).get("id");
        return Integer.parseInt(String.valueOf(id));
    }

    //TODO: test coverage
    public Integer getProductId(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(PRODUCT_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return null;
        } else {
            try {
                Product[] product = new ObjectMapper().readValue(jsonArray.toJSONString(), Product[].class);
                return product[0].getId();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    //TODO: test coverage
    public Product createNewProduct(String name) {
        Map<String, Object> params = new HashMap<>();

        // TODO: move classification filtering in emitter
        // get the first possible classification
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("Classification.filter", Arrays.asList((Object) params));
        Object classificationId = ((JSONObject) jsonArray.get(0)).get("id");

        params.put("name", name);
        params.put("classification", classificationId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_PRODUCT_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Product.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: test coverage
    public Build[] getBuilds(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(BUILD_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Build[0];
        } else {
            try {
                Build[] builds = new ObjectMapper().readValue(jsonArray.toJSONString(), Build[].class);
                return builds;
            } catch (IOException e) {
                e.printStackTrace();
                return new Build[0];
            }
        }
    }

    //TODO: test coverage
    public Build createBuild(String name, int versionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("version", versionId);
        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_BUILD_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Build.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: test coverage
    public Version[] getVersions(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(VERSION_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Version[0];
        } else {
            try {
                Version[] versions = new ObjectMapper().readValue(jsonArray.toJSONString(), Version[].class);
                return versions;
            } catch (IOException e) {
                e.printStackTrace();
                return new Version[0];
            }
        }
    }

    //TODO: test coverage
    public Version createProductVersion(String version, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("value", version);
        params.put("product", productId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_VERSION_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Version.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: test coverage
    public Priority[] getPriority(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(PRIORITY_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
            return new Priority[0];
        } else {
            try {
                return new ObjectMapper().readValue(jsonArray.toJSONString(), Priority[].class);
            } catch (IOException e) {
                e.printStackTrace();
                return new Priority[0];
            }
        }
    }

    //TODO: test coverage
    // TODO: Create Category class
    public JSONArray getCategory(Map<String, Object> filter) {
        return (JSONArray) executeViaPositionalParams(CATEGORY_FILTER, Arrays.asList((Object) filter));
    }

    //TODO: test coverage
    public TestExecutionStatus getTestExecutionStatus(String name, String weightLookup) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("TestExecutionStatus.filter", Arrays.asList((Object) filter));

        // name not found, search by weight
        if (jsonArray == null || jsonArray.isEmpty()) {
            filter.remove("name");
            // eq, gt or lt zero
            filter.put("weight" + weightLookup, 0);
            jsonArray = (JSONArray) executeViaPositionalParams("TestExecutionStatus.filter", Arrays.asList((Object) filter));
        }

        try {
            TestExecutionStatus[] statuses = new ObjectMapper().readValue(jsonArray.toJSONString(), TestExecutionStatus[].class);
            return statuses[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestCase getTestCaseById(int testCaseId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("id", testCaseId);
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams(TEST_CASE_FILTER, Collections.singletonList(filter));
        if (jsonArray == null || jsonArray.isEmpty() || jsonArray.toJSONString().equals("[{}]")) {
            System.out.printf("-- kiwitcms-junit-plugin -- Case ID \"%s\" not found%n", testCaseId);
            return null;
        }

        try {
            TestCase[] testCases = new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
            return testCases[0];
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
