package com.lahoriagency.cikolive.Classes;

import com.google.gson.annotations.SerializedName;

public class LoginReply extends BaseReply {
    @SerializedName("user_id")
    private Long userId;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("min_match_value")
    private int minMatchValue;
    @SerializedName("sex_choice")
    private String sexChoice;
    private int radius;
    @SerializedName("age_range_min")
    private int ageRangeMin;
    @SerializedName("age_range_max")
    private int ageRangeMax;
    private String description;
    private String type;
    private String status;

    public LoginReply() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getMinMatchValue() {
        return minMatchValue;
    }

    public void setMinMatchValue(int minMatchValue) {
        this.minMatchValue = minMatchValue;
    }

    public String getSexChoice() {
        return sexChoice;
    }

    public void setSexChoice(String sexChoice) {
        this.sexChoice = sexChoice;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAgeRangeMin() {
        return ageRangeMin;
    }

    public void setAgeRangeMin(int ageRangeMin) {
        this.ageRangeMin = ageRangeMin;
    }

    public int getAgeRangeMax() {
        return ageRangeMax;
    }

    public void setAgeRangeMax(int ageRangeMax) {
        this.ageRangeMax = ageRangeMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
