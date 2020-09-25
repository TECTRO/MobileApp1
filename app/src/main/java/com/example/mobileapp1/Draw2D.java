package com.example.mobileapp1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Draw2D extends View {
    IGameState game;

    public Draw2D(Context context, IGameState game) {
        super(context);
        this.game = game;
        //Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        game.DrawHandle(canvas);
    }

}
