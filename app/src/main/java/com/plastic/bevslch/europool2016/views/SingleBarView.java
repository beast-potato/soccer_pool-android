package com.plastic.bevslch.europool2016.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Admin on 6/12/2016.
 */

public class SingleBarView extends View {
    private static final String TAG = "SingleBarView";
    private RectF canvasRect;
    private Paint barPaint;

    private int color;
    private float percent;

    public SingleBarView(Context context) {
        super(context);
        init();
    }

    public SingleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStrokeWidth(10);
        barPaint.setStrokeJoin(Paint.Join.ROUND);
        barPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvasRect != null) {
            barPaint.setStrokeWidth(canvasRect.height());
            barPaint.setColor(color);
            float xScale = 1f - barPaint.getStrokeWidth() / canvasRect.width();
            canvas.save();
            canvas.translate(canvasRect.width() * (1f - xScale) / 2f, 0);
            canvas.scale(xScale, 1);
            canvas.drawLine(canvasRect.left, canvasRect.height() / 2, canvasRect.width() * percent, canvasRect.height() / 2, barPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasRect = new RectF(0, 0, w, h);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }
}
