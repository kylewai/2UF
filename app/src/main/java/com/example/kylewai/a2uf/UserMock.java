package com.example.kylewai.a2uf;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;

public class UserMock {
    List<String> classes;
    Timestamp dateCreated;
    String mockId;
    String mockName;
    List<Map<String, String>> weeklyMeetTimes;

    public UserMock(){}

    public UserMock(List<String> classes, Timestamp dateCreated, String mockId, String mockName, List<Map<String, String>> weeklyMeetTimes){
        this.classes = classes;
        this.dateCreated = dateCreated;
        this.mockId = mockId;
        this.mockName = mockName;
        this.weeklyMeetTimes = weeklyMeetTimes;
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<Map<String, String>> getWeeklyMeetTimes() {
        return weeklyMeetTimes;
    }

    public String getMockId() {
        return mockId;
    }

    public String getMockName() {
        return mockName;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }
}
