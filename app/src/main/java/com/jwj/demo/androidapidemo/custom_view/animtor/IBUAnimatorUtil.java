package com.jwj.demo.androidapidemo.custom_view.animtor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

/**
 * Created by jwj on 17/10/13.
 */
public class IBUAnimatorUtil {

    ValueAnimator bgAnimator, recyclerAnimator;
    boolean isEnd, recylcerEnd, animatorSetEnd;
    AnimatorSet animatorSet;


    public void startAnimal(final BgImageView bgView, final RecyclerView recyclerView, final int limitHeight, final View coverView) {


        if (bgAnimator == null) {
            bgAnimator = createBgAnimator(bgView, limitHeight, coverView);
        }

        if (recyclerAnimator == null) {
            recyclerAnimator = createRecyclerAnimator(recyclerView, limitHeight + 240);
            recyclerAnimator.setStartDelay(100);
        }

        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            animatorSet.playTogether(bgAnimator, recyclerAnimator);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
        }

        if (animatorSetEnd) {
            ArrayList<Animator> mList = animatorSet.getChildAnimations();
            for (int i = mList.size() - 1; i >= 0; i--) {
                Animator animator = mList.get(i);
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                if (valueAnimator == bgAnimator) {
                    bgAnimator.setStartDelay(200);
                }
                valueAnimator.reverse();
            }
            animatorSetEnd = false;
        } else {
            bgAnimator.setStartDelay(0);
            animatorSet.start();
            animatorSetEnd = true;
        }

    }

    private ValueAnimator createBgAnimator(final BgImageView bgView, final int limitHeight, final View coverView) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                int y = Math.round(percent * limitHeight);

                Log.d("animator", y + "");

                if (y > limitHeight) {
                    y = limitHeight;
                }

                ViewCompat.setTranslationY(bgView, -y);
                ViewCompat.setTranslationY(coverView, -y);
                bgView.setCustomAlpha(1 - percent);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isEnd = !isEnd;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(300);
        return valueAnimator;
    }


    int mLastY;

    private ValueAnimator createRecyclerAnimator(final RecyclerView recyclerView, final int limitHeight) {

        final ValueAnimator recyclerAnimator = ValueAnimator.ofFloat(0, 1);
        recyclerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                int y = Math.round(percent * limitHeight);

                if (y > limitHeight) {
                    y = limitHeight;
                }
                ViewCompat.setTranslationY(recyclerView, -y);

//                recyclerView.scrollTo(0, y);
//                recyclerView.smoothScrollBy(0, y);
            }
        });

        recyclerAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                recylcerEnd = !recylcerEnd;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        recyclerAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        recyclerAnimator.setDuration(300);

        return recyclerAnimator;
    }

}
