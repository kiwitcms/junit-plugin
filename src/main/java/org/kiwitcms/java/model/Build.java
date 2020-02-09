// Copyright (c) 2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2020 Alexander Todorov <atodorov@MrSenko.com>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Build {
    private int id;
    private String name;
    private String product;
    private int productId;

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

    public int getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }
}

//Choices are: id, build_run, description, is_active, name, product, product_id, testcaserun
