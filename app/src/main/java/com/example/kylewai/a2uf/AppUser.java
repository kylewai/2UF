package com.example.kylewai.a2uf;

import java.util.List;
import java.util.Map;

public class AppUser {
    private List<String> classes;
    private List<String> posts;
    private List<Map<String, String>> weeklyMeetTimes;


    public AppUser(){
        //Required empty constructor
    }

    public AppUser(List<String> classes, List<String> posts, List<Map<String, String>> weeklyMeetTimes){
        this.classes = classes;
        this.posts = posts;
        this.weeklyMeetTimes = weeklyMeetTimes;
    }

    public List<String> getClasses(){
        return classes;
    }

    public List<String> getPosts(){
        return posts;
    }

    public List<Map<String, String>> getWeeklyMeetTimes(){
        return weeklyMeetTimes;
    }
}
