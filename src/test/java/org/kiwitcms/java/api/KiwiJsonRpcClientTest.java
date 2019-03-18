// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.kiwitcms.java.junit.KiwiTcmsExtension;
import org.kiwitcms.java.model.TestCase;
import org.kiwitcms.java.model.TestRun;
import org.mockito.Mockito;

import static org.kiwitcms.java.api.KiwiJsonRpcClient.*;

@ExtendWith(KiwiTcmsExtension.class)
public class KiwiJsonRpcClientTest {

    @Test
    public void createNewTCTest(){
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // props to test
        String product = "product_id";
        String summary = "summary";

        // test object
        JSONObject json = new JSONObject();
        json.put(product, 345);
        json.put(summary, "Test Summary");
        // Prevent/stub logic in super.method()
        Mockito.doReturn(json).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());

        TestCase expectedTC = new TestCase();
        expectedTC.setProductId(345);
        expectedTC.setSummary("Test Summary");
        assertThat(spy.createNewTC(345, "Test Summary"), Matchers.samePropertyValuesAs(expectedTC));
    }


    @Test
    public void getRunWhenValidIdTest() {
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

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
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(null).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());
        assertThat(spy.getRun(1), is(equalTo(null)));
    }

    @Test
    public void getRunWhenMissingIdTest() {
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(new JSONArray()).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());
        assertThat(spy.getRun(-1), is(equalTo(null)));
    }

    @Disabled("Causes failures, not sure why")
    @Test
    public void getRunWhenServiceResponceMisformattedTest() {
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // test object
        JSONObject json = new JSONObject();
        json.put("id", "rt");
        JSONArray array = new JSONArray();
        array.add(json);
        // Prevent/stub logic in super.method()
        Mockito.doReturn(array).when((BaseRpcClient) spy).callPosParamService(anyString(), anyList());

        assertThat(spy.getRun(1), is(equalTo(null)));
    }

    @Test
    void loginWithCorrectCredentialsTest() {
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn("fakeToken").when((BaseRpcClient) spy).callPosParamService(eq(LOGIN_METHOD), anyList());

        assertThat(spy.login("daenerys", "targaryen"), is(equalTo("fakeToken")));
    }

    @Test
    void loginWithIncorrectCredentials() {
        KiwiJsonRpcClient spy = Mockito.spy(new KiwiJsonRpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(null).when((BaseRpcClient) spy).callPosParamService(eq(LOGIN_METHOD), anyList());

        assertThat(spy.login("daenerys", "targaryen"), is(equalTo(null)));    }

    @Test
    void loginWithEmptyParamsTest() {
        System.out.println("needs fixing");
    }
}
