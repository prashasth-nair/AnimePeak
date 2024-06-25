package com.example.animepeak.Model;

import java.util.List;

public class AnimeResponseModel {
    private int currentPage;
    private boolean hasNextPage;
    private List<AnimeInfoModel> results;

    public AnimeResponseModel(int currentPage, boolean hasNextPage, List<AnimeInfoModel> results) {
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

    public List<AnimeInfoModel> getResults() {
        return results;
    }

    public void setResults(List<AnimeInfoModel> results) {
        this.results = results;
    }
}
