package com.example.clockinfragment.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clockinfragment.R;
import com.example.clockinfragment.bean.Icon;

import java.util.List;

public class AddHabitRecyclerViewAdapter extends RecyclerView.Adapter<AddHabitRecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "TestTT_AddHabitRecyclerViewAdapter";
    List<Icon> iconList;
    private OnItemClickListener listener;

    public AddHabitRecyclerViewAdapter(List<Icon> iconList, OnItemClickListener listener) {
        this.iconList = iconList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddHabitRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_habit_icon_all_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddHabitRecyclerViewAdapter.MyViewHolder holder, int position) {
        Icon icon = iconList.get(position);
        int color = Color.parseColor(icon.getIconBackground());
        holder.imageButton.setBackgroundColor(color);
        holder.imageButton.setImageResource(icon.getIconIn());
        Log.d(TAG, "点击了");
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了开始判空");
                if (listener != null) {
                    Log.d(TAG, "点击了hhh");
                    Log.d(TAG, String.valueOf(icon.getIconIn()));
                    listener.onItemClick(color, icon.getIconIn());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList == null ? 0 : iconList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageButton imageButton;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.recycler_AddHabit_IconAll_item_cardView);
            imageButton = itemView.findViewById(R.id.recycler_AddHabit_IconAll_item_ImageView);
            view = itemView;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int color, int iconIn);
    }
}
