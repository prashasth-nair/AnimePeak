package com.example.animepeak.Model;

import java.util.List;

public class AnimeInfoModel {
    private String id,title,url,images,description,status,subOrDub,otherName,releaseDate,type;
    private int totalEpisodes;
    private List<String> genres;
    private List<Episode> episodes;

    public AnimeInfoModel(String id, String title, String url, String images, String description,
                          String status, String subOrDub, String otherName, String releaseDate,
                          String type, int totalEpisodes, List<String> genres, List<Episode> episodes) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.images = images;
        this.description = description;
        this.status = status;
        this.subOrDub = subOrDub;
        this.otherName = otherName;
        this.releaseDate = releaseDate;
        this.type = type;
        this.totalEpisodes = totalEpisodes;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubOrDub() {
        return subOrDub;
    }

    public void setSubOrDub(String subOrDub) {
        this.subOrDub = subOrDub;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
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
        private String id,url;
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
