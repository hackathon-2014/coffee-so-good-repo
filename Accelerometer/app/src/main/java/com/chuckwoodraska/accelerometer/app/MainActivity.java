package com.chuckwoodraska.accelerometer.app;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    TextView title, xvalue, yvalue, zvalue;
    RelativeLayout layout;


    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //refer layout file code below
        //get the sensor service
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //get layout
        layout = (RelativeLayout)findViewById(R.id.relative);
        //get textviews
        title=(TextView)findViewById(R.id.name);
        xvalue =(TextView)findViewById(R.id.xval);
        yvalue =(TextView)findViewById(R.id.yval);
        zvalue =(TextView)findViewById(R.id.zval);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.
    }

    public void onShake(float force) {

        // Do your stuff here

        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "Motion detected",
        Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        // Many sensors return 3 values, one for each axis.
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        //display values using TextView
        title.setText(R.string.app_name);
        xvalue.setText("X axis" + "\t\t" + x);
        yvalue.setText("Y axis" + "\t\t" + y);
        zvalue.setText("Z axis" + "\t\t" + z);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
