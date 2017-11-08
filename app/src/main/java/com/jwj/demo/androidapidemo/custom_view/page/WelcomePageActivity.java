package com.jwj.demo.androidapidemo.custom_view.page;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.jwj.demo.androidapidemo.R;

public class WelcomePageActivity extends ActionBarActivity {

    PageAdapter pageAdapter;
    ViewPager mPager;
    PageContainer pageContainer;
    PageIndicatorView pageIndicatorView;
    SparseArray<int[]> mLayoutViewIdsMap = new SparseArray<int[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome_page);
        mPager = (ViewPager) findViewById(R.id.pager);
        pageContainer = (PageContainer) findViewById(R.id.page_container);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.page_indicator);

        pageAdapter = new PageAdapter(this);
        mPager.setAdapter(pageAdapter);
        //mPager.setPageTransformer(true, new ParallaxTransformer(PARALLAX_COEFFICIENT, DISTANCE_COEFFICIENT));
        mPager.addOnPageChangeListener(pageContainer);
        pageIndicatorView.setViewPager(mPager);
    }

    class ParallaxTransformer implements ViewPager.PageTransformer {

        float parallaxCoefficient;
        float distanceCoefficient;

        public ParallaxTransformer(float parallaxCoefficient, float distanceCoefficient) {
            this.parallaxCoefficient = parallaxCoefficient;
            this.distanceCoefficient = distanceCoefficient;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View page, float position) {
            float scrollXOffset = page.getWidth() * parallaxCoefficient;

            ViewGroup pageViewWrapper = (ViewGroup) page;
            @SuppressWarnings("SuspiciousMethodCalls")
            int[] layer = mLayoutViewIdsMap.get(pageViewWrapper.getChildAt(0).getId());
            for (int id : layer) {
                View view = page.findViewById(id);
                if (view != null) {
                    view.setTranslationX(scrollXOffset * position);
                }
                scrollXOffset *= distanceCoefficient;
            }
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        ArgbEvaluator mColorEvaluator;

        int mPageWidth, mTotalScrollWidth;

        int mGuideStartBackgroundColor, mGuideEndBackgroundColor;

        String[] mGuideTips;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public GuidePageChangeListener() {
            mColorEvaluator = new ArgbEvaluator();

            mPageWidth = getWindowManager().getDefaultDisplay().getWidth();
            mTotalScrollWidth = mPageWidth * pageAdapter.getCount();
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            float ratio = (mPageWidth * position + positionOffsetPixels) / (float) mTotalScrollWidth;
            Integer color = (Integer) mColorEvaluator.evaluate(ratio, mGuideStartBackgroundColor, mGuideEndBackgroundColor);
            mPager.setBackgroundColor(color);
        }

        @Override
        public void onPageSelected(int position) {
            if (mGuideTips != null && mGuideTips.length > position) {
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
