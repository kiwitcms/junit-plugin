// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import org.kiwitcms.java.config.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class BaseRpcClient {

    public static final String RPC_ENDPOINT = "/json-rpc/";
    public static final String BASE_URL = Config.getInstance().getKiwiUrl();

    protected String sessionId;

    protected JSONRPC2Session prepareSession(){
        URL serverURL = null;

        try {
            serverURL = new URL(new URL(BASE_URL), RPC_ENDPOINT);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
        mySession.getOptions().setRequestContentType("application/json");
        mySession.getOptions().trustAllCerts(true);
        return mySession;
    }

    protected Object callNameParamService(String serviceMethod, Map<String, Object> params){

        JSONRPC2Session mySession = prepareSession();
        mySession.setConnectionConfigurator(new SessionConfigurator(sessionId));

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(serviceMethod, requestID);
        request.setNamedParams(params);

        // Send request
        try {
            return getResponse(mySession.send(request));
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    protected Object callPosParamService(String serviceMethod, List<Object> params){

        JSONRPC2Session mySession = prepareSession();
        mySession.setConnectionConfigurator(new SessionConfigurator(sessionId));

        // Construct new request
        int requestID = 1;
        JSONRPC2Request request = new JSONRPC2Request(serviceMethod, requestID);
        request.setPositionalParams(params);

        // Send request
        try {
            return getResponse(mySession.send(request));
        } catch (JSONRPC2SessionException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    protected Object getResponse(JSONRPC2Response response){
        if (response.indicatesSuccess()) {
            return response.getResult();
        } else {
            System.out.println(response.getError().getMessage());
        }
        return response.getError();
    }
}
