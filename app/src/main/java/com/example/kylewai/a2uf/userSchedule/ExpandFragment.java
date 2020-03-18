package com.example.kylewai.a2uf.userSchedule;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kylewai.a2uf.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The fragment that pops up with class info when you select a cell with a class in the week schedule
//TODO: Delete functionality, Formatting
public class ExpandFragment extends Fragment {
    String courseCode;
    String name;
    String description;
    String department;
    String prereqs;
    String coreqs;
    String courseNumber;
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

    public ExpandFragment(String courseCode, String name, String description,
                          String department, String prereqs, String coreqs,
                          List<String> instructors, List<Map<String, String>> meetTimes, String examTime) {
        // Required empty public constructor
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.department = department;
        this.prereqs = prereqs;
        this.coreqs = coreqs;
        this.instructors = instructors;
        this.meetTimes = meetTimes;
        this.examTime = examTime;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_expand, container, false);
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
        Log.d("ExpandFrag", instructorString);
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

        Button dropButton = view.findViewById(R.id.dropButton);
        dropButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Map<String, String> dropClass = new HashMap<>();
                //dropClass.put("classNumber", );
                dropClass.put("course", courseCode);
                dropClass.put("days", meetTimes.get(0).get("days"));
                dropClass.put("periodBegin", meetTimes.get(0).get("periodBegin"));
                dropClass.put("periodEnd", meetTimes.get(0).get("periodEnd"));

                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("users").document(UserScheduleFragment.transferuid).update("weeklyMeetTimes", FieldValue.arrayRemove(dropClass)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dbUpdate", "DocumentSnapshot successfully updated!");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("dbUpdate", "Error updating document", e);
                            }
                        });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();
        startPostponedEnterTransition();
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
