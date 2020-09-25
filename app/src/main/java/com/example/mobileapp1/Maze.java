package com.example.mobileapp1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze
{
    List<Cell> Maze;
    int MazeWidth  = 31;
    int MazeHeight = 31;

    Random randNumber = new Random();
    private Paint mPaint = new Paint();

    public Maze(int mazeWidth, int mazeHeight)
    {
        MazeHeight = mazeHeight;
        MazeWidth = mazeWidth;
        Maze = new ArrayList<Cell>();
        Create();
    }

    public Maze()
    {
        Maze = new ArrayList<Cell>();
        Create();
    }

    private void Create()
    {
        ArrayList<Cell> prevRow = null;
        for (int i = 0; i< MazeHeight; i++)
        {
            ArrayList<Cell> newRow = new ArrayList<>();
            Cell left = null;

            for (int j = 0; j< MazeWidth;j++)
            {

                Cell newCell = new Cell();

                newCell.X = i;
                newCell.Y = j;

                newCell.IsFilled = true;// randNumber.nextBoolean();

                newCell.Left = left;
                if(left!=null)left.Right = newCell;

                if(prevRow!=null)
                    if(prevRow.size()>0) {
                        newCell.Top = prevRow.get(j);
                        prevRow.get(j).Bottom = newCell;
                    }

                Maze.add(newCell);
                newRow.add(newCell);
                left = newCell;

            }
            prevRow = newRow;
        }
    }

    public void Draw(Canvas canvas)
    {
        int minSize = Math.min(canvas.getHeight(),canvas.getWidth());

        float x = (canvas.getHeight() - minSize) /2.0f;
        float y = (canvas.getWidth() - minSize) /2.0f;

        float cellSize = minSize / (float)Math.max(MazeHeight,MazeWidth);
        // mPaint.setStyle(Paint.Style.STROKE);

        for (Cell cur: Maze)
        {
            if(cur.IsFilled)
                mPaint.setColor(Color.BLACK);
            else
                mPaint.setColor(Color.BLUE);

            canvas.drawRect(
                    y+cellSize*cur.Y, x+cellSize*cur.X,
                    y+cellSize*cur.Y+cellSize, x+cellSize*cur.X+cellSize,
                    mPaint);
        }
    }

    public void Build()
    {
        Cell root = Maze.get(randNumber.nextInt(Maze.size()));
        recBuild(root);
    }

    private void recBuild(Cell cell)
    {
        ArrayList<Integer> bounds = new ArrayList<Integer>();
        bounds.add(1);
        bounds.add(2);
        bounds.add(3);
        bounds.add(4);
        //1 = left, 2 = right, 3 = top, 4 = bottom

        ArrayList<Integer> randBounds = new ArrayList<Integer>();


        while (bounds.size()>0)
        {
            int index = randNumber.nextInt(bounds.size());
            randBounds.add(bounds.get(index));
            bounds.remove(index);
        }

        cell.IsBuilt =true;
        cell.IsFilled = false;

        for(Integer cur: randBounds)
        {
            switch(cur.intValue())
            {
                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 1:{
                    if(cell.Left!=null)
                        if(!cell.Left.IsBuilt)
                        {
                            if(cell.Left.Left!=null)
                                if(!cell.Left.Left.IsBuilt)
                                {
                                    cell.Left.IsBuilt = true;
                                    cell.Left.IsFilled = false;
                                    recBuild(cell.Left.Left);
                                }
                        }
                }; break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 2:{
                    if(cell.Right!=null)
                        if(!cell.Right.IsBuilt)
                        {
                            if(cell.Right.Right!=null)
                                if(!cell.Right.Right.IsBuilt)
                                {
                                    cell.Right.IsBuilt = true;
                                    cell.Right.IsFilled = false;
                                    recBuild(cell.Right.Right);
                                }
                        }
                }; break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 3:{
                    if(cell.Top!=null)
                        if(!cell.Top.IsBuilt)
                        {
                            if(cell.Top.Top!=null)
                                if(!cell.Top.Top.IsBuilt)
                                {
                                    cell.Top.IsBuilt = true;
                                    cell.Top.IsFilled = false;
                                    recBuild(cell.Top.Top);
                                }
                        }
                }; break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 4:{
                    if(cell.Bottom!=null)
                        if(!cell.Bottom.IsBuilt)
                        {
                            if(cell.Bottom.Bottom!=null)
                                if(!cell.Bottom.Bottom.IsBuilt)
                                {
                                    cell.Bottom.IsBuilt = true;
                                    cell.Bottom.IsFilled = false;
                                    recBuild(cell.Bottom.Bottom);
                                }
                        }
                }; break;
            }
        }
    }
}
