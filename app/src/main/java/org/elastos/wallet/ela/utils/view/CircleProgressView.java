package org.elastos.wallet.ela.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.elastos.wallet.R;
import org.elastos.wallet.ela.utils.ScreenUtil;

public class CircleProgressView extends View {
    private Paint paint;
    private float pointX;
    private float pointY;
    private float radius;


    public void setProgress(float progress) {
        this.progress = progress;
    }

    private float progress = 0;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(ScreenUtil.dp2px(getContext(), 12));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.whiter15));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas, radius);
        drawArc(canvas);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.blue11));
        drawCircle(canvas, radius - ScreenUtil.dp2px(getContext(), 2));
        drawText(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        radius = Math.min(width, height) / 2.0f;
        pointX = width / 2.0f;
        pointY = height / 2.0f;
    }

    private void drawCircle(Canvas canvas, float radius) {

        canvas.drawCircle(pointX, pointY, radius, paint);
    }

    private void drawArc(Canvas canvas) {
        paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        RectF rect = new RectF(0, 0, radius * 2, radius * 2);
        canvas.drawArc(rect, 270, progress / 100 * 360, true, paint);
    }

    private void drawText(Canvas canvas) {

        String text = progress + "%";

        paint.setTextSize(ScreenUtil.dp2px(getContext(), 11));
        //测量字符串长度
        float textLength = paint.measureText(text);

        paint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        //把文本画在圆心居中
        canvas.drawText(text, radius - textLength / 2, radius, paint);
    }
}