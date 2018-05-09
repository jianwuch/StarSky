package com.jove.starskylib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import com.jove.starskylib.model.Star;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jianw on 18-4-21.
 */

public class StarSkyView extends FrameLayout {

    private static final int DEFAULT_FAR_STAR_TRANS_TIMES_SECOND = 40 * 1000;
    private static final int DEFAULT_METEOR_SPEED = 4;//每次刷新移动的距离
    private static final int DEFAULT_METEOR_STAR_SIZE = 2;
    private static final int DEFAULT_STAR_NUMS = 10;
    private int mHeight, mWidth;

    private ValueAnimator mFarStarAnimator;
    private float mTranslationY;
    private int meteorTran;//流星水平移动的距离

    //近点和远点星星分别多少个
    private int mStarNums;

    //移动一个循环需要的时间
    private int mTimes;
    private List<Star> mNearStarList;
    private List<Star> mFarStarList;

    //流星
    private Paint mMeteorPaint;
    private int meteorSize = 100;
    private int meteorRadius = DEFAULT_METEOR_STAR_SIZE;
    private float mRandomPosition;//流星开始的随机位置
    private Random mMeteorRandom;

    private android.os.Handler mHandler;

    public StarSkyView(@NonNull Context context) {
        this(context, null);
    }

    public StarSkyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarSkyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarSkyView);
        meteorRadius = typedArray.getInt(R.styleable.StarSkyView_meteor_head_size,
                DEFAULT_METEOR_STAR_SIZE);
        mStarNums = typedArray.getInt(R.styleable.StarSkyView_star_nums, DEFAULT_STAR_NUMS);

        mTimes = typedArray.getInt(R.styleable.StarSkyView_one_cycle_time_ms,
                DEFAULT_FAR_STAR_TRANS_TIMES_SECOND);
        typedArray.recycle();
    }

    private void init() {
        mNearStarList = new ArrayList<>();
        mFarStarList = new ArrayList<>();

        mMeteorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMeteorPaint.setColor(Color.WHITE);
        mMeteorPaint.setStyle(Paint.Style.FILL);
        mMeteorRandom = new Random();

        mHandler = new android.os.Handler();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到布局的高宽
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        randomNewStar();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starAnim();
            }
        }, 1000);
    }

    private void drawMeteor(Canvas canvas) {

        //meteorSize流星的宽度，最开始的时候流星不显示，需要移出去流星的长度距离
        float needTransX;
        float needTransY;
        //float scal = 1;
        meteorTran += DEFAULT_METEOR_SPEED;

        //mHeight+metrorSize是为了让流星整个完全的滑出去
        if (meteorTran >= mHeight + meteorSize) {

            //为了保证流星能够全屏分布，需要考虑左下角的情况
            mRandomPosition = mMeteorRandom.nextInt(mWidth + mHeight);
            mRandomPosition -= mHeight;
            meteorTran = 0;
        }
        needTransX = (meteorTran + mRandomPosition);
        needTransY = meteorTran - meteorSize;
        //if (meteorTran <= mHeight * (2 / 3.0f)) {
        //    scal = (float) (0.5 + 0.5 * meteorTran / (mHeight * (2 / 3.0f)));
        //}

        canvas.save();
        //canvas.scale(scal, scal, meteorSize / 2, meteorSize / 2);
        canvas.translate(needTransX, needTransY);

        canvas.drawCircle(meteorSize - meteorRadius, meteorSize - meteorRadius, meteorRadius,
                mMeteorPaint);
        Path triangle = new Path();
        triangle.lineTo(meteorSize - meteorRadius, (meteorSize - (meteorRadius * 2)));
        triangle.lineTo((meteorSize - (meteorRadius * 2)), meteorSize - meteorRadius);
        triangle.close();

        canvas.drawPath(triangle, mMeteorPaint);

        canvas.restore();
    }

    /**
     * 实现星星的从右往左动画
     */
    private void starAnim() {
        if (null == mFarStarAnimator) {
            mFarStarAnimator = ValueAnimator.ofFloat(0, 1);
            mFarStarAnimator.setRepeatCount(ValueAnimator.INFINITE);//设置无限重复
            mFarStarAnimator.setRepeatMode(ValueAnimator.RESTART);//设置重复模式
            mFarStarAnimator.setInterpolator(new LinearInterpolator());
            mFarStarAnimator.setDuration(mTimes);
            mFarStarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mTranslationY = (float) animation.getAnimatedValue() * mWidth;
                    invalidate();
                }
            });
        }
        mFarStarAnimator.cancel();
        mFarStarAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制星星
        DrawStar(canvas);

        //绘制流星
        drawMeteor(canvas);
    }

    private void DrawStar(Canvas canvas) {
        for (int i = 0; i < mNearStarList.size(); i++) {
            reComputerStarPosition(mNearStarList.get(i), 2).draw(canvas);
        }

        for (int i = 0; i < mFarStarList.size(); i++) {
            reComputerStarPosition(mFarStarList.get(i), 1).draw(canvas);
        }
    }

    @NonNull
    private Star reComputerStarPosition(Star star, float rate) {
        float realTrans = mTranslationY * rate;
        int currentY;
        if (realTrans > star.position.x) {
            currentY = (int) (mWidth - (realTrans - star.position.x));
        } else {
            currentY = (int) (star.position.x - realTrans);
        }

        star.realPosition.x = currentY;

        if (star.realPosition.x < 0) {
            star.realPosition.x = mWidth + star.realPosition.x;
        }
        return star;
    }

    private void randomNewStar() {
        mNearStarList.clear();
        mFarStarList.clear();
        Random rand = new Random();
        for (int i = 0; i < mStarNums; i++) {
            int x = rand.nextInt(mWidth);
            int y = rand.nextInt(mHeight);
            Star star = new Star(2, new Point(x, y));
            mFarStarList.add(star);
        }

        for (int i = 0; i < mStarNums; i++) {
            int x = rand.nextInt(mWidth);
            int y = rand.nextInt(mHeight);
            Star star = new Star(4, new Point(x, y));
            mNearStarList.add(star);
        }
    }

    /**
     * 暂停动画
     */
    public void pauseAnim() {
        if (mFarStarAnimator == null) return;
        mFarStarAnimator.pause();
    }

    /**
     * 继续动画
     */
    public void resumeAnim() {
        if (mFarStarAnimator == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFarStarAnimator.resume();
            }
        }, 500);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}
