// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.*;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class KiwiJsonRpcClient extends BaseRpcClient {

    private static final String LOGIN_METHOD = "Auth.login";
    private static final String LOGOUT_METHOD = "Auth.logout";
    private static final String GET_RUN_TCS_METHOD = "TestRun.get_cases";
    private static final String CREATE_RUN_METHOD = "TestRun.create";
    private static final String CREATE_TC_METHOD = "TestCase.create";
    private static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    private static final String RUN_FILTER = "TestRun.filter";
    private static final String PRODUCT_FILTER = "Product.filter";
    private static final String CREATE_PRODUCT_METHOD = "Product.create";
    private static final String BUILD_FILTER = "Build.filter";
    private static final String CREATE_BUILD_METHOD = "Build.create";
    private static final String ADD_TC_TO_PLAN_METHOD = "TestPlan.add_case";
    private static final String CREATE_PLAN_METHOD = "TestPlan.create";
    private static final String TEST_PLAN_FILTER = "TestPlan.filter";
    private static final String TEST_CASE_RUN_FILTER = "TestCaseRun.filter";
    private static final String CREATE_TC_RUN_METHOD = "TestCaseRun.create";
    private static final String UPDATE_TC_RUN_METHOD = "TestCaseRun.update";
    private static final String CREATE_VERSION_METHOD = "Version.create";
    private static final String VERSION_FILTER = "Version.filter";



    public TestCase createNewTC(int category, int product, String summary) {
        Map<String, Object> params = new HashMap<>();
        //category: Functional - 76
        params.put("category", category);
        params.put("product", product);
        // X-Product - 8
        params.put("summary", summary);
        //CONFIRMED
        params.put("case_status", 2);
        params.put("is_automated", "true");
        params.put("priority", 9);
        JSONObject json = (JSONObject) callPosParamService(CREATE_TC_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestCase.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestRun createNewRun(int build, String manager, int plan, String summary) {
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

    public TestCase[] getRunIdTestCases(int runId) {
        JSONArray jsonArray = (JSONArray) callPosParamService(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new TestCase[0];
        }
    }

    public TestCaseRun addTestCaseToRunId(int runId, int caseId) {
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

    public boolean planExists(int planId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", planId);
        JSONArray jsonArray = (JSONArray) callPosParamService(TEST_PLAN_FILTER, Arrays.asList((Object) filter));
        return !jsonArray.isEmpty();
    }

    public Integer getProductId(String name) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", name);
        JSONArray jsonArray = (JSONArray) callPosParamService(PRODUCT_FILTER, Arrays.asList((Object) filter));
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

    //TODO: investigate error 'Method not found: "Product.create"'
    public Product createNewProduct(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        JSONObject json = (JSONObject) callPosParamService(CREATE_PRODUCT_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Product.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestRun getRun(int runId) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("run_id", runId);

        JSONArray jsonArray = (JSONArray) callPosParamService(RUN_FILTER, Arrays.asList((Object) filter));
        if (jsonArray.isEmpty()) {
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

    public TestPlan createNewTP(int productId, String name, int type, int versionId) {
        Map<String, Object> params = new HashMap<>();
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

    public void addTestCaseToPlan(int planId, int caseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("plan_id", planId);
        params.put("case_id", caseId);

        callNameParamService(ADD_TC_TO_PLAN_METHOD, params);
    }

    public Build[] getBuilds(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) callPosParamService(BUILD_FILTER, Arrays.asList((Object) filter));
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

    public Build createBuild(String name, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("product", productId);
        JSONObject json = (JSONObject) callPosParamService(CREATE_BUILD_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Build.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestCaseRun getTestCaseRun(Map<String, Object> filter) {
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

    public TestCaseRun createTestCaseRun(int runId, int caseId, int build, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("run", runId);
        params.put("case", caseId);
        params.put("build", build);
        params.put("status", status);
//        TODO: why not working?
//        params.put("tested_by", user);
        JSONObject json = (JSONObject) callPosParamService(CREATE_TC_RUN_METHOD, Arrays.asList((Object) params));
        try {
            System.out.println(json.toJSONString());
            return new ObjectMapper().readValue(json.toJSONString(), TestCaseRun.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestCaseRun updateTestCaseRun(int tcRunId, int status) {
        Map<String, Object> values = new HashMap<>();
        values.put("status", status);

        Map <String, Object> params = new HashMap<>();
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

    public Version[] getVersions(Map<String, Object> filter) {
        JSONArray jsonArray = (JSONArray) callPosParamService(VERSION_FILTER, Arrays.asList((Object) filter));
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

    public Version createProductVersion(String version, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("value", version);
        params.put("product", productId);

        JSONObject json = (JSONObject) callPosParamService(CREATE_VERSION_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Version.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String login(String username, String password) {
        sessionId = (String) callPosParamService(LOGIN_METHOD, Arrays.asList(username, password));
        return sessionId;
    }

    //TODO: Seems a bad practice, remove/replace
    public String login() {
        return login(Config.getInstance().getKiwiUsername(), Config.getInstance().getKiwiPassword());
    }

    public void logout() {
        JSONRPC2Session mySession = prepareSession();
        mySession.setConnectionConfigurator(new SessionConfigurator(sessionId));

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
//    com.thetransactioncompany.jsonrpc2.JSONRPC2Error
}
