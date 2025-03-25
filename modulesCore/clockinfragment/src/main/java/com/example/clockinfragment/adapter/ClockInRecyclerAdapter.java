package com.example.clockinfragment.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clockinfragment.R;
import com.example.clockinfragment.StringFinder;
import com.example.clockinfragment.bean.CheckInBean;
import com.example.clockinfragment.myview.LineView;
import com.example.clockinfragment.myview.RectangleProgressBar;
import com.example.clockinfragment.singleton.DateSingleton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClockInRecyclerAdapter extends RecyclerView.Adapter<ClockInRecyclerAdapter.ViewHolder> {
    private static final String TAG = "TestTT_ClockInRecyclerAdapter";
    /*ClockInRecyclerAdapterListener listener;
    public interface ClockInRecyclerAdapterListener {

    }*/
    //对象的引用，以后要与后端对接转为后端获取的数据对象
    List<CheckInBean.CheckinData> checkinBeanList;
    String data;

    public void setCheckinBeanList(List<CheckInBean.CheckinData> checkinBeanList) {
        this.checkinBeanList = checkinBeanList;
    }

    public void setData(String data) {
        this.data = data;
    }

    private ClockInRecyclerAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int i, String data);
        void modifyTheClockInInformation(String title, String start_date, String end_date, int icon, int target_checkin_count, String motivation_message);
        void modifyTheAttendanceInformationByID(int id);
        void modifyTheBinding(String id, String start_time);

    }


    public ClockInRecyclerAdapter(List<CheckInBean.CheckinData> testBeads, String data, ClockInRecyclerAdapter.OnItemClickListener listener) {
        this.checkinBeanList = testBeads;
        this.listener = listener;
        this.data = data;
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
        CheckInBean.CheckinData checkinData = checkinBeanList.get(position);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        if (formattedDate.equals(DateSingleton.getInstance().getDate())) {
            Log.d(TAG, formattedDate + "==" + DateSingleton.getInstance().getDate());
            holder.textView_finish_tect.setText("结束");
        } else {
            holder.textView_finish_tect.setText("删除");
        }
        holder.textView_Name.setText(checkinData.getCheckin().getTitle());
        holder.textView_progress.setText("已完成 " + checkinData.getDecodedCheckinCount().get(data) + "/" + checkinData.getCheckin().getTarget_checkin_count());
        holder.imageButton.setImageResource(checkinData.getCheckin().getIcon());
        int color = Color.parseColor(StringFinder.getStringFromInt(checkinData.getCheckin().getIcon()));
        holder.rectangleProgressBar.setProgressBarColor(color);
        Log.d(TAG, data);
        Log.d(TAG, String.valueOf(checkinData.getDecodedCheckinCount().get(data)));
        holder.rectangleProgressBar.setProgress((float) (((float) (checkinData.getDecodedCheckinCount().get(data))) * 1.0 /
                        (checkinData.getCheckin().getTarget_checkin_count())));
        holder.imageButton.setBackgroundColor(color);
        holder.imageButton_finish_once_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行点击事件，使其添加一个
                Log.d(TAG, "执行了加以");
                listener.onItemClick(checkinData.getCheckin().getId(), data);
            }
        });

        holder.cardView_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.modifyTheBinding(String.valueOf(checkinData.getCheckin().getId()), checkinData.getCheckin().getStart_date());
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了整体");
                /*listener.modifyTheClockInInformation(checkinData.getCheckin().getTitle(), checkinData.getCheckin().getStart_date(),
                        checkinData.getCheckin().getEnd_date(), checkinData.getCheckin().getIcon(), checkinData.getCheckin().getTarget_checkin_count(),
                        checkinData.getCheckin().getMotivation_message());*/
                listener.modifyTheAttendanceInformationByID(checkinData.getCheckin().getId());
            }
        });
        if (checkinData.getDecodedCheckinCount().get(data) == checkinData.getCheckin().getTarget_checkin_count()) {
            Log.d(TAG, data + "ifFFF");
            Log.d(TAG, checkinData.getDecodedCheckinCount().get(data) + "==" + checkinData.getCheckin().getTarget_checkin_count());
            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    holder.lineView.setLineLength(interpolatedTime * holder.textView_Name.getWidth());
                }
            };
            animation.setDuration(1000);
            holder.lineView.startAnimation(animation);
            holder.imageButton_finish_once_button.setEnabled(false);
        } else {
            Log.d(TAG, data + "else");
            Log.d(TAG, checkinData.getDecodedCheckinCount().get(data) + "==" + checkinData.getCheckin().getTarget_checkin_count());
            holder.lineView.setLineLength(0);
            holder.imageButton_finish_once_button.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return checkinBeanList == null ? 0 : checkinBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView_A;
        public CardView cardView_B;
        ImageButton imageButton;
        TextView textView_Name;
        TextView textView_progress;
        View view;
        LineView lineView;
        CardView cardView;
        RectangleProgressBar rectangleProgressBar;
        ImageButton imageButton_finish_once_button;
        TextView textView_finish_tect;
        public boolean isAnimationRunning = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.clockIn_recycler_imageView_11);
            textView_Name = itemView.findViewById(R.id.clockIn_recycler_TextView_Name);
            textView_progress = itemView.findViewById(R.id.clockIn_recycler_TextView_progress);
            view = itemView;
            rectangleProgressBar = itemView.findViewById(R.id.rectangleProgressBar);
            cardView = itemView.findViewById(R.id.finish_once_button);
            lineView = itemView.findViewById(R.id.lineView);
            imageButton_finish_once_button = itemView.findViewById(R.id.imageButton_finish_once_button);
            cardView_A = itemView.findViewById(R.id.layout_A_item);
            cardView_B = itemView.findViewById(R.id.layout_B_item);
            textView_finish_tect = itemView.findViewById(R.id.textView_finish_tect);
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
