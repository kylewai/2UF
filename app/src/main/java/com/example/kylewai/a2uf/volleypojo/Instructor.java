package com.example.kylewai.a2uf.volleypojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instructor {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
