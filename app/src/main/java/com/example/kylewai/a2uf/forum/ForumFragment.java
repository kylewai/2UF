package com.example.kylewai.a2uf.forum;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.makePostActivity.MakePostActivity;
import com.example.kylewai.a2uf.mockList.MockListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.VERTICAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {
    RecyclerView recyclerView;
    private FirebaseFirestore db;
    ForumFeedAdapter adapter;
    Spinner spinner;
    TextView makePost;

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
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        initRecyclerView(view);

        spinner = view.findViewById(R.id.spinner);
        ArrayList<String> filters = new ArrayList<>();
        filters.add("Latest");
        filters.add("Top");
        filters.add("Favorites");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filters);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                if(name.equals("Latest")){
                    Log.i("MockListFrag", "latest");
                    Query query = db.collection("posts").orderBy("dateCreated", Query.Direction.DESCENDING);
                    FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post.class)
                            .build();
                    adapter.stopListening();
                    adapter = new ForumFeedAdapter(firestoreRecyclerOptions, getContext(), false);
                    adapter.startListening();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
                else if(name.equals("Top")){
                    Query query = db.collection("posts").orderBy("likes", Query.Direction.DESCENDING);

                    FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                            .setQuery(query, Post.class)
                            .build();
                    adapter.stopListening();
                    adapter = new ForumFeedAdapter(firestoreRecyclerOptions, getContext(), false);
                    adapter.startListening();
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);
                }
                else{
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        AppUser user = doc.toObject(AppUser.class);
                                        String username = user.getUsername();
                                        List<String> favoritePosts = user.getFavoritePosts();
                                        if(favoritePosts.isEmpty()){
                                            favoritePosts.add("aaaaaaaa");
                                        }
                                        Query query = db.collection("posts")
                                                .whereEqualTo("author", username)
                                                .whereIn(FieldPath.documentId(), favoritePosts).orderBy("dateCreated", Query.Direction.DESCENDING);
                                        FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                                                .setQuery(query, Post.class)
                                                .build();
                                        adapter.stopListening();
                                        adapter = new ForumFeedAdapter(firestoreRecyclerOptions, getContext(), true);
                                        adapter.startListening();
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        makePost = view.findViewById(R.id.makePost);
        makePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MakePostActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void initRecyclerView(View view){
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("posts").orderBy("dateCreated", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();
        adapter = new ForumFeedAdapter(firestoreRecyclerOptions, getContext(), false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
