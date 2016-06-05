package com.example.app.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.app.R;

import java.util.List;

public class CompassActivity extends Activity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

//        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        for (Sensor s : sensorList) {
//            System.out.println(s.getName());
//        }
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        // 传输数据的速度
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                // 0：北面 1：
                System.out.format("value:%f\n", event.values[0]);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
