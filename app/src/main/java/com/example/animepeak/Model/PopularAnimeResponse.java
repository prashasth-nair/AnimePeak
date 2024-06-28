package com.example.animepeak.Model;

import java.util.List;

public class PopularAnimeResponse {
    private int currentPage;
    private boolean hasNextPage;
    private List<PopularAnime> results;

    public PopularAnimeResponse(int currentPage, boolean hasNextPage, List<PopularAnime> results) {
        this.currentPage = currentPage;
        this.hasNextPage = hasNextPage;
        this.results = results;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<PopularAnime> getResults() {
        return results;
    }

    public void setResults(List<PopularAnime> results) {
        this.results = results;
    }

    public class PopularAnime {
        private String id,title, image,url;
        private List<String> genres;

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

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }
    }


}
