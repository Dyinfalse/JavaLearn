package com.servlet.web.dao;

public class Member {
    private Long id;
    private String name;
    private Integer mobile;
    private Integer sex;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member (){

    }

    public Member (Long id, String name, Integer mobile, Integer sex, String description){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.description = description;
        this.mobile = mobile;
    }
}
