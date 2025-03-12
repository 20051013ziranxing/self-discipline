package com.example.communityfragment;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class NoAlphaItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder,
                                 RecyclerView.ViewHolder newHolder,
                                 int fromLeft, int fromTop, int toLeft, int toTop) {
        // 这里可以复制 DefaultItemAnimator 中 animateChange 的部分代码
        // 并根据需求修改 alpha 的调用，比如去掉 alpha(0) 和 alpha(1)

        // 示例：假设你只修改新旧 View 的动画逻辑
        if (oldHolder != null) {
            ViewPropertyAnimatorCompat oldAnim = ViewCompat.animate(oldHolder.itemView);
            // 原先是 oldAnim.alpha(0)...，现在直接设置监听器
            oldAnim.setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull View view) {

                }

                @Override
                public void onAnimationEnd(@NonNull View view) {
                    dispatchChangeFinished(oldHolder, true);
                }

                @Override
                public void onAnimationCancel(@NonNull View view) {

                }
            });
        }

        if (newHolder != null) {
            ViewPropertyAnimatorCompat newAnim = ViewCompat.animate(newHolder.itemView);
            newAnim.translationX(0)
                    .translationY(0)
                    .setDuration(getChangeDuration())
                    // 原先是 alpha(1) 后接 setListener，这里去掉 alpha(1)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull View view) {

                        }

                        @Override
                        public void onAnimationEnd(@NonNull View view) {
                            dispatchChangeFinished(newHolder, false);
                        }

                        @Override
                        public void onAnimationCancel(@NonNull View view) {

                        }
                    });
        }

        // 返回 true 表示动画已开始
        return true;
    }
}
