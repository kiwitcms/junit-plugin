// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.config;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.util.Optional;

public class Config {

    private IniPreferences config;
    private static Config instance;

    private Config() {
        try {
            String config_path = System.getProperty(
                "tcmsConfigPath",
                System.getProperty("user.home") + "/.tcms.conf"
            );
            config = new IniPreferences(new Ini(new File(config_path)));
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
        String runId = Optional.ofNullable(System.getenv("TCMS_RUN_ID")).
                               orElse(config.node("tcms").get("runId", null));

        if (runId == null) {
            return null;
        } else {
            return Integer.parseInt(runId.trim());
        }
    }

    public String getProduct() {
        return
                Optional.ofNullable(System.getenv("TCMS_PRODUCT")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_REPO_SLUG")).
                                orElse(Optional.ofNullable(System.getenv("JOB_NAME")).
                                        orElse(config.node("tcms").get("product", null))));
    }

    public String getProductVersion() {
        return
                Optional.ofNullable(System.getenv("TCMS_PRODUCT_VERSION")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_COMMIT")).
                                orElse(Optional.ofNullable(System.getenv("TRAVIS_PULL_REQUEST_SHA")).
                                        orElse(Optional.ofNullable(System.getenv("GIT_COMMIT")).
                                                orElse(config.node("tcms").get("productVer", null)))));
    }

    public String getBuild() {
        return
                Optional.ofNullable(System.getenv("TCMS_BUILD")).
                        orElse(Optional.ofNullable(System.getenv("TRAVIS_BUILD_NUMBER")).
                                orElse(Optional.ofNullable(System.getenv("BUILD_NUMBER")).
                                        orElse(config.node("tcms").get("build", null))));
    }

    public String getRunName() {
        return Optional.ofNullable(System.getProperty("tcmsRunName")).
                               orElse(config.node("tcms").get("runName", null));
    }
}
