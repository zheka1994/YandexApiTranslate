package com.example.eugen.translateapplication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eugen on 16.02.2018.
 */

public class Languages {
    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private Map<String,String> langs = new HashMap<>();

    public Map<String, String> getLangs() {
        return langs;
    }

    public void setLangs(Map<String, String> langs) {
        this.langs = langs;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }


}
