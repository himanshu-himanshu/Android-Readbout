package com.finalproject.milestone_readbout.models;

public class SavedNewsFirebaseModel {
    String userID, title, description, webUrl, imageUrl;

    public SavedNewsFirebaseModel(String userID, String title, String description, String webUrl, String imageUrl) {
        this.userID = userID;
        this.title = title;
        this.description = description;
        this.webUrl = webUrl;
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
