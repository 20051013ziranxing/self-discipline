package com.example.communityfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Route(path = "/communityPageView/CommunityFragemnt")
public class CommunityFragemnt extends Fragment implements ICommunityContract.View {
    private static final String TAG = "CommunityFragemntTAG";
    private FragmentCommunityBinding binding;
    private PostAdapter adapter;
    private CommunityPresenter mPresenter;

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
                    mPresenter.unlikePost(postId);
                } else {
                    mPresenter.likePost(postId);
                }
            }

            @Override
            public void onDeleteClick(int postId) {
                mPresenter.deletePost(postId);
            }
        });


    }


    public void onDataReceived(List<Post> postList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (postList == null || postList.isEmpty()) {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.rlvCommunity.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "onDataReceived: " + postList.size());
                        binding.tvEmpty.setVisibility(View.GONE);
                        binding.rlvCommunity.setVisibility(View.VISIBLE);
                        Collections.reverse(postList);
                        // 查看是否点赞
                        for (Post post : postList) {
                            mPresenter.checkLikeStatus(post.getId());
                        }
                        adapter.setPostList(postList);
                    }
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getData();
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

}