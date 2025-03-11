package com.example.communityfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.communityfragment.adapter.PostAdapter;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.databinding.FragmentCommunityBinding;
import com.example.communityfragment.presenter.CommunityPresenter;
import com.example.eventbus.UserBaseMessageEventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Route(path = "/communityPageView/CommunityFragemnt")
public class CommunityFragemnt extends Fragment implements ICommunityContract.View {
    private static final String TAG = "CommunityFragemntTAG";
    private FragmentCommunityBinding binding;
    private PostAdapter adapter;
    private CommunityPresenter mPresenter;
    private UserBaseMessageEventBus userBaseMessageEventBus;
    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new CommunityPresenter(this);

//        initPost();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new PostAdapter();
        binding.rlvCommunity.setLayoutManager(manager);
        binding.rlvCommunity.setAdapter(adapter);

        mPresenter.getData();

        binding.fabPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/communityPageView/PublishActivity").navigation();
            }
        });

        adapter.setOnPostActionListener(new PostAdapter.OnPostActionListener() {
            @Override
            public void onLikeClick(int postId, boolean isLiked) {
                if (isLiked) {
                    mPresenter.unlikePost(postId, getUserId());
                } else {
                    mPresenter.likePost(postId, getUserId());
                }
            }

            @Override
            public void onDeleteClick(int postId) {
                mPresenter.deletePost(postId, getUserId());
            }
        });

        binding.swipeCommunityRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getData();
            }
        });


    }

    private String getUserId() {
        userBaseMessageEventBus = EventBus.getDefault().getStickyEvent(UserBaseMessageEventBus.class);
        if (userBaseMessageEventBus != null && userBaseMessageEventBus.getUserId() != null)
            return userBaseMessageEventBus.getUserId();
        else
            return "";
    }


    public void onDataReceived(List<Post> postList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.swipeCommunityRefresh.setRefreshing(false);
                    if (postList == null || postList.isEmpty()) {
                        binding.tvCommunityEmpty.setVisibility(View.VISIBLE);
                        binding.rlvCommunity.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "onDataReceived: " + postList.size());
                        binding.tvCommunityEmpty.setVisibility(View.GONE);
                        binding.rlvCommunity.setVisibility(View.VISIBLE);
                        Collections.reverse(postList);
                        // 查看是否点赞
                        for (Post post : postList) {
                            mPresenter.checkLikeStatus(post.getId(), getUserId());
                        }
                        adapter.setPostList(postList);
                    }
                }
            });
        }
    }

    public void updatePostLikeStatus(int postId, boolean isLiked) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Post> postList = adapter.getPostList();
                for (int i = 0; i < postList.size(); i++) {
                    Post post = postList.get(i);
                    if (post.getId() == postId) {
                        post.setLiked(isLiked);
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userBaseMessageEventBus != null) {
            EventBus.getDefault().unregister(userBaseMessageEventBus);
        }
    }
}