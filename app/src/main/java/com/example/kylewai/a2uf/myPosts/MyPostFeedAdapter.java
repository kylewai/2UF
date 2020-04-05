package com.example.kylewai.a2uf.myPosts;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;
import com.example.kylewai.a2uf.myPosts.PostActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostFeedAdapter extends FirestoreRecyclerAdapter<Post, MyPostFeedAdapter.FeedPostViewHolder> {

    public MyPostFeedAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyPostFeedAdapter.FeedPostViewHolder holder, int position, @NonNull Post model) {
        holder.setPost(model);
    }

    @NonNull
    @Override
    public MyPostFeedAdapter.FeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_item, parent, false);
        return new FeedPostViewHolder(view);
    }

    class FeedPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView author;
        TextView description;
        TextView major;
        TextView dateCreated;
        Post mPost;

        public FeedPostViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            major = itemView.findViewById(R.id.major);
            dateCreated = itemView.findViewById(R.id.dateCreated);
        }

        void setPost(Post post){
            mPost = post;
            title.setText(post.getTitle());
            author.setText("@" + post.getAuthor());
            description.setText(post.getDescription());
            major.setText(post.getMajor());

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
