package com.jwj.demo.androidapidemo.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;

import com.jwj.demo.androidapidemo.system_util.SystemUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Description: 描述
 * Author: wjxie
 * Date: 2017/9/28
 * Copyright: Ctrip
 */

public class AnimatorSetUtil {


    /**
     * AnimatorSet方法倒播动画，的方式有3中
     * 1. 遍历animatorList，逐个reverse
     * 2. 反射调用reverse方法
     * 3. android 8.0开始，animatorSet支持倒播
     *
     * @param animatorSet
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void reverseAnimator(AnimatorSet animatorSet) throws InvocationTargetException, IllegalAccessException {
        List<Animator> animatorList = animatorSet.getChildAnimations();

        for (Animator animator : animatorList) {
            //两种方式调用
            if (animator instanceof ValueAnimator) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;
                valueAnimator.reverse();
            }

            //反射
            Class cls = animator.getClass();
            try {
                Method method = cls.getDeclaredMethod("reverse", cls);
                method.setAccessible(true);
                method.invoke(animator);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        //method 2
        Class asCls = animatorSet.getClass();
        try {
            Method method = asCls.getDeclaredMethod("reverse", asCls);
            method.setAccessible(true);
            method.invoke(animatorSet);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        //method 3
        if (SystemUtil.getSdkVersion() > 26) {  // Build.VERSION_CODES.O
//            animatorSet.reverse();
        }

    }

}
