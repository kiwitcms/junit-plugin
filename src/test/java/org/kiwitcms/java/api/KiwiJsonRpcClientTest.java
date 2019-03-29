// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.kiwitcms.java.junit.KiwiTcmsExtension;
import org.mockito.Mockito;

import static org.kiwitcms.java.api.KiwiJsonRpcClient.*;


//@ExtendWith(KiwiTcmsExtension.class)
public class KiwiJsonRpcClientTest {

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

        assertThat(spy.login("daenerys", "targaryen"), is(equalTo(null)));
    }

    @Test
    void loginWithEmptyParamsTest() {
        System.out.println("needs fixing");
    }
}
