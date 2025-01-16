package cn.jarlen.richcommon.widget.toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;

import cn.jarlen.jwebview.sample.R;


/**
 * 通用ToolBar
 * Created by jarlen on 2018/5/2 0002.
 */

public class CommonToolBar extends RelativeLayout implements View.OnClickListener {
    private OnToolBarButtonClickListener barButtonClickListener;
    private View toolbarLayout;

    public CommonToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private int leftFirstBtnIconId;
    private int leftButtonColorId;
    private int rightButtonColorId;
    private int leftSecondBtnIconId;
    private int rightFirstBtnIconId;
    private int rightSecondBtnIconId;
    private boolean isTitleCenter;

    private void initView(Context context, AttributeSet attrs) {
        toolbarLayout = LayoutInflater.from(context).inflate(R.layout.common_widget_layout_tool_bar, null);
        if (toolbarLayout == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonToolBar);
        //设置Title
        int titleTextId = typedArray.getResourceId(R.styleable.CommonToolBar_titleText, Resources.ID_NULL);
        CharSequence titleText = titleTextId == Resources.ID_NULL
                ? typedArray.getText(R.styleable.CommonToolBar_titleText)
                : getResources().getString(titleTextId);

        isTitleCenter = typedArray.getBoolean(R.styleable.CommonToolBar_titleCenter, false);
        float titleSize = typedArray.getDimensionPixelSize(R.styleable.CommonToolBar_titleSize, getResources().getDimensionPixelSize(R.dimen.common_widget_toolbar_title_text_size));
        ColorStateList titleColorStateList = typedArray.getColorStateList(R.styleable.CommonToolBar_titleColor);
        setTitle(titleText,
                titleSize,
                titleColorStateList,
                isTitleCenter,
                typedArray.getBoolean(R.styleable.CommonToolBar_titleBold, false));
        //设置背景
//            toolbarLayout.setBackground(getBackground());
        /*设置toolbar按钮*/
        float btnTextSize = typedArray.getDimensionPixelSize(R.styleable.CommonToolBar_btnTextSize, getResources().getDimensionPixelSize(R.dimen.common_widget_toolbar_title_button_text_size));
        //配置左侧第一个按钮样式
        ColorStateList leftBtnColor = typedArray.getColorStateList(R.styleable.CommonToolBar_leftBtnColor);
        if (leftBtnColor == null) {
            leftBtnColor = ColorStateList.valueOf(Color.BLACK);
        }
        int leftFirstBtnTextId = typedArray.getResourceId(R.styleable.CommonToolBar_leftFirstText, Resources.ID_NULL);
        CharSequence leftFirstBtnText = leftFirstBtnTextId == Resources.ID_NULL
                ? typedArray.getText(R.styleable.CommonToolBar_leftFirstText)
                : getResources().getString(leftFirstBtnTextId);
        Drawable leftFirstBtnDrawable = typedArray.getDrawable(R.styleable.CommonToolBar_leftFirstIcon);
        boolean leftFirstBtnIsShown = typedArray.getBoolean(R.styleable.CommonToolBar_leftFirstBtnIsShown, true);
        setButton(leftFirstBtnText, leftBtnColor, btnTextSize, leftFirstBtnDrawable, leftFirstBtnIsShown, ToolBarButtonType.LEFT_FIRST_BUTTON);

        //配置左侧第二个按钮样式
        int leftSecondBtnTextId = typedArray.getResourceId(R.styleable.CommonToolBar_leftSecondText, Resources.ID_NULL);
        CharSequence leftSecondBtnText = leftSecondBtnTextId == Resources.ID_NULL
                ? typedArray.getText(R.styleable.CommonToolBar_leftSecondText)
                : getResources().getString(leftSecondBtnTextId);
        Drawable leftSecondBtnDrawable = typedArray.getDrawable(R.styleable.CommonToolBar_leftSecondIcon);
        setButton(leftSecondBtnText, leftBtnColor, btnTextSize, leftSecondBtnDrawable, true, ToolBarButtonType.LEFT_SECOND_BUTTON);

        //配置右侧第一个按钮样式
        ColorStateList rightBtnColor = typedArray.getColorStateList(R.styleable.CommonToolBar_rightBtnColor);
        if (rightBtnColor == null) {
            rightBtnColor = ColorStateList.valueOf(Color.BLACK);
        }

        int rightFirstBtnTextId = typedArray.getResourceId(R.styleable.CommonToolBar_rightFirstText, Resources.ID_NULL);
        CharSequence rightFirstBtnText = rightFirstBtnTextId == Resources.ID_NULL
                ? typedArray.getText(R.styleable.CommonToolBar_rightFirstText)
                : getResources().getString(rightFirstBtnTextId);
        Drawable rightFirstBtnDrawable = typedArray.getDrawable(R.styleable.CommonToolBar_rightFirstIcon);
        setButton(rightFirstBtnText, rightBtnColor, btnTextSize, rightFirstBtnDrawable, true, ToolBarButtonType.RIGHT_FIRST_BUTTON);

        //配置右侧第二个按钮样式
        int rightSecondBtnTextId = typedArray.getResourceId(R.styleable.CommonToolBar_rightSecondText, Resources.ID_NULL);
        CharSequence rightSecondBtnText = rightSecondBtnTextId == Resources.ID_NULL
                ? typedArray.getText(R.styleable.CommonToolBar_rightSecondText)
                : getResources().getString(rightSecondBtnTextId);
        Drawable rightSecondBtnDrawable = typedArray.getDrawable(R.styleable.CommonToolBar_rightSecondIcon);
        setButton(rightSecondBtnText, rightBtnColor, btnTextSize, rightSecondBtnDrawable, true, ToolBarButtonType.RIGHT_SECOND_BUTTON);

        boolean isDividerShown = typedArray.getBoolean(R.styleable.CommonToolBar_dividerIsShown, true);
        setDividerShown(isDividerShown);

        addView(toolbarLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isLayouted) {
            return;
        }
        int defaultPadding = getResources().getDimensionPixelOffset(R.dimen.common_widget_toolbar_title_button_padding);
        int buttonMarginLeft = getResources().getDimensionPixelOffset(R.dimen.common_widget_toolbar_button_left_padding);
        int buttonMarginRight = buttonMarginLeft;
        isLayouted = true;
        View childAt = getChildAt(0);
        ToolbarButton leftFirstBtn = childAt.findViewById(R.id.toolbar_left_first_btn);
        ToolbarButton leftSecondBtn = childAt.findViewById(R.id.toolbar_left_second_btn);
        ToolbarButton rightFirstBtn = childAt.findViewById(R.id.toolbar_right_first_btn);
        ToolbarButton rightSecondBtn = childAt.findViewById(R.id.toolbar_right_second_btn);
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        int leftPadding = 0;
        int rightPadding = 0;
        if (leftFirstBtn.isShown()) {
            leftPadding += leftFirstBtn.getWidth() + buttonMarginLeft;
        }
        if (leftSecondBtn.isShown()) {
            leftPadding += leftSecondBtn.getWidth() + buttonMarginLeft;
        }
        if (rightFirstBtn.isShown()) {
            rightPadding += rightFirstBtn.getWidth() + buttonMarginRight;
        }
        if (rightSecondBtn.isShown()) {
            rightPadding += rightSecondBtn.getWidth() + buttonMarginRight;
        }
        if (isTitleCenter) {
            titleView.setGravity(Gravity.CENTER);
        }
        //左右都为0说明只有一个title居中
        if (rightPadding == 0 && leftPadding == 0) {
            //标题两侧都不存在按钮
            rightPadding = leftPadding = defaultPadding;
        } else {
            //标题左侧存在，右侧可能存在
            if (titleView.getGravity() == Gravity.CENTER) {
                leftPadding = rightPadding = Math.max(leftPadding, rightPadding);
            } else {
                leftPadding += buttonMarginLeft;
            }
        }
        titleView.setPadding(leftPadding, 0, rightPadding, 0);
        titleView.invalidate();
    }

