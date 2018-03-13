package com.jwj.demo.androidapidemo.struct.aspectJ;

import android.widget.Toast;

import com.jwj.demo.androidapidemo.CustomApplication;
import com.jwj.demo.androidapidemo.logger.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by jwj on 18/2/12.
 */

@Aspect
public class AspectPermisson {

    @Pointcut("execution (@com.jwj.demo.androidapidemo.struct.aspectJ.PermissionManager * *(..))")
    public void dealPoint(){

    }

    @Around("dealPoint()")
    public void dealHandle(ProceedingJoinPoint point){
        MethodSignature signature = (MethodSignature)point.getSignature();
        PermissionManager permission = signature.getMethod().getAnnotation(PermissionManager.class);

        long startTime = System.currentTimeMillis();
        try{
            //需要管理权限,并且当前权限是管理员
            if(permission.role() == CustomApplication.getApplication().getUser().role){
                if(permission.role() == Role.MANAGER){
                    point.proceed();
                    LogUtil.d("permission is %s","manager");
                }else if(permission.role() == Role.NORMAL){
                    point.proceed();
                    LogUtil.d("permission is %s", "normal");
                }
            }else{
                Toast.makeText(CustomApplication.getApplication(),"您没有权限",Toast.LENGTH_SHORT)
                        .show();
            }

            long durtion = System.currentTimeMillis() - startTime;
            LogUtil.d("%s方法花费的时间为%dms",signature.getMethod().getName(),durtion);
        }catch (Throwable e){
            LogUtil.e("permisson is error %s",e.getMessage());
        }
    }
}
