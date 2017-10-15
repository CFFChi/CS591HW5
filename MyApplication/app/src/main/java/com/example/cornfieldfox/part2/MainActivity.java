package com.example.cornfieldfox.part2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnDoubleTapListener,GestureDetector.OnGestureListener{

    private final float FLING_THRESHOLD = 100;
    private final float FLING_VELO_THREASHOLD = 100;

    private Animation rota;
    GestureDetector GD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GD = new GestureDetector(this,this);
        GD.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        ImageView pic = (ImageView) findViewById(R.id.mainActImg);
        rota = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.act1);
        pic.startAnimation(rota);






        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
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
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        GD.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent m1, MotionEvent m2, float vx, float vy) {
        float direX = m1.getX()-m2.getX();
        float direY = m1.getY()-m2.getY();
        //detect right fling:
        //1. |direX|>|direY|
        //2. |direX|>FLING_THRESHOLD
        //3. |vx| > FLING_VELO_THRESHOLD
        //4. direX<0 (m2.getX()>m1.getX())
        if(Math.abs(direX)>Math.abs(direY) && Math.abs(direX)>FLING_THRESHOLD
                && Math.abs(vx) > FLING_VELO_THREASHOLD && direX<0)
        {
            Intent intent = new Intent(this.getApplicationContext(),Activity2.class);
            startActivity(intent);
        }


        return true;
    }
}
