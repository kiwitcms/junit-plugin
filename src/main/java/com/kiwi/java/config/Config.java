package com.kiwi.java.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class Config {

    private Configuration config;
    private static Config instance;

    private Config(){
        Configurations configs = new Configurations();
        try
        {
            config = configs.properties(new File("kiwi.tcms.conf"));
            // access configuration properties

        }
        catch (ConfigurationException cex)
        {
            // Something went wrong
        }
    }

    public static Config getInstance(){
        if (null == instance){
            instance = new Config();
        }
        return instance;

    }

    public String getKiwiHost(){
        return config.getString("TCMS_API_URL");
    }

    public String getKiwiUsername(){
        return config.getString("TCMS_USERNAME");
    }

    public String getKiwiPassword(){
        return config.getString("TCMS_PASSWORD");
    }

    public String getKiwiRunId(){
        return config.getString("TCMS_RUN_ID");
    }

    public String getProduct(){ return config.getString("TCMS_PRODUCT");}

    public String getProductVersion(){ return config.getString("TCMS_PRODUCT_VERSION");}


}
