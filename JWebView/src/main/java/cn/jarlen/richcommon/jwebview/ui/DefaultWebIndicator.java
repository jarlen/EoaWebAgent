package cn.jarlen.richcommon.jwebview.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.jarlen.richcommon.jwebview.R;
import cn.jarlen.richcommon.jwebview.util.DensityUtils;

public class DefaultWebIndicator extends FrameLayout implements IWebIndicator {

    /**
     * 进度条颜色
     */
    private int mIndicatorColor;
    /**
     * 进度条的画笔
     */
    private Paint mPaint;
    /**
     * 进度条动画
     */
    private Animator mAnimator;
    /**
     * 控件的宽度
     */
    private int mTargetWidth = 0;

    /**
     * 默认匀速动画最大的时长
     */
    public static final int MAX_UNIFORM_SPEED_DURATION = 8 * 1000;
    /**
     * 默认加速后减速动画最大时长
     */
    public static final int MAX_DECELERATE_SPEED_DURATION = 450;
    /**
     * 结束动画时长 ， Fade out 。
     */
    public static final int DO_END_ANIMATION_DURATION = 600;

    /**
     * 当前匀速动画最大的时长
     */
    private static int CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
    /**
     * 当前加速后减速动画最大时长
     */
    private static int CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;

    /**
     * 标志当前进度条的状态
     */
    private int TAG = 0;
    public static final int UN_START = 0;
    public static final int STARTED = 1;
    public static final int FINISH = 2;

    private float mTarget = 0f;

    private float mCurrentProgress = 0F;

    /**
     * 默认的高度
     */
    public static int mIndicatorHeight = 3;

    public DefaultWebIndicator(@NonNull Context context) {
        this(context, null);
    }

    public DefaultWebIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DefaultWebIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DefaultWebIndicator);
            this.mIndicatorColor = typedArray.getColor(R.styleable.DefaultWebIndicator_indicator_color, Color.parseColor("#FF599BF8"));
            this.mIndicatorHeight = (int) typedArray.getDimension(R.styleable.DefaultWebIndicator_indicator_height, DensityUtils.dip2px(context, 2));
            typedArray.recycle();
        } else {
            this.mIndicatorColor = Color.parseColor("#FF599BF8");
            this.mIndicatorHeight = DensityUtils.dip2px(context, 2);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mIndicatorColor);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);

        mTargetWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setColor(int color) {
        this.mIndicatorColor = color;
        mPaint.setColor(color);
    }

    public void setColor(String color) {
        this.setColor(Color.parseColor(color));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.AT_MOST) {
            w = Math.min(w, getContext().getResources().getDisplayMetrics().widthPixels);
        }
        if (hMode == MeasureSpec.AT_MOST) {
            h = mIndicatorHeight;
        }
        this.setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mCurrentProgress / 100 * (float) this.getWidth(), this.getHeight(), mPaint);
    }

    @Override
    public void show() {
        if (getVisibility() != View.GONE) {
            return;
        }
        this.setVisibility(View.VISIBLE);
        mCurrentProgress = 0f;
        startAnim(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mTargetWidth = getMeasuredWidth();
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        if (mTargetWidth >= screenWidth) {
            CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION;
            CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION;
        } else {
            //取比值
            float rate = this.mTargetWidth / (float) screenWidth;
            CURRENT_MAX_UNIFORM_SPEED_DURATION = (int) (MAX_UNIFORM_SPEED_DURATION * rate);
            CURRENT_MAX_DECELERATE_SPEED_DURATION = (int) (MAX_DECELERATE_SPEED_DURATION * rate);
        }
    }

    public void setProgress(float progress) {
        if (getVisibility() == View.GONE) {
            setVisibility(View.VISIBLE);
        }
        if (progress < 95f) {
            return;
        }
        if (TAG != FINISH) {
            startAnim(true);
        }
    }

    @Override
    public void hide() {
        TAG = FINISH;
    }

    private void startAnim(boolean isFinished) {

        float v = isFinished ? 100 : 95;

        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
        boolean progressStart = Float.compare(mCurrentProgress, 0f) == 0;
        mCurrentProgress = progressStart ? 0.00000001f : mCurrentProgress;

        if (!isFinished) {
            ValueAnimator mAnimator = ValueAnimator.ofFloat(mCurrentProgress, v);
            float residue = 1f - mCurrentProgress / 100 - 0.05f;
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setDuration((long) (residue * CURRENT_MAX_UNIFORM_SPEED_DURATION));
            mAnimator.addUpdateListener(mAnimatorUpdateListener);
            mAnimator.start();
            this.mAnimator = mAnimator;
        } else {

            ValueAnimator segment95Animator = null;
            if (mCurrentProgress < 95f) {
                segment95Animator = ValueAnimator.ofFloat(mCurrentProgress, 95);
                float residue = 1f - mCurrentProgress / 100f - 0.05f;
                segment95Animator.setInterpolator(new LinearInterpolator());
                segment95Animator.setDuration((long) (residue * CURRENT_MAX_DECELERATE_SPEED_DURATION));
                segment95Animator.setInterpolator(new DecelerateInterpolator());
                segment95Animator.addUpdateListener(mAnimatorUpdateListener);
            }


            ObjectAnimator mObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
            mObjectAnimator.setDuration(DO_END_ANIMATION_DURATION);
            ValueAnimator mValueAnimatorEnd = ValueAnimator.ofFloat(95f, 100f);
            mValueAnimatorEnd.setDuration(DO_END_ANIMATION_DURATION);
            mValueAnimatorEnd.addUpdateListener(mAnimatorUpdateListener);

            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mObjectAnimator, mValueAnimatorEnd);

            if (segment95Animator != null) {
                AnimatorSet mAnimatorSet1 = new AnimatorSet();
                mAnimatorSet1.play(mAnimatorSet).after(segment95Animator);
                mAnimatorSet = mAnimatorSet1;
            }
            mAnimatorSet.addListener(mAnimatorListenerAdapter);
            mAnimatorSet.start();
            mAnimator = mAnimatorSet;
        }

        TAG = STARTED;
        mTarget = v;
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float t = (float) animation.getAnimatedValue();
            DefaultWebIndicator.this.mCurrentProgress = t;
            DefaultWebIndicator.this.invalidate();
        }
    };

    private AnimatorListenerAdapter mAnimatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            doEnd();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        /**
         * animator cause leak , if not cancel;
         */
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private void doEnd() {
        boolean progressComplete = Float.compare(mCurrentProgress, 100f) == 0;
        if (TAG == FINISH && progressComplete) {
            setVisibility(GONE);
            mCurrentProgress = 0f;
            this.setAlpha(1f);
        }
        TAG = UN_START;
    }

    @Override
    public void reset() {
        mCurrentProgress = 0;
        if (mAnimator != null && mAnimator.isStarted()) {
            mAnimator.cancel();
        }
    }

    @Override
    public void setProgress(int newProgress) {
        setProgress(Float.valueOf(newProgress));
    }
}
