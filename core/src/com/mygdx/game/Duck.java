package com.mygdx.game;

import static com.mygdx.game.MyGame.SCR_HEIGHT;
import static com.mygdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Duck {
    MyGame mgg;
    float x, y;
    float vx, vy;
    float width, height;
    int faza, nFaz = 12;
    boolean isAlive = true;
    boolean isKryak;
    boolean isFlip;

    public Duck(MyGame myGdxGame){
        mgg = myGdxGame;
        width = height = MathUtils.random(mgg.sizeMosquitos-30, mgg.sizeMosquitos+30);
        if (MathUtils.random(0,1) == 0){
            x = MathUtils.random(-SCR_WIDTH, -width);
            vx = MathUtils.random(5, 10);
        }
        else {
            x = MathUtils.random(SCR_WIDTH+width, SCR_WIDTH*2);
            vx = MathUtils.random(-10, -5);
        }
        y = MathUtils.random(SCR_HEIGHT/3, SCR_HEIGHT-height);
        vy = MathUtils.random(-0.5f, 0.5f);
        faza = MathUtils.random(0, nFaz-1);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            //outOfBounds2();
            isFlip = vx > 0;
            changePhase();
        }
        kryak();
    }

    void kryak(){
        if(isKryak) {
            isKryak = false;
        } else {
            if(MathUtils.random(500) == 1) isKryak = true;
        }
    }

    void outOfBounds1(){
        if(x<0 || x> SCR_WIDTH -width) vx = -vx;
        if(y<0 || y> SCR_HEIGHT -height) vy = -vy;
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

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            faza = nFaz;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}
