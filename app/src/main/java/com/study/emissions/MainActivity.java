package com.study.emissions;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager; // 传感控制
    private TestSensorListener mSensorListener; // 监听器
    private TextView mSensorTip, mSensorInfo;
    private String TAG = MainActivity.class.getName();
    private long lastTimeStamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mSensorListener = new TestSensorListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    private void initViews() {
        mSensorTip = (TextView) findViewById(R.id.tv_sensor_tip);
        mSensorInfo = (TextView) findViewById(R.id.tv_sensor_info);
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

    private class TestSensorListener implements SensorEventListener {
        final float mAlpha = 0.8f;
        private float[] mGravity = new float[3];
        private float[] mLinearAcceleration = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 读取加速度传感器数值，values数组0,1,2分别对应x,y,z轴的加速度
            if (event.sensor == null) {
                return;
            }

            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    long stamp = System.currentTimeMillis() / 1000l;// 1393844912

                    mGravity[0] = mAlpha * mGravity[0] + (1 - mAlpha) * event.values[0];
                    mGravity[1] = mAlpha * mGravity[1] + (1 - mAlpha) * event.values[1];
                    mGravity[2] = mAlpha * mGravity[2] + (1 - mAlpha) * event.values[2];

                    float[] temp = new float[3];

                    temp[0] = event.values[0] - mGravity[0];
                    temp[1] = event.values[1] - mGravity[1];
                    temp[2] = event.values[2] - mGravity[2];


                    int px = (int) Math.abs(mLinearAcceleration[0] - temp[0]);
                    int py = (int) Math.abs(mLinearAcceleration[1] - temp[1]);
                    int pz = (int) Math.abs(mLinearAcceleration[2] - temp[2]);

                    if ((stamp - lastTimeStamp) > 1) {
                        if (getMaxValue(px, py, pz) > 2) {
                            lastTimeStamp = stamp;
                            mSensorInfo.setText(temp[0] + "\n" + temp[1] + "\n" + temp[2]);
                            mSensorTip.setText("检测手机在移动..");
                        } else {
                            mSensorTip.setText("检测手机没有移动..");
                        }
                    }

                    mLinearAcceleration[0] = temp[0];
                    mLinearAcceleration[1] = temp[1];
                    mLinearAcceleration[2] = temp[2];
                    break;

                case Sensor.TYPE_GRAVITY:
                    mGravity[0] = event.values[0];
                    mGravity[1] = event.values[1];
                    mGravity[2] = event.values[2];
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.i(TAG, "onAccuracyChanged");
        }
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    public int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }

        return max;
    }
}
