package com.mygdx.game;

import

public class Duck {
    MyGame mgg;
    Float x,y;
    float vx, vy;
    float width, height;
    int faza, nFaz = 10;
    boolean isAlive = true;

    public Duck(MyGame MyGame) {
        mgg = MyGame;
        width = height = MathUtils.random(mgg.sizeDuck - 30, mgg.sizeDuck + 30);
        x = SCR_WIDTH / 2f - width / 2;
        y = SCR_HEIGHT / 2f - height / 2;
        vx = MathUtils.random(-mgg.speedDuck, mgg.speedDuck);
        vy = MathUtils.random(-mgg.speedDuck, mgg.speedDuck);
        faza = MathUtils.random(0, nFaz - 1);
    }

    Void fly(){
    x += vx;
    y =+ vy;}
    if(isAlive)  {
        outOfBounds2();
        ChangePhase();
    }
}

void outOfBounds1(){
    if(x<0 || x>SCR_WIDTH -width) vx = -vx
    if(y<0 || y>SCR_HEIGHT -height) vy = -vy;
}

void outOfBounds2(){
 if(x<0-width) x = SCR_WIDTH;
        if(x> SCR_WIDTH) x = 0-width;
        if(y<0-height) y = SCR_HEIGHT;
        if(y> SCR_HEIGHT) y = 0-height;
}

    void changePhase(){
        if(++faza == nFaz) faza = 0;
        //faza = ++faza % nFaz;
    }

    boolean isFlip(){
        return vx>0;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            faza = 10;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}