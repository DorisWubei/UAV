package com.projects.wu.wateruav;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DataChartActivity extends Activity implements HistoryChartView.OnViewLayoutListener {

    private final static int DAY_MODE = 0;
    private final static int WEEK_MODE = 1;
    private final static int MONTH_MODE = 2;
    private final static int YEAR_MODE = 3;

    private HistoryModeView Day;
    private HistoryModeView Week;
    private HistoryModeView Month;
    private HistoryModeView Year;
    private HistoryModeView[] Array;

    private HistoryChartView mHistoryChartView;

    private int mode = DAY_MODE;

    private int checkColor;
    private int unCheckColor;

    private String mStrDayO2Data = "8.71,8.62,8.52,8.74,8.67,8.56,8.54,8.55,8.53,8.36,8.43,8.36,8.51,8.43,8.53,8.51,8.52,8.62,8.64,8.67,8.56,8.53,8.56,8.54";
    private String mStrDayNH3Data = "8.51,8.43,8.59,8.81,8.92,8.92,8.64,8.67,8.06,8.43,8.86,8.51,8.43,8.23,8.81,8.92,8.32,8.64,8.17,8.26,8.43,8.86,8.54,8.93";
    private String mStrDayPHData = "8.2,8.3,7.4,8.6,7.5,8.8,8.7,8.5,8.6,8.5,8.7,7.8,8.7,7.4,8.5,7.6,8.6,8.6,8.6,8.2,8.4,7.6,8.4,8.4";


    private String mStrWeekO2Data = "7.32,7.64,7.67,7.56,7.43,7.86,7.51";
    private String mStrWeekNH3Data = "8.54,8.55,8.53,8.36,8.43,8.36,8.51";
    private String mStrWeekPHData = "8.6,8.6,8.6,8.6,8.2,8.4,7.6,8.4";

    private String mStrMonthO2Data = "8.71,8.62,8.52,8.74,8.67,8.56,8.54,8.55,8.53,8.36,8.43,8.36,8.51,8.43,8.53,8.51,8.52,8.62,8.64,8.67,8.56,8.53,8.56,8.54,8.92,8.32,8.64,8.67,8.56,8.43";
    private String mStrMonthNH3Data = "8.59,8.81,8.51,8.43,8.59,8.81,8.92,8.92,8.64,8.67,8.06,8.43,8.86,8.51,8.43,8.23,8.81,8.92,8.32,8.64,8.17,8.26,8.43,8.86,8.54,8.93,8.43,8.86,9.54,8.93";
    private String mStrMonthPHData = "8.9,7.5,8.9,8.4,8.9,8.6,7.5,8.8,8.7,8.5,8.6,8.5,8.7,7.8,8.7,8.6,7.5,8.8,8.7,7.5,7.7,7.8,7.7,7.4,7.5,7.6,8.6,8.6,8.6,8.6";

    private String mStrYearO2Data = "8.54,8.55,8.53,8.36,8.43,8.36,8.51,8.65,8.23,7.81,8.92,8.32";
    private String mStrYearNH3Data = "8.59,8.81,8.92,8.92,8.64,8.67,8.56,8.43,8.86,8.92,8.32,8.64";
    private String mStrYearPHData = "7.6,8.1,8.9,8.4,8.9,7.6,8.8,7.5,8.9,8.6,8.6,8.6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datachart);
        initData();
        initView();
    }
    public void initView(){
        mHistoryChartView = (HistoryChartView) findViewById(R.id.mutiHistoryChartView);
        Day = (HistoryModeView)findViewById(R.id.day);
        Week = (HistoryModeView) findViewById(R.id.week);
        Month = (HistoryModeView) findViewById(R.id.month);
        Year = (HistoryModeView) findViewById(R.id.year);
        Array = new HistoryModeView[]{Day,Week,Month,Year};
        Day.setOnClickListener(onClickListener);
        Week.setOnClickListener(onClickListener);
        Month.setOnClickListener(onClickListener);
        Year.setOnClickListener(onClickListener);
        mHistoryChartView.setOnViewLayoutListener(this);
    }
    public void initData(){
        checkColor = this.getResources().getColor(R.color.blue);
        unCheckColor = this.getResources().getColor(R.color.grey_light);
    }
    //点击事件实例
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.day:
                    mode = DAY_MODE;
                    break;

                case R.id.week:
                    mode = WEEK_MODE;
                    break;

                case R.id.month:
                    mode = MONTH_MODE;
                    break;
                case R.id.year:
                    mode = YEAR_MODE;
                    break;


                default:
                    break;
            }
            updateView();
        }
    };
    //更新界面
    protected void updateView(){
        for (int i=0;i<Array.length;i++){
            if (i==mode){
                Array[i].setHistoryViewSelected(true);
                Array[i].setTextColor(checkColor);
            }else{
                Array[i].setHistoryViewSelected(false);
                Array[i].setTextColor(unCheckColor);
            }
            mHistoryChartView.setData(getAllHistoryViewData(),mode);
        }

    }
    //获取要绘制的历史数据全状态
    private String getAllHistoryViewData(){
        String allHistoryData = "";
        switch (mode){
            case DAY_MODE:
                allHistoryData = mStrDayO2Data + "-" + mStrDayNH3Data + "-"
                        + mStrDayPHData;
                break;
            case WEEK_MODE:
                allHistoryData = mStrWeekO2Data + "-" + mStrWeekNH3Data + "-"
                        + mStrWeekPHData;
                break;
            case MONTH_MODE:
                allHistoryData = mStrMonthO2Data + "-" + mStrMonthNH3Data
                        + "-" + mStrMonthPHData;
                break;
            case YEAR_MODE:
                allHistoryData = mStrYearO2Data + "-" + mStrYearNH3Data + "-"
                        + mStrYearPHData;
                break;
            default:
                break;
        }
        return allHistoryData;
    }
    @Override
    public void onLayoutSuccess(){
        updateView();
    }
}
