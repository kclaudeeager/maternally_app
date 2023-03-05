package com.example.navigationdrawerapp.model;

public class HealthTip {
    private String createdAt;
    private String updatedAt;
    private int id;
    private String tipName;
    private String title;
    private String description;
    private int createdBy;

    public HealthTip(String createdAt, String updatedAt, int id, String tipName, String title, String description, int createdBy) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = id;
        this.tipName = tipName;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
