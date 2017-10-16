package com.jwj.demo.androidapidemo.custom_view.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jwj.demo.androidapidemo.logger.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/10/13
 * Copyright: Ctrip
 */

public class TouchAnimaUtil {

    private int navigition = 1;
    private int splitHeight = 200;
    private float deltaYCaculate, downDeltayCaculate;
    private int TAP = 2;
    int deltaY;
    int scrollY;
    int total;
    int startHeight = 150;

    private View topContentView;
    private BgImageView bgView;
    private View coverIconView;
    private int coverTopHeight;
    private RecyclerView recyclerView;

    private float tempDeltaY;
    private float tempDeltaY2;

    private float distance;

    private List<Integer> mList = new ArrayList<>();

    AccelerateDecelerateInterpolator mInterpolator;




    public void init(BgImageView bgView, View topContentView, View iconView, RecyclerView recyclerView) {
        this.topContentView = topContentView;
        this.bgView = bgView;
        this.coverIconView = iconView;
        this.recyclerView = recyclerView;


        coverIconView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (coverTopHeight == 0) {
                    coverTopHeight = (int) coverIconView.getY();
                    Log.d("top", coverIconView.getTop() + "");
                }
                coverIconView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        mInterpolator = new AccelerateDecelerateInterpolator();
    }




    boolean isEnd;
    ValueAnimator valueAnimator;
    public void startAnimator(){
        if(valueAnimator !=null && valueAnimator.isRunning()){
            return;
        }

        if(valueAnimator == null){
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    int y = Math.round(percent * startHeight);

                    Log.d("animator", y + "");

                    if (y > startHeight) {
                        y = splitHeight;
                    }

                    topContentView.scrollTo(0 ,y);
                    ViewCompat.setTranslationY(bgView, -y);
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
            valueAnimator.setDuration(200);
        }

        if(!isEnd){
            valueAnimator.start();
        }else{
            valueAnimator.reverse();
        }
    }

    public boolean isIntercept(){
        if(valueAnimator !=null && valueAnimator.isRunning()){
            return true;
        }
        return false;
    }

    void animator(int des){
        if(valueAnimator !=null && valueAnimator.isRunning()){
            return;
        }

        if(valueAnimator == null){
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    int y = Math.round(percent * startHeight);

                    Log.d("animator", y + "");

                    if (y > startHeight) {
                        y = splitHeight;
                    }

                    topContentView.scrollTo(0 ,y);
                    ViewCompat.setTranslationY(bgView, -y);
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
            valueAnimator.setDuration(200);
        }

        if(!isEnd){
            valueAnimator.start();
        }else{
            valueAnimator.reverse();
        }


    }




    private int totalDeltaY;
    private int num = 10;
    boolean done;
    int tempScroll;


    public void scrollFloatView(int scrollY, int deltaY) {
        if (coverTopHeight == 0) {
            return;
        }


        if(deltaY > 0){
            deltaY += 2;
            topContentView.scrollBy(0 , deltaY);
            if(topContentView.getScrollY() >= coverTopHeight){



            }

        }


        return;



//        float percent = scrollY * 1f / coverTopHeight;
//        int result = (int) (percent * coverTopHeight);
//
//        log(1, "percent" + percent + ", deltaY =" + deltaY + ", scrollY =" + scrollY + "result = " + (int) (percent * coverTopHeight));
//        log(2, (int) (percent * coverTopHeight) + "");
//
//        if(scrollY == 0 && deltaY > 0){
//            startAnimator();
//            done = false;
//        }else if(scrollY <= startHeight && deltaY < 0 && !done){
//            startAnimator();
//            done = true;
//        }
//
//        if(topContentView.getScrollY() == startHeight){
//            tempScroll = scrollY;
//        }
//
//        if(topContentView.getScrollY() >=startHeight){
//            percent = (coverTopHeight - startHeight) * 1f / (coverTopHeight - tempScroll);
//            topContentView.scrollBy(0 , (int)(percent * deltaY));
//        }


//        if (percent >= 1) {
//            percent = 1;
//            topContentView.scrollTo(0, coverTopHeight);
//        } else {
//            topContentView.scrollTo(0, (int) ((scrollY + deltaY)));
//        }
//        log(4, mInterpolator.getInterpolation(percent) + "");


//        log(3, "scrollY" + scrollY + "new_y = " + scrollY + deltaY * rateUp(percent));


//        totalDeltaY += deltaY;


//        log(1, "percent" + percent + "");
//        float newPercent = mInterpolator.getInterpolation(percent);
//        log(2, "new percent" + newPercent);
//
//        float dy = newPercent * totalDeltaY;
//
//        if (newPercent > percent) {
//            deltaY += num * newPercent;
//        } else {
//            deltaY -= num * newPercent;
//        }
//
//        topContentView.scrollBy(0, deltaY);
//
//
//        log(3, "stopPosition =" + topContentView.getScrollY() + ", scrollY = " + scrollY);
//


//        Log.d("recyclerview_scrollY", scrollY + "");
//
//        Log.d("top_content_height", coverTopHeight + "");
//        float stopPosition = topContentView.getScrollY();
//        Log.d("top_content_y", stopPosition + "");
//
//        float percent = scrollY * 1.0f / coverTopHeight;
//
//        Log.d("percent", percent + "");
//
//
//        float rate = rateUp(percent);
//
//        Log.d("rate", rate + "");
//        Log.d("reateUp * deltaY = ", rate * deltaY + ", deltaY =" + deltaY + ",percent = " + percent);
//        //设置icon背景透明度
//        //alphaIconView(1 - percent, wechatIconView);
//        scrollToView(topContentView, coverTopHeight, 1f, 2f, deltaY, scrollY);
    }

