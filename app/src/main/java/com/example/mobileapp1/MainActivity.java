package com.example.mobileapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

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

        CurrentView = new Draw2D(this, game);
        setContentView(CurrentView);

        gd = new GestureDetectorCompat(this,this);
        gd.setOnDoubleTapListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        game.DoubleTapHandle(e);
        CurrentView.invalidate();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        game.TapDownHandle(e);
        CurrentView.invalidate();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        game.TapHandle(e);
        CurrentView.invalidate();
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
        CurrentView.invalidate();
        return false;
    }
}