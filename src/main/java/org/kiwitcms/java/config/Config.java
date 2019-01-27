package org.kiwitcms.java.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.Properties;

public class Config {

    private Properties config;
    private static Config instance;

    private Object getValue(String name, Class<?> type) {
        String value = config.getProperty(name);
        if (value == null)
            return null;
        if (type == String.class)
            return value;
        if (type == boolean.class)
            return Boolean.parseBoolean(value);
        if (type == int.class)
            return Integer.parseInt(value);
        if (type == float.class)
            return Float.parseFloat(value);
        throw new IllegalArgumentException("Unknown configuration value type: " + type.getName());
    }

    private Config(){
        config = new Properties();
        try {
            config.load(new FileInputStream(new File("kiwi.tcms.conf")));
        }catch(java.io.IOException fnfe){
            fnfe.printStackTrace();
        }
    }

    public static Config getInstance(){
        if (null == instance){
            instance = new Config();
        }
        return instance;

    }

    public String getKiwiHost(){
        return config.getProperty("TCMS_API_URL");
    }

    public String getKiwiUsername(){
        return config.getProperty("TCMS_USERNAME");
    }

    public String getKiwiPassword(){
        return config.getProperty("TCMS_PASSWORD");
    }

    public Integer getKiwiRunId(){
        String runId = config.getProperty("TCMS_RUN_ID");
        if (runId.isEmpty()){
            return null;
        } else {
            return Integer.getInteger(runId);
        }
    }

    public String getProduct(){
        return
                Optional.ofNullable(System.getenv("TRAVIS_REPO_SLUG")).
                        orElse(config.getProperty("TCMS_PRODUCT"));
    }

    public String getProductVersion(){
        return
                Optional.ofNullable(System.getenv("TRAVIS_COMMIT")).
                        orElse(config.getProperty("TCMS_PRODUCT_VERSION"));
    }

    public String getKiwiBuild(){
        return
            Optional.ofNullable(System.getenv("TRAVIS_BUILD_NUMBER")).
                    orElse(config.getProperty("TCMS_BUILD"));

    }

}
