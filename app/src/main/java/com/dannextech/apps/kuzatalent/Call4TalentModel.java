package com.dannextech.apps.kuzatalent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 4/15/18.
 */

public class Call4TalentModel {
    String organization;
    String talent;
    String datePosted;
    String location;
    String url;

    public Call4TalentModel() {
    }

    public Call4TalentModel(String organization, String talent,String location,String datePosted) {
        this.organization = organization;
        this.talent = talent;
        this.datePosted = datePosted;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
