package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Priority {
    private String name;
    private int id;
    private int productId;


    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @JsonSetter("priority_id")
    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    @JsonSetter("product_id")
    public void setProductId(int productId) {
        this.productId = productId;
    }
}