    boolean isLog;

    public void scrollToView(View topView, int desHeight, float rateUp, float rateDown, int distanceY, int scrollY) {
        int deltaY = distanceY;// Math.round(y);
        final int stopPosition = topView.getScrollY();
        final int recyclerTopHeight = desHeight;


        if (distanceY > 0) {   //向上滑,滑动指定位置就停止
            if (stopPosition > desHeight) {
                topView.scrollTo(0, desHeight);
                log(2, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " detalY = " + deltaY);
            } else if (stopPosition < desHeight) {
                if (stopPosition + deltaY > desHeight) {
                    topView.scrollBy(0, desHeight - stopPosition);
                    log(3, "detalY = " + deltaY);
                    log(3, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " desHeight - stopPosition = " + (desHeight - stopPosition));
                } else {
                    if (stopPosition < splitHeight) {
                        deltaY = deltaY + TAP;
                        if (deltaY + stopPosition > splitHeight) {
                            topView.scrollBy(0, splitHeight - stopPosition);
                            totalDeltaY += splitHeight - stopPosition;
                        } else {
                            totalDeltaY += TAP;
                        }
                        deltaYCaculate = 0;
                        downDeltayCaculate = 0;
                        distance = scrollY;
                        log(4, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " detalY = " + deltaY);
                    } else if (stopPosition == splitHeight) {
                        distance = scrollY;
                    } else {
                        float percent = deltaY * 1.0f / (recyclerTopHeight - distance);
                        float deltaYTab = percent * totalDeltaY;
                        deltaYCaculate += deltaYTab;
                        if (deltaYCaculate > 1) {
                            int m = (int) deltaYCaculate;
                            if (deltaY - m < 0) {
                                deltaY = 0;
                                deltaYCaculate -= deltaY;
                            } else {
                                deltaY -= m;
                                mList.add(m);
                            }
                            deltaYCaculate = deltaYCaculate - (int) deltaYCaculate;
                        }

                        tempDeltaY += deltaYTab;
                        tempDeltaY2 += deltaY;

                    }
                    topView.scrollBy(0, deltaY);
                }
            }
            if (stopPosition == desHeight && scrollY == desHeight) {
                log(1, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY);
                isLog = true;
            }

        } else if (distanceY < 0) {
            if (scrollY > desHeight) {  // 445  -5, 442  440
                if (scrollY + deltaY < desHeight) {
                    int tap = stopPosition + deltaY - desHeight;
                    topView.scrollBy(0, tap);
                }
            } else {
                log(-1, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " detalY = " + deltaY);
                if (stopPosition > splitHeight) {
                    if (stopPosition + deltaY > splitHeight) {
                        float percent = Math.abs(deltaY) * 1.0f / (recyclerTopHeight - distance);
                        float deltaYTab = percent * totalDeltaY;

                        if (downDeltayCaculate > 1) {
                            deltaY += (int) downDeltayCaculate;
                            downDeltayCaculate = downDeltayCaculate - (int) downDeltayCaculate;
                        }
                        downDeltayCaculate += deltaYTab;
                        topView.scrollBy(0, deltaY);
                    } else {
                        topView.scrollBy(0, splitHeight - stopPosition);
                    }

                    log(-2, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " detalY = " + deltaY);
                } else if (stopPosition > 0) {
                    deltaY = deltaY - TAP;
                    if (stopPosition + deltaY < 0) {
                        topView.scrollBy(0, -stopPosition);
                        totalDeltaY -= stopPosition;
                    } else {
                        topView.scrollBy(0, deltaY);
                        totalDeltaY -= TAP;
                    }

                    log(-3, "stopPosition =" + topView.getScrollY() + ", scrollY = " + scrollY + " detalY = " + deltaY);

                } else {
                    topView.scrollTo(0, 0);
                    deltaYCaculate = 0;
                    downDeltayCaculate = 0;
                    totalDeltaY = 0;
                }

                log(-1, "distance =" + distance);
            }
        }

        if (stopPosition == desHeight && scrollY == desHeight) {
            log(1, "stopPosition =" + stopPosition + ", scrollY = " + scrollY);
            isLog = false;
        }


        float per = stopPosition * 1f / desHeight;
        bgView.setCustomAlpha(1 - per);
        ViewCompat.setTranslationY(bgView, -stopPosition);
    }


