package com.example.kylewai.a2uf.forum;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Course;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostScheduleExpandFragment extends Fragment {

    String postId;
    String classNumber;
    String courseCode;
    String name;
    String description;
    String department;
    String prereqs;
    String coreqs;
    List<String> instructors;
    List<Map<String, String>> meetTimes;
    String examTime;

    TextView textView_code;
    TextView textView_name;
    TextView textView_description;
    TextView textView_department;
    TextView textView_prereqs;
    TextView textView_coreqs;
    TextView textView_instructors;
    TextView textView_meetTimes;
    TextView textView_examTime;

    public PostScheduleExpandFragment(){}

    public PostScheduleExpandFragment(Course course, String postId) {
        this.postId = postId;
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.department = department;
        this.prereqs = prereqs;
        this.coreqs = coreqs;
        this.instructors = instructors;
        this.meetTimes = meetTimes;
        this.examTime = examTime;
        this.classNumber = classNumber;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_schedule_expand, container, false);
        createCloseButtonListener(view);
        textView_code = view.findViewById(R.id.code);
        textView_code.setText(this.courseCode);
        textView_name = view.findViewById(R.id.name);
        textView_name.setText(this.name);
        textView_description = view.findViewById(R.id.description);
        textView_description.setText(this.description);
        textView_prereqs = view.findViewById(R.id.prereqs);
        String prereqsString = "Prereqs: " + this.prereqs;
        textView_prereqs.setText(prereqsString);
        textView_coreqs = view.findViewById(R.id.coreqs);
        String coreqsString = "Coreqs: " + this.coreqs;
        textView_coreqs.setText(coreqsString);
        textView_instructors = view.findViewById(R.id.instructors);
        String instructorString = "Instructors:";
        for(String instructor : instructors){
            instructorString += "\n" + instructor;
        }

        textView_instructors.setText(instructorString);

        textView_department = view.findViewById(R.id.department);
        textView_department.setText(this.department);

        textView_meetTimes = view.findViewById(R.id.meetTimes);
        String meetTimesString = "Meetings:";
        for(Map<String, String> meetTime : meetTimes){
            meetTimesString += "\n" + meetTime.get("days");
            meetTimesString += "\n" + meetTime.get("periodBegin");
            meetTimesString += "\n" + meetTime.get("periodEnd");
            meetTimesString += "\n" + meetTime.get("building");
            meetTimesString += "\n" + meetTime.get("days");
        }
        textView_meetTimes.setText(meetTimesString);
        textView_examTime = view.findViewById(R.id.examTime);
        textView_examTime.setText(this.examTime);
        return view;
    }

    public void createCloseButtonListener(View inflatedView){
        Button closeButton = inflatedView.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
