// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRun {
    private int id;
    private String summary;
    private int plan;
    private int build;
    private String manager;
    private String productVersion;

    public int getId() {
        return id;
    }

    @JsonSetter("run_id")
//TODO: doesn't this need to be setId? Is it used at all?
    public void seId(int id) {
        this.id = id;
    }

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

    @JsonSetter("plan_id")
    public void setPlan(int plan) {
        this.plan = plan;
    }

    public int getBuild() {
        return build;
    }

    @JsonSetter("build_id")
    public void setBuild(int build) {
        this.build = build;
    }

    public String getManager() {
        return manager;
    }

    @JsonSetter("manager")
    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getProductVersion() {
        return productVersion;
    }

    @JsonSetter("product_version")
    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

//    [{"summary":"Test run for TestPlan","cc":[],"product_version":"3.1","notes":"","run_id":694,"manager":"apetkova","stop_date":null,"product_version_id":47,"default_tester_id":2259,"build_id":29,"build":"unspecified","manager_id":2259,"tag":[],"plan":"TestPlan","default_tester":"apetkova","plan_id":1304,"start_date":"2019-01-23 21:38:04"}]
//{"summary":"Automatic test run","cc":[],"product_version":"1","run_id":705,"notes":"","manager":"apetkova","product_version_id":376,"stop_date":null,"default_tester_id":null,"build_id":4,"build":"3.39-localhost","manager_id":2259,"tag":[],"plan":"TestPlan","plan_id":1304,"default_tester":null,"start_date":"2019-01-26 22:23:02"}



}
