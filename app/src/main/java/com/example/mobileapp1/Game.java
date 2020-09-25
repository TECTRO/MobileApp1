package com.example.mobileapp1;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game implements IGameState, IChangeState
{
    //SINGLETON========================
    private static Game current;
    public static Game CreateInstance()
    {
        if(current == null) current = new Game();
        return current;
    }
    private Game()
    {
        GameStates = Arrays.asList(new IGameState[]
        {
            new Maze(this),
                new Menu(this)
        });

        ActiveState = GameStates.get(0);
    }
    //=================================
    IGameState ActiveState;
    List<IGameState> GameStates;


    @Override
    public void FlingHandle(MotionEvent e1, MotionEvent e2) {
        ActiveState.FlingHandle(e1,e2);
    }

    @Override
    public void TapHandle(MotionEvent e) {
        ActiveState.TapHandle(e);
    }

    @Override
    public void TapDownHandle(MotionEvent e) {

    }

    @Override
    public void DoubleTapHandle(MotionEvent e) {
        ActiveState.DoubleTapHandle(e);
    }

    @Override
    public void DrawHandle(Canvas canvas) {
        ActiveState.DrawHandle(canvas);
    }

    @Override
    public void ChangeState(String stateName, String[] args) {
        for (IGameState cur : GameStates)
            if(cur.getClass().getName() == stateName)
                ActiveState = cur;
    }
}