package com.example.allaboutcats;

import com.google.gson.annotations.SerializedName;

public class catclass {
    String id;
    String url;

    @SerializedName("categories[id]")
    int catid;

    @SerializedName("categories[name]")
    String catname;

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

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }
}
