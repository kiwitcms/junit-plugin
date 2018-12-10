package com.kiwi.java.model;

import com.kiwi.java.config.Config;

public class TestRun {
    private String summary;
    private String plan;
    private int build;
    private String manager;

    public TestRun(String plan, int build){
        this.summary = "Automated test run";
        this.plan = plan;
        this.build = build;
        this.manager = Config.getInstance().getKiwiUsername();
    }
}
