package com.example.kylewai.a2uf.forum;


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
import android.widget.Spinner;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.mockList.MockListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.widget.GridLayout.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    ForumFeedAdapter adapter;
    Spinner filter;

    public ForumFragment() {
        // Required empty public constructor
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
        Query query = db.collection("posts").orderBy("dateCreated", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Post post = document.toObject(Post.class);
                        Log.d("ForumFrag", post.getTitle());
                    }
                }
                else{
                    Log.d("ForumFrag", "Query doesn't work");
                }
            }
        });
        adapter = new ForumFeedAdapter(firestoreRecyclerOptions);
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        filter = view.findViewById(R.id.spinner);

        return view;
    }

}
