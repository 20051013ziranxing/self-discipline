package com.example.cancelyouraccount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cancelyouraccount.R;

import java.util.List;

public class RecyclerCancelTipAdapter extends RecyclerView.Adapter<RecyclerCancelTipAdapter.MyViewHolder> {
    List<String> stringList;

    public RecyclerCancelTipAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public RecyclerCancelTipAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cancel_tip_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerCancelTipAdapter.MyViewHolder holder, int position) {
        String tip = stringList.get(position);
        holder.textView.setText(tip);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recyclerView_reminderFor_textView);
        }
    }
}
