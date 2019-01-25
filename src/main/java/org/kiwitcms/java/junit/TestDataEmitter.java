package org.kiwitcms.java.junit;

import org.kiwitcms.java.api.KiwiJsonRpcClient;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.model.TestCase;

public class TestDataEmitter {

    private KiwiJsonRpcClient client;

    public TestDataEmitter(){
        Config config = Config.getInstance();
        client = new KiwiJsonRpcClient();
        client.login(config.getKiwiUsername(), config.getKiwiPassword());
    }

    public void emitNewTestCase(String summary){
        client.createNewTC(76, 1, summary);
    }

    public void closeSession(){
        client.logout();
    }
}
