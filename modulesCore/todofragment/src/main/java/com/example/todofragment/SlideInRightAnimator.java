package com.example.todofragment;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import java.util.List;

public class SlideInRightAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateDisappearance(RecyclerView.ViewHolder holder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        return false;
    }

    @Override
    public boolean animateAppearance(RecyclerView.ViewHolder holder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        if (holder.getAdapterPosition() >= 0) {
            View view = holder.itemView;
            view.setTranslationX(view.getRootView().getWidth());
            view.animate()
                    .translationX(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setDuration(30000)
                    .start();
            return true;
        }
        return false;
    }
}
