package com.example.mobileapp1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Draw2D extends View {
    //List<Cell> Maze;
    //final int MazeWidth  = 31;
    //final int MazeHeight = 31;
    //Random randNumber = new Random();
    Maze maze;

    public Draw2D(Context context, Maze maze) {
        super(context);
        this.maze = maze;
        //Maze = new ArrayList<Cell>();

        //CreateMaze(Maze);
        //BuildMaze(Maze);
        //Toast.makeText(context, "!!!", Toast.LENGTH_SHORT).show();
    }


    private Paint mPaint = new Paint();


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(Color.LTGRAY);
        canvas.drawPaint(mPaint);
        //=================================
        maze.Draw(canvas);
}}