    private boolean isLayouted = false;

    public void setAlpha(int color, float alpha) {
        setBackgroundColor(ColorUtils.setAlphaComponent(color, (int) alpha));
    }

    public void setDividerShown(boolean isDividerShown) {
        View divider = toolbarLayout.findViewById(R.id.toolbar_divider);
        divider.setVisibility(isDividerShown ? VISIBLE : GONE);
    }

    /**
     * 设置标题
     *
     * @param titleTextId   标题资源id
     * @param isTitleCenter 是否居中
     */
    public void setTitle(int titleTextId, boolean isTitleCenter) {
        if (titleTextId <= 0) {
            return;
        }
        setTitle(getResources().getString(titleTextId), isTitleCenter, false);
    }

    /**
     * 设置标题
     *
     * @param titleText     标题
     * @param isTitleCenter 是否居中
     */
    public void setTitle(CharSequence titleText, boolean isTitleCenter) {
        setTitle(titleText, isTitleCenter, false);
    }

    /**
     * 设置标题默认居左
     *
     * @param titleTextId 标题资源id
     */
    public void setTitle(int titleTextId) {
        setTitle(getResources().getString(titleTextId));
    }

    /**
     * 设置标题默认居左
     *
     * @param titleTextId 标题文字id
     */
    public void setTitle(String titleTextId) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        if (TextUtils.isEmpty(titleTextId)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
            titleView.setText(titleTextId);
        }
    }

    public void setTitleSize(float titleSize) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }

    /**
     * 设置标题是否居中(默认居左)
     *
     * @param isTitleCenter 是否居中
     */
    public void setTitleCenter(boolean isTitleCenter) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        if (isTitleCenter) {
            titleView.setGravity(Gravity.CENTER);
        } else {
            titleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
    }

    /**
     * 设置标题参数
     *
     * @param titleText     标题文字
     * @param isTitleCenter 是否居中
     * @param isBold        标题是否加粗
     */
    public void setTitle(CharSequence titleText, boolean isTitleCenter, boolean isBold) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        if (isTitleCenter) {
            titleView.setGravity(Gravity.CENTER);
        } else {
            titleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }

        if (TextUtils.isEmpty(titleText)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
            titleView.setText(titleText);
        }
        titleView.setTypeface(isBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    /**
     * 设置标题参数
     *
     * @param titleText           标题
     * @param titleSize           标题字体大小(px)
     * @param titleColorStateList 标题颜色
     * @param isTitleCenter       是否居中
     * @param isBold              标题是否加粗
     */
    private void setTitle(CharSequence titleText, float titleSize, ColorStateList titleColorStateList, boolean isTitleCenter, boolean isBold) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        titleView.setTextColor(titleColorStateList);
        if (isTitleCenter) {
            titleView.setGravity(Gravity.CENTER);
        } else {
            titleView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        if (TextUtils.isEmpty(titleText)) {
            titleView.setVisibility(GONE);
        } else {
            titleView.setVisibility(VISIBLE);
            titleView.setText(titleText);
        }
        titleView.setTypeface(isBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    /**
     * 设置标题
     *
     * @param titleColor 标题颜色
     */
    public void setTitleColor(@ColorInt int titleColor) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        titleView.setTextColor(titleColor);
    }

    /**
     * 设置标题加粗
     *
     * @param isBold 标题是否加粗
     */
    public void setTitleBold(boolean isBold) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        titleView.getPaint().setFakeBoldText(isBold);
    }

    /**
     * 设置标题透明度
     *
     * @param titleColor 标题颜色
     */
    public void setTitleAlpha(float alpha, @ColorInt int titleColor) {
        TextView titleView = toolbarLayout.findViewById(R.id.toolbar_title);
        if (titleColor > 0) {
            titleView.setTextColor(titleColor);
        }
        titleView.setAlpha(alpha);
    }

    /**
     * 设置按钮可见状态
     *
     * @param buttonType
     * @param visibility
     */
    public void setButtonVisibility(ToolBarButtonType buttonType, int visibility) {
        int buttonId = 0;
        switch (buttonType) {
            case LEFT_FIRST_BUTTON:
                buttonId = R.id.toolbar_left_first_btn;
                leftFirstBtnIconId = 0;
                break;
            case LEFT_SECOND_BUTTON:
                buttonId = R.id.toolbar_left_second_btn;
                leftSecondBtnIconId = 0;
                break;
            case RIGHT_FIRST_BUTTON:
                buttonId = R.id.toolbar_right_first_btn;
                rightFirstBtnIconId = 0;
                break;
            case RIGHT_SECOND_BUTTON:
                buttonId = R.id.toolbar_right_second_btn;
                rightSecondBtnIconId = 0;
                break;
            default:
                break;
        }
        ToolbarButton toolbarButton = toolbarLayout.findViewById(buttonId);
        if (toolbarButton != null) {
            toolbarButton.setVisibility(visibility);
            isLayouted = false;
        }
    }

    /**
     * 设置按钮图标
     *
     * @param buttonType 按钮位置类型
     * @param resId
     */
    public void setButtonIcon(ToolBarButtonType buttonType, int resId) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null && resId > 0) {
            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setButtonIcon(AppCompatResources.getDrawable(getContext(), resId));
            toolbarButton.setClickable(true);
            toolbarButton.setButtonText(null);
        }
    }

    /**
     * 设置按钮图标
     *
     * @param buttonType 按钮位置类型
     */
    public void setButtonIcon(ToolBarButtonType buttonType, Drawable drawable) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null && drawable != null) {
            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setClickable(true);
            toolbarButton.setButtonIcon(drawable);
            toolbarButton.setButtonText(null);
        }
    }

    /**
     * 设置按钮颜色
     *
     * @param buttonType 按钮位置类型
     */
    public void setButtonColor(ToolBarButtonType buttonType, @ColorInt int color) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null && color != 0) {
//            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setColor(color);
        }
    }

    /**
     * 设置按钮透明度
     *
     * @param alpha 透明度
     */
    public void setButtonAlpha(ToolBarButtonType buttonType, float alpha) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null) {
//            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setAlpha(alpha);
        }
    }

    /**
     * 设置按钮图标
     *
     * @param buttonType 按钮位置类型
     * @param bitmap
     */
    public void setButtonIcon(ToolBarButtonType buttonType, Bitmap bitmap) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null && bitmap != null) {
            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setButtonIcon(bitmap);
            toolbarButton.setClickable(true);
            toolbarButton.setButtonText(null);
        }
    }

    /**
     * 设置按钮文字按钮
     *
     * @param buttonType 按钮位置类型
     */
    public void setButtonText(ToolBarButtonType buttonType, String text) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton != null && !TextUtils.isEmpty(text)) {
            toolbarButton.setVisibility(VISIBLE);
            toolbarButton.setButtonText(text);
            toolbarButton.setClickable(true);
            toolbarButton.setButtonIcon((Bitmap) null);
        }
    }

    public void setButtonEnable(ToolBarButtonType buttonType, boolean isEnable) {
        ToolbarButton button = getButtonIdByType(buttonType);
        if (button != null) {
            button.setEnabled(isEnable);
        }
    }

    public void setButton(CharSequence btnText,
                          ColorStateList colorStateList,
                          float btnTextSize,
                          Drawable btnDrawable,
                          boolean isShown,
                          ToolBarButtonType buttonType) {
        ToolbarButton toolbarButton = getButtonIdByType(buttonType);
        if (toolbarButton == null) {
            return;
        }
        toolbarButton.setButtonTextSize(btnTextSize);
        toolbarButton.setColor(colorStateList);
        toolbarButton.setEnabled(true);
        toolbarButton.setClickable(true);
        toolbarButton.setOnClickListener(this);
        toolbarButton.setVisibility(isShown ? VISIBLE : GONE);
        if (btnDrawable != null) {
            toolbarButton.setButtonText(null);
            toolbarButton.setButtonIcon(btnDrawable);
        } else if (!TextUtils.isEmpty(btnText)) {
            toolbarButton.setButtonIcon((Bitmap) null);
            toolbarButton.setButtonText(btnText);
        } else {
            toolbarButton.setVisibility(GONE);
        }
    }

    public ToolbarButton getButtonIdByType(ToolBarButtonType buttonType) {
        int buttonId = 0;
        switch (buttonType) {
            case LEFT_FIRST_BUTTON:
                buttonId = R.id.toolbar_left_first_btn;
                break;
            case LEFT_SECOND_BUTTON:
                buttonId = R.id.toolbar_left_second_btn;
                break;
            case RIGHT_FIRST_BUTTON:
                buttonId = R.id.toolbar_right_first_btn;
                break;
            case RIGHT_SECOND_BUTTON:
                buttonId = R.id.toolbar_right_second_btn;
                break;
            default:
                break;
        }
        ToolbarButton toolbarButton = toolbarLayout.findViewById(buttonId);
//        toolbarButton.setDisplayPriority(ToolbarButton.IMAGEVIEW_HIGH_PRIORITY);
        return toolbarButton;
    }

    /**
     * 设置按钮点击事件
     *
     * @param btn
     */

    private void bindClick2Button(boolean isShown, ToolbarButton btn) {
        if (btn == null) {
            return;
        }
        if (isShown) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
        btn.setClickable(true);
        btn.setOnClickListener(this);
    }

    /**
     * 按钮点击监听
     *
     * @param barButtonClickListener
     */
    public void setBarButtonClickListener(OnToolBarButtonClickListener barButtonClickListener) {
        this.barButtonClickListener = barButtonClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (barButtonClickListener != null) {
            if (id == R.id.toolbar_left_first_btn) {
                barButtonClickListener.onClick(v, ToolBarButtonType.LEFT_FIRST_BUTTON);
            }
            if (id == R.id.toolbar_left_second_btn) {
                barButtonClickListener.onClick(v, ToolBarButtonType.LEFT_SECOND_BUTTON);
            }
            if (id == R.id.toolbar_right_first_btn) {
                barButtonClickListener.onClick(v, ToolBarButtonType.RIGHT_FIRST_BUTTON);
            }
            if (id == R.id.toolbar_right_second_btn) {
                barButtonClickListener.onClick(v, ToolBarButtonType.RIGHT_SECOND_BUTTON);
            }
        } else {
            //如果不设点击事件,默认增加返回点击关闭功能
            if (id == R.id.toolbar_left_first_btn) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        }
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (null != toolbarLayout) {
            toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        super.setBackgroundColor(color);
        if (null != toolbarLayout) {
            toolbarLayout.setBackgroundColor(color);
        }
    }

}

