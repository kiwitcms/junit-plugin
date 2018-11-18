package com.kiwi.java.api;

import com.kiwi.java.config.Config;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class KiwiJsonRpcClient {

    private static final String LOGIN_METHOD = "Auth.login";
    private static final String RPC_ENDPOINT = "/json-rpc/";
    private static final String BASE_HOST = Config.getInstance().getKiwiHost();

    public boolean login(String username, String password) {
        URL serverURL = null;

        try {
            serverURL = new URL(new URL(BASE_HOST), RPC_ENDPOINT);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
        mySession.getOptions().setRequestContentType("application/json");
        mySession.getOptions().trustAllCerts(true);

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(LOGIN_METHOD, requestID);
        request.setPositionalParams(Arrays.asList((Object) username, (Object) password));
        // Send request
        JSONRPC2Response response = null;
        try {
            response = mySession.send(request);
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return false;
        }
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
