package com.kiwi.java.junit;

import com.kiwi.java.api.KiwiJsonRpcClient;
import com.kiwi.java.config.Config;

public class TestDataEmitter {
    public static void main(String[] args) {
        //initialize params
        Config config = Config.getInstance();
        KiwiJsonRpcClient client = new KiwiJsonRpcClient();
        client.login(config.getKiwiUsername(), config.getKiwiPassword());
    }
}
