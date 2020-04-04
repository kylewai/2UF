package com.example.kylewai.a2uf.mockList;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.individualMockActivity.MockScheduleActivity;
import com.example.kylewai.a2uf.userSchedule.AddClassPager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.GridLayout.HORIZONTAL;
import static android.widget.GridLayout.VERTICAL;


//This fragment displays the mock list
public class MockListFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    MockListAdapter adapter;
    private String uid;

    public MockListFragment(){
        //Required no arg constructor
    }

    public static MockListFragment newInstance(String uid) {
        // Required empty public constructor
        MockListFragment mockFrag = new MockListFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        mockFrag.setArguments(args);
        return mockFrag;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.uid = getArguments().getString("uid");
        db = FirebaseFirestore.getInstance();
//        String uid = getArguments().getString("uid");
        View view = inflater.inflate(R.layout.fragment_mock_list, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAlertDialog().show();
            }
        });


        Query query = db.collection("userMocks").document(this.uid).collection("mockInfo").orderBy("dateCreated", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<UserMock> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<UserMock>()
            .setQuery(query, UserMock.class)
            .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserMock mock = document.toObject(UserMock.class);
                    }
                } else {
                    Log.d("MockListFragment", "Error getting documents: ", task.getException());
                }
            }
        });
        Log.d("MockListFragment","done");
        adapter = new MockListAdapter(firestoreRecyclerOptions);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }


    private AlertDialog.Builder setUpAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Create New Mock");
        alertDialog.setMessage("Name");
        EditText edit = new EditText(getContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edit.setLayoutParams(param);
        alertDialog.setView(edit);

        alertDialog.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mockName = edit.getText().toString();
                        db.collection("userMocks").document(uid).collection("mockInfo").whereEqualTo("mockName", mockName)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            QuerySnapshot d = task.getResult();
                                            if(d.isEmpty()){
                                                DocumentReference doc = db.collection("userMocks").document(uid).collection("mockInfo").document();
                                                UserMock mock = new UserMock(new ArrayList<String>(), Timestamp.now(), "", mockName, new ArrayList<Map<String, String>>());
                                                doc.set(mock)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("NewMock", "Successfully created new mock");
                                                                String mockId = doc.getId();
                                                                Intent intent = new Intent(getContext(), MockScheduleActivity.class);
                                                                intent.putExtra("userMock", mock);
                                                                intent.putExtra("mockId", mockId);
                                                                getContext().startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("NewMock", "Failed to create new mock");
                                                                return;
                                                            }
                                                        });
                                            }
                                            else{
                                                Toast.makeText(getContext(), "You already have a mock with that name", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            Log.d("NewMock", "Checking name task failed");
                                        }
                                    }
                                });
                    }
                }
        );

        alertDialog.setNegativeButton("Cancel",
                new Dialog.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){
                        dialogInterface.cancel();
                    }
                }
        );
        return alertDialog;
    }

}
