package com.example.demo.dto;

import java.util.List;

public class Response {
    private String url;
    private List<Xpaths> xpaths;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<Xpaths> getXpaths() {
        return xpaths;
    }
    public void setXpaths(List<Xpaths> xpaths) {
        this.xpaths = xpaths;
    }

    
}
