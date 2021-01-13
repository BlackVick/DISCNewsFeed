package com.eswofficial.discnews.Api;

import com.eswofficial.discnews.Models.News;

import java.util.List;

public class NewsResponse {

    private String status;
    private int totalResults;
    private List<News> articles;

    public NewsResponse() {
    }

    public NewsResponse(String status, int totalResults, List<News> articles) {
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

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }
}
