package com.projects.wu.wateruav;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class HistoryChartView extends View {

    private String TAG = "HistoryChartView";

    TypedArray ta;

    private float MarginTop = 100;

    private float MarginBottom = 100;

    private float MarginLeft = 100;

    private float MarginRight = 100;

    private float mYLabelSize = 50;

    private float mXlabelSize = 35;

    private float mXUnitTextSize;

    private float mYUnitTextSize;
    // 圆半径
    private int circleRadius = 8;

    private int lineStrokeWidth = 3;

    private int dataStrokeWidth = 3;

    private long duration = 1500;
    private PathMeasure mO2PathMeasure;
    private PathMeasure mNH3PathMeasure;
    private Path mO2Path;
    private Path mNH3Path;

    /**
     * 柱形绘制进度
     */
    private float mRectFration;

    // X,Y轴的单位长度
    private float Xscale = 20;
    private float Yscale = 20;

    // 绘制X轴总长度
    private float xLength;
    // 绘制Y轴总长度
    private float yLength;

    // X轴第1个节点的偏移位置
    private float xFirstPointOffset;

    // y轴显示的节点间隔距离
    private int yScaleForData = 1;

    // x轴显示的节点间隔距离
    private int xScaleForData = 1;
    // 画线颜色
    private int lineColor;

    private int O2LineColor;
    private int NH3LineColor;
    private int PHLineColor;
    private int mUnitColor;

    private String mXUnitText;
    private String mY1UnitText;
    private String mY2UnitText;

    private int mMode = 1;// 从Activity传过来的模式值 1：天 2：周 3：月 4：年

    // 原点坐标
    private float Xpoint;
    private float Ypoint;
    // X,Y轴上面的显示文字
    private String[] Xlabel = { "1", "2", "3", "4", "5", "6", "7" };
    private String[] Ylabel = { "0", "2.5", "5", "7.5", "10" };
    private String[] Ylabel2 = { "0", "25", "50", "75", "100" };

    private final static int X_SCALE_FOR_DATA_DAY = 2;
    private final static int X_SCALE_FOR_DATA_WEEK = 1;
    private final static int X_SCALE_FOR_DATA_YEAR = 1;
    private final static int X_SCALE_FOR_DATA_MOUNTH = 5;

    private final static int DAY_MODE = 0;
    private final static int WEEK_MODE = 1;
    private final static int MONTH_MODE = 2;
    private final static int YEAR_MODE = 3;
    // 曲线数据
    private float[] O2DataArray = { 15, 15, 15, 15, 15, 15, 15 };
    private float[] NH3DataArray = { 16, 16, 16, 16, 16, 16, 16 };
    private float[] PHDataArray = { 100, 100, 100, 100, 100, 100, 100 };

    /**
     * 各条柱形图当前top值数组
     */
    private Float[] rectCurrentTops;

    private ValueAnimator mValueAnimator;

    private Paint linePaint;
    private Paint NH3Paint;
    private Paint O2Paint;
    private PathEffect mO2Effect;
    private PathEffect mNH3Effect;
    //定义一个内存中的图片，该图片将作为缓冲区
    Bitmap mCacheBitmap = null;
    //定义cacheBitmap上的Canvas对象
    Canvas mCacheCanvas = null;

    public HistoryChartView(Context context, String[] xlabel, String[] ylabel,
                            float[] O2Array) {
        super(context);
        this.Xlabel = xlabel;
        this.Ylabel = ylabel;
        this.O2DataArray = O2Array;
    }
    public HistoryChartView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ta = context
                .obtainStyledAttributes(attrs, R.styleable.HistoryChartView);

        setDefaultAttrrbutesValue();

        initPaint();

        initData();

        initParams();

        initPath();

        ta.recycle();
    }

    public HistoryChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryChartView(Context context) {
        this(context, null);
    }

    /**
     * 设置显示数据
     *
     * @param strAlldata
     */
    public void setData(String strAlldata, int mode) {
        // LogUtils.verbose(TAG, "strAlldata = " + strAlldata);
        String[] allHistroyArray = strAlldata.split("-");

        String[] arrayO2Data = allHistroyArray[0].split(",");
        String[] arrayNH3Data = allHistroyArray[1].split(",");
        String[] arrayPHData = allHistroyArray[2].split(",");

        mMode = mode;

        initXData(arrayO2Data);

        initO2Data(arrayO2Data);

        initNH3Data(arrayNH3Data);

        initPHData(arrayPHData);

        initData();

        initParams();

        initPath();

        startAnimation();
    }
    private void initPaint(){
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        linePaint.setColor(lineColor);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(lineStrokeWidth);
        O2Paint = new Paint();
        O2Paint.setStyle(Paint.Style.STROKE);
        O2Paint.setAntiAlias(true);
        O2Paint.setColor(O2LineColor);
        O2Paint.setDither(true);
        O2Paint.setStrokeWidth(dataStrokeWidth);

        NH3Paint = new Paint();
        NH3Paint.setStyle(Paint.Style.STROKE);
        NH3Paint.setAntiAlias(true);
        NH3Paint.setColor(NH3LineColor);
        NH3Paint.setDither(true);
        NH3Paint.setStrokeWidth(dataStrokeWidth);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        mO2Path = new Path();
        mNH3Path = new Path();

        rectCurrentTops = new Float[O2DataArray.length];
    }

    /**
     * 初始化宽高比例等数据
     */
    public void initParams() {
        // LogUtils.error(TAG, "initParams");
        Xpoint = MarginLeft;

        xLength = this.getWidth() - MarginLeft - MarginRight
                - (MarginRight + MarginLeft) / 16;
        yLength = this.getHeight() - MarginTop - MarginBottom;

        Ypoint = this.getHeight() - MarginBottom + mYLabelSize / 3;
        Xscale = (xLength - xFirstPointOffset * 2) / (this.Xlabel.length - 1);
        Yscale = yLength / (this.Ylabel.length - 1);

    }

    /**
     * 初始化path
     */
    private void initPath() {
        initO2Path(O2DataArray);
        initNH3Path(NH3DataArray);
    }

    /**
     * 初始化溶氧数据
     *
     *
     */
    private void initO2Data(String[] arrayO2Data) {
        O2DataArray = new float[arrayO2Data.length];
        for (int i = 0; i < arrayO2Data.length; i++) {
            if (arrayO2Data[i].length() > 0) {
                O2DataArray[i] = Float.parseFloat(arrayO2Data[i]);
                // LogUtils.error(TAG, "" + O2DataArray[i]);
            }
        }
    }

    /**
     * 初始化设定溶氨氮数据
     *
     *
     */
    private void initNH3Data(String[] arrayNH3Data) {
        NH3DataArray = new float[arrayNH3Data.length];
        for (int i = 0; i < arrayNH3Data.length; i++) {
            if (arrayNH3Data[i].length() > 0) {
                NH3DataArray[i] = Float.parseFloat(arrayNH3Data[i]);
            }
        }
    }


    /**
     * 初始化ph数据
     *
     *
     */
    private void initPHData(String[] arrayPHData) {
        PHDataArray = new float[arrayPHData.length];
        for (int i = 0; i < arrayPHData.length; i++) {
            if (arrayPHData[i].length() > 0) {
                PHDataArray[i] = Float
                        .parseFloat(arrayPHData[i]);
            }
        }
    }

    /**
     * 初始化X轴数据
     */
    private void initXData(String[] tempData) {
        switch (mMode) {
            case DAY_MODE:
                xScaleForData = X_SCALE_FOR_DATA_DAY;
                setXUnitText(getResources().getString(R.string.history_x_unit_hour));
                break;
            case WEEK_MODE:
                xScaleForData = X_SCALE_FOR_DATA_WEEK;
                setXUnitText(getResources().getString(R.string.history_x_unit_day));
                break;
            case MONTH_MODE:
                xScaleForData = X_SCALE_FOR_DATA_MOUNTH;
                setXUnitText(getResources().getString(R.string.history_x_unit_day));
                break;
            case YEAR_MODE:
                xScaleForData = X_SCALE_FOR_DATA_YEAR;
                setXUnitText(getResources()
                        .getString(R.string.history_x_unit_month));
                break;

            default:
                break;
        }

        Xlabel = new String[tempData.length];
        for (int i = 0; i < Xlabel.length; i++) {
            Xlabel[i] = Integer.toString(i + 1);
        }
    }

    private void setDefaultAttrrbutesValue() {
        LogUtils.error(TAG, "setDefaultAttrrbutesValue");
        float MarginTopPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_top, 50);
        float MarginBottomPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_bottom, 50);
        float MarginLeftPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_left, 50);
        float MarginRightPx = ta.getDimension(
                R.styleable.HistoryChartView_margin_right, 50);

        float yLabelSizePx = ta.getDimension(
                R.styleable.HistoryChartView_ylabel_text_size, 30);
        float xlabelSizePx = ta.getDimension(
                R.styleable.HistoryChartView_xlabel_text_size, 20);
        float xUnitSizePx = ta.getDimension(
                R.styleable.HistoryChartView_x_unit_text_size, 30);
        float yUnitSizePx = ta.getDimension(
                R.styleable.HistoryChartView_y_unit_text_size, 30);

        float xFirstPointOffsetPx = ta.getDimension(
                R.styleable.HistoryChartView_x_first_point_offset, 30);
        float lineStrokeWidthPx = ta.getDimension(
                R.styleable.HistoryChartView_line_stroke_width, 5);
        float dataStrokeWidthPx = ta.getDimension(
                R.styleable.HistoryChartView_data_stroke_width, 5);
        float circleRadiusPx = ta.getDimension(
                R.styleable.HistoryChartView_circle_radius, 6);

        xFirstPointOffset = GetDpSpUtils.px2sp(getContext(),
                xFirstPointOffsetPx);

        MarginTop = GetDpSpUtils.px2dip(getContext(), MarginTopPx);
        MarginBottom = GetDpSpUtils.px2dip(getContext(), MarginBottomPx);
        MarginLeft = GetDpSpUtils.px2dip(getContext(), MarginLeftPx);
        MarginRight = GetDpSpUtils.px2dip(getContext(), MarginRightPx);

        mYLabelSize = GetDpSpUtils.px2sp(getContext(), yLabelSizePx);
        mXlabelSize = GetDpSpUtils.px2sp(getContext(), xlabelSizePx);

        mXUnitTextSize = GetDpSpUtils.px2sp(getContext(), xUnitSizePx);
        mYUnitTextSize = GetDpSpUtils.px2sp(getContext(), yUnitSizePx);

        lineStrokeWidth = GetDpSpUtils.px2sp(getContext(), lineStrokeWidthPx);
        dataStrokeWidth = GetDpSpUtils.px2sp(getContext(), dataStrokeWidthPx);
        circleRadius = GetDpSpUtils.px2sp(getContext(), circleRadiusPx);

        lineColor = ta.getColor(R.styleable.HistoryChartView_line_color,
                getResources().getColor(R.color.blue));
        O2LineColor = ta.getColor(
                R.styleable.HistoryChartView_first_data_line_color,
                getResources().getColor(R.color.o2));
        NH3LineColor = ta.getColor(
                R.styleable.HistoryChartView_second_data_line_color,
                getResources().getColor(R.color.nh3));

        PHLineColor = ta.getColor(
                R.styleable.HistoryChartView_rect_background_color,
                getResources().getColor(R.color.ph));

        mUnitColor = ta.getColor(R.styleable.HistoryChartView_unit_color,
                getResources().getColor(R.color.grey_light));

        mXUnitText = ta.getString(R.styleable.HistoryChartView_x_unit_text);
        mY1UnitText = ta.getString(R.styleable.HistoryChartView_y1_unit_text);
        mY2UnitText = ta.getString(R.styleable.HistoryChartView_y2_unit_text);
    }

    /**
     * 设置X轴单位符号
     *
     * @param xUnit
     */
    public void setXUnitText(String xUnit) {
        mXUnitText = xUnit;
    }

    /**
     * 绘制单位符号
     *
     * @param canvas
     */
    private void drawUnit(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(dataStrokeWidth);
        p.setColor(mUnitColor);

        drawXUnit(canvas, p);
        drawY1Unit(canvas, p);
        drawY2Unit(canvas, p);
    }

    // 画横轴
    private void drawXLine(Canvas canvas, Paint p) {
        p.setColor(getResources().getColor(R.color.blue));
        canvas.drawLine(Xpoint, Ypoint, xLength + MarginLeft, Ypoint, p);
    }

    // 画灰色横轴
    private void drawGreyXLine(Canvas canvas, Paint p) {
        p.setColor(getResources().getColor(R.color.grey_line));
        float startX = Xpoint + MarginLeft / 4;
        // 纵向
        for (int i = yScaleForData; (yLength - i * Yscale) >= 0; i += yScaleForData) {
            float startY = Ypoint - i * Yscale;
            canvas.drawLine(startX - MarginLeft / 4, startY, xLength
                    + MarginLeft, startY, p);
        }
    }

    // 画数据
    private void drawData(Canvas canvas, float[] data, int dataColor) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStrokeWidth(dataStrokeWidth);
        p.setTextSize(mXlabelSize);
        // 横向
        for (int i = 0; i < Xlabel.length; i++) {
            int xLableInt = Integer.parseInt(Xlabel[i]);
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            if (xLableInt % xScaleForData == 0) {
                p.setColor(lineColor);
                canvas.drawText(this.Xlabel[i], startX - mXlabelSize / 3,
                        Ypoint + mXlabelSize * 3 / 2, p);
            }
            p.setColor(dataColor);
            canvas.drawCircle(startX, getDataY(data[i], Ylabel), circleRadius,
                    p);
        }

        p.setTextSize(mYLabelSize);
        // 纵向
        for (int i = 0; (yLength - i * Yscale) >= 0; i += yScaleForData) {
            p.setColor(lineColor);
            canvas.drawText(this.Ylabel[i], MarginLeft / 4,
                    getDataY(Float.valueOf(Ylabel[i]), Ylabel) + mYLabelSize
                            / 3, p);
            canvas.drawText(this.Ylabel2[i], this.getWidth() - MarginLeft,
                    getDataY(Float.valueOf(Ylabel2[i]), Ylabel2) + mYLabelSize
                            / 3, p);
        }
    }

    // 获取O2绘线Path数据
    private void initO2Path(float[] data) {
        mO2Path.reset();
        // Path path = new Path();
        float pointX;
        float pointY;
        // 横向
        mO2Path.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        mO2Path.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mO2Path.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mO2Path.lineTo(pointX, pointY);
            }
        }
        mO2PathMeasure = new PathMeasure(mO2Path, false);
    }

    /**
     * 获取NH3绘线Path数据
     *
     * @param data
     */
    private void initNH3Path(float[] data) {
        mNH3Path.reset();
        float pointX;
        float pointY;
        // 横向
        mNH3Path.moveTo(Xpoint + xFirstPointOffset,
                getDataY(data[0], Ylabel));
        for (int i = 0; i < Xlabel.length; i++) {
            float startX = Xpoint + i * Xscale + xFirstPointOffset;
            // 绘制数据连线
            if (i != 0) {
                pointX = Xpoint + (i - 1) * Xscale + xFirstPointOffset;
                pointY = getDataY(data[i - 1], Ylabel);
                mNH3Path.lineTo(pointX, pointY);
            }
            if (i == Xlabel.length - 1) {
                pointX = startX;
                pointY = getDataY(data[i], Ylabel);
                mNH3Path.lineTo(pointX, pointY);
            }
        }
        mNH3PathMeasure = new PathMeasure(mNH3Path, false);
    }

    // 绘制矩形图
    private void drawRect(Canvas canvas, float[] data, int dataColor) {
        Paint p = new Paint();
        float left;
        float top;
        float right;
        float bottom;
        float stopY = getDataY(Float.parseFloat(Ylabel[Ylabel.length - 1]),
                Ylabel);// 灰色线Y轴位置
        float rectYScale = (Ypoint - stopY)/10;

        p.setAntiAlias(true);
        p.setStrokeWidth(dataStrokeWidth);
        p.setColor(dataColor);

        // 横向
        for (int i = 0; i < Xlabel.length; i++) {
            // 绘制柱形图
            if (i != 0) {
                left = Xpoint + (i - 1) * Xscale + xFirstPointOffset + Xscale
                        / 6;
                top = Ypoint - data[i - 1] * rectYScale + lineStrokeWidth;// 要绘制的rect最终top值
                // 起点top + (起点top - 终点top) * mRectFration
                rectCurrentTops[i] = Ypoint - (Ypoint - top) * mRectFration;// 根据fraction动态更新top值
                right = left + Xscale * 4 / 6;
                bottom = Ypoint;
                canvas.drawRect(left, rectCurrentTops[i], right, bottom, p);// 每次valueAnimator更新时重绘最新top值
            }
        }
    }

    private void drawY1Unit(Canvas canvas, Paint p) {
        int maxYLabelValue = Integer.valueOf(Ylabel[Ylabel.length - 1]);
        p.setTextSize(mYUnitTextSize);
        float textWidth = p.measureText(mY1UnitText);
        canvas.drawText(mY1UnitText, MarginLeft / 2 - textWidth / 2,
                getDataY(maxYLabelValue, Ylabel) - mYLabelSize - mYLabelSize
                        / 5, p);
    }

    private void drawY2Unit(Canvas canvas, Paint p) {
        int maxYLabel2Value = Integer.valueOf(Ylabel2[Ylabel2.length - 1]);
        p.setTextSize(mYUnitTextSize);
        float textWidth = p.measureText(mY2UnitText);
        canvas.drawText(mY2UnitText, this.getWidth() - MarginRight / 2
                - textWidth * 3 / 4, getDataY(maxYLabel2Value, Ylabel2)
                - mYLabelSize - mYLabelSize / 5, p);
    }

    private void drawXUnit(Canvas canvas, Paint p) {
        p.setTextSize(mXUnitTextSize);
        float textWidth = p.measureText(mXUnitText);
        canvas.drawText(mXUnitText, this.getWidth() / 2 - textWidth / 2, Ypoint
                + mXlabelSize * 3 + mXlabelSize / 5, p);
    }

    /**
     * 获取data对应绘制Y点值
     */
    private float getDataY(float dataY, String[] Ylabel) {
        float y0 = 0;
        float y1 = 0;
        try {
            y0 = Float.parseFloat(Ylabel[0]);
            y1 = Float.parseFloat(Ylabel[1]);
        } catch (Exception e) {
            return 0;
        }
        try {
            return Ypoint - ((dataY - y0) * Yscale / (y1 - y0));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // LogUtils.error(TAG, "onLayout");
        initParams();

        if (onViewLayoutListener != null) {
            onViewLayoutListener.onLayoutSuccess();
        }

        //创建一个与该View相同大小的缓冲区
        mCacheBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        mCacheCanvas = new Canvas();
        //设置cacheCanvas将会绘制到内存中cacheBitmap上
        mCacheCanvas.setBitmap(mCacheBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCacheCanvas.drawColor(Color.BLACK);

        drawGreyXLine(mCacheCanvas, linePaint);

        drawUnit(mCacheCanvas);

        if (PHDataArray.length > 1) {
            drawRect(mCacheCanvas, PHDataArray, PHLineColor);
        }

        mCacheCanvas.drawPath(mO2Path, O2Paint);

        if (O2DataArray.length > 1) {
            drawData(mCacheCanvas, O2DataArray, O2LineColor);
        }

        mCacheCanvas.drawPath(mNH3Path, NH3Paint);

        if (NH3DataArray.length > 1) {
            drawData(mCacheCanvas, NH3DataArray, NH3LineColor);
        }

        drawXLine(mCacheCanvas, linePaint);

        Paint bmpPaint = new Paint();
        //将cacheBitmap绘制到该View组件
        canvas.drawBitmap(mCacheBitmap, 0, 0, bmpPaint);
    }
    private void startAnimation(){
        if (mValueAnimator != null){
            mValueAnimator.cancel();
        }
        final float NH3Length = mNH3PathMeasure.getLength();
        final float O2Length = mO2PathMeasure.getLength();
        mValueAnimator = ValueAnimator.ofFloat(1,0);
        mValueAnimator.setDuration(duration);
        //减速插值器

        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (Float) animation.getAnimatedValue();
                // 更新mNH3Effect
                mNH3Effect = new DashPathEffect(new float[] {
                        NH3Length, NH3Length }, fraction
                        * NH3Length);
                NH3Paint.setPathEffect(mNH3Effect);
                // 更新mO2Effect
                mO2Effect = new DashPathEffect(new float[] {
                        O2Length, O2Length }, fraction
                        * O2Length);
                O2Paint.setPathEffect(mO2Effect);
                // 更新rect绘制fraction进度
                mRectFration = 1 - fraction;// fraction是1->0 我们需要的柱形图绘制比例是0->1
                //postInvalidate();
                invalidate();

            }
        });
        mValueAnimator.start();
    }

    public interface OnViewLayoutListener {
        public void onLayoutSuccess();
    }

    public void setOnViewLayoutListener(
            OnViewLayoutListener onViewLayoutListener) {
        this.onViewLayoutListener = onViewLayoutListener;
    }

    private OnViewLayoutListener onViewLayoutListener;

}