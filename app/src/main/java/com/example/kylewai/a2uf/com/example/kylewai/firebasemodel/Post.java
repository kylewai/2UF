package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post implements Parcelable {
    @DocumentId
    String documentId;
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

    protected Post(Parcel in) {
        documentId = in.readString();
        dateCreated = in.readParcelable(Timestamp.class.getClassLoader());
        title = in.readString();
        description = in.readString();
        author = in.readString();
        major = in.readString();
        Map<String, String> tempMap;
        weeklyMeetTimes = new ArrayList<>();
        int arraySize = in.readInt();
        for(int i = 0; i < arraySize; i++){
            int size = in.readInt();
            tempMap = new HashMap<>();
            for(int j = 0; j < size; j++){
                String key = in.readString();
                String val = in.readString();
                tempMap.put(key, val);
            }
            weeklyMeetTimes.add(tempMap);
        }
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getDocumentId() {
        return documentId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(documentId);
        parcel.writeParcelable(dateCreated, i);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(author);
        parcel.writeString(major);
        parcel.writeInt(weeklyMeetTimes.size());
        for(int k = 0; k < weeklyMeetTimes.size(); k++){
            Map<String, String> meetTime = weeklyMeetTimes.get(k);
            parcel.writeInt(meetTime.size());
            for(Map.Entry<String, String> entry : meetTime.entrySet()){
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }
        }
    }
    private Bundle mapToBundle(Map<String, String> meetTime){
        Bundle bundle = new Bundle();
        for(Map.Entry<String, String> entry : meetTime.entrySet()){
            bundle.putString(entry.getKey(), entry.getValue());
        }
        return bundle;
    }
}
