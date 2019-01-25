package org.kiwitcms.java.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kiwitcms.java.model.TestCase;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.kiwitcms.java.model.TestCaseRun;
import org.kiwitcms.java.model.TestRun;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class KiwiJsonRpcClient extends BaseRpcClient{

    private static final String LOGIN_METHOD = "Auth.login";
    private static final String LOGOUT_METHOD = "Auth.logout";
    private static final String GET_RUN_TCS_METHOD = "TestRun.get_cases";
    private static final String CREATE_RUN_METHOD = "TestRun.create";
    private static final String CREATE_TC_METHOD = "TestCase.create";
    private static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    private static final String TEST_PLAN_FILTER = "TestPlan.filter";

    public TestCase createNewTC(int category, int product, String summary){
        Map<String, Object> params = new HashMap<>();
        //category: Functional - 76
        params.put("category", category);
        params.put("product", product);
        // X-Product - 8
        params.put("summary", summary);
        //CONFIRMED
        params.put("case_status", 2);
        params.put("is_automated", 1);
        params.put("priority", 9);
        JSONObject json = (JSONObject) callPosParamService(CREATE_TC_METHOD, Arrays.asList((Object)params));
//        return (TestCase) callValueNameParamService(CREATE_TC_METHOD, params);
        try {
            return new ObjectMapper().readValue(json.toJSONString(), TestCase.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestRun createNewTR(int build, int manager, int plan, String summary){
        Map<String, Object> params = new HashMap<>();
        //category: Functional - 76
        params.put("build", build);
        params.put("manager", manager);
        // X-Product - 8
        params.put("plan", plan);
        params.put("summary", summary);
        return (TestRun) callValueNameParamService(CREATE_TC_METHOD, params);
    }

    public TestCase[] getRunIdTestCases(int runId) {
        JSONArray jsonArray = (JSONArray) callPosParamService(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
        try {
            return new ObjectMapper().readValue(jsonArray.toJSONString(), TestCase[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TestCaseRun addTestCaseToRunId(int runId, int caseId){
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

    public boolean runExists(int runId){
        Map<String, Object> filter = new HashMap<>();
        filter.put("pk", runId);
        JSONArray jsonArray = (JSONArray)callPosParamService(TEST_PLAN_FILTER, Arrays.asList((Object)filter));
        return ! jsonArray.isEmpty();
    }

    public String login(String username, String password) {
        sessionId = (String) callPosParamService(LOGIN_METHOD, Arrays.asList((Object) username, (Object) password));
        return sessionId;
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

}
