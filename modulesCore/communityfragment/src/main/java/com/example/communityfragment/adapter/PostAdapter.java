package com.example.communityfragment.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.communityfragment.R;
import com.example.communityfragment.bean.Post;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private List<Post> mPostList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.userAvatar.setImageResource(Integer.parseInt(mPostList.get(position).getUserAvatar()));
        holder.userName.setText(mPostList.get(position).getUserid());
        holder.postContent.setText(mPostList.get(position).getContent());
        holder.postLikeCount.setText(mPostList.get(position).getLikeConunt());
        if (mPostList.get(position).getImageUrl() != null && !mPostList.get(position).getImageUrl().equals("")) {
            Glide.with(holder.itemView.getContext())
                    .load(mPostList.get(position).getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .apply(RequestOptions.skipMemoryCacheOf(true)) //跳过内存缓存
                    .into(holder.postImg);
            holder.cvImg.setVisibility(View.VISIBLE);
            Log.d("CommunityModelTAG", mPostList.get(position).getImageUrl());
        } else {
            holder.cvImg.setVisibility(View.GONE);
        }


        if (mPostList.get(position).getIsLiked()) {
            holder.postLike.setImageResource(R.drawable.ic_liked);
        } else {
            holder.postLike.setImageResource(R.drawable.ic_like);
        }

        holder.cvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                Post currentPost = mPostList.get(currentPosition);
                ARouter.getInstance().build("/communityPageView/PostActivity")
                        .withSerializable("post", currentPost)
                        .navigation();
            }
        });


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                Post currentPost = mPostList.get(currentPosition);
                if (mActionListener != null) {
                    mActionListener.onLikeClick(currentPost.getId(), currentPost.getIsLiked());
                }

                if (currentPost.getIsLiked()) {
                    holder.postLike.setImageResource(R.drawable.ic_like);
                    currentPost.setLiked(false);
                    String likeConunt = String.valueOf(Integer.parseInt(currentPost.getLikeConunt()) - 1);
                    currentPost.setLikeConunt(likeConunt);
                    holder.postLikeCount.setText(likeConunt);
                } else {
                    holder.postLike.setImageResource(R.drawable.ic_liked);
                    currentPost.setLiked(true);
                    String likeConunt = String.valueOf(Integer.parseInt(currentPost.getLikeConunt()) + 1);
                    currentPost.setLikeConunt(likeConunt);
                    holder.postLikeCount.setText(likeConunt);
                }
            }
        };

        holder.postLike.setOnClickListener(listener);
        holder.postLikeCount.setOnClickListener(listener);
        holder.postMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.item_delete) {
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION && mActionListener != null) {
//                            mActionListener.onDeleteClick(mPostList.get(pos).getId());
                            mPostList.remove(pos);
                            notifyDataSetChanged();
                        }
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public void addPost(Post post) {
        mPostList.add(post);
        notifyDataSetChanged();
    }

    public void setPostList(List<Post> postList) {
        mPostList = postList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        Glide.with(holder.itemView.getContext()).clear(holder.postImg);
        super.onViewRecycled(holder);
    }

    public interface OnPostActionListener {
        void onLikeClick(int postId, boolean isLiked);

        void onDeleteClick(int postId);
    }

    private OnPostActionListener mActionListener;

    public void setOnPostActionListener(OnPostActionListener listener) {
        this.mActionListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cvPost;
        private CircleImageView userAvatar;
        private TextView userName;
        private TextView postContent;
        private ImageView postImg;
        private ImageView postLike;
        private TextView postLikeCount;
        private ImageView postMore;
        private CardView cvImg;

        public MyViewHolder(View view) {
            super(view);
            cvPost = view.findViewById(R.id.cv_post);
            userAvatar = view.findViewById(R.id.img_post_useravatar);
            userName = view.findViewById(R.id.tv_post_username);
            postContent = view.findViewById(R.id.tv_post_content);
            postImg = view.findViewById(R.id.img_post);
            postLike = view.findViewById(R.id.img_post_like);
            postLikeCount = view.findViewById(R.id.tv_post_likecount);
            postMore = view.findViewById(R.id.img_post_more);
            cvImg = view.findViewById(R.id.cv_post_img);
        }
    }
}
