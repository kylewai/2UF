package com.example.kylewai.a2uf.userSchedule;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddClassActivity extends AppCompatActivity {

    List<Map<String, String>> meetings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String UserID = intent.getStringExtra("ID");
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Log.d("UserID", "" + UserID);



        Button addClassButton = findViewById(R.id.submit);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Gather our strings for placing into DB.

                TextView textView = findViewById(R.id.ClassNumber);
                String classNumber = (String) textView.getText().toString();

                textView = findViewById(R.id.courseCode);
                String courseCode = (String) textView.getText().toString();

                textView = findViewById(R.id.CoReqs);
                String coreqs = (String) textView.getText().toString();

                textView = findViewById(R.id.Department);
                String department = (String) textView.getText().toString();

                textView = findViewById(R.id.ExamTime);
                String examTime = (String) textView.getText().toString();

                textView = findViewById(R.id.Instructor);
                String instructor = (String) textView.getText().toString();

                textView = findViewById(R.id.MeetDays);
                String meetDays = (String) textView.getText().toString();

                textView = findViewById(R.id.MeetTimeBegin);
                String meetTimeBegin = (String) textView.getText().toString();

                textView = findViewById(R.id.MeetTimeEnd);
                String meetTimeEnd = (String) textView.getText().toString();

                //Place our strings in a map to add to our User's schedule
                Map<String, String> addWeeklyMeetTime = new HashMap<>();
                addWeeklyMeetTime.put("classNumber", classNumber);
                addWeeklyMeetTime.put("course", courseCode);
                addWeeklyMeetTime.put("days", meetDays);
                addWeeklyMeetTime.put("periodBegin", meetTimeBegin);
                addWeeklyMeetTime.put("periodEnd", meetTimeEnd);

                //flagChecker used to denote if the adding failed.
                List<Boolean> flagCheck = new ArrayList<>();
                flagCheck.add(false);

               //final Map<String, String> currentUserSchedule = new HashMap<>();

                DocumentReference docRef = database.collection("users").document(UserID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("getCurrentSched", "DocumentSnapshot data: " + document.getData());
                                List<Map<String, String>> currentUserSchedule = (List<Map<String, String>>) document.get("weeklyMeetTimes");
                                Log.d("firstClass", currentUserSchedule.get(0).get("classNumber"));

                                for(Map<String, String> course : currentUserSchedule)
                                {
                                    //Check courseNumber
                                    Log.d("classChecks", "" + course.get("classNumber") + classNumber);
                                    if(course.get("classNumber").equals(classNumber))
                                    {
                                        Log.d("classChecks", "Inside classNumber");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Add Course Failure: Already Registered for this Class", Toast.LENGTH_LONG);
                                        toast.show();
                                        //flagCheck.set(0, true);
                                        return;
                                    }

                                    //Check course code
                                    Log.d("classChecks", "" + course.get("course") + courseCode);
                                    if(course.get("course").equals(courseCode))
                                    {
                                        Log.d("classChecks", "Inside courseCode");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Add Course Failure: Already Registered for this Class", Toast.LENGTH_LONG);
                                        toast.show();
                                        return;
                                    }

                                    //Check all times if meetDays are the same.
                                    if(course.get("days").equals(meetDays))
                                    {
                                        Log.d("classChecks", "Inside MeetTimes");
                                        int beginTime = Integer.parseInt(course.get("periodBegin"));
                                        int endTime = Integer.parseInt(course.get("periodBegin"));

                                        int toAddBeginTime = Integer.parseInt(meetTimeBegin);
                                        int toAddEndTime = Integer.parseInt(meetTimeEnd);

                                        for(int i = beginTime; i <= endTime + 1; i++)
                                        {
                                            for(int j = toAddBeginTime; j <= toAddEndTime + 1; j++)
                                            {
                                                if(i == j)
                                                {
                                                    Log.d("classChecks", "" + i + " " + j);
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Add Course Failure: Conflicting Times", Toast.LENGTH_LONG);
                                                    toast.show();
                                                    //flagCheck.set(0, true);
                                                    return;


                                                }
                                            }
                                        }
                                    }


                                }


                                //Testing: Need to put database manipulation inside here.
                                //Make sure we didn't hit any fail conditions.
                                //vvv This vvv
                                if(flagCheck.get(0) == false)
                                {

                                    database.collection("users").document(UserID).update("weeklyMeetTimes", FieldValue.arrayUnion(addWeeklyMeetTime)).addOnSuccessListener(new OnSuccessListener<Void>() {
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


                                    Map<String, Object> addClass = new HashMap<>();
                                    List<String> instructorList = new ArrayList<>();
                                    instructorList.add(instructor);

                                    List<Map<String, String>> meetTimes = new ArrayList<>();
                                    meetTimes.add(addWeeklyMeetTime);

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

                //Make sure we didn't hit any fail conditions.
                //vvv Used to be here vvv

            }
        });

    }

}
