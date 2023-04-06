package com.finalproject.milestone_readbout.models;

import java.util.ArrayList;
import java.util.Collection;

public class GuardianNewsModel {
    private String status;
    private ArrayList<ResultsModel> results;

    public GuardianNewsModel(String status, ArrayList<ResultsModel> results) {
        this.status = status;
        this.results = results;
    }

    public GuardianNewsModel() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ResultsModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultsModel> results) {
        this.results = results;
    }


}
