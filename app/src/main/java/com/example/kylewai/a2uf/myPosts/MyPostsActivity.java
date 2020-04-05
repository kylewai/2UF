package com.example.kylewai.a2uf.myPosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.forum.ForumFeedAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.widget.GridLayout.VERTICAL;

public class MyPostsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyPostFeedAdapter adapter;
    FirebaseFirestore db;
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
                            adapter = new MyPostFeedAdapter(firestoreRecyclerOptions);
                            recyclerView = findViewById(R.id.recyclerview);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
                            recyclerView.addItemDecoration(dividerItemDecoration);
                            adapter.startListening();
                        }
                    }
                });
    }
}
