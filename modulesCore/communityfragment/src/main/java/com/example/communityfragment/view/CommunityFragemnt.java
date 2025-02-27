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
        Post post1 = new Post("已经想要自律好几年了，觉得自己根本做不到，每次计划的很好却不由自主抱着手机动不起来，然后耽误时间后就焦虑自责", "10", null, "用户011");
        Post post2 = new Post("看看自己以往的日记，大多内容都是些不坚持而悔恨。现在一事无成，要拾起来原来上中学时的坚持和拼劲。", "102", null, "1");
        Post post3 = new Post("从今天开始坚持自律，不管多忙每天至少锻炼1个小时。正常吃喝，不禁食，不减脂，多吃多练把块头练大点。每天晚上9点之前睡觉，打卡第一天#自律#", "102", null, "用户0211");
        Post post4 = new Post("任何事情都是从小事做起始于点滴的！加油！" +
                "今天糊涂过了，不过万幸开始认识到要做计划，保持自律，要把自己的事情好好上心，明天加油！", "13", null, "1");
        Post post5 = new Post("整个寒假都在颓废，每天最大的运动量就是上厕所，今天开学，每天打卡 50 个俯卧撑（我身体素质真的很差，50 个已经极限了，以后会慢慢加的）加油吧", "132", null, "用户03211");
        post3.setImageUrl("https://raw.githubusercontent.com/betteryuxuan/Image/refs/heads/main/yunxing.png");
        post5.setImageUrl("https://raw.githubusercontent.com/betteryuxuan/Image/refs/heads/main/yunxing.png");
        adapter.addPost(post1);
        adapter.addPost(post2);
        adapter.addPost(post3);
        adapter.addPost(post4);
        adapter.addPost(post5);
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