package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import com.google.firebase.firestore.DocumentId;

import java.util.List;
import java.util.Map;

//This class is a model for the "course" collection
public class Course {
    @DocumentId
    private String classNumber;
    private String code;
    private String name;
    private String prereqs;
    private String coreqs;
    private String department;
    private String description;
    private String examTime;
    private List<String> instructors;
    private List<Map<String, String>> meetTimes;


    public Course(){
        //Required empty constructor
    }


    public Course(String classNumber, String code, String coreqs, String name, String prereqs,
                  String department, String description, String examTime,
                  List<String> instructors, List<Map<String, String>> meetTimes){
        this.classNumber = classNumber;
        this.code = code;
        this.name = name;
        this.prereqs = prereqs;
        this.coreqs = coreqs;
        this.department = department;
        this.description = description;
        this.examTime = examTime;
        this.instructors = instructors;
        this.examTime = examTime;
        this.meetTimes = meetTimes;
    }


    public String getClassNumber() {
        return classNumber;
    }

    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }


    public String getPrereqs() {
        return prereqs;
    }


    public String getCoreqs() {
        return coreqs;
    }


    public String getDepartment() {
        return department;
    }


    public String getDescription() {
        return description;
    }


    public String getExamTime() {
        return examTime;
    }


    public List<String> getInstructors() {
        return instructors;
    }


    public List<Map<String, String>> getMeetTimes() {
        return meetTimes;
    }

}
