package com.example.myfragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfragment.R;
import com.example.myfragment.bean.Function;

import org.w3c.dom.Text;

import java.util.List;

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder> {
    List<Function> functionList;

    public FunctionAdapter(List<Function> functionList) {
        this.functionList = functionList;
    }

    @NonNull
    @Override
    public FunctionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_function, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FunctionAdapter.MyViewHolder holder, int position) {
        Function function = functionList.get(position);
        holder.imageView.setImageResource(function.getFunctionIcon());
        holder.textView.setText(function.getFunctionName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行点击事件进行不同的操作
            }
        });
    }

    @Override
    public int getItemCount() {
        return functionList == null ? 0 : functionList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_Function);
            textView = itemView.findViewById(R.id.textView_FunctionName);
            view = itemView;
        }
    }
}
