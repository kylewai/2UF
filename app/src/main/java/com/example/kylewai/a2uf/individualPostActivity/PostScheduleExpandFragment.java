package com.example.kylewai.a2uf.individualPostActivity;


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
        this.courseCode = course.getCode();
        this.name = course.getName();
        this.description = course.getDescription();
        this.department = course.getDepartment();
        this.prereqs = course.getPrereqs();
        this.instructors = course.getInstructors();
        this.meetTimes = course.getMeetTimes();
        this.examTime = course.getExamTime();
        this.classNumber = course.getClassNumber();
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
        String[] prereqsSplit = this.prereqs.split(":|\\.", -1);
        String prereqsString;
        if(this.prereqs.equals("")) {
            prereqsString = "";
        }
        else{
            prereqsString = prereqsSplit[1].substring(1);
        }
        textView_prereqs.setText(prereqsString);
        textView_coreqs = view.findViewById(R.id.coreqs);
        TextView coreqs_label = view.findViewById(R.id.coreqs_label);
        if(prereqsSplit.length <= 3){
            textView_coreqs.setVisibility(View.GONE);
            coreqs_label.setVisibility(View.GONE);
        }
        else{
            textView_coreqs.setText(prereqsSplit[3].substring(1));
        }
        textView_instructors = view.findViewById(R.id.instructors);
        String instructorString = "";
        int k = 0;
        for(String instructor : instructors){
            instructorString += (k == 0)? instructor : "\n" + instructor;
            k++;
        }

        textView_instructors.setText(instructorString);

        textView_department = view.findViewById(R.id.department);
        textView_department.setText(this.department);

        textView_meetTimes = view.findViewById(R.id.meetTimes);
        String meetTimesString = "";
        k = 0;
        for(Map<String, String> meetTime : meetTimes){
            meetTimesString += (k == 0)? "Days: " + meetTime.get("days") : "\n\nDays: " + meetTime.get("days");
            meetTimesString += "\nPeriods: " + meetTime.get("periodBegin");
            meetTimesString += " - " + meetTime.get("periodEnd");
            meetTimesString += "\nBuilding: " + meetTime.get("building");
            meetTimesString += "\nRoom: " + meetTime.get("room");
            k++;
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
