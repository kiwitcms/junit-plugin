// Copyright (c) 2018-2019 Aneta Petkova <aneta.v.petkova@gmail.com>
// Copyright (c) 2022 Alexander Todorov <atodorov@otb.bg>

// Licensed under the GPLv3: https://www.gnu.org/licenses/gpl.html

package org.kiwitcms.java.model;


public class TestMethod {
    public String name;
    public String containingClass;
    public String result;
    public Throwable exception;
    public int id;

    public TestMethod(){};

    public TestMethod(String name, String containingClass, String result){
        this.name = name;
        this.containingClass = containingClass;
        this.result = result;
    }

    public String getSummary() {
        return containingClass + "." + name;
    }
}
