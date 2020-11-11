package com.etitgib.cricketstrikemvvm.models.teams;

import com.google.gson.annotations.SerializedName;

public class TeamsListModel {

    @SerializedName("isBatting")
    private Boolean isBatting;
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("shortName")
    private String shortName;
    @SerializedName("logoUrl")
    private String logoUrl;

    public TeamsListModel(Integer id, String name, String shortName, String logoUrl) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.logoUrl = logoUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Boolean getBatting() {
        return isBatting;
    }
}
