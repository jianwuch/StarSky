package com.jove.starskylib.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * Created by jianw on 18-4-21.
 */

public class Star {

    //星星大小
    public int size;

    //星星的初始化位置
    public Point position;

    //动画之后需要画的真实位置
    public Point realPosition;

    public Star(int size, Point position) {
        this.size = size;
        this.position = position;
        realPosition = new Point(position);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        //设置光晕
        paint.setShadowLayer(2, realPosition.x, realPosition.y, Color.WHITE);
        canvas.drawCircle(realPosition.x, realPosition.y, size, paint);
    }
}
