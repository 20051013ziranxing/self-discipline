package com.example.thematicSection.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thematicSection.R;
import com.example.thematicSection.bean.Listing;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapterListing extends RecyclerView.Adapter<RecyclerViewAdapterListing.MyViewHolder> {
    private RecyclerViewAdapterListingListen listingListen;
    public interface RecyclerViewAdapterListingListen {
        void hhh();
    }
    List<Listing> listingList;
    int icon;

    public RecyclerViewAdapterListing(List<Listing> listingList, int icon, RecyclerViewAdapterListingListen listingListen) {
        this.listingListen = listingListen;
        this.listingList = listingList;
        this.icon = icon;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterListing.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_listing_item,  parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterListing.MyViewHolder holder, int position) {
        Listing listing = listingList.get(position);
        holder.textView_name.setText(listing.getName());
        holder.imageView.setImageResource(icon);
        holder.imageView.setColorFilter(Color.parseColor(listing.getColor()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加点击事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return listingList == null ? 0 : listingList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_name;
        ImageView imageView;
        View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.textView_nameChoice);
            imageView = itemView.findViewById(R.id.imageView_icon_listing);
            view = itemView;
        }
    }
}
