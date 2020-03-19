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
        Bundle tempBundle = new Bundle();
        Map<String, String> tempMap;
        weeklyMeetTimes = new ArrayList<>();
        for(int i = 0; i < in.readInt(); i++){
            tempBundle = in.readBundle();
            tempMap = new HashMap<>();
            for(String key : tempBundle.keySet()){
                tempMap.put(key, tempBundle.getString(key));
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
        Bundle temp;
        for(int k = 0; k < weeklyMeetTimes.size(); k++){
            temp = mapToBundle(weeklyMeetTimes.get(k));
            parcel.writeBundle(temp);
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
