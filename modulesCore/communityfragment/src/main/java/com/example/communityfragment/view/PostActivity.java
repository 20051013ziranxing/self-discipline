package com.example.communityfragment.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import com.example.communityfragment.adapter.CommentAdapter;
import com.example.communityfragment.bean.Comment;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.IPostContract;
import com.example.communityfragment.databinding.ActivityPostBinding;
import com.example.communityfragment.presenter.PostPresenter;
import com.example.communityfragment.utils.TimeUtils;
import com.example.eventbus.UserBaseMessageEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

@Route(path = "/communityPageView/PostActivity")
public class PostActivity extends AppCompatActivity implements IPostContract.View {
    public static final String TAG = "PostFunctionTAG";

    private ActivityPostBinding binding;
    private PostPresenter mPresenter = new PostPresenter(this);

    @Autowired(required = true)
    protected Post post;

    private UserBaseMessageEventBus userBaseMessageEventBus;
    private CommentAdapter adapter;

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
        boolean focusCommentInput = getIntent().getBooleanExtra("focusCommentInput", false);
        if (focusCommentInput) {
            Log.d(TAG, "onCreate: " + focusCommentInput);
            binding.etPostText.requestFocus();
            showKeyboard(binding.etPostText);
        }

        mPresenter.getComments(post.getId());

        binding.imgMypostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.tvMypostContent.setText(post.getContent());
//        binding.tvPostLikeCount.setText(post.getLikeConunt());
        Glide.with(this)
                .load(post.getUserAvatar())
                .error("http://ssjwo2ece.hn-bkt.clouddn.com/post-images/1741094306314528200_KIb9cH")
                .into(binding.imgMypostAvatar);
        binding.tvMypostUsername.setText(post.getUserName());
        binding.tvMypostTime.setText(TimeUtils.getFormatTime(post.getCreatedTime()));
        binding.tvMypostReply.setText(String.format("共 %s 条回复", post.getCommentCount()));

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

        binding.imgMypostMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_share, popupMenu.getMenu());

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.item_post_share) {
                            Intent sendIntent = new Intent(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, post.getContent());
                            sendIntent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(sendIntent, "title");
                            if (sendIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                                v.getContext().startActivity(shareIntent);
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

    }

    // 获取评论
    @Override
    public void onCommentsSuccess(List<Comment> comments) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (comments == null || comments.isEmpty()) {
                    binding.tvMypostEmpty.setVisibility(View.VISIBLE);
                    binding.rvMypostReply.setVisibility(View.GONE);
                } else {
                    binding.tvMypostEmpty.setVisibility(View.GONE);
                    binding.rvMypostReply.setVisibility(View.VISIBLE);

                    binding.rvMypostReply.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                    Collections.reverse(comments);
                    adapter = new CommentAdapter(PostActivity.this, comments);
                    binding.rvMypostReply.setAdapter(adapter);

                    post.setCommentCount(String.valueOf(comments.size()));
                    binding.tvMypostReply.setText(String.format("共 %s 条回复", post.getCommentCount()));
                }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(binding.etPostText.getWindowToken(), 0);
                }

                binding.etPostText.setText("");
                mPresenter.getComments(post.getId());
                binding.tvMypostReply.setText(String.format("共 %s 条回复", post.getCommentCount()));
            }
        });
    }

    @Override
    public void onPublishCommentFailure() {

    }

    private void showKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}