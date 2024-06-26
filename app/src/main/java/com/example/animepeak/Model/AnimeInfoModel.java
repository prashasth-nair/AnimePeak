package com.example.animepeak.Model;

import java.util.List;

public class AnimeInfoModel {
    private String id,title,description,subOrDub,status,image,url,type,releaseDate;
    private List<String> genres;
    private List<Episode> episodes;

    public AnimeInfoModel(String id, String title, String description, String subOrDub,
                          String status, String image, String url, String type, String releaseDate,
                          List<String> genres, List<Episode> episodes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subOrDub = subOrDub;
        this.status = status;
        this.image = image;
        this.url = url;
        this.type = type;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSubOrDub() {
        return subOrDub;
    }

    public void setSubOrDub(String subOrDub) {
        this.subOrDub = subOrDub;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    private class Episode {
        private String id, url;
        private int number;

        public Episode(String id, String url, int number) {
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


}
