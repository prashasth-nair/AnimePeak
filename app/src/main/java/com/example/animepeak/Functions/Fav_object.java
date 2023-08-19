package com.example.animepeak.Functions;

import static com.example.animepeak.Activity.MainActivity.fav_list;

import android.util.Log;

import java.io.Serializable;

public class Fav_object implements Serializable {

    public String title;
    public String id;
    public String img;
    public String fav_source;
    // Default constructor (no-argument constructor)
    public Fav_object() {
        // Required for Firebase deserialization
    }
    public Fav_object(String title, String id, String img, String fav_source) {
        this.title = title;
        this.id = id;
        this.img = img;
        this.fav_source = fav_source;


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

    public String getFavSource() {
        return fav_source;
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
