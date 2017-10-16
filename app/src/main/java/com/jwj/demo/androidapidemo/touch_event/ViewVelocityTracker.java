package com.jwj.demo.androidapidemo.touch_event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.jwj.demo.androidapidemo.logger.LogUtil;

/**
 * Created by jwj on 17/10/14.
 */
public class ViewVelocityTracker extends View{

    VelocityTracker mVelocityTracker;

    public ViewVelocityTracker(Context context) {
        super(context);
    }

    public ViewVelocityTracker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(event);

                break;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);

                break;

            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();

                LogUtil.d("initialVelocity = %d", initialVelocity);

            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                break;

        }


        return true;
    }


    private void recycleVelocityTracker(){
        if(mVelocityTracker !=null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }


    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }
}
