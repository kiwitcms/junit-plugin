// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mockito;

import static org.kiwitcms.java.api.RpcClient.*;


public class RpcClientTest {

    @Test
    void loginWithCorrectCredentialsTest() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn("fakeToken").when((BaseRpcClient) spy).executeViaPositionalParams(eq(LOGIN_METHOD), anyList());

        assertThat(spy.login("daenerys", "targaryen"), is(equalTo("fakeToken")));
    }

    @Test
    void loginWithIncorrectCredentials() {
        RpcClient spy = Mockito.spy(new RpcClient());

        // Prevent/stub logic in super.method()
        Mockito.doReturn(null).when((BaseRpcClient) spy).executeViaPositionalParams(eq(LOGIN_METHOD), anyList());

        assertThat(spy.login("daenerys", "targaryen"), is(equalTo(null)));
    }

    @Test
    void loginWithEmptyParamsTest() {
        System.out.println("needs fixing");
    }
}
