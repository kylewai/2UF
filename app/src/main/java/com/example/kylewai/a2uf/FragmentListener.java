package com.example.kylewai.a2uf;

import java.util.List;
import java.util.Map;

public interface FragmentListener {
    void onSwitch(String courseCode, String name, String description,
                  String department, String prereqs,
                  List<String> instructors,
                  List<Map<String, String>> meetTimes,
                  String examTime, String classNumber);
}
