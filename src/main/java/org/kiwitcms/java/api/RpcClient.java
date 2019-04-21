// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.*;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;

import java.io.IOException;

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

    private KiwiTestingJsonRpcClient testingClient;

    public RpcClient(){
        super();
        testingClient = new KiwiTestingJsonRpcClient();
    }

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

    public TestCase createNewTC(int productId, int categoryId, int priorityId, int caseStatusId, String summary) {
        return testingClient.createNewTC(productId, categoryId, priorityId, caseStatusId, summary);
    }

    public TestCase createNewConfirmedTC(int productId, int categoryId, int priorityId, String summary) {
        int caseStatusId = testingClient.getConfirmedTCStatusId();
        return testingClient.createNewTC(productId, categoryId, priorityId, caseStatusId, summary);
    }

    public TestRun createNewRun(int build, String manager, int plan, String summary) {
       return testingClient.createNewRun(build, manager, plan, summary);
    }

    public TestCase[] getRunIdTestCases(int runId) {
        return testingClient.getRunIdTestCases(runId);
    }

    public TestCase[] getPlanIdTestCases(int planId) {
        return testingClient.getPlanIdTestCases(planId);
    }

    public TestExecution addTestCaseToRunId(int runId, int caseId) {
        return testingClient.addTestCaseToRunId(runId, caseId);
    }

    public TestRun getRun(int runId) {
        return testingClient.getRun(runId);
    }

    public TestPlan createNewTP(int productId, String name, int versionId) {
        return testingClient.createNewTP(productId, name, versionId);
    }

    public int getTestPlanId(String name, int productId){
        return testingClient.getTestPlanId(name, productId);
    }

    public void addTestCaseToPlan(int planId, int caseId) {
        testingClient.addTestCaseToPlan(planId, caseId);
    }

    public TestExecution getTestExecution(Map<String, Object> filter) {
       return testingClient.getTestExecution(filter);
    }

    public TestExecution createTestExecution(int runId, int caseId, int build, int status) {
       return testingClient.createTestExecution(runId, caseId, build, status);
    }

    public TestExecution updateTestExecution(int tcRunId, int status) {
       return testingClient.updateTestExecution(tcRunId, status);
    }


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

    public Product createNewProduct(String name) {
        Map<String, Object> params = new HashMap<>();

        // TODO: move classification filtering in emitter
        // get the first possible classification
        JSONArray jsonArray = (JSONArray) executeViaPositionalParams("Classification.filter", Arrays.asList((Object) params));
        Object classificationId = ((JSONObject) jsonArray.get(0)).get("id");

        params.put("name", name);
        params.put("classification_id", classificationId);

        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_PRODUCT_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Product.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public Build createBuild(String name, int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("product", productId);
        JSONObject json = (JSONObject) executeViaPositionalParams(CREATE_BUILD_METHOD, Arrays.asList((Object) params));
        try {
            return new ObjectMapper().readValue(json.toJSONString(), Build.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    // TODO: Create Category class
    public JSONArray getCategory(Map<String, Object> filter) {
        return (JSONArray) executeViaPositionalParams(CATEGORY_FILTER, Arrays.asList((Object) filter));
    }
}
