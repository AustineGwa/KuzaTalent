package com.dannextech.apps.kuzatalent;

public class ViewSpecificCall4TalentModel {
    String location,description,organization,datePosted,talent,email,phone,website;

    public ViewSpecificCall4TalentModel() {
    }


    public ViewSpecificCall4TalentModel(String location, String description, String organization, String datePosted, String talent, String email, String phone, String website) {
        this.location = location;
        this.description = description;
        this.organization = organization;
        this.datePosted = datePosted;
        this.talent = talent;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
