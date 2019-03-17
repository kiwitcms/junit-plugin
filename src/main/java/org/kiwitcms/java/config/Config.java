// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

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

    private Config() {
        config = new Properties();
        try {
            config.load(new FileInputStream(new File("kiwi.tcms.conf")));
        } catch (java.io.IOException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (null == instance) {
            instance = new Config();
        }
        return instance;

    }

    public String getKiwiHost() {
        return
                Optional.ofNullable(config.getProperty("TCMS_API_URL")).
                    orElse(System.getenv("TCMS_API_URL"));
    }

    public String getKiwiUsername() {
        return
                Optional.ofNullable(config.getProperty("TCMS_USERNAME")).
                    orElse(System.getenv("TCMS_USERNAME"));
    }

    public String getKiwiPassword() {
        return
                Optional.ofNullable(config.getProperty("TCMS_PASSWORD")).
                    orElse(System.getenv("TCMS_PASSWORD"));
    }

    public Integer getKiwiRunId() {
        String runId = System.getenv("TCMS_RUN_ID");
        if (runId == null) {
            return null;
        } else {
            return Integer.getInteger(runId);
        }
    }

    public String getProduct() {
        return
                Optional.ofNullable(System.getenv("TCMS_PRODUCT")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_REPO_SLUG")).
                                    orElse(System.getenv("JOB_NAME")));
    }

    public String getProductVersion() {
        return
                Optional.ofNullable(System.getenv("TCMS_PRODUCT_VERSION")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_COMMIT")).
                                    orElse(Optional.ofNullable(System.getenv("TRAVIS_PULL_REQUEST_SHA")).
                                                orElse(System.getenv("GIT_COMMIT"))));
    }

    public String getKiwiBuild() {
        return
                Optional.ofNullable(System.getenv("TCMS_BUILD")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_BUILD_NUMBER")).
                                    orElse(System.getenv("BUILD_NUMBER")));
    }
}
