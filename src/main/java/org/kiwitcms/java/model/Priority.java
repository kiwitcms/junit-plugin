package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Priority {
    private String name;
    private int id;
    private boolean isActive;


    public String getName() {
        return name;
    }

    @JsonSetter("value")
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    @JsonSetter("is_active")
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
