package com.jwj.demo.androidapidemo.custom_view.page;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jwj.demo.androidapidemo.R;

public class WelcomePageActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    PageAdapter pageAdapter;
    ViewPager mPager;
    PageContainer pageContainer;
    PageIndicatorView pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome_page);
        mPager = (ViewPager) findViewById(R.id.pager);
        pageContainer = (PageContainer) findViewById(R.id.page_container);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);

        pageAdapter = new PageAdapter(this);
        mPager.setAdapter(pageAdapter);
        mPager.addOnPageChangeListener(this);
        pageIndicatorView.setViewPager(mPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pageContainer.onPageScrolled(position, positionOffset, positionOffsetPixels);
        float percent = position + positionOffset - 2;
        if (percent > 0) {
            percent = (1 - percent) / 2f;
            if (percent < 0) {
                percent = 0;
            }
            ViewCompat.setAlpha(pageIndicatorView, percent);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPager.getCurrentItem() == pageAdapter.getCount() - 1) {
            startAnimation(pageAdapter.getLastView());
        } else if (mPager.getCurrentItem() == pageAdapter.getCount() - 2 && state == ViewPager.SCROLL_STATE_IDLE) {
            final View view = pageAdapter.getLastView();
            if (view != null) {
                final View brandTv = view.findViewById(R.id.new_brand_tv);
                final View logo = view.findViewById(R.id.logo);
                final View openBtn = view.findViewById(R.id.open_btn);
                brandTv.setAlpha(0);
                logo.setAlpha(0);
                openBtn.setAlpha(0);
            }
        }
    }


    private void startAnimation(final View view) {
        if (view == null) return;

        final View brandTv = view.findViewById(R.id.new_brand_tv);
        final View logo = view.findViewById(R.id.logo);
        final View openBtn = view.findViewById(R.id.open_btn);

        if (brandTv.getAlpha() != 0) return;  //滑动始终停在这个页面，不要重复触发动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                ViewCompat.setAlpha(logo, alpha);

                if (alpha > 0.2f) {
                    ViewCompat.setAlpha(brandTv, (alpha - 0.2f) / 0.8f);
                    ViewCompat.setAlpha(openBtn, (alpha - 0.2f) / 0.8f);
                }
            }
        });
        valueAnimator.setDuration(800);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }
}
