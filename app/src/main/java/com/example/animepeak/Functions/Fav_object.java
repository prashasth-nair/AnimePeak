package com.example.animepeak.Functions;

import static com.example.animepeak.Activity.MainActivity.fav_list;


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



    public static void removeFavByID(String id) {
        for (int i = 0; i < fav_list.size(); i++) {
            if (fav_list.get(i).getID().equals(id)) {
                fav_list.remove(i);
                break;
            }
        }
    }

}
