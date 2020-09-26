package com.example.mobileapp1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze implements IGameState
{
    //CONSTRUCTORS================================
    public Maze(IChangeState stateChanger, int mazeWidth, int mazeHeight)
    {
        StateChanger = stateChanger;
        MazeHeight = mazeHeight;
        MazeWidth = mazeWidth;
        Maze = new ArrayList<Cell>();
        Create();
        StartGame();
    }

    public Maze(IChangeState stateChanger)
    {
        StateChanger = stateChanger;
        Maze = new ArrayList<Cell>();
        Create();
        StartGame();
    }
    //============================================

    IChangeState StateChanger;
    List<Cell> Maze;
    int MazeWidth  = 7;
    int MazeHeight = 7;

    Player player;
    Cell finish;

    Random randNumber = new Random();
    private Paint mPaint = new Paint();

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
    public Maze Build()
    {
        Cell root = Maze.get(randNumber.nextInt(Maze.size()));
        recBuild(root);
        return this;
    }
    public Maze Rebuild()
    {
        for (Cell cur : Maze)
        {
            cur.IsBuilt = false;
            cur.IsFilled = true;
        }
        return Build();
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
//sqrt(pow(x2 – x1, 2) + pow(y2 – y1, 2))
    public Cell GetAnyCell(CellFilter filter)
    {
        switch (filter)
        {
            default: return Maze.get(randNumber.nextInt(Maze.size()));
            case OPENED:
                {
                    ArrayList<Cell> Opened = new ArrayList<Cell>();
                    for (Cell cur: Maze)
                        if(!cur.IsFilled)
                            Opened.add(cur);
                        return Opened.get(randNumber.nextInt(Opened.size()));
                }
            case CLOSED:{
                ArrayList<Cell> Closed = new ArrayList<Cell>();
                for (Cell cur: Maze)
                    if(cur.IsFilled)
                        Closed.add(cur);
                return Closed.get(randNumber.nextInt(Closed.size()));
            }
        }
    }
    public Cell GetAnyCell(Cell point, float MinRange)
    {
        ArrayList<Cell> Opened = new ArrayList<Cell>();
        for (Cell cur: Maze)
            if(!cur.IsFilled && Math.sqrt(Math.pow(cur.X - point.X,2.0f) + Math.pow(cur.Y - point.Y, 2))>MinRange)
                Opened.add(cur);
        return Opened.get(randNumber.nextInt(Opened.size()));
    }

    @Override
    public void FlingHandle(MotionEvent e1, MotionEvent e2) {
        if(player!=null)
        {
            float xDiff = e1.getX() - e2.getX();
            float yDiff = e1.getY() - e2.getY();

            if(xDiff>0 && yDiff>0)
                player.move(Player.sides.UP);
            if(xDiff>0 && yDiff<0)
                player.move(Player.sides.RIGHT);
            if(xDiff<0 && yDiff>0)
                player.move(Player.sides.LEFT);
            if(xDiff<0 && yDiff<0)
                player.move(Player.sides.DOWN);
        }
        StateChanger.Invalidate();
    }

    @Override
    public void TapHandle(MotionEvent e) { }
    @Override
    public void TapDownHandle(MotionEvent e) { }
    @Override
    public void DoubleTapHandle(MotionEvent e) { StartGame(); }

    private float GetIsoX(float decX,float decY)
    {
        return decX - decY;
    }

    private float GetIsoY(float decX,float decY)
    {
        return (decX + decY)/2.0f;
    }

    private float GetDecX(float isoX, float isoY)
    {
        return  (2.0f * isoY + isoX) / 2.0f;
    }

    private float GetDecY(float isoX, float isoY)
    {
        return  (2.0f * isoY - isoX) / 2.0f;
    }

    Path path = new Path();
    private  Path GetIsoPath(Cell drawingCell, float cellSize, float biasX, float biasY)
    {
        float x1 = cellSize*drawingCell.X;
        float y1 = cellSize*drawingCell.Y;

        float x2 = cellSize*drawingCell.X+cellSize;
        float y2 = cellSize*drawingCell.Y+cellSize;



        path.moveTo(biasY+GetIsoX(x1,y1),biasX+GetIsoY(x1,y1));
        path.lineTo(biasY+GetIsoX(x1,y2),biasX+GetIsoY(x1,y2));
        path.lineTo(biasY+GetIsoX(x2,y2),biasX+GetIsoY(x2,y2));
        path.lineTo(biasY+GetIsoX(x2,y1),biasX+GetIsoY(x2,y1));

        path.close();

        return path;
    }
    @Override
    public void DrawHandle(Canvas canvas) {
        mPaint.setColor(Color.argb(255,44,36,48));
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),mPaint);

        int minSize = Math.min(canvas.getHeight(),canvas.getWidth());

        float x = (canvas.getHeight() - minSize) /2.0f;
        float y = (canvas.getWidth() - minSize) /2.0f;

        float centerX = canvas.getHeight()/2.0f;
        float centerY = canvas.getWidth()/2.0f;

        float cellSize = minSize / (float)Math.max(MazeHeight,MazeWidth);
        // mPaint.setStyle(Paint.Style.STROKE);

        for (Cell cur: Maze)
        {
            //====================================================
            float x1 = cellSize * cur.X;
            float y1 = cellSize * cur.Y;

            float x2 = cellSize * cur.X + cellSize;
            float y2 = cellSize * cur.Y + cellSize;

            if(!cur.IsFilled)
            {
                path.moveTo(centerY + GetIsoX(x1, y1), x + GetIsoY(x1, y1));
                path.lineTo(centerY + GetIsoX(x1, y2), x + GetIsoY(x1, y2));
                path.lineTo(centerY + GetIsoX(x2, y2), x + GetIsoY(x2, y2));
                path.lineTo(centerY + GetIsoX(x2, y1), x + GetIsoY(x2, y1));

                path.close();
                mPaint.setColor(Color.BLUE);
                canvas.drawPath(path, mPaint);
                path.reset();
            }

            if(cur.IsFilled) {
                //mPaint.setColor(Color.argb(150,50,50,50));

                float biasX = x - cellSize / 2.0f;
                float biasY = centerY;

                //path = new Path();
                //20 20 60
                //top
                path.moveTo(biasY+GetIsoX(x1,y1),biasX+GetIsoY(x1,y1));
                path.lineTo(biasY+GetIsoX(x1,y2),biasX+GetIsoY(x1,y2));
                path.lineTo(biasY+GetIsoX(x2,y2),biasX+GetIsoY(x2,y2));
                path.lineTo(biasY+GetIsoX(x2,y1),biasX+GetIsoY(x2,y1));

                path.close();
                mPaint.setColor(Color.argb(255,20,20,60));
                canvas.drawPath(path,mPaint);
                path.reset();
                //80 84 140
                //left
                if(cur.Right == null || !cur.Right.IsFilled)
                {
                    path.moveTo(biasY+GetIsoX(x1,y2),biasX+GetIsoY(x1,y2));
                    path.lineTo(centerY+GetIsoX(x1,y2),x+GetIsoY(x1,y2));
                    path.lineTo(centerY+GetIsoX(x2,y2),x+GetIsoY(x2,y2));
                    path.lineTo(biasY+GetIsoX(x2,y2),biasX+GetIsoY(x2,y2));

                    path.close();
                    mPaint.setColor(Color.argb(255,80,84,140));
                    canvas.drawPath(path,mPaint);
                    path.reset();
                }
                //path.reset();

                //11 15 45
                //right
                if(cur.Bottom == null || !cur.Bottom.IsFilled)
                {
                    path.moveTo(biasY + GetIsoX(x2, y2), biasX + GetIsoY(x2, y2));
                    path.lineTo(centerY + GetIsoX(x2, y2), x + GetIsoY(x2, y2));
                    path.lineTo(centerY + GetIsoX(x2, y1), x + GetIsoY(x2, y1));
                    path.lineTo(biasY + GetIsoX(x2, y1), biasX + GetIsoY(x2, y1));

                    path.close();
                    mPaint.setColor(Color.argb(255,11,15,45));
                    canvas.drawPath(path,mPaint);
                    path.reset();
                }
                //canvas.drawPath(path, mPaint);
                mPaint.clearShadowLayer();
            }

            //====================================================

            if (finish!=null)
                if(finish == cur)
                {
                    mPaint.setColor(Color.RED);
                    canvas.drawPath(GetIsoPath(finish,cellSize,x,centerY),mPaint);
                    path.reset();
                }

            if(player!=null)
                if(player.Location == cur)
                {
                    mPaint.setColor(Color.YELLOW);

                    float xP = cellSize*player.Location.X + cellSize/4.0f;
                    float yP = cellSize*player.Location.Y+ cellSize/4.0f;

                    canvas.drawCircle(
                            centerY+GetIsoX(xP,yP),
                            x+GetIsoY(xP,yP),
                            cellSize/1.7f,
                            mPaint);
                }
        }
    }

    public enum CellFilter
    {
        ANY,
        OPENED,
        CLOSED
    }

    public void StartGame()
    {
        Rebuild();
        player = new Player(GetAnyCell(CellFilter.OPENED));
        finish = GetAnyCell(player.Location,(MazeWidth+MazeHeight)/4.0f);
        StateChanger.Invalidate();
    }
}
