package com.example.kylewai.a2uf.mockList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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

        View view = inflater.inflate(R.layout.fragment_mock_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

}
