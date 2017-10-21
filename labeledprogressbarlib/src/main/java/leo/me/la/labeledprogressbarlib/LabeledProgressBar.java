package leo.me.la.labeledprogressbarlib;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LabeledProgressBar extends View implements ProgressBar {

    private boolean indeterminate;

    private String labelText;

    private LabelValueType labelValueType;

    private int progressHeight;

    private int value;

    private int maxValue;

    private int minValue;

    Paint labelBackgroundPaint;

    Paint backgroundPaint;

    Paint labelTextPaint;

    Paint progressPaint;

    private static final int INDICATOR_HEIGHT = 4;                      //in dp

    private static final int INDICATOR_WIDTH = 9;                       //in dp

    private static final int PADDING_TOP_BOTTOM_PROGRESS_LABEL = 2;     //in dp

    private static final int PADDING_LEFT_RIGHT_PROGRESS_LABEL = 4;     //in dp
    private static final int RADIUS_SIZE = 4;                           //in dp
    private int indicatorHeight;                            //in px

    private int indicatorWidth;                             //in px
    private int paddingTopBottomProgressLabel;              //in px
    private int paddingLeftRightProgressLabel;              //in px

    private RectF rectF;

    private float alpha;

    private int beginColor;
    private int completeColor;
    private ValueAnimator indeterminateAnimator;

    public LabeledProgressBar(Context context) {
        super(context);
        initView();
        createIndeterminateAnimator();
    }

    private void createIndeterminateAnimator() {
        indeterminateAnimator = ValueAnimator.ofInt(minValue + (maxValue - minValue) / 10, maxValue - (maxValue - minValue) / 10);
        indeterminateAnimator.setDuration(500);
        indeterminateAnimator.setRepeatMode(ValueAnimator.REVERSE);
        indeterminateAnimator.setRepeatCount(ValueAnimator.INFINITE);
        indeterminateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                value = (int) valueAnimator.getAnimatedValue();
                invalidateWithColorUpdate();
            }
        });
    }

    public LabeledProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GProgress);

        indeterminate = typedArray.getBoolean(R.styleable.GProgress_indeterminate, false);
        if (indeterminate)
            alpha = 0;
        labelValueType =
                LabelValueType.values()[typedArray
                        .getInt(R.styleable.GProgress_labelValueType,
                                LabelValueType.Percentage.ordinal())];

        beginColor = typedArray
                .getColor(R.styleable.GProgress_progressBeginColor, Color.parseColor("#8BC34A"));
        completeColor = typedArray
                .getColor(R.styleable.GProgress_progressCompleteColor, Color.parseColor("#445566"));
        int labelBackgroundColor = typedArray
                .getColor(R.styleable.GProgress_labelBackgroundColor,
                        Color.parseColor("#8BC34A"));
        int labelTextColor = typedArray
                .getColor(R.styleable.GProgress_textColor,
                        Color.parseColor("#F1F8E9"));

        progressHeight = typedArray.getDimensionPixelSize(R.styleable.GProgress_progressHeight,
                Utils.dpToPx(5));

        int labelTextSize = typedArray
                .getDimensionPixelSize(R.styleable.GProgress_textSize,
                        Utils.dpToPx(12));

        maxValue = typedArray.getInt(R.styleable.GProgress_maxValue, 100);
        minValue = typedArray.getInt(R.styleable.GProgress_minValue, 0);
        value = typedArray.getInt(R.styleable.GProgress_value, minValue);
        if (value < minValue)
            value = minValue;
        if (value > maxValue)
            value = maxValue;
        labelText = typedArray.getString(R.styleable.GProgress_labelText);
        typedArray.recycle();

        labelBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelBackgroundPaint.setColor(labelBackgroundColor);
        labelBackgroundPaint.setStyle(Paint.Style.FILL);

        labelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelTextPaint.setColor(labelTextColor);
        labelTextPaint.setTextSize(labelTextSize);

        changeProgressColor((float) (this.value - minValue) / (maxValue - minValue));
        createIndeterminateAnimator();
    }

    private void initView() {
        alpha = 1f;
        indicatorHeight = Utils.dpToPx(INDICATOR_HEIGHT);
        indicatorWidth = Utils.dpToPx(INDICATOR_WIDTH);
        paddingTopBottomProgressLabel = Utils.dpToPx(PADDING_TOP_BOTTOM_PROGRESS_LABEL);
        paddingLeftRightProgressLabel = Utils.dpToPx(PADDING_LEFT_RIGHT_PROGRESS_LABEL);

        rectF = new RectF();

        indeterminate = false;

        labelValueType = LabelValueType.Percentage;

        beginColor = Color.parseColor("#8BC34A");
        completeColor = Color.parseColor("#445566");
        int labelBackgroundColor = Color.parseColor("#8BC34A");
        int labelTextColor = Color.parseColor("#F1F8E9");

        progressHeight = Utils.dpToPx(5);

        int labelTextSize = Utils.dpToPx(12);

        maxValue = 100;
        minValue = 0;
        value = minValue;

        labelText = "";

        labelBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelBackgroundPaint.setColor(labelBackgroundColor);
        labelBackgroundPaint.setStyle(Paint.Style.FILL);

        labelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelTextPaint.setColor(labelTextColor);
        labelTextPaint.setTextSize(labelTextSize);

        changeProgressColor((float) (this.value - minValue) / (maxValue - minValue));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;

        int desiredHeight;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Paint.FontMetrics fm = labelTextPaint.getFontMetrics();
        int textHeight = (int) (fm.bottom - fm.top);
        int progressLabelHeight = 2 * paddingTopBottomProgressLabel + textHeight + indicatorHeight;
        desiredHeight = progressLabelHeight + progressHeight / 2 + getPaddingBottom() + getPaddingTop();

        if (heightMode == MeasureSpec.EXACTLY) {
            height = desiredHeight;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(widthSize, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        if (indeterminate) {
            if (!indeterminateAnimator.isStarted()) {
                indeterminateAnimator.start();
            }
            drawLoadingProgress(canvas);
            drawLabel(canvas);
            return;
        }
        drawProgress(canvas);
        drawLabel(canvas);
    }

    private void drawLoadingProgress(Canvas canvas) {
        int progressWidth = getWidth() - 2 * indicatorWidth - (getPaddingLeft() + getPaddingRight());
        int centerX = (value - minValue) * progressWidth / (maxValue - minValue) + indicatorWidth + getPaddingLeft();
        int left = centerX - progressWidth / 10;
        int right = centerX + progressWidth / 10;
        int top = getHeight() - getPaddingBottom() - progressHeight;
        int bottom = getHeight() - getPaddingBottom();
        rectF.set(left, top, right, bottom);
        canvas.drawRoundRect(rectF, progressHeight / 2, progressHeight / 2, progressPaint);
    }

    private void drawBackground(Canvas canvas) {
        int right = getWidth() - getPaddingRight() - indicatorWidth;
        int left = getPaddingLeft() + indicatorWidth;
        int top = getHeight() - getPaddingBottom() - progressHeight;
        int bottom = getHeight() - getPaddingBottom();
        rectF.set(left, top, right, bottom);
        canvas.drawRoundRect(rectF, progressHeight / 2, progressHeight / 2, backgroundPaint);
    }

    private void drawProgress(Canvas canvas) {
        int progressWidth = getWidth() - 2 * indicatorWidth - (getPaddingLeft() + getPaddingRight());
        int right = (value - minValue) * progressWidth / (maxValue - minValue) + indicatorWidth + getPaddingLeft();
        int left = getPaddingLeft() + indicatorWidth;
        int top = getHeight() - getPaddingBottom() - progressHeight;
        int bottom = getHeight() - getPaddingBottom();
        rectF.set(left, top, right, bottom);
        canvas.drawRoundRect(rectF, progressHeight / 2, progressHeight / 2, progressPaint);
    }

    private void drawLabel(Canvas canvas) {
        if (alpha == 0)
            return;
        String displayLabel;
        switch (labelValueType) {
            case Value:
                displayLabel = String.valueOf(value);
                break;
            case Custom:
                displayLabel = labelText == null ? "" : labelText;
                break;
            case Percentage:
            default:
                displayLabel = String.valueOf((value - minValue) * 100 / (maxValue - minValue)) + "%";
                break;
        }
        int radiusSize = Utils.dpToPx(RADIUS_SIZE);
        Rect bounds = new Rect();
        labelTextPaint.getTextBounds(displayLabel, 0, displayLabel.length(), bounds);
        Paint.FontMetrics fm = labelTextPaint.getFontMetrics();
        int textWidth = bounds.width();

        int bubbleWidth = textWidth + 2 * paddingLeftRightProgressLabel;
        if (bubbleWidth > getWidth() - (getPaddingLeft() + getPaddingRight())) {
            bubbleWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
        }
        if (bubbleWidth < indicatorWidth * 3)
            bubbleWidth = indicatorWidth * 3;

        int bubbleHeight = (int) (fm.bottom - fm.top + 2 * paddingTopBottomProgressLabel);

        int progressWidth = getWidth() - 2 * indicatorWidth - (getPaddingLeft() + getPaddingRight());
        int arrowLeftMostX = (value - minValue) * progressWidth / (maxValue - minValue) + indicatorWidth / 2 + getPaddingLeft();
        int arrowLeftMostY = bubbleHeight + getPaddingTop();

        int arrowBottomX = arrowLeftMostX + indicatorWidth / 2;
        int arrowBottomY = arrowLeftMostY + indicatorHeight;

        int arrowRightMostX = arrowLeftMostX + indicatorWidth;

        int top, left, right, bottom;
        top = getPaddingTop();
        bottom = bubbleHeight + getPaddingTop();
        if (arrowBottomX >= bubbleWidth / 2 + getPaddingLeft() && arrowBottomX + bubbleWidth / 2 <= getWidth() - getPaddingRight()) {
            left = arrowBottomX - bubbleWidth / 2;
            right = arrowBottomX + bubbleWidth / 2;
        } else if (arrowBottomX < bubbleWidth / 2 + getPaddingLeft()) {
            left = getPaddingLeft();
            right = bubbleWidth + getPaddingLeft();
        } else {
            left = getWidth() - getPaddingRight() - bubbleWidth;
            right = getWidth() - getPaddingRight();
        }

        Path labelPath = new Path();
        // arrow left most point
        labelPath.moveTo(arrowLeftMostX, arrowLeftMostY);
        // arrow bottom point
        labelPath.lineTo(arrowBottomX, arrowBottomY);
        //arrow right most point
        labelPath.lineTo(arrowRightMostX, arrowLeftMostY);
        //bottom right point
        labelPath.lineTo(right - radiusSize, bottom);
        //curve at bottom right
        rectF.set(right - 2 * radiusSize, bottom - 2 * radiusSize, right, bottom);
        labelPath.arcTo(rectF, 90, -90);
        // top right point
        labelPath.lineTo(right, top - radiusSize);
        // curve at top right
        rectF.set(right - 2 * radiusSize, top, right, top + 2 * radiusSize);
        labelPath.arcTo(rectF, 0, -90);
        // top left point
        labelPath.lineTo(left + radiusSize, top);
        // curve at top left
        rectF.set(left, top, left + 2 * radiusSize, top + 2 * radiusSize);
        labelPath.arcTo(rectF, 270, -90);
        // bottom left point
        labelPath.lineTo(left, bottom - radiusSize);
        // curve at bottom left
        rectF.set(left, bottom - 2 * radiusSize, left + 2 * radiusSize, bottom);
        labelPath.arcTo(rectF, 180, -90);
        labelPath.lineTo(arrowLeftMostX, arrowLeftMostY);
        labelPath.close();
        if (alpha < 1) {
            labelTextPaint.setAlpha((int) (alpha * 255));
            labelBackgroundPaint.setAlpha((int) (alpha * 255));
        }
        canvas.drawPath(labelPath, labelBackgroundPaint);
        canvas.drawText(displayLabel,
                (right + left) / 2 - textWidth / 2,
                top + paddingTopBottomProgressLabel - fm.top,
                labelTextPaint);
    }

    @Override
    public void setValue(int value) {
        if (indeterminate)
            return;
        this.value = value;
        invalidateWithColorUpdate();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void animateToValue(int value, long duration) {
        if (indeterminate)
            return;
        if (value < minValue)
            value = minValue;
        if (value > maxValue)
            value = maxValue;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);

        ValueAnimator animatorLabel = ValueAnimator.ofInt(this.value, value);
        animatorLabel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LabeledProgressBar.this.value = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        float begin = (float) (this.value - minValue) / (maxValue - minValue);
        float complete = (float) (value - minValue) / (maxValue - minValue);
        ValueAnimator animatorProgress = ValueAnimator.ofFloat(begin, complete);
        animatorProgress.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                changeProgressColor((float) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        animatorSet.play(animatorProgress).with(animatorLabel);
        animatorSet.start();
    }

    private void changeProgressColor(float value) {
        final float[] from = new float[3],
                to = new float[3];
        Color.colorToHSV(beginColor, from);
        Color.colorToHSV(completeColor, to);
        final float[] hsv = new float[3];
        hsv[0] = from[0] + (to[0] - from[0]) * value;
        hsv[1] = from[1] + (to[1] - from[1]) * value;
        hsv[2] = from[2] + (to[2] - from[2]) * value;
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(Color.HSVToColor(hsv));
        progressPaint.setStyle(Paint.Style.FILL);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.HSVToColor(hsv));
        backgroundPaint.setAlpha(100);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public int getMinValue() {
        return minValue;
    }

    @Override
    public void setMinValue(int minValue) {
        if (indeterminate)
            return;
        this.minValue = minValue;
        invalidateWithColorUpdate();
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public void setMaxValue(int maxValue) {
        if (indeterminate)
            return;
        this.maxValue = maxValue;
        invalidateWithColorUpdate();
    }

    @Override
    public void setProgressStartColor(int color) {
        beginColor = color;
        invalidateWithColorUpdate();
    }

    @Override
    public void setProgressCompleteColor(int color) {
        completeColor = color;
        invalidateWithColorUpdate();
    }

    @Override
    public void setLabelValueType(LabelValueType type) {
        labelValueType = type;
        invalidateWithColorUpdate();
    }

    @Override
    public void setLabelText(String value) {
        labelText = value;
        invalidateWithColorUpdate();
    }

    @Override
    public void setLabelBackgroundColor(int color) {
        labelBackgroundPaint.setColor(color);
        invalidateWithColorUpdate();
    }

    @Override
    public void setTextColor(int color) {
        labelTextPaint.setColor(color);
        invalidateWithColorUpdate();
    }

    @Override
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        if (indeterminate && !indeterminateAnimator.isRunning()) {
            createIndeterminateAnimator();
            ValueAnimator hide = ValueAnimator.ofFloat(0, 1);
            hide.setDuration(500);
            hide.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    alpha = 1 - valueAnimator.getAnimatedFraction();
                    invalidateWithColorUpdate();
                }
            });
            hide.start();
        }
        if (!indeterminate && indeterminateAnimator.isRunning()) {
            indeterminateAnimator.removeAllUpdateListeners();
            indeterminateAnimator.end();
            ValueAnimator show = ValueAnimator.ofFloat(0, 1);
            show.setDuration(500);
            show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    alpha = valueAnimator.getAnimatedFraction();
                    invalidateWithColorUpdate();
                }
            });
            show.start();
        }
        invalidateWithColorUpdate();
    }


    public void invalidateWithColorUpdate() {
        changeProgressColor((float) (value - minValue) / (maxValue - minValue));
        invalidate();
    }

    public enum LabelValueType {
        Percentage, Value, Custom
    }
}
