package com.example.todofragment;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todofragment.adapter.RecyclerViewToDoAdapter;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todofragment.adapter.RecyclerViewToDoAdapter;

public class SwipeToActionCallback extends ItemTouchHelper.Callback {
    private SwipeToActionCallbackListener listener;
    private static final String TAG = "TestTT_SwipeToActionCallback";
    private static final float MAX_SWIPE_DISTANCE = 0.35f;

    // 用于记录当前处于左滑状态的 ViewHolder
    private RecyclerViewToDoAdapter.MyViewHolder currentlySwipedViewHolder = null;

    public SwipeToActionCallback(SwipeToActionCallbackListener listener) {
        this.listener = listener;
    }

    public interface SwipeToActionCallbackListener {
        void unbind();
        void harkBackTo();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // 允许左滑和右滑
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // 不处理拖拽操作
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 不处理滑动删除
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            RecyclerViewToDoAdapter.MyViewHolder holder = (RecyclerViewToDoAdapter.MyViewHolder) viewHolder;
            View layoutA = holder.itemView.findViewById(R.id.main_content);
            View layoutB = holder.itemView.findViewById(R.id.action_layout);
            int screenWidth = recyclerView.getWidth();
            float maxSwipeDistance = screenWidth * MAX_SWIPE_DISTANCE;
            Log.d(TAG, "onChildDraw: dX = " + dX + ", layoutB visibility = " + layoutB.getVisibility());

            // 如果当前有其他 ViewHolder 正在左滑，先恢复它的状态
            if (currentlySwipedViewHolder != null && currentlySwipedViewHolder != holder) {
                resetSwipedView(currentlySwipedViewHolder);
                currentlySwipedViewHolder = null; // 清空当前滑动状态
                Log.d(TAG, "重置前一个 ViewHolder 的状态");
            }

            // 更新当前正在滑动的 ViewHolder
            currentlySwipedViewHolder = holder;

            // 判断滑动方向
            if (dX > 0) { // 右滑
                Log.d(TAG, "右滑：" + dX);
                // 右滑时，允许恢复原状
                if (Math.abs(holder.itemView.getTranslationX()) >= maxSwipeDistance) {
                    resetSwipedView(holder);
                }
            } else { // 左滑
                Log.d(TAG, "左滑：" + dX);
                // 左滑时，限制滑动距离
                dX = Math.max(dX, -maxSwipeDistance);
                layoutA.setTranslationX(dX);
                layoutB.setTranslationX(0);

                // 如果滑动距离超过阈值，显示操作布局
                if (Math.abs(dX) >= maxSwipeDistance * 0.8f) {
                    if (layoutB.getVisibility() != View.VISIBLE && !holder.isAnimationRunning) {
                        holder.isAnimationRunning = true; // 标记动画正在运行
                        layoutB.setVisibility(View.VISIBLE); // 确保布局B可见
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(layoutB, "alpha", 0f, 1f);
                        ObjectAnimator slideUp = ObjectAnimator.ofFloat(layoutB, "translationX", 100f, 0f);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(fadeIn, slideUp);
                        animatorSet.setDuration(500);
                        animatorSet.start();
                        listener.unbind();
                    }
                } else {
                    if (layoutB.getVisibility() != View.GONE) {
                        layoutB.setVisibility(View.GONE);
                        holder.isAnimationRunning = false; // 重置动画状态
                    }
                }
            }

            super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        RecyclerViewToDoAdapter.MyViewHolder holder = (RecyclerViewToDoAdapter.MyViewHolder) viewHolder;
        holder.constraintLayout_main.setTranslationX(0);
        holder.constraintLayout_action.setVisibility(View.GONE);

        // 重置动画状态标志位
        holder.isAnimationRunning = false;

        // 清空当前滑动状态
        if (currentlySwipedViewHolder == holder) {
            currentlySwipedViewHolder = null;
        }

        Log.d(TAG, "clearView: 重置 ViewHolder 的状态");
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        // 增加滑动的逃逸速度阈值
        return defaultValue * 2; // 默认值的 2 倍
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        // 增加滑动的距离阈值
        return 0.5f; // 滑动距离超过 50% 才认为是滑动
    }

