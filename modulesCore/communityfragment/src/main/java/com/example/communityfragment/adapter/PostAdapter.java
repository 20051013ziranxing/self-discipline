package com.example.communityfragment.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.communityfragment.R;
import com.example.communityfragment.bean.Post;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private List<Post> mPostList = new ArrayList<>();
    private int type;

    public PostAdapter(int type) {
        this.type = type;
    }

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
        holder.postComment.setText(mPostList.get(position).getCommentCount());
        holder.userName.setText(mPostList.get(position).getUserName());
        Glide.with(holder.itemView.getContext())
                .load(mPostList.get(position).getUserAvatar())
                .placeholder(R.drawable.ic_default)
                .error(R.drawable.ic_default)
                .into(holder.userAvatar);

        if (mPostList.get(position).getImageUrl() != null && !mPostList.get(position).getImageUrl().equals("")) {
            Glide.with(holder.itemView.getContext())
                    .load(mPostList.get(position).getImageUrl())
                    .placeholder(R.drawable.dialog_background)
                    .dontAnimate()
                    .skipMemoryCache(false)
                    .into(holder.postImg);

            holder.cvImg.setVisibility(View.VISIBLE);
            Log.d("CommunityModelTAG", mPostList.get(position).getImageUrl());
        } else {
            holder.cvImg.setVisibility(View.GONE);
        }


        if (mPostList.get(position).getIsLiked()) {
            holder.postLike.setImageResource(R.drawable.ic_like_green);
            holder.postLikeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grenn1));
        } else {
            holder.postLike.setImageResource(R.drawable.ic_like_gray);
            holder.postLikeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray3));
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
                        .withBoolean("focusCommentInput", false)
                        .navigation();
            }
        });

        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }

                Post currentPost = mPostList.get(currentPosition);
                ARouter.getInstance().build("/communityPageView/PostActivity")
                        .withSerializable("post", currentPost)
                        .withBoolean("focusCommentInput", true)
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
                    holder.postLike.setImageResource(R.drawable.ic_like_gray);
                    holder.postLikeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.gray3));
                    currentPost.setLiked(false);
                    String likeConunt = String.valueOf(Integer.parseInt(currentPost.getLikeConunt()) - 1);
                    currentPost.setLikeConunt(likeConunt);
                    holder.postLikeCount.setText(likeConunt);
                } else {
                    holder.postLike.setImageResource(R.drawable.ic_like_green);
                    holder.postLikeCount.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grenn1));
                    currentPost.setLiked(true);
                    String likeConunt = String.valueOf(Integer.parseInt(currentPost.getLikeConunt()) + 1);
                    currentPost.setLikeConunt(likeConunt);
                    holder.postLikeCount.setText(likeConunt);
                }
            }
        };

        holder.llLike.setOnClickListener(listener);

        if (type == 1) {
            holder.postMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.item_copy) {
                            int pos = holder.getAdapterPosition();
                            if (pos == RecyclerView.NO_POSITION) {
                                return false;
                            }

                            Post currentPost = mPostList.get(pos);
                            Toast.makeText(v.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData content = ClipData.newPlainText("content", currentPost.getContent());
                            clipboard.setPrimaryClip(content);
                            return true;
                        }
                        return false;
                    });
                    popupMenu.show();
                }
            });
        }else if (type == 2) {
            holder.postMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu_2, popupMenu.getMenu());

                    int pos = holder.getAdapterPosition();
                    Post currentPost = mPostList.get(pos);

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.item_copy) {
                            Toast.makeText(v.getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData content = ClipData.newPlainText("content", currentPost.getContent());
                            clipboard.setPrimaryClip(content);
                            return true;
                        } else if (item.getItemId() == R.id.item_delete) {
                            if (mActionListener != null) {
                                mActionListener.onDeleteClick(currentPost.getId());
                            }
                            mPostList.remove(pos);
                        }
                        return false;
                    });
                    popupMenu.show();
                }
            });
        }


        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                Post currentPost = mPostList.get(currentPosition);
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentPost.getContent());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, "title");
                if (sendIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(shareIntent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public List<Post> getPostList() {
        return mPostList;
    }

    public void clearPostList() {
        mPostList.clear();
    }

    public void addPostList(List<Post> newPosts) {
        if (this.mPostList == null) {
            this.mPostList = new ArrayList<>();
        }
        this.mPostList.addAll(newPosts);
        notifyDataSetChanged();
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
        private TextView postComment;
        private ImageView postImg;
        private ImageView postLike;
        private TextView postLikeCount;
        private ImageView postMore;
        private CardView cvImg;
        private LinearLayout llLike;
        private LinearLayout llComment;
        private LinearLayout llShare;

        public MyViewHolder(View view) {
            super(view);
            cvPost = view.findViewById(R.id.cv_post);
            userAvatar = view.findViewById(R.id.img_post_useravatar);
            userName = view.findViewById(R.id.tv_post_username);
            postContent = view.findViewById(R.id.tv_post_content);
            postComment = view.findViewById(R.id.tv_post_commentcount);
            postImg = view.findViewById(R.id.img_post);
            postLike = view.findViewById(R.id.img_post_like);
            postLikeCount = view.findViewById(R.id.tv_post_likecount);
            postMore = view.findViewById(R.id.img_post_more);
            cvImg = view.findViewById(R.id.cv_post_img);
            llLike = view.findViewById(R.id.ll_post_like);
            llComment = view.findViewById(R.id.ll_post_comment);
            llShare = view.findViewById(R.id.ll_post_share);
        }
    }
}
