package com.example.mobileapp1;

public class Player
{
    public Cell Location;

    public boolean move(sides side)
    {
        switch (side)
        {
            case UP:
                {
                    if(Location.Top!=null)
                    if(!Location.Top.IsFilled)
                    {
                        Location = Location.Top;
                        return true;
                    }
                    return false;
                }
            case DOWN:
                {
                    if(Location.Bottom!=null)
                        if(!Location.Bottom.IsFilled)
                    {
                        Location = Location.Bottom;
                        return true;
                    }
                    return false;
                }
            case LEFT:
                {
                    if(Location.Left!=null)
                        if(!Location.Left.IsFilled)
                    {
                        Location = Location.Left;
                        return true;
                    }
                    return false;
                }
            case RIGHT:
                {
                    if(Location.Right!=null)
                        if(!Location.Right.IsFilled)
                    {
                        Location = Location.Right;
                        return true;
                    }
                    return false;
                }
        }
        return false;
    }

    public Player(Cell startPoint)
    {
        Location = startPoint;
    }
    //==========================
    public enum sides
    {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}
