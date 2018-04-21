package com.jove.starskylib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private int mHeight, mWidth;

    private ValueAnimator mFarStarAnimator, mNearStarAnimator;
    private float mTranslationY;

    //近远点星星的数量
    static int NEAR_STARS_NUM = 10, FAR_STARS_NUM = 10;
    private List<Star> mNearStarList;
    private List<Star> mFarStarList;

    //流星
    private Paint mMeteorPaint;
    private int meteorSize = 100;
    private int meteorRadius = 4;

    public StarSkyView(@NonNull Context context) {
        this(context, null);
    }

    public StarSkyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarSkyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mNearStarList = new ArrayList<>();
        mFarStarList = new ArrayList<>();

        mMeteorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMeteorPaint.setColor(Color.WHITE);
        mMeteorPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //拿到布局的高宽
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        randomNewStar();
        starAnim();
    }

    private void drawMeteor(Canvas canvas) {

        canvas.save();
        canvas.translate(mTranslationY, mTranslationY);

        canvas.drawCircle(meteorSize - meteorRadius, meteorSize - meteorRadius, meteorRadius, mMeteorPaint);
        canvas.drawLine(0, 0, meteorSize - meteorRadius, (meteorSize - (meteorRadius * 2)), mMeteorPaint);
        canvas.drawLine(0, 0, (meteorSize - (meteorRadius * 2)), meteorSize - meteorRadius, mMeteorPaint);
        canvas.drawLine(meteorSize - meteorRadius, (meteorSize - (meteorRadius * 2)), (meteorSize - (meteorRadius * 2)), meteorSize - meteorRadius, mMeteorPaint);

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
            mFarStarAnimator.setDuration(40000);
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
        for (int i = 0; i < FAR_STARS_NUM; i++) {
            int x = rand.nextInt(mWidth);
            int y = rand.nextInt(mHeight);
            Star star = new Star(2, new Point(x, y));
            mFarStarList.add(star);
        }

        for (int i = 0; i < NEAR_STARS_NUM; i++) {
            int x = rand.nextInt(mWidth);
            int y = rand.nextInt(mHeight);
            Star star = new Star(4, new Point(x, y));
            mNearStarList.add(star);

        }
    }
}
