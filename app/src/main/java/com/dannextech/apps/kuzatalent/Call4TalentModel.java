package com.dannextech.apps.kuzatalent;

/**
 * Created by root on 4/15/18.
 */

public class Call4TalentModel {
    String organization;
    String talent;
    String datePosted;

    public Call4TalentModel() {
    }

    public Call4TalentModel(String organization, String talent, String status, String datePosted) {
        this.organization = organization;
        this.talent = talent;
        this.datePosted = datePosted;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setOrganization(String organization) {

        this.organization = organization;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    public String getOrganization() {

        return organization;
    }

    public String getTalent() {
        return talent;
    }
}
