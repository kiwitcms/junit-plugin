package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseRun {
    private int tcRunId;
    private int runId;
    private int caseId;
    private int build;

    private String status;

    public int getTcRunId() {
        return tcRunId;
    }

    @JsonSetter("case_run_id")
    public void setTcRunId(int tcRunId) {
        this.tcRunId = tcRunId;
    }

    public int getRunId() {
        return runId;
    }

    @JsonSetter("run_id")
    public void setRunId(int runId) {
        this.runId = runId;
    }

    public int getCaseId() {
        return caseId;
    }

    @JsonSetter("case_id")
    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getBuild() {
        return build;
    }

    @JsonSetter("build_id")
    public void setBuild(int build) {
        this.build = build;
    }

    public String getStatus() {
        return status;
    }

    @JsonSetter("status")
    public void setStatus(String status) {
        this.status = status;
    }


//    [{"case_text_version":0,"close_date":"2019-01-27 00:24:34","run_id":706,"case_run_id":2799,"run":"Automatic test run","tested_by_id":2259,"tested_by":"apetkova","sortkey":0,"status_id":4,"build_id":1,"build":"unspecified","case_id":3431,"assignee":null,"case":"KiwiJsonRpcClientTest.loginWithEmptyParamsTest1","assignee_id":null,"status":"PASSED"}]

}
