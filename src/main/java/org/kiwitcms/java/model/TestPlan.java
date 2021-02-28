// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2020-2021 Alexander Todorov <atodorov@MrSenko.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

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

    @JsonSetter("id")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
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

}
