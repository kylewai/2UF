package com.example.kylewai.a2uf.mockList;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kylewai.a2uf.individualMockActivity.MockScheduleActivity;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//RecyclerView adapter for the mock list
public class MockListAdapter extends FirestoreRecyclerAdapter<UserMock, MockListAdapter.MockListViewHolder> {

    public MockListAdapter(@NonNull FirestoreRecyclerOptions<UserMock> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MockListAdapter.MockListViewHolder holder, int position, @NonNull UserMock model) {
        holder.setMock(model);
    }

    @NonNull
    @Override
    public MockListAdapter.MockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mocklist_item, parent, false);
        return new MockListViewHolder(view);
    }



    class MockListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView;
        TextView dateCreated;
        UserMock userMock;
        public MockListViewHolder(final View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            nameView = itemView.findViewById(R.id.name);
            dateCreated = itemView.findViewById(R.id.dateCreated);
        }

        void setMock(UserMock userMock){
            this.userMock = userMock;
            nameView.setText(userMock.getMockName());
            dateCreated.setText(userMock.getDateCreated().toString());
            //-----Parse date object!!!!
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MockScheduleActivity.class);
            intent.putExtra("userMock", userMock);
            view.getContext().startActivity(intent);
        }
    }
}
