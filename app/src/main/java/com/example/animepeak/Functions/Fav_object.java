package com.example.animepeak.Functions;



import java.io.Serializable;

public class Fav_object implements Serializable {

    public String title;
    public String id;
    public String img;

    // Default constructor (no-argument constructor)
    public Fav_object() {
        // Required for Firebase deserialization
    }

    public Fav_object(String title, String id, String img) {
        this.title = title;
        this.id = id;
        this.img = img;


    }

    public String getTitle() {
        return title;
    }

    public String getID() {
        return id;
    }

    public String getImg() {
        return img;
    }




}
