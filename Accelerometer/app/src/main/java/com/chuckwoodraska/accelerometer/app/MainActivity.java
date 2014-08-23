package com.chuckwoodraska.accelerometer.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements SensorEventListener {
    // DRAW THINGIES
    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public static int xmove;
    public static int ymove;

    // ACCELEROMETER STHUFF
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // SHAKE VARS
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    // VIEW VARS
    TextView title, xvalue, yvalue, zvalue, speedvalue;
    RelativeLayout layout;


    @Override
    public final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); //refer layout file code below
        //get the sensor service
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //get layout
       // layout = (RelativeLayout)findViewById(R.id.relative);
        //get textviews
        title=(TextView)findViewById(R.id.name);
        xvalue =(TextView)findViewById(R.id.xval);
        yvalue =(TextView)findViewById(R.id.yval);
        zvalue =(TextView)findViewById(R.id.zval);
        speedvalue =(TextView)findViewById(R.id.speed);

        mCustomDrawableView = new CustomDrawableView(this);
        setContentView(mCustomDrawableView);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // Do something here if sensor accuracy changes.
    }

    public void onShake(float force) {

        // Do your stuff here

        // Called when Motion Detected
        Toast.makeText(getBaseContext(), "SHAKING IT!!!",
        Toast.LENGTH_SHORT).show();
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
//        title.setText("CoffeeSoGood");

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                  //  speedvalue.setText("Speeds" + "\t\t" + speed);
                    onShake(speed);
                }
//                xvalue.setText("X axis" + "\t\t" + x);
  //              yvalue.setText("Y axis" + "\t\t" + y);
    //            zvalue.setText("Z axis" + "\t\t" + z);

                last_x = x;
                last_y = y;
                last_z = z;

                xmove = (int) Math.pow(x, 3);
                ymove = (int) Math.pow(y, 3);
            }


        }
    }

    public class CustomDrawableView extends View
    {
        // Change later for brush size
        static final int width = 50;
        static final int height = 50;

        public CustomDrawableView(Context context)
        {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds(ymove, xmove, ymove + width, xmove + height);
        }

        protected void onDraw(Canvas canvas)
        {
            RectF oval = new RectF(ymove, xmove, ymove + width, xmove + height); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.RED);
            canvas.drawOval(oval, p);
            invalidate();
        }
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
