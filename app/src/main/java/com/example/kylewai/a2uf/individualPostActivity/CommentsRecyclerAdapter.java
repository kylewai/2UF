package com.example.kylewai.a2uf.individualPostActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Comment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsRecyclerAdapter extends FirestoreRecyclerAdapter<Comment, CommentsRecyclerAdapter.CommentViewHolder> {

    Context context;

    public CommentsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentsRecyclerAdapter.CommentViewHolder holder, int position, @NonNull Comment model) {
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(holder.itemView.getResources(), R.color.pewter, null));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.setComment(model);
    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        Comment comment;
        TextView author;
        TextView content;
        TextView dateCreated;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            dateCreated = itemView.findViewById(R.id.dateCreated);
        }

        void setComment(Comment comment){
            this.comment = comment;
            author.setText("@" + comment.getAuthor());
            content.setText(comment.getText());
            dateCreated.setText(timeStampToTime(comment.getDateCreated()));
        }

        private String timeStampToTime(Timestamp dateCreated){
            Date date = dateCreated.toDate();
            Date currDate = new Date();
            long millisElapsed = currDate.getTime() - date.getTime();
            String timeElapsed;
            long unitsElapsed = millisElapsed;
            if((unitsElapsed = unitsElapsed / 1000) < 60){
                timeElapsed = unitsElapsed + "s";
            }
            else if((unitsElapsed = unitsElapsed / 60) < 60){
                timeElapsed = unitsElapsed + "m";
            }
            else if((unitsElapsed = unitsElapsed / 60) < 24){
                timeElapsed = unitsElapsed + "h";
            }
            else if((unitsElapsed = unitsElapsed / 24) < 365){
                timeElapsed = unitsElapsed + "d";
            }
            else{
                unitsElapsed = unitsElapsed / 365;
                timeElapsed = unitsElapsed + "y";
            }
            return timeElapsed;
        }
    }
}
