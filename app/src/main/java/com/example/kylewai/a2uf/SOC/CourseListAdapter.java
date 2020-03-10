package com.example.kylewai.a2uf.SOC;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.volleypojo.Section;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//RecyclerView adapter for SOC
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
    private final List<Section> sectionList;
    private LayoutInflater mInflater;

    class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView codeText;
        public final TextView nameText;
        public final TextView creditText;
        final CourseListAdapter mAdapter;
        private Section sectionObj;
        public CourseViewHolder(View courseItemView, CourseListAdapter adapter){
            super(courseItemView);
            codeText = courseItemView.findViewById(R.id.code);
            nameText = courseItemView.findViewById(R.id.name);
            creditText = courseItemView.findViewById(R.id.credit);
            this.mAdapter = adapter;
            this.sectionObj = null;
            courseItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

        }
    }

    public CourseListAdapter(Context context, List<Section> sectionList){
        this.sectionList = sectionList;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_item_view, parent, false);
        return new CourseViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Section mCurrent = this.sectionList.get(position);
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#65a7f7"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#7db5fa"));
        }
        holder.sectionObj = mCurrent;
        String code = mCurrent.getCode();
        String name = mCurrent.getName();
        String credit = mCurrent.getCredits();
        holder.codeText.setText(code);
        holder.nameText.setText(name);
        holder.creditText.setText("Credits " + credit);
        Log.i("Adapter", code);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public void update(List<Section> sectionList){
        Log.i("Adapter", sectionList.get(0).getCode());
        this.sectionList.addAll(sectionList);
        Log.i("Adapter", this.sectionList.size() + "");
        notifyDataSetChanged();
    }
}
