// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestRun;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.kiwitcms.java.api.RpcClient.CREATE_TC_METHOD;
import static org.kiwitcms.java.api.RpcClient.TEST_CASE_STATUS_FILTER;
import static org.kiwitcms.java.api.RpcClient.TEST_CASE_FILTER;
import static org.mockito.ArgumentMatchers.*;


class TestingRpcTest {

    @Test
    void createTestCaseTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // props to test
        String categoryId = "category_id";
        String priorityId = "priority_id";
        String summary = "summary";
        String caseStatusId = "case_status";
        String productId = "product";

        // test object
        JSONObject json = new JSONObject();
        json.put(productId, 33);
        json.put(categoryId, 4);
        json.put(priorityId, 0);
        json.put(caseStatusId, 1);
        json.put(summary, "Test Summary");
        // Prevent/stub logic in super.method()
        Mockito.doReturn(1).when((BaseRpcClient) spy).executeViaPositionalParams(eq(TEST_CASE_STATUS_FILTER), anyList());
        Mockito.doReturn(json).when((BaseRpcClient) spy).executeViaPositionalParams(eq(CREATE_TC_METHOD), anyList());

        TestCase expectedTC = new TestCase();
        //inconsistent tcms behaviour
        expectedTC.setProduct("33");
        //todo: ^^^ product probably needs to be removed
        expectedTC.setCategoryId(4);
        expectedTC.setPriorityId(0);
        expectedTC.setSummary("Test Summary");
        assertThat(spy.createTestCase(4, 0, 1, "Test Summary"), Matchers.samePropertyValuesAs(expectedTC));
    }

    @Test
    void getRunWhenValidIdTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // test object
        JSONObject json = new JSONObject();
        json.put("id", 1);
        JSONArray array = new JSONArray();
        array.add(json);
        // Prevent/stub logic in super.method()
        Mockito.doReturn(array).when((BaseRpcClient) spy).executeViaPositionalParams(anyString(), anyList());

        TestRun expectedResult = new TestRun();
        expectedResult.setId(1);
        assertThat(spy.getRun(1), Matchers.samePropertyValuesAs(expectedResult));
    }

    @Test
    void getRunWhenServiceErrorTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(null).when((BaseRpcClient) spy).executeViaPositionalParams(anyString(), anyList());
        assertThat(spy.getRun(1), is(equalTo(null)));
    }

    @Test
    void getRunWhenMissingIdTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(new JSONArray()).when((BaseRpcClient) spy).executeViaPositionalParams(anyString(), anyList());
        assertThat(spy.getRun(-1), is(equalTo(null)));
    }

    @Test
    void getRunWhenServiceResponseMisformattedTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // test object
        JSONObject json = new JSONObject();
        json.put("id", "rt");
        JSONArray array = new JSONArray();
        array.add(json);
        // Prevent/stub logic in super.method()
        Mockito.doReturn(array).when((BaseRpcClient) spy).executeViaPositionalParams(anyString(), anyList());

        assertThat(spy.getRun(1), is(equalTo(null)));
    }
    
    @Test
    void getTestParametersByTestId() {
        RpcClient spy = Mockito.spy(new RpcClient());
    
        //TODO enable when date assignment in TestCase.class is working; returns null when mapping a valid date format
//        Date date = Date.from(Instant.now());
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        String formattedDate = sdf.format(date);
    
        //Test object
        JSONArray result = new JSONArray();
        JSONObject resultBody = new JSONObject();
        resultBody.put("id", "2");
//        resultBody.put("created_date", formattedDate); //TODO enable when date assignment in TestCase.class is working
        resultBody.put("is_automated", true);
        resultBody.put("script", "");
        resultBody.put("arguments", "");
        resultBody.put("extra_link", null);
        resultBody.put("summary", "Test summary");
        resultBody.put("requirement", null);
        resultBody.put("notes", "");
        resultBody.put("text", "Scenario text");
        resultBody.put("case_status", 2);
        resultBody.put("case_status__name", "CONFIRMED");
        resultBody.put("category", 1);
        resultBody.put("category__name", "Category");
        resultBody.put("priority", 1);
        resultBody.put("priority__value", "P1");
        resultBody.put("author", 4);
        resultBody.put("author__username", "a.user");
        resultBody.put("default_tester", 4);
        resultBody.put("default_tester__username", "a.user");
        resultBody.put("reviewer", null);
        resultBody.put("reviewer__username", null);
        resultBody.put("setup_duration", 0.0);
        resultBody.put("testing_duration", 0.0);
        resultBody.put("expected_duration", 0.0);
        
        result.add(resultBody);
    
        Mockito.doReturn(result).when((BaseRpcClient) spy).executeViaPositionalParams(eq(TEST_CASE_FILTER), anyList());
        
        //Expected test case
        TestCase expectedTc = new TestCase();
        expectedTc.setCaseId(2);
//        expectedTc.setCreateDate(date); //TODO enable when date assignment in TestCase.class is working
        expectedTc.setAuthor("4");
        expectedTc.setAutomated(true);
        expectedTc.setSummary("Test summary");
        expectedTc.setArguments("");
        
        //Get result from the "api"
        TestCase resultingTc = spy.getTestCaseById(2);
        
        // Resulting TestCase should be the same as in JSON response
        assertThat(resultingTc, Matchers.samePropertyValuesAs(expectedTc));
    }
    
    @Test
    void getTestParametersByTestIdWithEmptyResponse() {
        RpcClient spy = Mockito.spy(new RpcClient());
        
        //Empty response for getTestCaseById
        JSONArray result = new JSONArray();
        Mockito.doReturn(result).when((BaseRpcClient) spy).executeViaPositionalParams(eq(TEST_CASE_FILTER), anyList());
    
        assertThat(spy.getTestCaseById(1), is(nullValue()));
    }
    
    @Test
    void getTestParametersByTestIdWithEmptyObject() {
        RpcClient spy = Mockito.spy(new RpcClient());
        
        //Empty response for getTestCaseById
        JSONArray result = new JSONArray();
        JSONObject emptyCase = new JSONObject();
        result.add(emptyCase);
        Mockito.doReturn(result).when((BaseRpcClient) spy).executeViaPositionalParams(eq(TEST_CASE_FILTER), anyList());
        
        assertThat(spy.getTestCaseById(1), is(nullValue()));
    }
}
