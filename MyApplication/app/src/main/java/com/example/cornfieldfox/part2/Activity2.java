package com.example.cornfieldfox.part2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class Activity2 extends AppCompatActivity implements SensorEventListener, GestureDetector.OnGestureListener{
    // Imagine
    private ImageView leoImg;


    private SensorManager sm;
    private Sensor acc;
    private float[] gravity;
    private float x,y,z;
    private float move_threshold = 10;
    float diff;//for detect shake or move
    float max;
    // shake:
    private float shake_threshold = 20;


    //picture move speed:
    private int translate_speed = 1;
    //picture rotate speed
    private int rotate_speed = 1;
    // animation's rotate time
    private int TOTAL_ROTATE_TIME = 5000;

    private int TOTAL_TRANS_TIME = 2000;
    private int FAST_THRESHOLD =  2000;
    private int FLING_THRESHOLD = 100;

    private GestureDetector GD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gravity = new float[3];
        setContentView(R.layout.activity_2);
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        leoImg = (ImageView)findViewById(R.id.leopard);

        GD = new GestureDetector(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        sm.registerListener(this, acc,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, acc,SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();

        sm.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //accelerometer
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            x = event.values[0] - gravity[0];
            y = event.values[1] - gravity[1];
            z = event.values[2] - gravity[2];

            max = Math.max(Math.abs(x), Math.abs(y));

//            float max = Math.max(Math.abs(x), Math.abs(y));
            diff = (float) Math.sqrt(x * x + y * y + z * z);

            // check speed:
            if(translate_speed==0)
            {
                translate_speed = 1;
            }



            if (max > move_threshold && max == Math.abs(x)) {
                Log.w("diff::", Float.toString(diff));
                Log.w("x::", Float.toString(x));
                Log.w("y::", Float.toString(y));
                if (x > 0) {
                    //left
                    Animation anime = AnimationUtils.loadAnimation(this, R.anim.left);
                    anime.setDuration((long)(TOTAL_TRANS_TIME/translate_speed));
                    leoImg.startAnimation(anime);


                } else {
                    //right
                    Animation anime = AnimationUtils.loadAnimation(this, R.anim.right);
                    anime.setDuration((long)(TOTAL_TRANS_TIME/translate_speed));
                    leoImg.startAnimation(anime);

                }
            } else {
                if (max > move_threshold && max == Math.abs(y)) {
                    Log.w("diff::", Float.toString(diff));
                    Log.w("x::", Float.toString(x));
                    Log.w("y::", Float.toString(y));
                    if (y < 0) {
                        //up
                        Animation anime = AnimationUtils.loadAnimation(this, R.anim.up);
                        anime.setDuration((long)(TOTAL_TRANS_TIME/translate_speed));
                        leoImg.startAnimation(anime);
                    } else {
                        //down
                        Animation anime = AnimationUtils.loadAnimation(this, R.anim.down);
                        anime.setDuration((long)(TOTAL_TRANS_TIME/translate_speed));
                        leoImg.startAnimation(anime);
                    }
                }

            }



            if (diff > shake_threshold) {
                Log.w("translate speed::", Integer.toString(translate_speed));

                //shake
                Animation anime = AnimationUtils.loadAnimation(this, R.anim.shake);
                anime.setDuration((long)(TOTAL_TRANS_TIME/translate_speed));
                if(translate_speed>1)
                    anime.setRepeatCount(translate_speed-1);

                leoImg.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        translate_speed/=2;

        Toast.makeText(this, "speed halve", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.w("long press::",Double.toString(motionEvent.getPressure()));
        translate_speed *=2;
        Toast.makeText(this, "speed double", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {


        //right <0
        float diffX = e1.getX()-e2.getX();
        float diffY = e1.getY()-e2.getY();
        // is a fling and direction is left/right:
        int degree = 358;
        if(Math.abs(diffX)>FLING_THRESHOLD && Math.abs(diffX)>Math.abs(diffY))
        {
            //whether it is a fast fling?
            if(Math.abs(vx)>FAST_THRESHOLD)
            {
                rotate_speed = 10*translate_speed;
            }
            else{
                rotate_speed = 5*translate_speed;
            }

            if(diffX<0)
            {
                degree = Math.abs(degree);
            }
            else
            {
                degree = -Math.abs(degree);
            }
            // rotate around the picture's center point.
            RotateAnimation anime = new RotateAnimation(0,degree,Animation.RELATIVE_TO_SELF,0.5F, Animation.RELATIVE_TO_SELF,0.5f);
            // for speed:
            Log.w("fling::speed:",Integer.toString(rotate_speed));
            anime.setDuration((long) (TOTAL_ROTATE_TIME/rotate_speed)); //5x's or 10x's
            //why substract 1? because repeatCount means "the number of repetition after the first run of animation"
            // for example: set repeatCount to 9. the animation will rotate 10 times.
            anime.setRepeatCount((int) rotate_speed- 1);
            //"@android:anim/linear_interpolator"
            anime.setInterpolator(new LinearInterpolator());
            leoImg.startAnimation(anime);

        }


        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GD.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
