package com.example.mobileapp1;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze implements IGameState {
    //CONSTRUCTORS================================
    public Maze(IChangeState stateChanger, int mazeWidth, int mazeHeight) {
        StateChanger = stateChanger;
        MazeHeight = mazeHeight;
        MazeWidth = mazeWidth;
        InitAnimator();
        Create();
        StartGame();
    }

    public Maze(IChangeState stateChanger) {
        StateChanger = stateChanger;
        InitAnimator();
        Create();
        StartGame();
    }

    //============================================
    ValueAnimator Animator;
    float AnimateBiasX = 0;
    float AnimateBiasY = 0;
    float xMotionDiff = 0;
    float yMotionDiff = 0;
    Boolean ReadyToMove = true;

    IChangeState StateChanger;
    List<Cell> Maze = new ArrayList<Cell>();
    int MazeWidth = 7;
    int MazeHeight = 7;

    Player player;
    Cell finish;

    Random randNumber = new Random();
    private Paint mPaint = new Paint();
    Path path = new Path();

    public void InitAnimator() {
        Animator = ValueAnimator.ofFloat(0, 1);
        Animator.setDuration(200);
        Animator.setInterpolator(new DecelerateInterpolator());
        Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float AnimatingValue = (float) animation.getAnimatedValue();

                if (xMotionDiff > 0 && yMotionDiff > 0 && player.Location.Top != null && !player.Location.Top.IsFilled) {
                    ReadyToMove = false;
                    AnimateBiasX = -AnimatingValue;
                }

                if (xMotionDiff > 0 && yMotionDiff < 0 && player.Location.Right != null && !player.Location.Right.IsFilled) {
                    ReadyToMove = false;
                    AnimateBiasY = AnimatingValue;
                }

                if (xMotionDiff < 0 && yMotionDiff > 0 && player.Location.Left != null && !player.Location.Left.IsFilled) {
                    ReadyToMove = false;
                    AnimateBiasY = -AnimatingValue;
                }

                if (xMotionDiff < 0 && yMotionDiff < 0 && player.Location.Bottom != null && !player.Location.Bottom.IsFilled) {
                    ReadyToMove = false;
                    AnimateBiasX = AnimatingValue;
                }

                if (!ReadyToMove)
                    StateChanger.Invalidate();

                if (Float.compare(AnimatingValue, 1) == 0) {
                    AnimateBiasY = AnimateBiasX = 0;
                    onEndAnimation();
                }
            }
        });
    }

    void onEndAnimation() {
        if (player != null) {

            if (xMotionDiff > 0 && yMotionDiff > 0)
                player.move(Player.sides.UP);
            if (xMotionDiff > 0 && yMotionDiff < 0)
                player.move(Player.sides.RIGHT);
            if (xMotionDiff < 0 && yMotionDiff > 0)
                player.move(Player.sides.LEFT);
            if (xMotionDiff < 0 && yMotionDiff < 0)
                player.move(Player.sides.DOWN);
        }
        StateChanger.Invalidate();
        ReadyToMove = true;

        if(player.Location == finish)
            StateChanger.ChangeState(Menu.class.getName(),null);
    }

    private void Create() {
        ArrayList<Cell> prevRow = null;
        for (int i = 0; i < MazeHeight; i++) {
            ArrayList<Cell> newRow = new ArrayList<>();
            Cell left = null;

            for (int j = 0; j < MazeWidth; j++) {

                Cell newCell = new Cell();

                newCell.X = i;
                newCell.Y = j;

                newCell.IsFilled = true;// randNumber.nextBoolean();

                newCell.Left = left;
                if (left != null) left.Right = newCell;

                if (prevRow != null)
                    if (prevRow.size() > 0) {
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

    public Maze Build() {
        Cell root = Maze.get(randNumber.nextInt(Maze.size()));
        recBuild(root);
        return this;
    }

    public void Rebuild() {
        for (Cell cur : Maze) {
            cur.IsBuilt = false;
            cur.IsFilled = true;
        }
        Build();
    }
    public void StartGame(int incX,int incY)
    {
        if(incX == 0 && incY == 0)
        {
            StartGame();
            return;
        }

        if(MazeWidth+ incX>=3)MazeWidth+=incX;
        if(MazeHeight+ incY>=3) MazeHeight+=incY;
        Maze.clear();

        Create();
        StartGame();
    }

    private void recBuild(Cell cell) {
        ArrayList<Integer> bounds = new ArrayList<Integer>();
        bounds.add(1);
        bounds.add(2);
        bounds.add(3);
        bounds.add(4);
        //1 = left, 2 = right, 3 = top, 4 = bottom

        ArrayList<Integer> randBounds = new ArrayList<Integer>();


        while (bounds.size() > 0) {
            int index = randNumber.nextInt(bounds.size());
            randBounds.add(bounds.get(index));
            bounds.remove(index);
        }

        cell.IsBuilt = true;
        cell.IsFilled = false;

        for (Integer cur : randBounds) {
            switch (cur.intValue()) {
                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 1: {
                    if (cell.Left != null)
                        if (!cell.Left.IsBuilt) {
                            if (cell.Left.Left != null)
                                if (!cell.Left.Left.IsBuilt) {
                                    cell.Left.IsBuilt = true;
                                    cell.Left.IsFilled = false;
                                    recBuild(cell.Left.Left);
                                }
                        }
                }
                ;
                break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 2: {
                    if (cell.Right != null)
                        if (!cell.Right.IsBuilt) {
                            if (cell.Right.Right != null)
                                if (!cell.Right.Right.IsBuilt) {
                                    cell.Right.IsBuilt = true;
                                    cell.Right.IsFilled = false;
                                    recBuild(cell.Right.Right);
                                }
                        }
                }
                ;
                break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 3: {
                    if (cell.Top != null)
                        if (!cell.Top.IsBuilt) {
                            if (cell.Top.Top != null)
                                if (!cell.Top.Top.IsBuilt) {
                                    cell.Top.IsBuilt = true;
                                    cell.Top.IsFilled = false;
                                    recBuild(cell.Top.Top);
                                }
                        }
                }
                ;
                break;

                //1 = left, 2 = right, 3 = top, 4 = bottom
                case 4: {
                    if (cell.Bottom != null)
                        if (!cell.Bottom.IsBuilt) {
                            if (cell.Bottom.Bottom != null)
                                if (!cell.Bottom.Bottom.IsBuilt) {
                                    cell.Bottom.IsBuilt = true;
                                    cell.Bottom.IsFilled = false;
                                    recBuild(cell.Bottom.Bottom);
                                }
                        }
                }
                ;
                break;
            }
        }
    }

    //sqrt(pow(x2 – x1, 2) + pow(y2 – y1, 2))
    public Cell GetAnyCell(CellFilter filter) {
        switch (filter) {
            default:
                return Maze.get(randNumber.nextInt(Maze.size()));
            case OPENED: {
                ArrayList<Cell> Opened = new ArrayList<Cell>();
                for (Cell cur : Maze)
                    if (!cur.IsFilled)
                        Opened.add(cur);
                return Opened.get(randNumber.nextInt(Opened.size()));
            }
            case CLOSED: {
                ArrayList<Cell> Closed = new ArrayList<Cell>();
                for (Cell cur : Maze)
                    if (cur.IsFilled)
                        Closed.add(cur);
                return Closed.get(randNumber.nextInt(Closed.size()));
            }
        }
    }

    public Cell GetAnyCell(Cell point, float MinRange) {
        ArrayList<Cell> Opened = new ArrayList<Cell>();
        do {
            for (Cell cur : Maze) {
                float t = (float) Math.sqrt(Math.pow(cur.X - point.X, 2.0f) + Math.pow(cur.Y - point.Y, 2.0f));
                if (!cur.IsFilled && Math.sqrt(Math.pow(cur.X - point.X, 2.0f) + Math.pow(cur.Y - point.Y, 2.0f)) > MinRange)
                    Opened.add(cur);
            }
            MinRange -= 0.5f;
        } while (Opened.isEmpty());

        return Opened.get(randNumber.nextInt(Opened.size()));
    }

    @Override
    public void FlingHandle(MotionEvent e1, MotionEvent e2) {
        if (player != null && !Animator.isRunning()) {
            xMotionDiff = e1.getX() - e2.getX();
            yMotionDiff = e1.getY() - e2.getY();

            Animator.start();
        }
    }

    @Override
    public void TapHandle(MotionEvent e) {
    }

    @Override
    public void TapDownHandle(MotionEvent e) {
    }

    @Override
    public void DoubleTapHandle(MotionEvent e) {
        //StartGame();
    }

    private float GetIsoX(float decX, float decY) {
        return decX - decY;
    }

    private float GetIsoY(float decX, float decY) {
        return (decX + decY) / 2.0f;
    }

    private float GetDecX(float isoX, float isoY) {
        return (2.0f * isoY + isoX) / 2.0f;
    }

    private float GetDecY(float isoX, float isoY) {
        return (2.0f * isoY - isoX) / 2.0f;
    }

    public void DrawCell(Canvas canvas, Cell cur, float cellSize, float x1, float x2, float y1, float y2, float centerX, float centerY) {

        if (cur.IsFilled) {

            float biasX = centerX - cellSize / 2.0f;
            float biasY = centerY;

            //20 20 60
            //top
            path.moveTo(biasY + GetIsoX(x1, y1), biasX + GetIsoY(x1, y1));
            path.lineTo(biasY + GetIsoX(x1, y2), biasX + GetIsoY(x1, y2));
            path.lineTo(biasY + GetIsoX(x2, y2), biasX + GetIsoY(x2, y2));
            path.lineTo(biasY + GetIsoX(x2, y1), biasX + GetIsoY(x2, y1));

            path.close();
            mPaint.setColor(Color.argb(255, 20, 20, 60));
            canvas.drawPath(path, mPaint);
            path.reset();

            //80 84 140
            //left
            if (cur.Right == null || !cur.Right.IsFilled) {
                path.moveTo(biasY + GetIsoX(x1, y2), biasX + GetIsoY(x1, y2));
                path.lineTo(centerY + GetIsoX(x1, y2), centerX + GetIsoY(x1, y2));
                path.lineTo(centerY + GetIsoX(x2, y2), centerX + GetIsoY(x2, y2));
                path.lineTo(biasY + GetIsoX(x2, y2), biasX + GetIsoY(x2, y2));

                path.close();
                mPaint.setColor(Color.argb(255, 80, 84, 140));
                canvas.drawPath(path, mPaint);
                path.reset();
            }

            //11 15 45
            //right
            if (cur.Bottom == null || !cur.Bottom.IsFilled) {
                path.moveTo(biasY + GetIsoX(x2, y2), biasX + GetIsoY(x2, y2));
                path.lineTo(centerY + GetIsoX(x2, y2), centerX + GetIsoY(x2, y2));
                path.lineTo(centerY + GetIsoX(x2, y1), centerX + GetIsoY(x2, y1));
                path.lineTo(biasY + GetIsoX(x2, y1), biasX + GetIsoY(x2, y1));

                path.close();
                mPaint.setColor(Color.argb(255, 11, 15, 45));
                canvas.drawPath(path, mPaint);
                path.reset();
            }
            mPaint.clearShadowLayer();
        }
    }

    @Override
    public void DrawHandle(Canvas canvas) {
        mPaint.setColor(Color.argb(255, 44, 36, 48));
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);

        int minSize = Math.min(canvas.getHeight(), canvas.getWidth());

        float x = (canvas.getHeight() - minSize) / 2.0f;
        float y = (canvas.getWidth() - minSize) / 2.0f;

        float centerX = canvas.getHeight() / 2.0f;
        float centerY = canvas.getWidth() / 2.0f;

        float cellSize = minSize / (float) Math.max(MazeHeight, MazeWidth);

        for (Cell cur : Maze) {
            //====================================================

            float x1 = cellSize * cur.X - cellSize * (player.Location.X + AnimateBiasX);
            float y1 = cellSize * cur.Y - cellSize * (player.Location.Y + AnimateBiasY);

            float x2 = cellSize * cur.X + cellSize - cellSize * (player.Location.X + AnimateBiasX);
            float y2 = cellSize * cur.Y + cellSize - cellSize * (player.Location.Y + AnimateBiasY);

            if (!cur.IsFilled) {
                path.moveTo(centerY + GetIsoX(x1, y1), centerX + GetIsoY(x1, y1));
                path.lineTo(centerY + GetIsoX(x1, y2), centerX + GetIsoY(x1, y2));
                path.lineTo(centerY + GetIsoX(x2, y2), centerX + GetIsoY(x2, y2));
                path.lineTo(centerY + GetIsoX(x2, y1), centerX + GetIsoY(x2, y1));

                path.close();
                mPaint.setColor(Color.BLUE);
                canvas.drawPath(path, mPaint);
                path.reset();
            }

            //113 14 45
            if (finish != null)
                if (finish == cur) {
                    path.moveTo(centerY + GetIsoX(x1, y1), centerX + GetIsoY(x1, y1));
                    path.lineTo(centerY + GetIsoX(x1, y2), centerX + GetIsoY(x1, y2));
                    path.lineTo(centerY + GetIsoX(x2, y2), centerX + GetIsoY(x2, y2));
                    path.lineTo(centerY + GetIsoX(x2, y1), centerX + GetIsoY(x2, y1));

                    path.close();
                    mPaint.setColor(Color.argb(255, 200, 14, 45));
                    canvas.drawPath(path, mPaint);
                    path.reset();
                }

            if (centerX + GetIsoY(x1, y1) <= centerX + GetIsoY(cellSize / 4.0f, cellSize / 4.0f)) {
                DrawCell(canvas, cur, cellSize, x1, x2, y1, y2, centerX, centerY);
            }

            //====================================================

        }
        if (player != null)
        {
            mPaint.setColor(Color.YELLOW);

            float xP = cellSize * player.Location.X + cellSize / 4.0f - cellSize * player.Location.X;
            float yP = cellSize * player.Location.Y + cellSize / 4.0f - cellSize * player.Location.Y;

            canvas.drawCircle(
                    centerY + GetIsoX(xP, yP),
                    centerX + GetIsoY(xP, yP),
                    cellSize / 1.7f,
                    mPaint);
        }
        for (Cell cur : Maze) {
            //====================================================

            float x1 = cellSize * cur.X - cellSize * (player.Location.X + AnimateBiasX);
            float y1 = cellSize * cur.Y - cellSize * (player.Location.Y + AnimateBiasY);

            float x2 = cellSize * cur.X + cellSize - cellSize * (player.Location.X + AnimateBiasX);
            float y2 = cellSize * cur.Y + cellSize - cellSize * (player.Location.Y + AnimateBiasY);

            if (centerX + GetIsoY(x1, y1) > centerX + GetIsoY(cellSize / 4.0f, cellSize / 4.0f)) {
                DrawCell(canvas, cur, cellSize, x1, x2, y1, y2, centerX, centerY);
            }

            //====================================================
        }
    }

    @Override
    public void ApplyArgs(String[] args) {
        if(args!=null)
        {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            StartGame(x, y);
        }
    }

    public enum CellFilter {
        ANY,
        OPENED,
        CLOSED
    }

    public void StartGame() {
        Rebuild();
        player = new Player(GetAnyCell(CellFilter.OPENED));
        finish = GetAnyCell(player.Location, ((MazeWidth + MazeHeight) / 2f));
        StateChanger.Invalidate();
    }
}
