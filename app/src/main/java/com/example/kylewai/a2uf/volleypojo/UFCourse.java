package com.example.kylewai.a2uf.volleypojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UFCourse {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("courseId")
    @Expose
    private String courseId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("termInd")
    @Expose
    private String termInd;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("prerequisites")
    @Expose
    private String prerequisites;
    @SerializedName("sections")
    @Expose
    private List<Section> sections = null;

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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

}
