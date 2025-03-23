package com.example.thematicSection.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thematicSection.ColorString;
import com.example.thematicSection.R;
import com.example.thematicSection.bean.ColorChoose;

import java.util.List;

public class RecyclerViewAdapterColor extends RecyclerView.Adapter<RecyclerViewAdapterColor.MyViewHolder> {
    List<ColorChoose> colors;
    RecyclerViewAdapterColorListen recyclerViewAdapterColorListen;

    public RecyclerViewAdapterColor(List<ColorChoose> colors, RecyclerViewAdapterColorListen recyclerViewAdapterListing) {
        this.colors = colors;
        this.recyclerViewAdapterColorListen = recyclerViewAdapterListing;
    }

    public interface RecyclerViewAdapterColorListen {
        void choose(String color);
    }
    @NonNull
    @Override
    public RecyclerViewAdapterColor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_color_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterColor.MyViewHolder holder, int position) {
        ColorChoose colorChoose = colors.get(position);
        holder.imageView_in.setColorFilter(Color.parseColor(colorChoose.getColor()));
        holder.imageButton_out.setColorFilter(Color.parseColor(colorChoose.getColor()));
        if (colorChoose.isSelected()) {
            holder.imageButton_out.setVisibility(View.VISIBLE);
        } else {
            holder.imageButton_out.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ColorChoose color : colors) {
                    color.setSelected(false);
                }
                colors.get(position).setSelected(true);
                notifyDataSetChanged();
                ColorString.getInstance().setMyColorString(colorChoose.getColor());
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors == null ? 0 : colors.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_in;
        ImageButton imageButton_out;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView_in = itemView.findViewById(R.id.imageView_circle_in);
            imageButton_out = itemView.findViewById(R.id.imageView_circle_out);
            view = itemView;
        }
    }
}
