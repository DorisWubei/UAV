package com.projects.wu.wateruav;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryModeView extends LinearLayout {
    private LinearLayout ll_click_view;
    private TextView tv_mode;
    private float mSelectTextSize;
    private float mUnSelectTextSize;

    public HistoryModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ll_click_view = (LinearLayout) View.inflate(context, R.layout.sub_history_click_view, this);
        tv_mode = (TextView) findViewById(R.id.tv_click_view);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HistoryModeView);
        setText(ta.getString(R.styleable.HistoryModeView_android_text));
        setSelectTextSize(ta.getDimension(R.styleable.HistoryModeView_select_text_size, 16));
        setUnSelectedTextSize(ta.getDimension(R.styleable.HistoryModeView_un_select_text_size, 12));
        setViewTextSize();
    }
    public HistoryModeView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public HistoryModeView(Context context){
        this(context,null);
    }
    //设置控件选中状态
    public void setHistoryViewSelected(boolean isSelected){
        ll_click_view.setSelected(isSelected);
    }
    //设置控件文字
    public void setText(String str){
        tv_mode.setText(str);
    }
    //设置控件文字颜色
    public void setTextColor(int color){
        tv_mode.setTextColor(color);
    }
    //设置根据view的选中状态更新text字号
    public void setViewTextSize(){
        if (ll_click_view.isSelected()){
            tv_mode.getPaint().setTextSize(mSelectTextSize);
        }else{
            tv_mode.getPaint().setTextSize(mUnSelectTextSize);
        }
    }
    //设置view选中的字体大小
    public void setSelectTextSize(float selectSize){
        mSelectTextSize = selectSize;
    }
    //设置view没有选中的字体大小
    public void setUnSelectedTextSize(float unSelectedSize){mUnSelectTextSize = unSelectedSize;}
}
