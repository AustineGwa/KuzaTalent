package com.dannextech.apps.kuzatalent;

public class UploadVideoModel {

    String title,talent,url;

    public UploadVideoModel() {
    }

    public UploadVideoModel(String talent, String title) {
        this.talent = talent;
        this.title = title;
    }

    public String getTalent() {
        return talent;
    }

    public void setTalent(String talent) {
        this.talent = talent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
