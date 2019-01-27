package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {
    private int buildId;
    private String name;
    private String classification;

    public int getBuildId() {
        return buildId;
    }

    @JsonSetter("build_id")
    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }
}

//Choices are: build_id, build_run, description, is_active, name, product, product_id, testcaserun
