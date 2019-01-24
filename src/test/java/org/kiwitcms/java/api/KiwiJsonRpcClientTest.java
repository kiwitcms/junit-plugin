package org.kiwitcms.java.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiwitcms.java.config.Config;
import org.kiwitcms.java.junit.KiwiTcmsExtension;

@ExtendWith(KiwiTcmsExtension.class)
public class KiwiJsonRpcClientTest {

    @Test
    void loginWithEmptyParamsTest(){
        System.out.println(Config.getInstance().getProduct());
    }

    @Test
    void loginWithEmptyParamsTest1(){
        System.out.println(Config.getInstance().getProductVersion());
    }

    @Test
    void loginWithEmptyParamsTest2(){
        System.out.println(Config.getInstance().getKiwiBuild());
    }
}
