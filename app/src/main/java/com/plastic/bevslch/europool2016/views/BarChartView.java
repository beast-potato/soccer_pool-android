package com.plastic.bevslch.europool2016.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.plastic.bevslch.europool2016.R;

import java.util.List;

/**
 * Created by Oleksiy Martynov on 2016-06-06.
 */
public class BarChartView extends View {

    private static final float textWidthPercen = 0.05f;
    private static final float barPadding = 0.5f;
    private static final float gridStroke = 2f;
    private static final float xScale = 0.9f;
    private static final float yScale = 0.7f;
    private static final float xDataPaddingPercent = 0.05f;
    private static final float yAxisMaxLabelCount = 5;

    private int yAxisGroupMin = 5;

    private int width, height;
    private List<BarItemData> data;
    private int colorGrid, colorText;
    private Paint gridPaint, textPaint, barPaint, debugPaint;
    private RectF screenRect;
    private int selectedItem;
    private BarTapListener barTapListener;

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        colorGrid = ContextCompat.getColor(getContext(), R.color.colorDivider);
        colorText = ContextCompat.getColor(getContext(), R.color.colorTextIcons);

        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(colorGrid);
        gridPaint.setStrokeWidth(gridStroke);

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStrokeWidth(10);
        barPaint.setStrokeJoin(Paint.Join.ROUND);
        barPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(colorText);

        debugPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        debugPaint.setColor(Color.BLACK);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        screenRect = new RectF(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data != null && screenRect != null && data.size() != 0) { //got points
            drawBars(canvas, drawGrid(canvas, screenRect));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && screenRect != null && data != null && data.size() > 0 && barTapListener != null) { //we dont get ACTION_UP for some reason
            float screenX = event.getX();
            float screenY = event.getY();
            float viewX = screenX - getLeft();
            float viewY = screenY - getTop();
            float step = screenRect.width() / data.size();
            selectedItem = (int) (viewX / step);
            barTapListener.onItemTap(selectedItem);
            return true;
        }
        return false;
    }

    public void setBarTapListener(BarTapListener barTapListener) {
        this.barTapListener = barTapListener;
    }

    private RectF drawGrid(Canvas canvas, RectF container) {
        canvas.save();
        canvas.translate(container.width() * (1f - xScale) / 2f, container.height() * (1f - yScale) / 2f); //translate and scale to fit clipped text
        canvas.scale(xScale, yScale);

        float paddingYLabel = container.height() * 0.1f;
        int ySections = calcYAxisStep();
        int xSections = calcXAxisStep();
        float textSize = getTextSize(container);
        textPaint.setTextSize(textSize);

        float widestYLabel = 0;
        for (int i = 0; i <= ySections; i++) {//0 to section count
            String xAxisLabel = "" + yAxisGroupMin * i;
            float labelWidth = textPaint.measureText(xAxisLabel);
            if (labelWidth > widestYLabel) {
                widestYLabel = labelWidth;
            }
        }

        //horizontal grid lines and yAxis labels
        float gridRight = container.right - widestYLabel;
        for (int i = 0; i <= ySections; i++) {//0 to section count
            float y = container.bottom - i * yAxisGroupMin * getYScale(container);
            canvas.drawLine(container.left + widestYLabel, y, gridRight, y, gridPaint);
            String xAxisLabel = "" + yAxisGroupMin * i;
            canvas.drawText(xAxisLabel, container.left, y + textSize / 2, textPaint);
        }

        //xAxis labels
        float space = (container.width() - widestYLabel - container.width() * xDataPaddingPercent) / xSections;
        for (int i = 0; i < data.size(); i++) {
            int x = (int) (i * space + space / 2);
            String labelXText = "";//no labels for now
            float charOffset = textPaint.measureText(labelXText) / 2;
            canvas.drawText(labelXText, widestYLabel + container.left + x - charOffset, container.bottom + textSize + paddingYLabel, textPaint);
        }

        return new RectF(container.left + widestYLabel, container.top, gridRight + widestYLabel - container.width() * xDataPaddingPercent, container.bottom);

    }

    private void drawBars(Canvas canvas, RectF container) {


        float space = container.width() / calcXAxisStep();
        float scale = getYScale(container);
        for (int i = 0; i < data.size(); i++) {
            if (i != selectedItem) {
                barPaint.setStrokeWidth(getBarWidth(container, data.size()));
                int item = data.get(i).value;
                float scaledHeight = (float) item * scale;
                float x = i * space + space / 2;
                barPaint.setColor(data.get(i).color);
                canvas.drawLine(container.left + x, container.bottom, container.left + x, container.bottom - (scaledHeight == 0 ? 1 : scaledHeight), barPaint);
            } else {
                barPaint.setStrokeWidth(getBarWidth(container, data.size()) * 2);
                int item = data.get(i).value;
                float scaledHeight = (float) item * scale;
                float x = i * space + space / 2;
                barPaint.setColor(data.get(i).color);
                canvas.drawLine(container.left + x, container.bottom, container.left + x, container.bottom - (scaledHeight == 0 ? 1 : scaledHeight), barPaint);
            }
        }
    }

    private float getTextSize(RectF container) {
        return container.width() * textWidthPercen;
    }

    private int calcYAxisStep() {
        int max = getDataMax();
        int sections = max / yAxisGroupMin;
        if (max % yAxisGroupMin > 0) {
            ++sections;
        }
        while (sections > yAxisMaxLabelCount) {
            yAxisGroupMin += yAxisGroupMin; //double group size until groups are less than yAxisMaxLabelCount
            sections = max / yAxisGroupMin;
            if (max % yAxisGroupMin > 0) {
                ++sections;
            }
        }
        return sections;
    }

    private int calcXAxisStep() {
        return data.size();
    }

    private float getYScale(RectF container) {
        float maxYAxisVal = (float) calcYAxisStep() * yAxisGroupMin;
        return container.height() / maxYAxisVal;
    }

    private int getBarWidth(RectF container, int itemCount) {
        return (int) ((container.width() / (float) itemCount) * barPadding);
    }

    private int getDataMax() {
        int max = 0;
        for (BarItemData val : data) {
            if (val.value > max) {
                max = val.value;
            }
        }
        return max;
    }

    public void setData(List<BarItemData> data) {
        boolean allZero = true;
        for (BarItemData itemData : data) {
            if (itemData.value > 0) {
                allZero = false;
            }
        }
        if (allZero) {
            for (BarItemData itemData : data) {
                itemData.value = 1;
            }
        }
        this.data = data;
        invalidate();
    }

    public void setSelectedItem(int position) {
        selectedItem = position;
        invalidate();
    }

    public static class BarItemData {
        public Integer value;
        public Integer color;

        public BarItemData(Integer value, Integer color) {
            this.value = value;
            this.color = color;
        }
    }

    public interface BarTapListener {
        void onItemTap(int itemIndex);
    }
}

