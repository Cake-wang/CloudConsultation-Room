package com.aries.template.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.aries.template.R;

import androidx.appcompat.widget.AppCompatTextView;

public class ScaleTextView extends AppCompatTextView {
    private int designedHeight = 768;
    private int baseScreenHeight = 720;


    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray type = context.obtainStyledAttributes(attrs,R.styleable.ScaleTextView);//获得属性值
//        int i = type.getInteger(R.styleable.ScaleTextView_textSizePx, 16);
        Float textSize = getTextSize();
        int i = textSize.intValue();
        // recompute the size
//        Log.d("LOGCAT","i:"+i);
        baseScreenHeight = type.getInteger(R.styleable.ScaleTextView_baseScreenHeight, designedHeight);
//        Log.d("LOGCAT","baseScreenHeight:"+baseScreenHeight);
        // re set
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(i));
        boolean _isBold=type.getBoolean(R.styleable.ScaleTextView_textBold, false);
        getPaint().setFakeBoldText(_isBold);
    }

    /**
     * 获取当前手机屏幕分辨率，然后根据和设计图的比例对照换算实际字体大小
     * @param textSize
     * @return
     */
    private int getFontSize(int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int rate = (int) (textSize * (float) screenHeight / baseScreenHeight);
        return rate;
    }
}
