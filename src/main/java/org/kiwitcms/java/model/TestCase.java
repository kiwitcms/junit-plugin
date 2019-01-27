package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private int category;
    private int priority;
    private String author;

    public int getCaseId() {
        return caseId;
    }

    @JsonSetter("case_id")
    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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

    public int getCategory() {
        return category;
    }

    @JsonSetter("category_id")
    public void setCategory(int category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    @JsonSetter("priority_id")
    public void setPriority(int priority) {
        this.priority = priority;
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

    //case_run_id
    public static Integer nameExists(String name, TestCase[] tests){
        if (name == null || name.isEmpty() || tests.length == 0){
            return null;
        } else {
            for (TestCase tc : tests) {
                System.out.println("Name to match: " + name);
                System.out.println("TC Summary: " + tc.summary);
                if (name.equals(tc.summary)){
                    return tc.caseId;
                }
            }
            return null;
        }
    }
}
