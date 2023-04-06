package com.finalproject.milestone_readbout.models;

import java.util.ArrayList;

public class NewsModel {
    private String status;
    private String totalResults;
    private ArrayList<ArticlesModel> articles;
    public NewsModel(String status, String totalResults, ArrayList<ArticlesModel> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<ArticlesModel> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<ArticlesModel> articles) {
        this.articles = articles;
    }
}

