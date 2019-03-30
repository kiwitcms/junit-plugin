// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiwitcms.java.junit.KiwiTcmsExtension;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestRun;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.kiwitcms.java.api.KiwiTestingJsonRpcClient.CREATE_TC_METHOD;
import static org.kiwitcms.java.api.KiwiTestingJsonRpcClient.TEST_CASE_STATUS_FILTER;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(KiwiTcmsExtension.class)
public class KiwiTestingJsonRpcClientTest {

    @Test
    public void createNewTCTest() {
        KiwiTestingJsonRpcClient spy = Mockito.spy(new KiwiTestingJsonRpcClient());

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
        Mockito.doReturn(1).when((BaseRpcClient) spy).callPosParamService(eq(TEST_CASE_STATUS_FILTER), anyList());
        Mockito.doReturn(json).when((BaseRpcClient) spy).callPosParamService(eq(CREATE_TC_METHOD), anyList());

        TestCase expectedTC = new TestCase();
        //inconsistent tcms behaviour
        expectedTC.setProduct("33");
        expectedTC.setCategoryId(4);
        expectedTC.setPriorityId(0);
        expectedTC.setSummary("Test Summary");
        assertThat(spy.createNewTC(33, 4, 0, 1, "Test Summary"), Matchers.samePropertyValuesAs(expectedTC));
    }

    @Test
    public void getRunWhenValidIdTest() {
        KiwiTestingJsonRpcClient spy = Mockito.spy(new KiwiTestingJsonRpcClient());

        // test object
        JSONObject json = new JSONObject();
        json.put("id", 1);
        JSONArray array = new JSONArray();
        array.add(json);
        // Prevent/stub logic in super.method()
        Mockito.doReturn(array).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());

        TestRun expectedResult = new TestRun();
        expectedResult.seId(1);
        assertThat(spy.getRun(1), Matchers.samePropertyValuesAs(expectedResult));
    }

    @Test
    public void getRunWhenServiceErrorTest() {
        KiwiTestingJsonRpcClient spy = Mockito.spy(new KiwiTestingJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(null).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());
        assertThat(spy.getRun(1), is(equalTo(null)));
    }

    @Test
    public void getRunWhenMissingIdTest() {
        KiwiTestingJsonRpcClient spy = Mockito.spy(new KiwiTestingJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(new JSONArray()).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());
        assertThat(spy.getRun(-1), is(equalTo(null)));
    }

    @Test
    public void getRunWhenServiceResponceMisformattedTest() {
        KiwiTestingJsonRpcClient spy = Mockito.spy(new KiwiTestingJsonRpcClient());

        // test object
        JSONObject json = new JSONObject();
        json.put("id", "rt");
        JSONArray array = new JSONArray();
        array.add(json);
        // Prevent/stub logic in super.method()
        Mockito.doReturn(array).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());

        assertThat(spy.getRun(1), is(equalTo(null)));
    }
}
