package com.example.mobileapp1;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface IGameState
{
    void FlingHandle(MotionEvent e1, MotionEvent e2);
    void TapHandle(MotionEvent e);
    void TapDownHandle(MotionEvent e);
    void DoubleTapHandle(MotionEvent e);
    void DrawHandle(Canvas canvas);
    void ApplyArgs(String[] args);


}
