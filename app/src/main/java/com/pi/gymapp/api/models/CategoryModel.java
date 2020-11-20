package com.pi.gymapp.api.models;

import com.google.gson.annotations.Expose;

public class CategoryModel {
    
    @Expose private int id;
    
    @Expose private String name;
    
    @Expose private String detail;

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

    public Object getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}