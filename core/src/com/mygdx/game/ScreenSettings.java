package com.mygdx.game;

import java.awt.TextArea;

public class ScreenSettings implements Screen{
    MyGame mgg;

    Texture imgBackGround;
    DuckButton  MosquitoButton btnMode, btnSound, btnMusic, btnClearRecords, btnBack;

    public ScreenSettings(MyGame myGame){
        mgg = myGame;
        imgBackGround = new Texture("background/bg_settings.png");
        btnMode = new DuckTexture(mgg.fontLarge, "Mode: Easy",














@Override
publick void render