package com.cyberclick.cbcakanisa.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Articles {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("medias")
    @Expose
    private String medias;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    public Articles(int id,String name, String description, String medias, String created_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.medias = medias;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMedias() {
        return medias;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMedias(String medias) {
        this.medias = medias;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
