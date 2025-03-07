package com.example.communityfragment.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.communityfragment.R;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.databinding.ActivityPostBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Route(path = "/communityPageView/PostActivity")
public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivityTAG";
    private ActivityPostBinding binding;

    @Autowired(required = true)
    protected Post post;


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
        Glide.with(this).load(post.getUserAvatar()).into(binding.imgMypostAvatar);
        binding.tvMypostUsername.setText(post.getUserid());
        binding.tvMypostTime.setText(getFormatTime(post.getCreatedTime()));

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

        if (false) {
            binding.tvMypostEmpty.setVisibility(View.GONE);
            binding.rvMypostReply.setVisibility(View.VISIBLE);
        } else {
            binding.tvMypostEmpty.setVisibility(View.VISIBLE);
            binding.rvMypostReply.setVisibility(View.GONE);
        }


    }

    private String getFormatTime(String createdTime) {
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):\\d{2}.*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(createdTime);
        if (matcher.find()) {
            String year = matcher.group(1);
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            String hour = matcher.group(4);
            String minute = matcher.group(5);

            return "发布于 " + year + "." + month + "." + day + " " + hour + ":" + minute;
        } else {
            return "未知";
        }
    }

}