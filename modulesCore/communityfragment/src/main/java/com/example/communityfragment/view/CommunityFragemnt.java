package com.example.communityfragment.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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


        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new PostAdapter();
        binding.rlvCommunity.setLayoutManager(manager);
        binding.rlvCommunity.setAdapter(adapter);

        initPost();
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

    private void initPost() {
        mPresenter.getData();
        Post post = new Post("test1","10",null,"1");
        Post post2 = new Post("test2test2test2test2test2test2test2test2test2","102",null,"1");
        Post post3 = new Post("test2test2test2test2test2test2test2test2test2","102",null,"1");
        Post post4 = new Post("test2test2test2test2test2test2test2test2test2","102",null,"1");
        post.setImageUrl("https://raw.githubusercontent.com/betteryuxuan/Image/refs/heads/main/yunxing.png");
        post3.setImageUrl("https://raw.githubusercontent.com/betteryuxuan/Image/refs/heads/main/yunxing.png");
        adapter.addPost(post);
        adapter.addPost(post2);
        adapter.addPost(post3);
        adapter.addPost(post4);
    }

    public void onDataReceived(List<Post> postList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setPostList(postList);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getData();
    }
}