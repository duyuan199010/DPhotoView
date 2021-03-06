package com.duyuan.photoview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import uk.co.senab.photoview.PhotoView;

/**
 * A ImageView can dismiss while slide up or slide down
 * Created by duyuan797 on 17/2/3.
 */

public class DPhotoView extends PhotoView {

    private Paint mPaint;
    private float mOldX, mOldY;//last coordinate
    private float mX, mY;//current coordinate
    private float mMoveY;//sliding distance on Y axis
    private float mDownX, mDownY;//the starting coordinate of the slide
    private int mAlpha = 255;//transparency of background
    private boolean mIsMoved;//whether is ACTION_MOVE event
    private boolean mIsVpEvent = true;//whether is ViewPager event,if it is, can not slide on Y axis

    private IPhotoViewListener mPhotoViewListener;
    private boolean mIsDismiss;//当Activity退出的时候,防止View再次绘制,从而导致最后图片会在屏幕中间闪现

    public DPhotoView(Context context) {
        this(context, null);
    }

    public DPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
    }

    @Override protected void onDraw(Canvas canvas) {
        if (!mIsDismiss) {
            mPaint.setAlpha(mAlpha);
            canvas.drawRect(0, 0, 3000, 4000, mPaint);
            if (!mIsVpEvent) {
                canvas.translate(0, mMoveY);
            }

            super.onDraw(canvas);
        }
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mX = event.getX();
                mY = event.getY();

                if (Math.abs(mX - mDownX) < 100 && Math.abs(mY - mDownY) < 100) {
                    mIsVpEvent = Math.abs(mX - mOldX) > Math.abs(mY - mOldY);
                }

                //reduce touch sensitivity
                if (!mIsVpEvent && mOldY != 0 && Math.abs(mY - mOldY) > 3) {
                    mMoveY = mY - mDownY;
                    //calculate transparency
                    if (mY > mDownY) {
                        //下半屏
                        mAlpha = (int) (255 * (1 - (Math.abs(mMoveY) / (getHeight() - mDownY))));
                    } else {
                        //上半屏
                        mAlpha = (int) (255 * (1 - (Math.abs(mMoveY) / mDownY)));
                    }
                    invalidate();
                    mIsMoved = true;
                }
                mOldX = event.getX();
                mOldY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsMoved) {
                    //a single click event
                    if (mPhotoViewListener != null) {
                        mPhotoViewListener.onTap();
                    }
                } else {
                    //dismiss page while the distance of the slide on Y axis more than 1/3 height of screen
                    if (Math.abs(mMoveY) > getHeight() / 3) {
                        if (mPhotoViewListener != null) {
                            mPhotoViewListener.onDismiss();
                            mIsDismiss = true;
                        }
                    } else {
                        //reset the position of photo
                        mMoveY = 0;
                        mAlpha = 255;
                        invalidate();
                    }
                }
                mIsMoved = false;
                mOldY = 0;
                mDownY = 0;
                mMoveY = 0;
                mIsVpEvent = true;
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    public void setPhotoViewListener(IPhotoViewListener listener) {
        this.mPhotoViewListener = listener;
    }

    public interface IPhotoViewListener {
        void onTap();

        void onDismiss();
    }
}
