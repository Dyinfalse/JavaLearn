package com.servlet.web.dao;

import java.io.Serializable;

public class Category implements Serializable {

    private static final long serialVersionUID = 565871682398123L;

    private int id;

    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category() {
    }
}
