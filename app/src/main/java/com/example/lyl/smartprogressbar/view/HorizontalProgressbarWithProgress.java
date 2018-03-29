package com.example.lyl.smartprogressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.example.lyl.smartprogressbar.R;

/**
 * Created by dllo on 18/3/28.
 */

public class HorizontalProgressbarWithProgress extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;
    private static final int DEFAULT_TEXT_OFFSET = 10;

    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    private int mUnReachHeight = DEFAULT_HEIGHT_UNREACH;
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();

    private int mRealWidth;

    public HorizontalProgressbarWithProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        obtain(attrs);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);

        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        boolean noNeedUnReach = false;

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);


        float radio = getProgress() * 1.0f / getMax();
        float progressX = radio * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }

        float endX = radio * mRealWidth - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);


        }

        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);

        if (!noNeedUnReach){
            float start = progressX + mTextOffset/2 + textWidth;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }

        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max
                    (Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }

        return result;
    }

    //获取自定义属性
    private void obtain(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressbarWithProgress);

        mTextSize =
                (int) typedArray.getDimension
                        (R.styleable.HorizontalProgressbarWithProgress_progress_text_size, mTextSize);

        mTextColor = typedArray.getColor
                (R.styleable.HorizontalProgressbarWithProgress_progress_text_color, mTextColor);

        mTextOffset = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressbarWithProgress_progress_text_offset, mTextOffset);

        mUnReachColor = typedArray.getColor(
                R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color, mUnReachColor
        );

        mUnReachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height, mUnReachHeight
        );

        mReachColor = typedArray.getColor(
                R.styleable.HorizontalProgressbarWithProgress_progress_reach_color, mReachColor);

        mReachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressbarWithProgress_progress_reach_height, mReachHeight);

        typedArray.recycle();

        mPaint.setTextSize(mTextSize);
    }


    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }
}
