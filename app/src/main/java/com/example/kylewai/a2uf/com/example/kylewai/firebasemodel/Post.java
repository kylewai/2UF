package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;

public class Post {
    Timestamp dateCreated;
    String title;
    String description;
    String author;
    String major;
    List<Map<String, String>> weeklyMeetTimes;
    Integer likes;

    public Post(){}

    public Post(Timestamp dateCreated, String title, String description, String author,
                String major, List<Map<String, String>> weeklyMeetTimes, Integer likes){
        this.dateCreated = dateCreated;
        this.title = title;
        this.description = description;
        this.author = author;
        this.major = major;
        this.weeklyMeetTimes = weeklyMeetTimes;
        this.likes = likes;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public List<Map<String, String>> getWeeklyMeetTimes() {
        return weeklyMeetTimes;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLikes() {
        return likes;
    }

    public String getAuthor() {
        return author;
    }

    public String getMajor() {
        return major;
    }

    public String getTitle() {
        return title;
    }
}
