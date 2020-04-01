package com.example.kylewai.a2uf.userSchedule;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kylewai.a2uf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The fragment that pops up with class info when you select a cell with a class in the week schedule
//TODO: Delete functionality, Formatting
public class ExpandFragmentAddClass extends Fragment {
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

    public ExpandFragmentAddClass(){}

    public ExpandFragmentAddClass(String courseCode, String name, String description,
                                  String department, String prereqs, String coreqs,
                                  List<String> instructors, List<Map<String, String>> meetTimes,
                                  String examTime, String classNumber) {
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
        this.classNumber = classNumber;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_expand_add_class, container, false);
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
                List<Map<String, String>> dropList = new ArrayList<>();
                for(Map<String, String> meetTime : meetTimes)
                {
                    Map<String, String> dropClass = new HashMap<>();
                    //dropClass.put("classNumber", );
                    dropClass.put("classNumber", classNumber);
                    dropClass.put("course", courseCode);
                    dropClass.put("days", meetTime.get("days"));
                    dropClass.put("periodBegin", meetTime.get("periodBegin"));
                    dropClass.put("periodEnd", meetTime.get("periodEnd"));

                    dropList.add(dropClass);
                }

                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                //Log.d("ArraySize", "Num Meets: " + dropList.size());
                //Toast toasty = Toast.makeText(view.getContext(), "Num Meets" + dropList.size(), Toast.LENGTH_LONG);
                //toasty.show();

                for(Map<String, String> dropClass : dropList)
                {
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
            }
        });

        Button addButton = view.findViewById(R.id.Add_Class_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Place our strings in a map to add to our User's schedule


                List<Map<String, String>> toAddWeeklyMeets = new ArrayList<>();

                for(Map<String, String> meetTime : meetTimes)
                {
                    Map<String, String> addWeeklyMeetTime = new HashMap<>();
                    addWeeklyMeetTime.put("classNumber", classNumber);
                    addWeeklyMeetTime.put("course", courseCode);
                    addWeeklyMeetTime.put("days", meetTime.get("days"));
                    addWeeklyMeetTime.put("periodBegin", meetTime.get("periodBegin"));
                    addWeeklyMeetTime.put("periodEnd", meetTime.get("periodEnd"));

                    toAddWeeklyMeets.add(addWeeklyMeetTime);
                }

                //flagChecker used to denote if the adding failed.
                List<Boolean> flagCheck = new ArrayList<>();
                flagCheck.add(false);

                //final Map<String, String> currentUserSchedule = new HashMap<>();
                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                DocumentReference docRef = database.collection("users").document(UserScheduleFragment.transferuid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("getCurrentSched", "DocumentSnapshot data: " + document.getData());
                                List<Map<String, String>> currentUserSchedule = (List<Map<String, String>>) document.get("weeklyMeetTimes");
                                Log.d("firstClass", currentUserSchedule.get(0).get("classNumber"));

                                for (Map<String, String> course : currentUserSchedule) {
                                    //Check courseNumber
                                    Log.d("classChecks", "" + course.get("classNumber") + classNumber);
                                    if (course.get("classNumber").equals(classNumber)) {
                                        Log.d("classChecks", "Inside classNumber");
                                        Toast toast = Toast.makeText(view.getContext(), "Add Course Failure: Already Registered for this Class", Toast.LENGTH_LONG);
                                        toast.show();
                                        //flagCheck.set(0, true);
                                        return;
                                    }

                                    //Check course code
                                    Log.d("classChecks", "" + course.get("course") + courseCode);
                                    if (course.get("course").equals(courseCode)) {
                                        Log.d("classChecks", "Inside courseCode");
                                        Toast toast = Toast.makeText(view.getContext(), "Add Course Failure: Already Registered for this Class", Toast.LENGTH_LONG);
                                        toast.show();
                                        return;
                                    }

                                    //Check all times if meetDays are the same.
                                    for(Map<String, String> meetTime : toAddWeeklyMeets)
                                    {
                                        if (course.get("days").equals(meetTime.get("days"))) {
                                            Log.d("classChecks", "Inside MeetTimes");
                                            int beginTime = Integer.parseInt(course.get("periodBegin"));
                                            int endTime = Integer.parseInt(course.get("periodBegin"));

                                            int toAddBeginTime = Integer.parseInt(meetTime.get("periodBegin"));
                                            int toAddEndTime = Integer.parseInt(meetTime.get("periodEnd"));

                                            for (int i = beginTime; i <= endTime + 1; i++) {
                                                for (int j = toAddBeginTime; j <= toAddEndTime + 1; j++) {
                                                    if (i == j) {
                                                        Log.d("classChecks", "" + i + " " + j);
                                                        Toast toast = Toast.makeText(view.getContext(), "Add Course Failure: Conflicting Times", Toast.LENGTH_LONG);
                                                        toast.show();
                                                        //flagCheck.set(0, true);
                                                        return;


                                                    }
                                                }
                                            }
                                        }
                                    }


                                }



                                //Make sure we didn't hit any fail conditions.
                                //vvv This vvv
                                if (flagCheck.get(0) == false) {

                                    for(Map<String, String> meetTime : toAddWeeklyMeets)
                                    {

                                        database.collection("users").document(UserScheduleFragment.transferuid).update("weeklyMeetTimes", FieldValue.arrayUnion(meetTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                    Map<String, Object> addClass = new HashMap<>();
                                    List<String> instructorList = new ArrayList<>();
                                    //instructorList.add(instructors);
                                    instructorList = instructors;

                                    List<Map<String, String>> meetTimes = new ArrayList<>();
                                    //meetTimes.add(addWeeklyMeetTime);
                                    meetTimes = toAddWeeklyMeets;

                                    //Need more variables here.
                                    addClass.put("code", courseCode);
                                    addClass.put("coreqs", coreqs);
                                    addClass.put("department", department);
                                    addClass.put("description", "");
                                    addClass.put("examTime", examTime);
                                    addClass.put("instructors", instructorList);
                                    addClass.put("meetTimes", meetTimes);
                                    addClass.put("name", "");
                                    addClass.put("prereqs", "");

                                    database.collection("classes").document(classNumber).set(addClass);
                                }

                            } else {
                                Log.d("getCurrentSched", "No such document");
                            }
                        } else {
                            Log.d("getCurrentSched", "get failed with ", task.getException());
                        }
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
