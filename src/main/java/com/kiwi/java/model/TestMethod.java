package com.kiwi.java.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestMethod {
    public String name;
    public String containingClass;
    public String result;
    public Throwable exception;

    public String toJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toJSONArrayString(List<TestMethod> list){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
