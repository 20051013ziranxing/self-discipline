package com.example.todofragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todofragment.LineView;
import com.example.todofragment.R;
import com.example.todofragment.bean.ToDoThing;

import java.util.List;

public class RecyclerViewToDoAdapter extends RecyclerView.Adapter<RecyclerViewToDoAdapter.MyViewHolder> {
    List<ToDoThing> toDoThings;

    public RecyclerViewToDoAdapter(List<ToDoThing> toDoThings) {
        this.toDoThings = toDoThings;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_show_todo_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoThing toDoThing = toDoThings.get(position);
        holder.checkBox.setChecked(toDoThing.getThingFinish());
        holder.textView_ThingName.setText(toDoThing.getThingName());
        holder.imageView_ThingGradle.setImageResource(GetThingGradle(toDoThing.getThingGradle()));
        holder.textView_ThingTimes.setText(toDoThing.getThingTime());
        if (ThingNeedTime(toDoThing.getThingTime())) {
            holder.imageButton_ThingTime.setImageResource(R.drawable.fanqie);
        } else {
            holder.imageButton_ThingTime.setVisibility(View.GONE);
        }
        if (toDoThing.getThingFinish()) {
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    holder.lineView.setLineLength(interpolatedTime * holder.textView_ThingName.getWidth());
                }
            };
            animation.setDuration(1000);
            holder.lineView.startAnimation(animation);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    Animation animation = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            super.applyTransformation(interpolatedTime, t);
                            holder.lineView.setLineLength(interpolatedTime * holder.textView_ThingName.getWidth());
                        }
                    };
                    animation.setDuration(1000);
                    holder.lineView.startAnimation(animation);
                    holder.lineView.setVisibility(View.VISIBLE);
                } else {
                    holder.lineView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoThings == null ? 0 : toDoThings.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textView_ThingName;
        ImageView imageView_ThingGradle;
        ImageButton imageButton_ThingTime;
        TextView textView_ThingTimes;
        View view;
        LineView lineView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.recyclerView_ToDoFragment_show_checkBox);
            textView_ThingName = itemView.findViewById(R.id.TextView_ToDoFragment_show_ThingName);
            imageView_ThingGradle = itemView.findViewById(R.id.imageView_ToDoFragment_show_ThingGradle);
            imageButton_ThingTime = itemView.findViewById(R.id.imageView_ToDoFragment_show_ThingIcon);
            textView_ThingTimes = itemView.findViewById(R.id.TextView_ToDoFragment_show_ThingTimes_daoji);
            lineView = itemView.findViewById(R.id.lineView);
            view = itemView;
        }
    }
    public int GetThingGradle(String s1) {
        if (s1.equals("一级")) {
            return R.drawable.one_1;
        } else if (s1.equals("二级")) {
            return R.drawable.two_2;
        } else if (s1.equals("三级")) {
            return R.drawable.three_3;
        } else if (s1.equals("四级")) {
            return R.drawable.four;
        }
        return R.drawable.four;
    }
    public boolean ThingNeedTime(String s1) {
        if (s1.equals("不计时")) {
            return false;
        }
        return true;
    }
}
