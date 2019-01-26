// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.api;

import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;

import java.net.HttpURLConnection;


public class SessionConfigurator implements ConnectionConfigurator {

    private String token;

    public SessionConfigurator(String token){
        this.token = token;
    }


    public void configure(HttpURLConnection connection){
        String cookie = "sessionid=" + token;
        connection.setRequestProperty("Cookie", cookie);
    }
}
