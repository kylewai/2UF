package com.example.kylewai.a2uf.myPosts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostFeedAdapter extends FirestoreRecyclerAdapter<Post, MyPostFeedAdapter.FeedPostViewHolder> {

    private boolean multipleSelection = false;
    private List<String> selected = new ArrayList<>();
    private ActionMode.Callback contextualOptions;
    private Context context;
    static ActionMode actionMode;
    private boolean favoritesFilter;

    public MyPostFeedAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context, boolean favorite) {
        super(options);
        if(actionMode != null){
            Log.d("MockListAdapty", "huh");
            actionMode.finish();
        }
        this.context = context;
        this.favoritesFilter = favorite;
        initContextualOptions();
    }

    private void initContextualOptions(){
        contextualOptions = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuItem fav_item = menu.add("Favorite");
                fav_item.setIcon(AppCompatResources.getDrawable(context, R.drawable.favorite_icon));
                MenuItem delete_item = menu.add("Delete");
                delete_item.setIcon(AppCompatResources.getDrawable(context, R.drawable.delete_icon));
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(item.getTitle() == "Favorite"){
                    //Unfavorite
                    if(favoritesFilter) {
                        for(String postId : selected) {
                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("favoriteMyPosts", FieldValue.arrayRemove(postId))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            notifyDataSetChanged();
                                        }
                                    });
                        }
                        Toast.makeText(context, "Unfavorited", Toast.LENGTH_LONG).show();
                    }
                    //Favorite
                    else {
                        for (String postId : selected) {
                            //Add to favorites
                            db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("favoriteMyPosts", FieldValue.arrayUnion(postId))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }
                                    });
                        }
                    }
                    mode.finish();
                }
                else if(item.getTitle() == "Delete"){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Delete Posts");

                    alertDialog.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    for (String postId : selected) {
                                        Log.d("MyPosty", "What");
                                        db.collection("posts").document(postId).delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        notifyDataSetChanged();
                                                    }
                                                });
                                    }
                                    mode.finish();
                                }
                            }
                    );

                    alertDialog.setNegativeButton("Cancel",
                            new Dialog.OnClickListener(){
                                public void onClick(DialogInterface dialogInterface, int i){
                                    dialogInterface.cancel();
                                    mode.finish();
                                }
                            }
                    );
                    alertDialog.show();
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selected.clear();
                multipleSelection = false;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onBindViewHolder(@NonNull MyPostFeedAdapter.FeedPostViewHolder holder, int position, @NonNull Post model) {
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(holder.itemView.getResources(), R.color.pewter, null));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.setPost(model);
    }

    @NonNull
    @Override
    public MyPostFeedAdapter.FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_item, parent, false);
        return new MyPostFeedAdapter.FeedPostViewHolder(view);
    }

    class FeedPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView title;
        TextView author;
        TextView description;
        TextView major;
        TextView dateCreated;
        TextView likes;
        ImageView thumbs;
        CheckBox checkBox;
        Post mPost;

        public FeedPostViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            major = itemView.findViewById(R.id.major);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            likes = itemView.findViewById(R.id.num_likes);
            thumbs = itemView.findViewById(R.id.like_button);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        void setPost(Post post){
            mPost = post;
            if(multipleSelection == false) {
                checkBox.setVisibility(View.INVISIBLE);
            }
            else {
                if(selected.contains(mPost.getDocumentId())){
                    Log.d("MyPosty", "Why here");
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                }
                else{
                    checkBox.setVisibility(View.INVISIBLE);
                    checkBox.setChecked(false);
                }
            }
            title.setText(post.getTitle());
            author.setText("@" + post.getAuthor());
            description.setText(post.getDescription());
            major.setText(post.getMajor());
            likes.setText(String.valueOf(post.getLikes()));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("likedPosts")
                    .document(mPost.getDocumentId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()){
                                    thumbs.setImageResource(R.drawable.liked_post);
                                }
                                else{
                                    thumbs.setImageResource(R.drawable.like);
                                }
                            }
                        }
                    });

            Timestamp timeStamp = post.getDateCreated();
            Date date = timeStamp.toDate();
            Date currDate = new Date();
            long millisElapsed = currDate.getTime() - date.getTime();
            String timeElapsed;
            long unitsElapsed = millisElapsed;
            if((unitsElapsed = unitsElapsed / 1000) < 60){
                timeElapsed = "Created " + unitsElapsed + "seconds ago";
            }
            else if((unitsElapsed = unitsElapsed / 60) < 60){
                timeElapsed = "Created " + unitsElapsed + " minutes ago";
            }
            else if((unitsElapsed = unitsElapsed / 60) < 24){
                timeElapsed = "Created " + unitsElapsed + " hours ago";
            }
            else if((unitsElapsed = unitsElapsed / 24) < 365){
                timeElapsed = "Created " + unitsElapsed + " days ago";
            }
            else{
                unitsElapsed = unitsElapsed / 365;
                timeElapsed = "Created " + unitsElapsed + " years ago";
            }
            dateCreated.setText(timeElapsed);
        }

        @Override
        public void onClick(View view) {
            if(multipleSelection){
                addSelected(mPost.getDocumentId());
            }
            else {
                Intent intent = new Intent(view.getContext(), PostActivity.class);
                intent.putExtra("postObject", mPost);
                view.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(!multipleSelection) {
                actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(contextualOptions);
                multipleSelection = true;
                addSelected(mPost.getDocumentId());
            }
            else{
                addSelected(mPost.getDocumentId());
            }
            return true;
        }

        private void addSelected(String postId){

            if(selected.contains(postId)){
                selected.remove(postId);
                checkBox.setChecked(false);
                checkBox.setVisibility(View.INVISIBLE);
            }
            else{
                selected.add(postId);
                checkBox.setChecked(true);
                checkBox.setVisibility(View.VISIBLE);
            }
        }
    }
}
