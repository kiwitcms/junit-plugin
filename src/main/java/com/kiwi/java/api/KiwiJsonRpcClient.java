package com.kiwi.java.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiwi.java.config.Config;
import com.kiwi.java.model.TestCase;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KiwiJsonRpcClient {

    private static final String LOGIN_METHOD = "Auth.login";
    private static final String GET_RUN_TCS_METHOD = "TestRun.get_cases";
    private static final String CREATE_RUN_METHOD = "TestRun.create";
    private static final String CREATE_TC_METHOD = "TestCase.create";
    private static final String ADD_TC_TO_RUN_METHOD = "TestRun.add_case";
    private static final String RPC_ENDPOINT = "/json-rpc/";
    private static final String BASE_HOST = Config.getInstance().getKiwiHost();

    public TestCase createNewTC(int category, int product, String summary){
        Map<String, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("product", product);
        params.put("summary", summary);
        params.put("priority", 1);
        return (TestCase) callNameParamService(CREATE_TC_METHOD, params);
    }

    public List<TestCase> getRunIdTestCases(int runId) throws IOException {
          String jsonArray = (String) callPosParamService(GET_RUN_TCS_METHOD, Arrays.asList((Object) runId));
          return new ObjectMapper().readValue(jsonArray, new TypeReference<List<TestCase>>() { });
    }

    public boolean addTestCasesToRunId(int runId, List<TestCase> tcs){
        // incorrect - TC is an object, not String
        for (TestCase tc : tcs){
            //first create TCs
            //then add them to run (damn! that's unnecessary complicated)
            callPosParamService(ADD_TC_TO_RUN_METHOD, Arrays.asList((Object) runId, (Object) tc));
        }
        return true;
    }

    public boolean login(String username, String password) {
        return (boolean) callPosParamService(LOGIN_METHOD, Arrays.asList((Object) username, (Object) password));
    }

    private Object callPosParamService(String serviceMethod, List<Object> params){

        JSONRPC2Session mySession = prepareSession();

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(serviceMethod, requestID);
        request.setPositionalParams(params);

        // Send request
        try {
            return responseSuccess(mySession.send(request));
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Object callNameParamService(String serviceMethod, Map<String, Object> params){

        JSONRPC2Session mySession = prepareSession();

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(serviceMethod, requestID);
        request.setNamedParams(params);

        // Send request
        try {
            return responseSuccess(mySession.send(request));
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private JSONRPC2Session prepareSession(){
        URL serverURL = null;

        try {
            serverURL = new URL(new URL(BASE_HOST), RPC_ENDPOINT);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
        mySession.getOptions().setRequestContentType("application/json");
        mySession.getOptions().trustAllCerts(true);
        return mySession;
    }

    private boolean responseSuccess(JSONRPC2Response response){
        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
            return true;
        } else {
            System.out.println(response.getError().getMessage());
        }
        return false;
    }
}
