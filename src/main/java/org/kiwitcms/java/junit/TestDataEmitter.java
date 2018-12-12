package org.kiwitcms.java.junit;

import org.kiwitcms.java.api.KiwiJsonRpcClient;
import org.kiwitcms.java.config.Config;

public class TestDataEmitter {
    public static void main(String[] args) {
        //initialize params
        Config config = Config.getInstance();
        KiwiJsonRpcClient client = new KiwiJsonRpcClient();
        client.login(config.getKiwiUsername(), config.getKiwiPassword());
    }
}
