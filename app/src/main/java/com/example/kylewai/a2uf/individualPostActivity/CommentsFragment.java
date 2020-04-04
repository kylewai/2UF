package com.example.kylewai.a2uf.individualPostActivity;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Comment;
import com.example.kylewai.a2uf.userSchedule.AddClassPager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.widget.GridLayout.VERTICAL;


public class CommentsFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    CommentsRecyclerAdapter adapter;
    Spinner filter;

    public CommentsFragment() {
        // Required empty public constructor
    }

    public static CommentsFragment newInstance(String postId){
        CommentsFragment commentsFragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putString("postId", postId);
        commentsFragment.setArguments(args);
        return commentsFragment;
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
        db = FirebaseFirestore.getInstance();
        String postId = getArguments().getString("postId");

        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "User Schedule", Toast.LENGTH_LONG);
                toast.show();

                //Creating and opening a new activity for adding classes.
                Intent intent = new Intent(getContext(), MakeCommentActivity.class);
                intent.putExtra("postId", postId);
                getContext().startActivity(intent);
            }
        });

        Query query = db.collection("posts").document(postId).collection("comments").orderBy("dateCreated", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Comment> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();
        adapter = new CommentsRecyclerAdapter(firestoreRecyclerOptions);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

}