    public void scrollByView(View topView, int desHeight, float rateUp, float rateDown, int distanceY) {
        float rate = (coverTopHeight - startHeight) * 1f / coverTopHeight;

        Log.d("rate:", rate + "");

        final int deltaY = Math.round(distanceY * 0.5f + 0.5f);
        final int stopPosition = scrollY;


        log(3, "topScrollY:" + topView.getScrollY() + ",stopPosition:" + stopPosition);


        if (distanceY > 0) {   //向上滑,滑动指定位置就停止
            if (stopPosition > desHeight) {
                topView.scrollTo(0, desHeight);
            } else if (stopPosition < desHeight) {
                if (stopPosition + deltaY > desHeight) {
                    topView.scrollBy(0, desHeight - stopPosition);
                } else {
                    log(2, "deltaY:" + deltaY + ", distanceY:" + distanceY);
                    topView.scrollBy(0, deltaY);
                }
            }
        } else {
            if (stopPosition > desHeight) {  // 445  -5, 442  440
                if (stopPosition + deltaY < desHeight) {
                    topView.scrollBy(0, stopPosition + deltaY - desHeight);
                } else {
                }
            } else {
                if (stopPosition > 0) {
                    if (stopPosition + deltaY > 0) {
                        topView.scrollBy(0, deltaY);
                    } else {
                        topView.scrollBy(0, -stopPosition);
                    }
                } else {
                    topView.scrollTo(0, 0);
                }
            }
        }

        scrollY += distanceY;
    }


/*
    1.向上滑动,两次的距离分别为 t1 = 4 ,t2 = 6
    这个是时候f是要大于1  假设f = 1.2f

    recyclerview滑动距离 t1 + t2 = 10
    顶部view滑动距离为  (t1 + t2) * f = 12

    此时向下滑动

 */

//    100
//    前面40不滑动，
//
//    滑动到60之后
//            t1 + t2 + t3 =100
//
//            t1 * f1+ t2 * f2 + t3 * f3=60


    public void alphaIconView(float percent, View view) {
        if (view != null && view.getBackground() != null) {
            int alpha = (int) (percent * 255);
            if (alpha > 255) {
                alpha = 255;
            }
            view.getBackground().setAlpha(alpha);
        }
    }


    /**
     * @param x (0 ~1.0)
     * @return
     */
    public float rateUp(float x) {
        return -1.6667f * (x - 0.5f) * (x - 0.5f) + 1.413f;
    }


    private float rateDown(float x) {
        if (x < 0.65f) {  //(0~0.65f)
            return 1f; //-1.25f * x * x + 1;
        } else if (x <= 1.0f) {
            return 0;  //-(x - 1) * (x - 1) + 1;
        }
        return 0;
    }


    /**
     * 滚动浮动的view的算法
     *
     * @param scrollY   y方向滚动的距离0坐标的距离
     * @param deltaY    一次滚动的间距
     * @param floatView 浮动的view
     */
    void scrollView(int scrollY, int deltaY, View floatView) {
        if (floatView != null) {
            float iconY = floatView.getY();

            Log.d("icon_y", iconY + "");
            Log.d("icon_top", scrollY + "");
            int toPosition = 0;  //需要滚动到的目标y坐标
            if (iconY - scrollY >= toPosition) {
                float percent = 1 - (iconY - scrollY - toPosition) * 1.0f / iconY;
                LogUtil.d("percent = %f", percent);

                if (percent < 0) {
                    percent = 0;
                } else if (percent > 1) {
                    percent = 1;
                }

                if (percent == 1) {
                } else {
                }
            } else {
//                ViewCompat.setAlpha(coverIconView, 1);
//                coverIconView.setVisibility(View.VISIBLE);
            }
            //上滑
            if (deltaY > 0 && (iconY - scrollY <= 0)) {  //由大到小
                Log.d("onScroll", "icon上滑到达顶点");

            } else if (deltaY < 0 && iconY - scrollY >= 0) {  //下滑  比例越来越大
                Log.d("onScroll", "icon下滑到达位置");
            }
        }

    }

    public void log(int v, String msg) {
        Log.d("scroll_y_" + v, msg);
    }

}
