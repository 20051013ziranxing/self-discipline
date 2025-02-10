package com.example.myfragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfragment.R;
import com.example.myfragment.bean.NewFunction;

import java.util.List;

public class AddFounctionAdapter extends RecyclerView.Adapter<AddFounctionAdapter.MyViewHolder> {
    List<NewFunction> newFunctionList;

    public AddFounctionAdapter(List<NewFunction> newFunctionList) {
        this.newFunctionList = newFunctionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_add_function, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewFunction newFunction = newFunctionList.get(position);
        holder.imageView.setImageResource(newFunction.getNewFunctionIcon());
        holder.textView.setText(newFunction.getNewFunctionName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行新功能的单击事件，使用Arouter进行跳转
            }
        });
    }

    @Override
    public int getItemCount() {
        return newFunctionList==null ? 0 : newFunctionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_addFunction);
            textView = itemView.findViewById(R.id.textView_addFunction);
            view = itemView;
        }
    }
}
