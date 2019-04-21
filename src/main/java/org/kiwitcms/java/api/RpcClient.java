// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.*;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import net.minidev.json.JSONArray;

import java.util.Arrays;
import java.util.Map;


public class RpcClient extends BaseRpcClient {

    public static final String LOGIN_METHOD = "Auth.login";
    public static final String LOGOUT_METHOD = "Auth.logout";


    private KiwiProductJsonRpcClient productClient;
    private KiwiTestingJsonRpcClient testingClient;

    public RpcClient(){
        super();
        productClient = new KiwiProductJsonRpcClient();
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
        return productClient.getProductId(name);
    }

    public Product createNewProduct(String name) {
        return productClient.createNewProduct(name);
    }

    public Build[] getBuilds(Map<String, Object> filter) {
        return productClient.getBuilds(filter);
    }

    public Build createBuild(String name, int productId) {
        return productClient.createBuild(name, productId);
    }

    public Version[] getVersions(Map<String, Object> filter) {
       return productClient.getVersions(filter);
    }

    public Version createProductVersion(String version, int productId) {
       return productClient.createProductVersion(version, productId);
    }

    public Priority[] getPriority(Map<String, Object> filter) {
        return productClient.getPriority(filter);
    }

    // TODO: Create Category class
    public JSONArray getCategory(Map<String, Object> filter) {
        return productClient.getCategory(filter);
    }
}