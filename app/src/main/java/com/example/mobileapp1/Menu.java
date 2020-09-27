package com.example.mobileapp1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.security.PublicKey;

class Button {
    public String Caption;
    public float FontSize;
    public RectF Size;
    public Boolean IsPressed;

    public Button(String caption) {
        IsPressed = false;
        Caption = caption;
    }

    public Boolean TryPress(float x, float y) {
        if (Size.intersect(x, y, x + 1, y + 1))
            IsPressed = true;
        return IsPressed;
    }

    public Boolean TryRealise(float x, float y) {
        IsPressed = false;
        return false;
    }

    public void DrawButton(Canvas canvas, Paint paint) {
        if (!IsPressed)
            paint.setColor(Color.argb(255, 20, 20, 60));
        else
            paint.setColor(Color.YELLOW);//Color.argb(255, 60, 60, 100)
        canvas.drawRect(Size, paint);

        paint.setColor(Color.argb(255, 11, 15, 45));
        paint.setStrokeWidth(7);
        canvas.drawLine(Size.left, Size.top, Size.left, Size.bottom, paint);
        canvas.drawLine(Size.right, Size.top, Size.right, Size.bottom, paint);
        canvas.drawLine(Size.left, Size.top, Size.right, Size.top, paint);
        canvas.drawLine(Size.left, Size.bottom, Size.right, Size.bottom, paint);

        paint.setColor(Color.LTGRAY);
        paint.setTextSize(FontSize);

        // ширина текста
        float width = paint.measureText(Caption);
        canvas.drawText(Caption, Size.left + (Size.right - Size.left - width) / 2f, Size.top + (Size.bottom - Size.top) / 2f + FontSize / 2.5f, paint);
    }

}

public class Menu implements IGameState {

    IChangeState StateChanger;
    Button AgainButton = new Button("Еще раз");
    Button HarderButton = new Button("Сложнее");
    Button LighterButton = new Button("Легче");

    public Menu(IChangeState state) {
        StateChanger = state;
    }

    @Override
    public void FlingHandle(MotionEvent e1, MotionEvent e2) {

    }

    @Override
    public void TapHandle(MotionEvent e) {

        if (AgainButton.IsPressed)
            StateChanger.ChangeState(Maze.class.getName(), new String[]{"0", "0"});
        if (LighterButton.IsPressed)
            StateChanger.ChangeState(Maze.class.getName(), new String[]{"-2", "-2"});
        if (HarderButton.IsPressed)
            StateChanger.ChangeState(Maze.class.getName(), new String[]{"2", "2"});

        AgainButton.TryRealise(e.getX(), e.getY() - 100);
        HarderButton.TryRealise(e.getX(), e.getY() - 100);
        LighterButton.TryRealise(e.getX(), e.getY() - 100);

        StateChanger.Invalidate();
    }

    @Override
    public void TapDownHandle(MotionEvent e) {
        AgainButton.TryPress(e.getX(), e.getY() - 100);
        HarderButton.TryPress(e.getX(), e.getY() - 100);
        LighterButton.TryPress(e.getX(), e.getY() - 100);

        StateChanger.Invalidate();
    }

    @Override
    public void DoubleTapHandle(MotionEvent e) {

    }

    private Paint mPaint = new Paint();

    @Override
    public void DrawHandle(Canvas canvas) {
        String HelloText = "Поздравляю!";
        String SubHelloText = "Вы прошли лабиринт";

        mPaint.setColor(Color.argb(255, 44, 36, 48));
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setTextSize(canvas.getWidth() / 10);
        // ширина текста
        float width = mPaint.measureText(HelloText);
        if (canvas.getHeight() > canvas.getWidth())
            canvas.drawText(HelloText, (canvas.getWidth() - width) / 2f, canvas.getHeight() / 4f, mPaint);
        mPaint.setTextSize(canvas.getWidth() / 20);
        width = mPaint.measureText(SubHelloText);
        if (canvas.getHeight() > canvas.getWidth())
            canvas.drawText(SubHelloText, (canvas.getWidth() - width) / 2f, canvas.getHeight() / 4f + canvas.getWidth() / 20 * 1.5f, mPaint);


        float buttonWidth = canvas.getWidth() / 1.5f;
        float buttonHeight = buttonWidth / 4f;
        if (canvas.getHeight() < canvas.getWidth()) {
            buttonWidth = canvas.getWidth() / 1.5f;
            buttonHeight = canvas.getHeight() / 7f;
        }


        AgainButton.Size = new RectF(
                (canvas.getWidth() - buttonWidth) / 2f,
                (canvas.getHeight() - buttonHeight * 4f) / 2,
                (canvas.getWidth() - buttonWidth) / 2f + buttonWidth,
                (canvas.getHeight() - buttonHeight * 4f) / 2 + buttonHeight);
        AgainButton.FontSize = canvas.getWidth() / 20;
        AgainButton.DrawButton(canvas, mPaint);

        HarderButton.Size = new RectF(
                (canvas.getWidth() - buttonWidth) / 2f,
                (canvas.getHeight() - buttonHeight * 4f) / 2 + buttonHeight + buttonHeight / 2f,
                (canvas.getWidth() - buttonWidth) / 2f + buttonWidth,
                (canvas.getHeight() - buttonHeight * 4f) / 2 + buttonHeight + buttonHeight + buttonHeight / 2f);
        HarderButton.FontSize = canvas.getWidth() / 20;
        HarderButton.DrawButton(canvas, mPaint);

        LighterButton.Size = new RectF(
                (canvas.getWidth() - buttonWidth) / 2f,
                (canvas.getHeight() - buttonHeight * 4f) / 2 + (buttonHeight + buttonHeight / 2f) * 2,
                (canvas.getWidth() - buttonWidth) / 2f + buttonWidth,
                (canvas.getHeight() - buttonHeight * 4f) / 2 + buttonHeight + (buttonHeight + buttonHeight / 2f) * 2);
        LighterButton.FontSize = canvas.getWidth() / 20;
        LighterButton.DrawButton(canvas, mPaint);

    }

    @Override
    public void ApplyArgs(String[] args) {

    }
}
