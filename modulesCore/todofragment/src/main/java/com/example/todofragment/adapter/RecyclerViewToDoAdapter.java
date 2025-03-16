package com.example.todofragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.todofragment.LineView;
import com.example.todofragment.R;
import com.example.todofragment.ToDoFragment;
import com.example.todofragment.bean.GetToDoThings;

import java.util.List;

public class RecyclerViewToDoAdapter extends RecyclerView.Adapter<RecyclerViewToDoAdapter.MyViewHolder> {
    private GestureDetector gestureDetector;
    private RecyclerViewToDoAdapterListener listener;
    public interface RecyclerViewToDoAdapterListener {

        void markComplete(String id, boolean checked);
        void modifyTheBinding();
        void deleteAnAgencyEvent(String id);
        void modifyTheToDoInformation(String id, String title);
    }
    private static final String TAG = "TestTT_RecyclerViewToDoAdapter";
    List<GetToDoThings.GetToDothingMessage> toDoThings;

    public RecyclerViewToDoAdapter(List<GetToDoThings.GetToDothingMessage> toDoThings, RecyclerViewToDoAdapterListener listener, ToDoFragment toDoFragment) {
        this.toDoThings = toDoThings;
        this.listener = listener;
        gestureDetector = new GestureDetector(toDoFragment.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "检测到单击事件");
                return true;
            }
        });
    }

    public void setToDoThings(List<GetToDoThings.GetToDothingMessage> toDoThings) {
        /*Log.d(TAG, String.valueOf(this.toDoThings.size()));*/
        this.toDoThings = toDoThings;
        /*Log.d(TAG, String.valueOf(toDoThings.size()));*/
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_show_todo_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetToDoThings.GetToDothingMessage toDoThing = toDoThings.get(position);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, String.valueOf(v.getId()));
                if (v.getId() != holder.delete_the_to_do_button_cardView.getId() && v.getId() != holder.modify_the_proxy_information_button_cardView.getId()) {
                    listener.modifyTheBinding();
                }
                holder.delete_the_to_do_button_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "点击了+++" + toDoThing.getId());
                        listener.deleteAnAgencyEvent(toDoThing.getId());
                    }
                });
                holder.modify_the_proxy_information_button_cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.modifyTheToDoInformation(toDoThing.getId(), toDoThing.getTitle());
                    }
                });
                return false;
            }
        });
        boolean isFinish;
        if (toDoThing.getStatus().equals("pending")) {
            isFinish = false;
        } else {
            isFinish = true;
        }
        holder.checkBox.setChecked(isFinish);
        holder.textView_ThingName.setText(toDoThing.getTitle());
        String description = toDoThing.getDescription();
        String[] result = description.split(",");
        holder.imageView_ThingGradle.setImageResource(GetThingGradle(result[0]));
        if (result[1].equals("倒计时")) {
            holder.textView_ThingTimes.setText(result[1] + "," + result[2]);
        } else {
            holder.textView_ThingTimes.setText(result[1]);
        }
        if (ThingNeedTime(result[1])) {
            holder.imageButton_ThingTime.setImageResource(R.drawable.fanqie);
        } else {
            holder.imageButton_ThingTime.setVisibility(View.GONE);
        }
        holder.imageButton_ThingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, result[1] + "," + result[2]);
                ARouter.getInstance().build("/PomodoroTimerActivity/PomodoroTimerActivity")
                        .withString("pomodoro", result[1] + "," + result[2])
                        .navigation();
            }
        });
        if (toDoThing.getStatus().equals("completed")) {
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
                listener.markComplete(toDoThings.get(position).getId().toString(), holder.checkBox.isChecked());
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
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout_main;
        public ConstraintLayout constraintLayout_action;
        CheckBox checkBox;
        TextView textView_ThingName;
        ImageView imageView_ThingGradle;
        ImageButton imageButton_ThingTime;
        TextView textView_ThingTimes;
        public CardView delete_the_to_do_button_cardView;
        public CardView modify_the_proxy_information_button_cardView;
        public boolean isAnimationRunning = false;
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
            constraintLayout_main = itemView.findViewById(R.id.main_content);
            constraintLayout_action = itemView.findViewById(R.id.action_layout);
            delete_the_to_do_button_cardView = itemView.findViewById(R.id.delete_the_to_do_button_cardView);
            modify_the_proxy_information_button_cardView = itemView.findViewById(R.id.modify_the_proxy_information_button_cardView);
            view = itemView;
        }
    }
    public int GetThingGradle(String s1) {
        if (s1.equals("1级")) {
            return R.drawable.one_1;
        } else if (s1.equals("2级")) {
            return R.drawable.two_2;
        } else if (s1.equals("3级")) {
            return R.drawable.three_3;
        } else if (s1.equals("4级")) {
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
