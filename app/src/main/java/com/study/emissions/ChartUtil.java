package com.study.emissions;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * 功能：图标设置工具
 * <p>
 * Created by danke on 2018/5/18.
 */

public class ChartUtil {

    protected static String[] mTimes = new String[] {
            "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s",
    };
    private LineChart mChart;
    private Context mContext;
    private LineData mLineData;

    public ChartUtil(Context context, LineChart chart) {
        this.mChart = chart;
        this.mContext = context;
    }

    /**
     * 初始化图表数据
     */
    public void initChart() {
        mChart.setOnChartValueSelectedListener(null);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        //显示边界
        mChart.setDrawBorders(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        CustomMarkerView mv = new CustomMarkerView(mContext, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        // 得到X轴
        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        // 设置X轴坐标之间的最小间隔（因为此图有缩放功能，X轴,Y轴可设置可缩放）
        xl.setGranularity(1f);
        xl.setLabelRotationAngle(45);
        xl.setValueFormatter(new IAxisValueFormatter() { // 设置X轴值为字符串
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int)value + "s";
            }
        });
        // 设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xl.setAxisMinimum(0f);
        xl.setAxisMaximum(30);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置（默认在上方
//        xl.setLabelCount(10, true); // 设置X轴的刻度数量

        // 得到Y轴 -- 左侧
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setInverted(false); // 设置y轴的Label以升序或者降序排列ASC 或者DESC
        leftAxis.setAxisMinimum(-20f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(20f);
//        leftAxis.setLabelCount(11,false);

        // 得到Y轴 -- 右侧
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false); // 右侧Y轴不显示
        // 设置x轴的LimitLine，index是从0开始的

        LimitLine xLimitLine = new LimitLine(0f,"水平线");
        xLimitLine.setLineColor(Color.RED);
        xLimitLine.setTextColor(Color.RED);
        leftAxis.addLimitLine(xLimitLine);

        // 曲线图下面的文字
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        // 设置描述内容
        Description description = new Description();
        description.setText("时间 / s");
        description.setTextColor(Color.RED);
        mChart.setDescription(description);


        // create a data object with the datasets
        mLineData = new LineData();
        // set data
        mChart.setData(mLineData);
    }

    public void initLine(String title, int color) {
        // 一个LineDataSet就是一条线
        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry(0, 0));
        LineDataSet dataSet = new LineDataSet(yVals, title);
        // 设置曲线值的圆点是实心还是空心
        dataSet.setDrawCircleHole(false);
        //  设置显示值的字体大小
        dataSet.setValueTextSize(9f);
        // 线模式为圆滑曲线（默认折线）
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(1.5f);
        dataSet.setCircleRadius(2f);

        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));

        mLineData.addDataSet(dataSet);
    }


    /**
     * 设置数据
     */
    public void setData(ArrayList<Entry> mEntries, float yVal, int index) {
        Entry entry = new Entry(mEntries.size(), yVal);
        mEntries.add(entry);

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            LineDataSet dataSet = (LineDataSet) mChart.getData().getDataSetByIndex(index);
            dataSet.setValues(mEntries);
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("title", dataSet.getLabel());
            bundle.putInt("color", dataSet.getColor());
            entry.setData(bundle);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            // dont forget to refresh the drawing
            mChart.invalidate();
        }
    }
}
