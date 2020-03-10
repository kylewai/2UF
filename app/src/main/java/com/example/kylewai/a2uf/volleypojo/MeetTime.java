package com.example.kylewai.a2uf.volleypojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetTime {

    @SerializedName("meetNo")
    @Expose
    private Integer meetNo;
    @SerializedName("meetDays")
    @Expose
    private List<String> meetDays = null;
    @SerializedName("meetTimeBegin")
    @Expose
    private String meetTimeBegin;
    @SerializedName("meetTimeEnd")
    @Expose
    private String meetTimeEnd;
    @SerializedName("meetPeriodBegin")
    @Expose
    private String meetPeriodBegin;
    @SerializedName("meetPeriodEnd")
    @Expose
    private String meetPeriodEnd;
    @SerializedName("meetBuilding")
    @Expose
    private String meetBuilding;
    @SerializedName("meetBldgCode")
    @Expose
    private String meetBldgCode;
    @SerializedName("meetRoom")
    @Expose
    private String meetRoom;

    public Integer getMeetNo() {
        return meetNo;
    }

    public void setMeetNo(Integer meetNo) {
        this.meetNo = meetNo;
    }

    public List<String> getMeetDays() {
        return meetDays;
    }

    public void setMeetDays(List<String> meetDays) {
        this.meetDays = meetDays;
    }

    public String getMeetTimeBegin() {
        return meetTimeBegin;
    }

    public void setMeetTimeBegin(String meetTimeBegin) {
        this.meetTimeBegin = meetTimeBegin;
    }

    public String getMeetTimeEnd() {
        return meetTimeEnd;
    }

    public void setMeetTimeEnd(String meetTimeEnd) {
        this.meetTimeEnd = meetTimeEnd;
    }

    public String getMeetPeriodBegin() {
        return meetPeriodBegin;
    }

    public void setMeetPeriodBegin(String meetPeriodBegin) {
        this.meetPeriodBegin = meetPeriodBegin;
    }

    public String getMeetPeriodEnd() {
        return meetPeriodEnd;
    }

    public void setMeetPeriodEnd(String meetPeriodEnd) {
        this.meetPeriodEnd = meetPeriodEnd;
    }

    public String getMeetBuilding() {
        return meetBuilding;
    }

    public void setMeetBuilding(String meetBuilding) {
        this.meetBuilding = meetBuilding;
    }

    public String getMeetBldgCode() {
        return meetBldgCode;
    }

    public void setMeetBldgCode(String meetBldgCode) {
        this.meetBldgCode = meetBldgCode;
    }

    public String getMeetRoom() {
        return meetRoom;
    }

    public void setMeetRoom(String meetRoom) {
        this.meetRoom = meetRoom;
    }

}
