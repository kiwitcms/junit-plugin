// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRun {
    private String summary;
    private int plan;
    private int build;
    private int manager;

    public String getSummary() {
        return summary;
    }

    @JsonSetter("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPlan() {
        return plan;
    }

    @JsonSetter("plan")
    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getBuild() {
        return build;
    }

    @JsonSetter("build")
    public void setBuild(int build) {
        this.build = build;
    }

    public int getManager() {
        return manager;
    }

    @JsonSetter("manager")
    public void setManager(int manager) {
        this.manager = manager;
    }

}
