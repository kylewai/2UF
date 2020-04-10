package com.example.kylewai.a2uf.userSchedule;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

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
    String credits;

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
    TextView textView_credits;

    public ExpandFragmentAddClass(){}

    public ExpandFragmentAddClass(String courseCode, String name, String description,
                                  String department, String prereqs, String coreqs,
                                  List<String> instructors, List<Map<String, String>> meetTimes,
                                  String examTime, String classNumber, String credits) {
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
        this.credits = credits;
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
        textView_credits = view.findViewById(R.id.credits);
        textView_credits.setText(this.credits);
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

//        Button dropButton = view.findViewById(R.id.dropButton);
//        dropButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                List<Map<String, String>> dropList = new ArrayList<>();
//                for(Map<String, String> meetTime : meetTimes)
//                {
//                    Map<String, String> dropClass = new HashMap<>();
//                    //dropClass.put("classNumber", );
//                    dropClass.put("classNumber", classNumber);
//                    dropClass.put("course", courseCode);
//                    dropClass.put("days", meetTime.get("days"));
//                    dropClass.put("periodBegin", meetTime.get("periodBegin"));
//                    dropClass.put("periodEnd", meetTime.get("periodEnd"));
//
//                    dropList.add(dropClass);
//                }
//
//                final FirebaseFirestore database = FirebaseFirestore.getInstance();
//
//                //Log.d("ArraySize", "Num Meets: " + dropList.size());
//                //Toast toasty = Toast.makeText(view.getContext(), "Num Meets" + dropList.size(), Toast.LENGTH_LONG);
//                //toasty.show();
//
//                for(Map<String, String> dropClass : dropList)
//                {
//                    database.collection("users").document(UserScheduleFragment.transferuid).update("weeklyMeetTimes", FieldValue.arrayRemove(dropClass)).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d("dbUpdate", "DocumentSnapshot successfully updated!");
//                        }
//                    })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("dbUpdate", "Error updating document", e);
//                                }
//                            });
//                }
//            }
//        });

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

                                List<Map<String, String>> currentUserSchedule = (List<Map<String, String>>) document.get("weeklyMeetTimes");

                                for (Map<String, String> course : currentUserSchedule) {
                                    //Check courseNumber
                                    if (course.get("classNumber").equals(classNumber)) {
                                        makeToast("Add Course Failure: Already Registered for this Class");
                                        //flagCheck.set(0, true);
                                        return;
                                    }

                                    //Check course code
                                    if (course.get("course").equals(courseCode)) {
                                        makeToast("Add Course Failure: Already Registered for this Class");
                                        return;
                                    }

                                    //Check all times if meetDays are the same.
                                    Map<Character, Integer> dayComparison = new HashMap<>();
                                    boolean checkTimes;
                                    for(Map<String, String> meetTime : toAddWeeklyMeets)
                                    {
                                        checkTimes = false;
                                        //Day comparison done to see if there are matching characters between
                                        //the two day strings. This catches a case like "TR" and "T", which conflict.
                                        dayComparison.clear();
                                        String meetDays = course.get("days");
                                        String toAddMeetDays = meetTime.get("days");
                                        for(int i = 0; i < meetDays.length(); i++){
                                            dayComparison.put(meetDays.charAt(i), 1);
                                        }
                                        for(int i = 0; i < toAddMeetDays.length(); i++){
                                            if(dayComparison.containsKey(toAddMeetDays.charAt(i))){
                                                checkTimes = true;
                                                break;
                                            }
                                        }
                                        //If there is a day conflict, check for time conflicts
                                        if (checkTimes) {
                                            Log.d("classChecks", "Inside MeetTimes");
                                            int beginTime = periodToInt(course.get("periodBegin"));
                                            int endTime = periodToInt(course.get("periodEnd"));

                                            int toAddBeginTime = periodToInt(meetTime.get("periodBegin"));
                                            int toAddEndTime = periodToInt(meetTime.get("periodEnd"));

                                            if(beginTime <= toAddEndTime && toAddBeginTime <= endTime) {
                                                makeToast("Add Course Failure: Conflicting Times");
                                                //flagCheck.set(0, true);
                                                return;
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

                                    //meetTimes.add(addWeeklyMeetTime);
                                    Course course = new Course(courseCode, name, prereqs, department, description, examTime, instructors, meetTimes, credits);


                                    //Store class info
                                    database.collection("classes").document(classNumber).set(course);

                                    makeToast("Added " + courseCode);
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


    private int periodToInt(String period){
        switch(period){
            case "E1": return 12;
            case "E2": return 13;
            case "E3": return 14;
            default: return Integer.parseInt(period);
        }
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

    private void makeToast(String message){
        //Customize toast
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        toastView.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryLight, null), PorterDuff.Mode.SRC_IN);
        TextView toastText = toastView.findViewById(android.R.id.message);
        toastText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.textOnSecondary, null));
        toastText.setTypeface(toastText.getTypeface(), Typeface.BOLD);
        toastText.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        toast.show();
    }
}
