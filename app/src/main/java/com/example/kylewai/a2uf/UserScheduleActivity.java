package com.example.kylewai.a2uf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class UserScheduleActivity extends AppCompatActivity {
    private TextView userInfo;
    FirebaseFirestore db;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        db = FirebaseFirestore.getInstance();
        userInfo = findViewById(R.id.user_info);
        Intent intent = getIntent();
        String username = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
        uid = intent.getStringExtra(LoginActivity.EXTRA_UID);
        String credentials = "Firebase:\nUsername: " + username + "\nUID: " + uid;
        userInfo.setText(credentials);
    }

    public void getClasses(View view){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("UserSchedules", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        List<String> classes = (List<String>)data.get("classes");
                        String toastString = "classes: ";
                        for(int i = 0; i < classes.size(); i++){
                            toastString += (" " + classes.get(i));
                        }
                        Toast.makeText(UserScheduleActivity.this, toastString, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Log.d("UserSchedules", "Nothing");
                    }
                }
                else{
                    Log.d("UserSchedule", "get failed");
                }
            }
        });
    }


    public void getMeetTimes(View view){
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("UserSchedules", "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        List<Map<String, String>> meetings = (List<Map<String, String>>)data.get("weeklyMeetTimes");
                        String toastString = "meetings:\n";
                        for(int i = 0; i < meetings.size(); i++){
                            Map<String, String> meetTime = meetings.get(i);
                            String course = meetTime.get("course");
                            String days = meetTime.get("days");
                            String periodBegin = meetTime.get("periodBegin");
                            String periodEnd = meetTime.get("periodEnd");
                            toastString += (i + ":\n" + course + "\n" + days + " " + periodBegin + "-" + periodEnd);
                        }
                        Toast.makeText(UserScheduleActivity.this, toastString, Toast.LENGTH_LONG).show();
                    }
                    else{
                        Log.d("UserSchedules", "Nothing");
                    }
                }
                else{
                    Log.d("UserSchedule", "get failed");
                }
            }
        });
    }
}
