package com.example.kylewai.a2uf.makePostActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.MainActivity;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MakePostActivity extends AppCompatActivity{
    final private String TAG = "MakePostActivity";
    FirebaseFirestore db;
    Spinner spinner;
    TextView description;
    TextView title;
    TextView major;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Make post");

        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.spinner);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        major = findViewById(R.id.major);
        spinnerSetup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.send){
            Log.i("Posty", "Got here");
            String selectedSchedule = spinner.getSelectedItem().toString();
            String title_text = title.getText().toString();
            String desc_text = description.getText().toString();
            String major_text = major.getText().toString();
            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            AppUser user = doc.toObject(AppUser.class);
                            String username = user.getUsername();
                            if(selectedSchedule.equals("My Schedule")) {
                                List<Map<String, String>> weeklyMeetTimes = user.getWeeklyMeetTimes();
                                Post post = new Post(Timestamp.now(), title_text, desc_text, username, major_text, weeklyMeetTimes, 0);
                                db.collection("posts").document().set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MakePostActivity.this, "Post Sent", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MakePostActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(MakePostActivity.this, "ERROR while sending post", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                db.collection("userMocks").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("mockInfo")
                                        .whereEqualTo("mockName", selectedSchedule)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("Posty", "Got here");
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        UserMock mock = document.toObject(UserMock.class);
                                                        List<Map<String, String>> weeklyMeetTimes = mock.getWeeklyMeetTimes();
                                                        Post post = new Post(Timestamp.now(), title_text, desc_text, username, major_text, weeklyMeetTimes, 0);
                                                        db.collection("posts").document().set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(MakePostActivity.this, "Post Sent", Toast.LENGTH_LONG).show();
                                                                    Intent intent = new Intent(MakePostActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                else{
                                                                    Toast.makeText(MakePostActivity.this, "ERROR while sending post", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                            }
                        }
                        else{

                        }
                    }
                });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.make_post_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void spinnerSetup(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("userMocks").document(uid).collection("mockInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> spinnerItems = new ArrayList<>();
                            spinnerItems.add("My Schedule");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UserMock userMock = document.toObject(UserMock.class);
                                spinnerItems.add(userMock.getMockName());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (MakePostActivity.this, android.R.layout.simple_spinner_item, spinnerItems);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerArrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