    // 用于重置滑动状态的方法
    private void resetSwipedView(RecyclerViewToDoAdapter.MyViewHolder holder) {
        if (holder == null) return;

        View layoutA = holder.itemView.findViewById(R.id.main_content);
        View layoutB = holder.itemView.findViewById(R.id.action_layout);

        // 取消所有动画
        layoutA.clearAnimation();
        layoutB.clearAnimation();

        // 恢复主布局位置
        layoutA.animate().cancel();
        layoutA.setTranslationX(0);

        // 恢复布局B的初始状态
        layoutB.animate().cancel();
        layoutB.setAlpha(0); // 恢复透明度
        layoutB.setTranslationX(100f); // 恢复位置
        layoutB.setVisibility(View.GONE); // 隐藏布局B

        // 重置动画状态标志位
        holder.isAnimationRunning = false;

        // 调用监听器的恢复方法
        listener.harkBackTo();

        Log.d(TAG, "重置 ViewHolder 的状态");
    }
}

/*
public class SwipeToActionCallback extends ItemTouchHelper.Callback {
    private SwipeToActionCallbackListener listener;

    public SwipeToActionCallback(SwipeToActionCallbackListener listener) {
        this.listener = listener;
    }

    public interface SwipeToActionCallbackListener {
        void unbind();
        void harkBackTo();
    }
    private static final String TAG = "TestTT_SwipeToActionCallback";
    private static final float MAX_SWIPE_DISTANCE = 0.35f;
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //左滑
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //处理拖拽
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //处理滑动删除
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            RecyclerViewToDoAdapter.MyViewHolder holder = (RecyclerViewToDoAdapter.MyViewHolder) viewHolder;
            View layoutA = holder.itemView.findViewById(R.id.main_content);
            View layoutB = holder.itemView.findViewById(R.id.action_layout);
            int screenWidth = recyclerView.getWidth();
            float maxSwipeDistance = screenWidth * MAX_SWIPE_DISTANCE;
            boolean isSwiped = Math.abs(holder.itemView.getTranslationX()) >= maxSwipeDistance;
            float swipeThreshold = 200f;
            if (Math.abs(dX) < swipeThreshold) {
                Log.d(TAG, "不认为是滑动");
                super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);
                return;
            }
            if (dX > 0) { // 右滑
                Log.d(TAG, "右滑：" + dX);
                if (isSwiped) {
                    // 如果已经滑动到最大距离，允许右滑恢复原状
                    dX = Math.min(dX, 0);
                } else {
                    // 如果没有滑动到最大距离，禁止右滑
                    dX = 0;
                }
            } else {
                dX = Math.max(dX, -maxSwipeDistance);
                Log.d(TAG, "zuo滑：" + dX);
            }
            layoutA.setTranslationX(dX);
            layoutB.setTranslationX(0);
            if (Math.abs(dX) >= maxSwipeDistance * 0.8f && !isSwiped) {
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(layoutB, "alpha", 0f, 1f); // 透明度动画
                ObjectAnimator slideUp = ObjectAnimator.ofFloat(layoutB, "translationX", 100f, 0f); // 从底部滑入
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(fadeIn, slideUp); // 同时执行透明度和滑动动画
                animatorSet.setDuration(500); // 动画时长 500ms
                layoutB.setVisibility(View.VISIBLE);
                animatorSet.start();
                listener.unbind();
            } else {
                layoutB.setVisibility(View.GONE);
            }
            if (dX == 0) {
                isSwiped = false;
            }
            // 确保滑动逻辑不会拦截点击事件
            if (dX < 500 && !isCurrentlyActive) {
                isSwiped = false;
            }
            super.onChildDraw(c, recyclerView, viewHolder, 0, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        RecyclerViewToDoAdapter.MyViewHolder holder = (RecyclerViewToDoAdapter.MyViewHolder) viewHolder;
        holder.constraintLayout_main.setTranslationX(0);
        holder.constraintLayout_action.setVisibility(View.GONE);
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        // 增加滑动的逃逸速度阈值
        return defaultValue * 2; // 默认值的 2 倍
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        // 增加滑动的距离阈值
        return 0.5f; // 滑动距离超过 50% 才认为是滑动
    }
}
*/
