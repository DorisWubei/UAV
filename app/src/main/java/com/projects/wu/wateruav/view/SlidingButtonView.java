package com.projects.wu.wateruav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.projects.wu.wateruav.R;

public class SlidingButtonView extends HorizontalScrollView {

    private static final String TAG = "SlidingButtonView";
    private TextView lTextView_Delete;//删除按钮
    private int lScrollWith;//横向滚动范围
    private boolean first = false; //标记第一次进入获取删除按钮控件

    public SlidingButtonView(Context context) {
        this(context, null);
    }

    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一次进入时获取删除控件
        if (!first) {
            lTextView_Delete = findViewById(R.id.tv_delete);
            first = true;//修改标记
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //默认隐藏删除按钮
        if (changed) {
            this.scrollTo(0, 0);
            //获取水平滚动条可以滚动的范围，也就是右侧删除按钮的宽度
            lScrollWith = lTextView_Delete.getWidth();
        }
    }

    //手势判断
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "onTouchEvent: ");
                changeScrollx();
                return true;
        }
        return super.onTouchEvent(ev);
    }

    //根据滑动距离判断是否显示删除按钮
    private void changeScrollx() {
        //触摸滑动的距离大于删除按钮的一半时
        if (getScrollX() >= (lScrollWith / 2)) {
            //显示删除按钮
            this.smoothScrollTo(380, 0);
        } else {
            //隐藏删除按钮
            this.smoothScrollTo(0, 0);
        }
    }
}
