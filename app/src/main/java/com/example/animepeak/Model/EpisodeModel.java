package com.example.animepeak.Model;

public class EpisodeModel {
    private String id, url;
    private int number;

    public EpisodeModel(String id, String url, int number) {
        this.id = id;
        this.url = url;
        this.number = number;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
