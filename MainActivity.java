package com.example.myandroidapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor proximity;
    private TextView statusTextView;
    private LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // בניית ממשק המשתמש
        rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(50, 50, 50, 50);
        rootLayout.setGravity(android.view.Gravity.CENTER);
        rootLayout.setBackgroundColor(0xFF121212);

        statusTextView = new TextView(this);
        statusTextView.setText("העבר יד מעל החיישן לשינוי צבעים");
        statusTextView.setTextColor(Color.WHITE);
        statusTextView.setTextSize(24);
        statusTextView.setGravity(android.view.Gravity.CENTER);
        rootLayout.addView(statusTextView);

        setContentView(rootLayout);

        // הגדרת החיישן
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && proximity != null) {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < event.sensor.getMaximumRange()) {
                updateSystemColors(0xFFD32F2F, "המערכת זיהתה תנועה!");
            } else {
                updateSystemColors(0xFF121212, "העבר יד מעל החיישן לשינוי צבעים");
            }
        }
    }

    private void updateSystemColors(int backgroundColor, String text) {
        rootLayout.setBackgroundColor(backgroundColor);
        statusTextView.setText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(backgroundColor);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
