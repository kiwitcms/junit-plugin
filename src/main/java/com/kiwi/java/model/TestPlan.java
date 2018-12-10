package com.kiwi.java.model;

public class TestPlan {
    private Integer id;
    private String name;
    private String text;
    private String createDate;
    private boolean isActive;
    private Integer productVerionId;
    private String productVersion;
    private Integer ownerId;
    private String owner;
    private Integer authorId;
    private String author;
    private Integer productId;
    private String product;
    private Integer typeId;
    private String type;
    private Integer parentId;
    private String parent;
    private String[] tag;

    public TestPlan (String product, String productVersion){
        this.name = "Automated test plan for " + product;
        this.product = product;
        this.productVersion = productVersion;
        this.type = "Unit";
    }

}
