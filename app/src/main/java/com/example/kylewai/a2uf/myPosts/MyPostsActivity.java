package com.example.kylewai.a2uf.myPosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.forum.ForumFeedAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import static android.widget.GridLayout.VERTICAL;

public class MyPostsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyPostFeedAdapter adapter;
    FirebaseFirestore db;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_my_posts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("My Posts");
        initRecyclerView();

        spinner = findViewById(R.id.spinner);

        initSpinner();

    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null) {
            adapter.startListening();
        }
    }

    private void initRecyclerView(){
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            AppUser user = doc.toObject(AppUser.class);
                            String username = user.getUsername();
                            Query query = db.collection("posts").whereEqualTo("author", username).orderBy("dateCreated", Query.Direction.DESCENDING);
                            FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                                    .setQuery(query, Post.class)
                                    .build();
                            adapter = new MyPostFeedAdapter(firestoreRecyclerOptions, MyPostsActivity.this, false);
                            adapter.startListening();
                            recyclerView = findViewById(R.id.recyclerview);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
                            recyclerView.setAdapter(adapter);
                            recyclerView.setHasFixedSize(false);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(MyPostsActivity.this, VERTICAL);
                            recyclerView.addItemDecoration(dividerItemDecoration);
                        }
                    }
                });
    }

    private void initSpinner(){
        ArrayList<String> filters = new ArrayList<>();
        filters.add("Latest");
        filters.add("Favorites");
        filters.add("Top");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filters);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                if(name.equals("Latest")){
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        AppUser user = doc.toObject(AppUser.class);
                                        String username = user.getUsername();
                                        Query query = db.collection("posts").whereEqualTo("author", username).orderBy("dateCreated", Query.Direction.DESCENDING);
                                        FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                                                .setQuery(query, Post.class)
                                                .build();
                                        adapter.stopListening();
                                        adapter = new MyPostFeedAdapter(firestoreRecyclerOptions, MyPostsActivity.this, false);
                                        adapter.startListening();
                                        Log.d("MyPosty", "Listening");
                                        recyclerView = findViewById(R.id.recyclerview);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            });
                }
                else if(name.equals("Favorites")){
                    db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot doc = task.getResult();
                                        AppUser user = doc.toObject(AppUser.class);
                                        String username = user.getUsername();
                                        List<String> favoritePosts = user.getFavoriteMyPosts();
                                        if(favoritePosts == null){
                                            favoritePosts = new ArrayList<>();
                                        }
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
                                        adapter = new MyPostFeedAdapter(firestoreRecyclerOptions, MyPostsActivity.this, true);
                                        adapter.startListening();
                                        recyclerView = findViewById(R.id.recyclerview);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            });
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
                                        Query query = db.collection("posts")
                                                .whereEqualTo("author", username)
                                                .orderBy("likes", Query.Direction.DESCENDING);
                                        FirestoreRecyclerOptions<Post> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Post>()
                                                .setQuery(query, Post.class)
                                                .build();
                                        adapter.stopListening();
                                        adapter = new MyPostFeedAdapter(firestoreRecyclerOptions, MyPostsActivity.this, false);
                                        adapter.startListening();
                                        recyclerView = findViewById(R.id.recyclerview);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(MyPostsActivity.this));
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
    }
}
