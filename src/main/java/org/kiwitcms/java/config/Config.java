// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.prefs.Preferences;

public class Config {

    private IniPreferences config;
    private static Config instance;

    private Config() {
        try {
            String home_dir = System.getProperty("user.home");
            config = new IniPreferences(new Ini(new File(home_dir + "/.tcms.conf")));
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

    public String getUrl() {
        return
                Optional.ofNullable(config.node("tcms").get("url", null)).
                    orElse(System.getenv("TCMS_API_URL"));
    }

    public String getUsername() {
        return
                Optional.ofNullable(config.node("tcms").get("username", null)).
                    orElse(System.getenv("TCMS_USERNAME"));
    }

    public String getPassword() {
        return
                Optional.ofNullable(config.node("tcms").get("password", null)).
                    orElse(System.getenv("TCMS_PASSWORD"));
    }

    public Integer getRunId() {
        String runId = System.getenv("TCMS_RUN_ID");
        if (runId == null) {
            return null;
        } else {
            return Integer.parseInt(runId);
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

    public String getBuild() {
        return
                Optional.ofNullable(System.getenv("TCMS_BUILD")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_BUILD_NUMBER")).
                                    orElse(System.getenv("BUILD_NUMBER")));
    }
}
