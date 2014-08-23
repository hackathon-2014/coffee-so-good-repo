package com.chuckwoodraska.accelerometer.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends Activity implements SensorEventListener {
    // DRAW THINGIES
    CustomDrawableView mCustomDrawableView = null;
    ShapeDrawable mDrawable = new ShapeDrawable();
    public int color = Color.RED;
    public int brush_size = 50;
    public static int xmove = 0;
    public static int ymove = 0;
    public ArrayList<RectF> paths = new ArrayList<RectF>();
    public ArrayList<Integer> paths_color = new ArrayList<Integer>();
    public ArrayList<Integer> paths_brushsize = new ArrayList<Integer>();
    public String[] colors_array = { "RED", "BLUE", "GREEN", "YELLOW", "BLACK", "WHITE"};
    public String[] brushsize_array = { "10","50","75"};

    // ACCELEROMETER STHUFF
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // SHAKE VARS
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 1000;

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
        paths = new ArrayList<RectF>();
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

                // Add oval to canvas
                Boolean tilt = false;
                if(((int)x) > 2 && ((int)y) > 2) {
                    xmove += 5;
                    ymove += 5;
                    tilt = true;
                }
                else if(((int)x) > 2 && ((int)y) < -2) {
                    xmove += 5;
                    ymove -= 5;
                    tilt = true;
                }
                else if(((int)x) > 2) {
                    xmove += 10;
                    tilt = true;
                }
                else if(((int)x) < -2 && ((int)y) > 2) {
                    xmove -= 5;
                    ymove += 5;
                    tilt = true;
                }
                else if(((int)x) < -2 && ((int)y) < -2) {
                    xmove -= 5;
                    ymove -= 5;
                    tilt = true;
                }
                else if(((int)x) < -2) {
                    xmove -= 10;
                    tilt = true;
                }
                else if (((int)y) > 2) {
                    ymove += 10;
                    tilt = true;
                }
                else if (((int)y) < -2) {
                    ymove -= 10;
                    tilt = true;
                }
                if(tilt) {
                    RectF oval = new RectF(ymove, xmove, ymove + brush_size, xmove + brush_size); // set bounds of rectangle
                    paths.add(oval);
                    paths_color.add(color);
                    paths_brushsize.add(brush_size);
                }

            }
        }
    }

    public class CustomDrawableView extends View
    {
        // Change later for brush size
        int width = brush_size;
        int height = brush_size;

        public CustomDrawableView(Context context)
        {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(color);
            mDrawable.setBounds(ymove, xmove, ymove + width, xmove + height);
        }

        protected void onDraw(Canvas canvas)
        {
            Paint p = new Paint(); // set some paint options
            for(int i =0; i < paths.size(); i++){
                p.setStrokeWidth(brush_size);
                p.setColor(paths_color.get(i)); // change later for color options
                canvas.drawOval(paths.get(i), p);
            }
            invalidate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_brushsize:
                final Dialog brush_size_dialog = new Dialog(this);
                brush_size_dialog.setTitle("Select level:");
                brush_size_dialog.setContentView(R.layout.brush_size_setting);

                final TextView brushsize_title = (TextView)brush_size_dialog.findViewById(R.id.brush_size_title);
                final SeekBar brushsize = (SeekBar)brush_size_dialog.findViewById(R.id.brush_size_slider);

                brushsize.setMax(100);
                brushsize.setProgress(100);

                brushsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    //change to progress
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        brushsize_title.setText(Integer.toString(progress));
                    }
                    //methods to implement but not necessary to amend
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

                Button okBtn = (Button)brush_size_dialog.findViewById(R.id.brush_size_ok);

                okBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    //respond to level
                        brush_size= brushsize.getProgress();
                        brush_size_dialog.dismiss();
                    }
                });

                brush_size_dialog.show();




//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Pick Brush Size:")
//                        .setItems(brushsize_array, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                brush_size = Integer.parseInt(brushsize_array[which]);
//                                // The 'which' argument contains the index position
//                                // of the selected item
//                            }
//                        });
//                builder.create();
//                builder.show();
                return true;
            case R.id.change_color:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Pick color:")
                        .setItems(colors_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                color = Color.parseColor(colors_array[which]);
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        });
                builder2.create();
                builder2.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
