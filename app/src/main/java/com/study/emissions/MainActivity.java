package com.study.emissions;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnChartValueSelectedListener, AcceleratedSensorListener.LinearAccelerationListener {

    private SensorManager mSensorManager; // 传感控制
    private AcceleratedSensorListener mSensorListener; // 监听器
    private TextView mSensorTip, mSensorInfo;
    private String TAG = MainActivity.class.getName();
    private LineChart mChart;
    private ChartUtil mChartUtil;
    private ArrayList<Entry>[] mEntries = new ArrayList[]{new ArrayList(), new ArrayList(), new ArrayList()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mSensorListener = new AcceleratedSensorListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void initViews() {
        mSensorTip = findViewById(R.id.tv_sensor_tip);
        mSensorInfo = findViewById(R.id.tv_sensor_info);
        mChart = findViewById(R.id.lineChart);
        mChartUtil = new ChartUtil(this, mChart);
        mChartUtil.initChart(); // 初始化图标
        mChartUtil.initLine("X方向的加速度", Integer.valueOf(Color.rgb(140, 234, 255))); // 初始化线
        mChartUtil.initLine("Y方向的加速度", Color.RED); // 初始化线
        mChartUtil.initLine("Z方向的加速度", Color.YELLOW); // 初始化线
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听器
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册监听器
        mSensorManager.unregisterListener(mSensorListener);
    }


    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        Log.i(TAG, "Value: " + entry.getY() + ", xIndex: " + entry.getX()
                + ", DataSet index: " + highlight.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChangeData(float[] linearAcceleration) {
        for (int i = 0; i < linearAcceleration.length; i++) {
            mChartUtil.setData(mEntries[i], linearAcceleration[i], i);
        }
    }

    @Override
    public void onMoved(boolean isMove) {
        mSensorTip.setText(isMove ? "检测手机正在移动" : "检测手机没有移动");
    }
}
