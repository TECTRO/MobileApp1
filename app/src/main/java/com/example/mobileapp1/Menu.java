package com.example.mobileapp1;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class Menu implements IGameState {

    IChangeState StateChanger;

    public Menu(IChangeState state) {
        StateChanger = state;
    }

    @Override
    public void FlingHandle(MotionEvent e1, MotionEvent e2) {

    }

    @Override
    public void TapHandle(MotionEvent e) {

    }

    @Override
    public void TapDownHandle(MotionEvent e) {

    }

    @Override
    public void DoubleTapHandle(MotionEvent e) {

    }

    @Override
    public void DrawHandle(Canvas canvas) {

    }
}
