// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestMethod {
    public String name;
    public String containingClass;
    public String result;
    public Throwable exception;

    public TestMethod(){};

    public TestMethod(String name, String containingClass, String result){
        this.name = name;
        this.containingClass = containingClass;
        this.result = result;
    }

    public String getKiwiSummary() {
        return containingClass + "." + name;
    }

    public int getTestExecutionStatus() {
        switch (result) {
            case "PASS":
                return 4;
            case "FAIL":
                return 5;
            default:
                //IDLE
                return 1;
        }
    }

    public String toJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toJSONArrayString(List<TestMethod> list) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
