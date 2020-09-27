package com.example.mobileapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class MainActivity
        extends
        AppCompatActivity
        implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private IGameState game;
    private GestureDetectorCompat gd;
    private View CurrentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.CreateInstance();
        Game.ViewContext = new Draw2D(this, game);

        setContentView(Game.ViewContext);
        gd = new GestureDetectorCompat(this,this);
        gd.setOnDoubleTapListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);

        int Action = event.getAction();
        if(Action == MotionEvent.ACTION_CANCEL || Action == MotionEvent.ACTION_UP)
            game.TapHandle(event);


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        game.DoubleTapHandle(e);
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        game.TapDownHandle(e);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        game.FlingHandle(e1,e2);
        return false;
    }
}