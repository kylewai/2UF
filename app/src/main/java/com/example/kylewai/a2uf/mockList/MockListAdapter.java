package com.example.kylewai.a2uf.mockList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.MainActivity;
import com.example.kylewai.a2uf.individualMockActivity.MockScheduleActivity;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

//RecyclerView adapter for the mock list
public class MockListAdapter extends FirestoreRecyclerAdapter<UserMock, MockListAdapter.MockListViewHolder> {

    private boolean multipleSelection = false;
    private List<String> selected = new ArrayList<>();
    private ActionMode.Callback contextualOptions;
    private Context context;
    boolean favoritesFilter;
    static ActionMode actionMode;


    public MockListAdapter(@NonNull FirestoreRecyclerOptions<UserMock> options, Context context, boolean favorites) {
        super(options);
        if(actionMode != null){
            actionMode.finish();
        }
        this.context = context;
        this.favoritesFilter =favorites;
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
                    if(favoritesFilter){
                        for(String id : selected){
                            //Add to favorites
                            db.collection("userMocks").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("mockInfo")
                                    .document(id)
                                    .update("favorite", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        Toast.makeText(context, "Unfavorited", Toast.LENGTH_LONG).show();
                    }
                    else {
                        for (String id : selected) {
                            //Add to favorites
                            db.collection("userMocks").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("mockInfo")
                                    .document(id)
                                    .update("favorite", true)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                    }
                }
                else if(item.getTitle() == "Delete"){
                    for(String id : selected) {
                        db.collection("userMocks").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("mockInfo")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
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
    protected void onBindViewHolder(@NonNull MockListAdapter.MockListViewHolder holder, int position, @NonNull UserMock model) {
//        if(position % 2 == 0){
//            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(holder.itemView.getResources(), R.color.pewter, null));
//        }
//        else{
//            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }
        holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.setMock(model, getSnapshots().getSnapshot(position).getId());
    }

    @NonNull
    @Override
    public MockListAdapter.MockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mocklist_item, parent, false);
        return new MockListViewHolder(view);
    }



    class MockListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView nameView;
        TextView dateCreated;
        CheckBox checkBox;
        UserMock userMock;
        String mockId;
        ImageView star;
        public MockListViewHolder(final View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            nameView = itemView.findViewById(R.id.name);
            dateCreated = itemView.findViewById(R.id.dateCreated);
            checkBox = itemView.findViewById(R.id.checkBox);
            star = itemView.findViewById(R.id.star);
        }

        void setMock(UserMock userMock, String mockId){
            this.mockId = mockId;

            if(multipleSelection == false) {
                checkBox.setVisibility(View.INVISIBLE);
            }
            else {
                if(selected.contains(mockId)){
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                }
                else{
                    checkBox.setVisibility(View.INVISIBLE);
                    checkBox.setChecked(false);
                }
            }
            this.userMock = userMock;
            nameView.setText(userMock.getMockName());

            if(userMock.getFavorite()){
                star.setVisibility(View.VISIBLE);
            }
            Timestamp timeStamp = userMock.getDateCreated();
            Date date = timeStamp.toDate();
            Date currDate = new Date();
            long millisElapsed = currDate.getTime() - date.getTime();
            String timeElapsed;
            long unitsElapsed = millisElapsed;
            if((unitsElapsed = unitsElapsed / 1000) < 60){
                timeElapsed = "Created " + unitsElapsed + " seconds ago";
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
            //-----Parse date object!!!!
        }

        @Override
        public void onClick(View view) {
            if(multipleSelection){
                addSelected(mockId);
            }
            else {
                Intent intent = new Intent(view.getContext(), MockScheduleActivity.class);
                intent.putExtra("userMock", userMock);
                intent.putExtra("mockId", mockId);
                view.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if(!multipleSelection) {
                actionMode = ((AppCompatActivity) view.getContext()).startSupportActionMode(contextualOptions);
                multipleSelection = true;
                addSelected(mockId);
            }
            else{
                addSelected(mockId);
            }
            return true;
        }

        private void addSelected(String mockId){

            if(selected.contains(mockId)){
                selected.remove(mockId);
                checkBox.setChecked(false);
                checkBox.setVisibility(View.INVISIBLE);
            }
            else{
                selected.add(mockId);
                checkBox.setChecked(true);
                checkBox.setVisibility(View.VISIBLE);
            }
        }
    }
}
