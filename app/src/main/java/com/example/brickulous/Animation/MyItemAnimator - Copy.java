package com.example.brickulous.Animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemAnimator extends DefaultItemAnimator {
    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        // Set the views to their final positions
        holder.itemView.setAlpha(0);
        holder.itemView.setTranslationY(holder.itemView.getHeight());

        // Animate the views into place
        holder.itemView.animate()
                .alpha(1)
                .translationY(100)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator(3f))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();

        return true;
    }
}

