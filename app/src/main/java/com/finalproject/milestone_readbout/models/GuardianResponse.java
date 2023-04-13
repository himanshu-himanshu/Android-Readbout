package com.finalproject.milestone_readbout.models;

public class GuardianResponse {
    GuardianNewsModel response;
    public GuardianResponse(GuardianNewsModel response) {
        this.response = response;
    }
    public GuardianNewsModel getResponse() {
        return response;
    }
    public void setResponse(GuardianNewsModel response) {
        this.response = response;
    }
}
