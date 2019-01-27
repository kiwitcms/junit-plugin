package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestPlan {
    private Integer id;
    private String name;
    private String text;
    private String createDate;
    private boolean isActive;
    private Integer productVerionId;
    private String productVersion;
    private Integer ownerId;
    private Integer authorId;
    private Integer productId;
    private Integer typeId;

    public Integer getId() {
        return id;
    }

    @JsonSetter("plan_id")
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreateDate() {
        return createDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSetter("create_date")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getProductVerionId() {
        return productVerionId;
    }

    public void setProductVerionId(Integer productVerionId) {
        this.productVerionId = productVerionId;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public TestPlan(){};

}

//{"parent":null,"product_version":"unspecified","product":"Dunder","is_active":true,"extra_link":null,"author":"apetkova","type_id":1,"product_version_id":1,"type":"Unit","parent_id":null,"product_id":1,"name":"Auto Test Plan 1","text":"WIP","tag":[],"create_date":"2019-01-26 23:21:35","author_id":2259,"plan_id":1507}

