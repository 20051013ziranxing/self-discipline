package com.example.communityfragment.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.communityfragment.R;
import com.example.communityfragment.utils.TimeUtils;
import com.example.communityfragment.adapter.CommentAdapter;
import com.example.communityfragment.bean.Comment;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.IPostContract;
import com.example.communityfragment.databinding.ActivityPostBinding;
import com.example.communityfragment.presenter.PostPresenter;
import com.example.eventbus.UserBaseMessageEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/communityPageView/PostActivity")
public class PostActivity extends AppCompatActivity implements IPostContract.View {
    public static final String TAG = "PostFunctionTAG";

    private ActivityPostBinding binding;
    private PostPresenter mPresenter = new PostPresenter(this);

    @Autowired(required = true)
    protected Post post;

    private UserBaseMessageEventBus userBaseMessageEventBus;
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EventBus.getDefault().register(this);

        ARouter.getInstance().inject(this);
        post = (Post) getIntent().getSerializableExtra("post");

        binding.imgMypostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvMypostContent.setText(post.getContent());
//        binding.tvPostLikeCount.setText(post.getLikeConunt());
        Glide.with(this).load("post.getUserAvatar()").into(binding.imgMypostAvatar);
        binding.tvMypostUsername.setText(post.getUserid());
        binding.tvMypostTime.setText(TimeUtils.getFormatTime(post.getCreatedTime()));

        if (post.getImageUrl() != null && !post.getImageUrl().equals("")) {
            Log.d(TAG, "本帖图" + post.getImageUrl());
            Glide.with(PostActivity.this)
                    .load(post.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .into(binding.imgMypostImage);
            binding.cvMypostPhoto.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "本帖无图");
            binding.cvMypostPhoto.setVisibility(View.GONE);
        }

        Comment comment1 = new Comment("上档次啊", "2025.8.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户12321");
        Comment comment2 = new Comment("说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊说的很好啊", "2025.8.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户1221321");
        Comment comment3 = new Comment("很上档次啊很上档次啊很上档次啊", "2025.8.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户1243321");
        Comment comment4 = new Comment("真有档次啊", "2025.3.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户12142321");
        Comment comment5 = new Comment("很上档次啊很上档次啊很上档次啊", "2025.8.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户1243321");
        Comment comment6 = new Comment("很上档次啊很上档次啊很上档次啊", "2025.8.21 17:35", "http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741261891308783029_RXev1R", "1", "用户1243321");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        comments.add(comment4);
        comments.add(comment5);
        comments.add(comment6);
        onCommentsSuccess(comments);
//        onCommentsFailue();


        binding.tvPostSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.etPostText.getText().toString();
                if (content.trim().isEmpty()) {
                    Toast.makeText(PostActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
//                    binding.tvPostSend.setEnabled(false);
                    if (userBaseMessageEventBus != null && userBaseMessageEventBus.getUserId() != null)
                        mPresenter.comment(post.getId(), userBaseMessageEventBus.getUserId(), content);
                    else
                        mPresenter.comment(post.getId(), "2", content);
                }
            }
        });
    }

    // 获取评论
    @Override
    public void onCommentsSuccess(List<Comment> comments) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tvMypostEmpty.setVisibility(View.GONE);
                binding.rvMypostReply.setVisibility(View.VISIBLE);
                binding.rvMypostReply.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                CommentAdapter adapter = new CommentAdapter(PostActivity.this, comments);
                binding.rvMypostReply.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onCommentsFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tvMypostEmpty.setVisibility(View.VISIBLE);
                binding.rvMypostReply.setVisibility(View.GONE);
            }
        });
    }

    // 发步评论
    @Override
    public void onPublishCommentSuccess() {

    }

    @Override
    public void onPublishCommentFailure() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}