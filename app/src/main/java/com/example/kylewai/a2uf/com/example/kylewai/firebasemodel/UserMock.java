package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMock implements Parcelable {
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

    protected UserMock(Parcel in) {
        classes = in.createStringArrayList();
        dateCreated = in.readParcelable(Timestamp.class.getClassLoader());
        mockId = in.readString();
        mockName = in.readString();
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

    public static final Creator<UserMock> CREATOR = new Creator<UserMock>() {
        @Override
        public UserMock createFromParcel(Parcel in) {
            return new UserMock(in);
        }

        @Override
        public UserMock[] newArray(int size) {
            return new UserMock[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(classes);
        parcel.writeParcelable(dateCreated, i);
        parcel.writeString(mockId);
        parcel.writeString(mockName);
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

}
