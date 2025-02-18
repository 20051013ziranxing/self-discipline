package com.example.clockinfragment.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clockinfragment.R;
import com.example.clockinfragment.bean.TestBead;

import java.util.List;

public class ClockInRecyclerAdapter extends RecyclerView.Adapter<ClockInRecyclerAdapter.ViewHolder> {
    //对象的引用，以后要与后端对接转为后端获取的数据对象
    List<TestBead> testBeads;

    public ClockInRecyclerAdapter(List<TestBead> testBeads) {
        this.testBeads = testBeads;
    }

    @NonNull
    @Override
    public ClockInRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clockin_recycler_item, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClockInRecyclerAdapter.ViewHolder holder, int position) {
        TestBead testBead = testBeads.get(position);
        holder.textView_Name.setText(testBead.getName());
        holder.textView_progress.setText("已完成 " + testBead.getFinishCount() + "/" + testBead.getSumCount());
        holder.imageButton.setImageResource(testBead.getIconId());
        int color = Color.parseColor(ChoiceBackgroundColor(testBead.getIconId()));
        holder.imageButton.setBackgroundColor(color);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此时代表完成一次，则进度条与完成情况都要发生相应的改变
            }
        });
    }

    @Override
    public int getItemCount() {
        return testBeads == null ? 0 : testBeads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton imageButton;
        TextView textView_Name;
        TextView textView_progress;
        View view;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.clockIn_recycler_imageView_11);
            textView_Name = itemView.findViewById(R.id.clockIn_recycler_TextView_Name);
            textView_progress = itemView.findViewById(R.id.clockIn_recycler_TextView_progress);
            view = itemView;
            cardView = itemView.findViewById(R.id.finish_once_button);
        }
    }

    //存储的时候应存储对应的图标，根据图标选择背景图的颜色
    public String ChoiceBackgroundColor(int type) {
        if (type == R.drawable.walter) {
            return "#ADD8E6";
        } else if (type == R.drawable.morning) {
            return "#FFFBEA";
        } else if (type == R.drawable.night) {
            return "#D8BFD8";
        } else if (type == R.drawable.firut) {
            return "#CAD1B7";
        } else if (type == R.drawable.exercise) {
            return "#ADD8E6";
        } else if (type == R.drawable.word) {
            return "#D2D2E6";
        }
        return "#ADD8E6";
    }
}
