package com.example.communityfragment.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.communityfragment.adapter.PostAdapter;
import com.example.communityfragment.bean.Post;
import com.example.communityfragment.contract.ICommunityContract;
import com.example.communityfragment.databinding.FragmentCommunityBinding;
import com.example.communityfragment.presenter.CommunityPresenter;
import com.example.communityfragment.utils.TimeUtils;
import com.example.eventbus.UserBaseMessageEventBus;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route(path = "/communityPageView/CommunityFragemnt")
public class CommunityFragemnt extends Fragment implements ICommunityContract.View {
    private static final String TAG = "CommunityFragemntTAG";
    private static final int TYPE = 1;
    private FragmentCommunityBinding binding;
    private PostAdapter adapter;
    private CommunityPresenter mPresenter;
    private UserBaseMessageEventBus userBaseMessageEventBus;

    private List<Post> allPosts = new ArrayList<>();      // 存储全部帖子
    private int currentPage = 1;                          // 当前页码
    private final int pageSize = 10;                      // 每页显示10个帖子

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMoonStickyEvent(UserBaseMessageEventBus userBaseMessageEventBus) {
        this.userBaseMessageEventBus = userBaseMessageEventBus;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new CommunityPresenter(this);

        EventBus.getDefault().register(this);
        if (userBaseMessageEventBus != null) {
            binding.tvCommunityWelcome.setText(String.format("%s！ %s！", TimeUtils.getTimeNormal(), userBaseMessageEventBus.getUserName()));
        } else {
            binding.tvCommunityWelcome.setText(String.format("%s！", TimeUtils.getTimeNormal()));
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        adapter = new PostAdapter(getUserId());
        adapter.setHasStableIds(true);
        binding.rlvCommunity.setLayoutManager(manager);
        binding.rlvCommunity.setAdapter(adapter);
//        ((DefaultItemAnimator) binding.rlvCommunity.getItemAnimator()).setSupportsChangeAnimations(false);


        binding.fabIndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/communityPageView/IndividualPostActivity")
                        .navigation();
            }
        });

        binding.fabPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/communityPageView/PublishActivity")
                        .navigation();
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
                mPresenter.deletePost(postId);
            }
        });

//        ((SimpleItemAnimator) binding.rlvCommunity.getItemAnimator()).setSupportsChangeAnimations(false);

        binding.swipeCommunityRefresh.setRefreshHeader(new ClassicsHeader(getContext()));
        binding.swipeCommunityRefresh.setRefreshFooter(new ClassicsFooter(getContext()));

        binding.swipeCommunityRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                binding.swipeCommunityRefresh.setNoMoreData(false);
                binding.swipeCommunityRefresh.finishRefresh(true);
                mPresenter.getData(null, TYPE);
            }
        });


        binding.swipeCommunityRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPage++;
                loadCurrentGroup();
                binding.swipeCommunityRefresh.finishLoadMore(true);
            }
        });


    }

    private void loadCurrentGroup() {
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allPosts.size());

        if (startIndex < endIndex) {
            List<Post> group = new ArrayList<>(allPosts.subList(startIndex, endIndex));
            if (currentPage == 1) {
                adapter.setPostList(group);
            } else {
                adapter.addPostList(group);
            }
        } else {
            binding.swipeCommunityRefresh.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getData(null, TYPE);
    }

    private String getUserId() {
        userBaseMessageEventBus = EventBus.getDefault().getStickyEvent(UserBaseMessageEventBus.class);
        if (userBaseMessageEventBus != null && userBaseMessageEventBus.getUserId() != null)
            return userBaseMessageEventBus.getUserId();
        else return "";
    }

    public void onDataReceived(List<Post> postList) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    binding.swipeCommunityRefresh.finishRefresh(true);
                    if (postList == null || postList.isEmpty()) {
                        binding.tvCommunityEmpty.setVisibility(View.VISIBLE);
                        binding.tvCommunityEmpty.setText("当前暂无帖子");
                        binding.rlvCommunity.setVisibility(View.GONE);
                    } else {
                        binding.tvCommunityEmpty.setVisibility(View.GONE);
                        binding.rlvCommunity.setVisibility(View.VISIBLE);
                        Collections.reverse(postList);

                        allPosts.clear();
                        allPosts.addAll(postList);

                        // 检查每个帖子是否被点赞
                        for (Post post : postList) {
                            mPresenter.checkLikeStatus(post.getId(), getUserId());
                        }

                        // 重置页码
                        currentPage = 1;
                        // 加载第一页数据
                        loadCurrentGroup();
                    }
                }
            });
        }
    }

    @Override
    public void deletePostSuccess(int postId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Post post : allPosts) {
                    if (post.getId() == postId) {
                        allPosts.remove(post);
                        break;
                    }
                }
                adapter.setPostList(allPosts);
            }
        });

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