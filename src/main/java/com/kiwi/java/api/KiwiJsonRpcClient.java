package com.kiwi.java.api;

import com.kiwi.java.config.Config;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class KiwiJsonRpcClient {

    private static final String LOGIN_METHOD = "Auth.login";
    private static final String RPC_ENDPOINT = "/json-rpc/";
    private static final String BASE_HOST = Config.getInstance().getKiwiHost();

    public boolean login(String username, String password) {
        return callPosParamService(LOGIN_METHOD, Arrays.asList((Object) username, (Object) password));
    }

    private boolean callPosParamService(String serviceMethod, List<Object> params){

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

    private boolean callNameParamService(String serviceMethod, Map<String, Object> params){

        JSONRPC2Session mySession = prepareSession();

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(LOGIN_METHOD, requestID);
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
