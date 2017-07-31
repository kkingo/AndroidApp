package com.cs.cqu.indoorposition.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.cs.cqu.indoorposition.R;

import java.util.ArrayList;
import java.util.List;


public class FloorPlanView extends View {
    private String TAG ="FloorPlan";

    private Paint mPaint;
    private Paint mStrokePaint;
    private Path mArrowPath;

    private int cR = 10;
    private int arrowR = 20;

    private float mCurX = 200;
    private float mCurY = 200;

    private int mOrient =0;


    private  Bitmap mBitmap;
    private List<PointF> mPointList = new ArrayList<>();

    public FloorPlanView(Context context){
        super(context);
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(mPaint);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        mArrowPath = new Path();
        mArrowPath.arcTo(new RectF(-arrowR,-arrowR,arrowR,arrowR),0,-180);
        mArrowPath.lineTo(0,-3*arrowR);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic1);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画图片，就是贴图
        //Log.d(TAG, "onDraw: "+getWidth()+getHeight());
        //Log.d(TAG, "onDraw: "+mBitmap.getWidth()+"   HRI:"+mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight()),new Rect(0,0,getWidth(),getHeight()), null);
        for (PointF p : mPointList) {
            canvas.drawCircle(p.x, p.y, cR, mPaint);
        }
        canvas.save(); // 保存画布
        canvas.translate(mCurX, mCurY); // 平移画布
        canvas.rotate(mOrient); // 转动画布
        canvas.drawPath(mArrowPath, mPaint);
        canvas.drawArc(new RectF(-arrowR * 0.8f, -arrowR * 0.8f, arrowR * 0.8f, arrowR * 0.8f),
                0, 360, false, mStrokePaint);
        canvas.restore(); // 恢复画布
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurX = event.getX();
        mCurY = event.getY();
       // Log.d(TAG, "onTouchEvent: wri"+getWidth()+"H:"+getHeight());
        Log.d(TAG, "onTouchEvent: "+mCurX+"y:"+mCurY);
        invalidate();
        return true;
    }

    public void addPoint(Float xaxis,Float yaxis){
        mCurY = yaxis;
        mCurX = xaxis;
        mPointList.add(new PointF(mCurX,mCurY));
        invalidate();
    }

    public void Located(Float Xaxis, Float Yaxis){
        mCurX = Xaxis;
        mCurY = Yaxis;
        invalidate();
    }
}
