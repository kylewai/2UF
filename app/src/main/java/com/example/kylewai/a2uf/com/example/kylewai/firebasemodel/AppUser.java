package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import java.util.List;
import java.util.Map;


//This class is a model for the "user" collection
public class AppUser {
    private String username;
    private List<String> favoritePosts;
    private List<String> favoriteMyPosts;
    private List<String> favoriteMocks;
    private List<String> posts;
    private List<Map<String, String>> weeklyMeetTimes;


    public AppUser(){
        //Required empty constructor
    }

    public AppUser(String username, List<String> favoriteMyPosts, List<String> favoriteMocks, List<String> favoritePosts, List<String> posts, List<Map<String, String>> weeklyMeetTimes){
        this.username = username;
        this.favoriteMyPosts = favoriteMyPosts;
        this.favoritePosts = favoritePosts;
        this.favoriteMocks = favoriteMocks;
        this.posts = posts;
        this.weeklyMeetTimes = weeklyMeetTimes;
    }

    public List<String> getFavoriteMyPosts() {
        return favoriteMyPosts;
    }

    public List<String> getFavoriteMocks() {
        return favoriteMocks;
    }

    public List<String> getFavoritePosts() {
        return favoritePosts;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getPosts(){
        return posts;
    }

    public List<Map<String, String>> getWeeklyMeetTimes(){
        return weeklyMeetTimes;
    }
}
