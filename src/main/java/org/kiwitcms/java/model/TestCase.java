// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2020-2021 Alexander Todorov <atodorov@MrSenko.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
    private int caseId;
    private Date createDate;
    private boolean isAutomated;
    private int[] plan;
    private String arguments;
    private String summary;
    private int categoryId;
    private int priorityId;
    private String author;
    private String product;
    private int productId;

    public int getCaseId() {
        return caseId;
    }

    @JsonSetter("id")
    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSetter("create_date")
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    @JsonSetter("is_automated")
    public void setAutomated(boolean automated) {
        isAutomated = automated;
    }

    public String getArguments() {
        return arguments;
    }

    @JsonSetter("arguments")
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public String getSummary() {
        return summary;
    }

    @JsonSetter("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getCategoryId() {
        return categoryId;
    }

    @JsonSetter("category_id")
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    @JsonSetter("priority_id")
    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getAuthor() {
        return author;
    }

    @JsonSetter("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    public int[] getPlan() {
        return plan;
    }

    @JsonSetter("plan")
    public void setPlan(int[] plan) {
        this.plan = plan;
    }

    public String getProduct() {
        return product;
    }

    @JsonSetter("product")
    public void setProduct(String product) {
        this.product = product;
    }

    public int getProductId() {
        return productId;
    }

    @JsonSetter("product_id")
    public void setProductId(int productId) {
        this.productId = productId;
    }


    public static Integer nameExists(String name, TestCase[] tests){
        if (name == null || name.isEmpty() || tests.length == 0){
            return null;
        } else {
            for (TestCase tc : tests) {
                if (name.equals(tc.summary)){
                    return tc.caseId;
                }
            }
            return null;
        }
    }
}
