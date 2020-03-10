package com.example.kylewai.a2uf.volleypojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Section {

    private String code;
    private String courseId;
    private String name;
    private String termInd;
    private String description;
    private String prerequisites;

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("classNumber")
    @Expose
    private String classNumber;
    @SerializedName("gradBasis")
    @Expose
    private String gradBasis;
    @SerializedName("acadCareer")
    @Expose
    private String acadCareer;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("credits")
    @Expose
    private String credits;
    @SerializedName("credits_min")
    @Expose
    private Integer creditsMin;
    @SerializedName("credits_max")
    @Expose
    private Integer creditsMax;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("dNote")
    @Expose
    private String dNote;
    @SerializedName("genEd")
    @Expose
    private List<Object> genEd = null;
    @SerializedName("sectWeb")
    @Expose
    private String sectWeb;
    @SerializedName("rotateTitle")
    @Expose
    private String rotateTitle;
    @SerializedName("deptCode")
    @Expose
    private Integer deptCode;
    @SerializedName("deptName")
    @Expose
    private String deptName;
    @SerializedName("courseFee")
    @Expose
    private Double courseFee;
    @SerializedName("lateFlag")
    @Expose
    private String lateFlag;
    @SerializedName("EEP")
    @Expose
    private String eEP;
    @SerializedName("LMS")
    @Expose
    private String lMS;
    @SerializedName("instructors")
    @Expose
    private List<Instructor> instructors = null;
    @SerializedName("meetTimes")
    @Expose
    private List<MeetTime> meetTimes = null;
    @SerializedName("addEligible")
    @Expose
    private String addEligible;
    @SerializedName("grWriting")
    @Expose
    private String grWriting;
    @SerializedName("finalExam")
    @Expose
    private String finalExam;
    @SerializedName("dropaddDeadline")
    @Expose
    private String dropaddDeadline;
    @SerializedName("pastDeadline")
    @Expose
    private Boolean pastDeadline;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getGradBasis() {
        return gradBasis;
    }

    public void setGradBasis(String gradBasis) {
        this.gradBasis = gradBasis;
    }

    public String getAcadCareer() {
        return acadCareer;
    }

    public void setAcadCareer(String acadCareer) {
        this.acadCareer = acadCareer;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public Integer getCreditsMin() {
        return creditsMin;
    }

    public void setCreditsMin(Integer creditsMin) {
        this.creditsMin = creditsMin;
    }

    public Integer getCreditsMax() {
        return creditsMax;
    }

    public void setCreditsMax(Integer creditsMax) {
        this.creditsMax = creditsMax;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDNote() {
        return dNote;
    }

    public void setDNote(String dNote) {
        this.dNote = dNote;
    }

    public List<Object> getGenEd() {
        return genEd;
    }

    public void setGenEd(List<Object> genEd) {
        this.genEd = genEd;
    }

    public String getSectWeb() {
        return sectWeb;
    }

    public void setSectWeb(String sectWeb) {
        this.sectWeb = sectWeb;
    }

    public String getRotateTitle() {
        return rotateTitle;
    }

    public void setRotateTitle(String rotateTitle) {
        this.rotateTitle = rotateTitle;
    }

    public Integer getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(Integer deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Double getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(Double courseFee) {
        this.courseFee = courseFee;
    }

    public String getLateFlag() {
        return lateFlag;
    }

    public void setLateFlag(String lateFlag) {
        this.lateFlag = lateFlag;
    }

    public String getEEP() {
        return eEP;
    }

    public void setEEP(String eEP) {
        this.eEP = eEP;
    }

    public String getLMS() {
        return lMS;
    }

    public void setLMS(String lMS) {
        this.lMS = lMS;
    }

    public List<Instructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<Instructor> instructors) {
        this.instructors = instructors;
    }

    public List<MeetTime> getMeetTimes() {
        return meetTimes;
    }

    public void setMeetTimes(List<MeetTime> meetTimes) {
        this.meetTimes = meetTimes;
    }

    public String getAddEligible() {
        return addEligible;
    }

    public void setAddEligible(String addEligible) {
        this.addEligible = addEligible;
    }

    public String getGrWriting() {
        return grWriting;
    }

    public void setGrWriting(String grWriting) {
        this.grWriting = grWriting;
    }

    public String getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(String finalExam) {
        this.finalExam = finalExam;
    }

    public String getDropaddDeadline() {
        return dropaddDeadline;
    }

    public void setDropaddDeadline(String dropaddDeadline) {
        this.dropaddDeadline = dropaddDeadline;
    }

    public Boolean getPastDeadline() {
        return pastDeadline;
    }

    public void setPastDeadline(Boolean pastDeadline) {
        this.pastDeadline = pastDeadline;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTermInd() {
        return termInd;
    }

    public void setTermInd(String termInd) {
        this.termInd = termInd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }
}
