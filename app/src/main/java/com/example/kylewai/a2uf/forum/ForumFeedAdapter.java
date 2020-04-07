package com.example.kylewai.a2uf.forum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kylewai.a2uf.MainActivity;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.individualPostActivity.PostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

public class ForumFeedAdapter extends FirestoreRecyclerAdapter<Post, ForumFeedAdapter.FeedPostViewHolder> {

    private boolean multipleSelection = false;
    private List<String> selected = new ArrayList<>();
    private ActionMode.Callback contextualOptions;
    private Context context;

    public ForumFeedAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        initContextualOptions();
    }

    private void initContextualOptions(){
        contextualOptions = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                menu.add("Favorite");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if(item.getItemId() == R.id.favorite_post){
                    for(String postId : selected){
                        //Add to favorites
                    }
                }
                mode.finish();
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
    protected void onBindViewHolder(@NonNull ForumFeedAdapter.FeedPostViewHolder holder, int position, @NonNull Post model) {
        holder.setPost(model);
    }

    @NonNull
    @Override
    public ForumFeedAdapter.FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_item, parent, false);
        return new FeedPostViewHolder(view);
    }

    class FeedPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView author;
        TextView description;
        TextView major;
        TextView dateCreated;
        TextView likes;
        ImageView thumbs;
        Post mPost;

        public FeedPostViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            major = itemView.findViewById(R.id.major);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            itemView.setBackgroundColor(Color.parseColor("#7db5fa"));
            likes = itemView.findViewById(R.id.num_likes);
            thumbs = itemView.findViewById(R.id.like_button);
            thumbs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("posts").document(mPost.getDocumentId()).update("likes", FieldValue.increment(1));
                }
            });
        }

        void setPost(Post post){
            mPost = post;
            title.setText(post.getTitle());
            author.setText("@" + post.getAuthor());
            description.setText(post.getDescription());
            major.setText(post.getMajor());
            likes.setText(String.valueOf(post.getLikes()));

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
            Intent intent = new Intent(view.getContext(), PostActivity.class);
            intent.putExtra("postObject", mPost);
            view.getContext().startActivity(intent);
        }
    }
}
