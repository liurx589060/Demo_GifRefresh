/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.example.liurunxiong.demo_gifrefresh.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.liurunxiong.demo_gifrefresh.DisplayUtils;
import com.example.liurunxiong.demo_gifrefresh.R;

import java.io.IOException;
import java.util.Random;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Yan Zhenjie on 2016/11/21.
 */
public class ParallaxGifHeader extends FrameLayout implements PtrUIHandler {

    ImageView mIvBack1;
    ImageView mIvBack2;
    GifImageView mIvIcon;
    private GifDrawable mGifDrawable;
    private Bitmap mBitmap;
    private int index;
    private boolean isRefreshBegin;
    private Random mRandom;

    private Context mContext;

    private Animation mBackAnim1;
    private Animation mBackAnim2;
//    private AnimationDrawable mAnimationDrawable;
    private boolean isRunAnimation = false;
    private int limitX;

    private void initialize() {
        LayoutInflater.from(getContext()).inflate(R.layout.refresh_parallax_gif, this);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.refresh_background));

        mIvBack1 = (ImageView) findViewById(R.id.iv_background_1);
        mIvBack2 = (ImageView) findViewById(R.id.iv_background_2);
        mIvIcon = (GifImageView) findViewById(R.id.iv_refresh_icon);
        try {
            mGifDrawable = new GifDrawable(getResources(),R.drawable.b);
            GifDrawable d = new GifDrawable(getResources(),R.drawable.background);
            mIvBack1.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRandom = new Random();

//        mAnimationDrawable = (AnimationDrawable) mIvIcon.getDrawable();
        mBackAnim1 = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_down_background_1);
        mBackAnim2 = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_down_background_2);
    }

    public ParallaxGifHeader(Context context) {
        this(context, null, 0);
    }

    public ParallaxGifHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxGifHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initialize();
    }

    /**
     * 开始刷新动画。
     */
    private void startAnimation() {
        if (!isRunAnimation) {
            isRunAnimation = true;
            mIvBack1.startAnimation(mBackAnim1);
            mIvBack2.startAnimation(mBackAnim2);
//            mIvIcon.setImageDrawable(mAnimationDrawable);
//            mAnimationDrawable.start();
        }
    }

    /**
     * 停止刷新动画。
     */
    private void stopAnimation() {
        if (isRunAnimation) {
            isRunAnimation = false;
            mIvBack1.clearAnimation();
            mIvBack2.clearAnimation();
//            mAnimationDrawable.stop();
        }
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        stopAnimation();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        isRefreshBegin = true;
//        stopAnimation();
//        startAnimation();
        GifDrawable drawable = null;
        try {
            drawable = new GifDrawable(getResources(),R.drawable.a);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.e("yy","begin");
        mIvIcon.setImageDrawable(drawable);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        stopAnimation();
        isRefreshBegin = false;
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int offsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int countFrame = mGifDrawable.getNumberOfFrames();

        int currentOffset = currentPos < offsetToRefresh ?currentPos:offsetToRefresh;
        float rate = currentOffset*1.0f / offsetToRefresh*1.0f;
        int index = (int) (rate*countFrame);
//        Log.e("yy","" + rate + "---index=" + index);

        if(!isRefreshBegin) {
            if(mBitmap != null) {
                mIvIcon.setImageBitmap(null);
                mBitmap.recycle();
            }
            mBitmap = mGifDrawable.getCurrentFrame();
            mGifDrawable.seekToFrameAndGet(index);
//            Log.e("yy","current=" + mGifDrawable.getCurrentFrameIndex() + "-----" + mBitmap);
            mIvIcon.setImageBitmap(mBitmap);
        }

        if (currentPos <= offsetToRefresh) {
            if (limitX == 0) calcLimitX();

            double percent = (double) currentPos / offsetToRefresh;
            int targetX = (int) (limitX * percent);
            mIvIcon.setTranslationX(targetX);

//            int newPercent = (int) (percent * 100);
//
//            if (newPercent % 10 == 0) {
//                double i = newPercent / 10;
//                if (i % 2 == 0) {
//                    mIvIcon.setImageResource(R.drawable.refresh_down_icon_3);
//                } else {
//                    mIvIcon.setImageResource(R.drawable.refresh_down_icon_1);
//                }
//            } else if (newPercent % 5 == 0) {
//                mIvIcon.setImageResource(R.drawable.refresh_down_icon_2);
//            }

//            index = mRandom.nextInt(mGifDrawable.getNumberOfFrames());
//            mGifDrawable.seekToFrame(index);
//            if(mBitmap != null) {
//                mIvIcon.setImageBitmap(null);
//               mBitmap.recycle();
//                mBitmap = mGifDrawable.getCurrentFrame();
//            }
//            Log.e("yy","" + mBitmap);
//            mIvIcon.setImageBitmap(mBitmap);
        }
    }

    private void calcLimitX() {
        limitX = DisplayUtils.screenWidth / 2;
        int mIconIvWidth = mIvIcon.getMeasuredWidth();
        limitX -= (mIconIvWidth / 2);
    }
}
