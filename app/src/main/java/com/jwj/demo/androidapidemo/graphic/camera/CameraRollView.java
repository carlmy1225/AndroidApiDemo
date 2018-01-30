package com.jwj.demo.androidapidemo.graphic.camera;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *  camera 实现各种切换的动画特效
 */
public class CameraRollView extends View {

    Camera camera;
    Matrix matrix;
    Paint paint;


    public CameraRollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        camera = new Camera();
        matrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffff0000);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        camera.save();
        camera.rotateX(30);
        camera.getMatrix(matrix);
        canvas.restore();

        canvas.setMatrix(matrix);
        canvas.drawCircle(200,200,100,paint);
    }
}
