package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;

public class MyGame extends Game {
	public static final int SCR_WIDTH = 1280;
	public static final int SCR_HEIGHT = 720;

	SpriteBatch batch; // ссылка на объект, отвечающий за вывод изображений
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font, fontLarge;
	InputKeyboard keyboard;

	ScreenIntro screenIntro;
	ScreenGame screenGame;
	ScreenSettings screenSettings;
	ScreenAbout screenAbout;

	boolean soundOn = true;
	boolean musicOn = true;
	public static final int MODE_EASY = 0, MODE_NORMAL = 1, MODE_HARD = 2;
	int modeOfGame = MODE_EASY; // сложность игры
	int numMosquitos = 5;
	float sizeMosquitos = 150;
	float speedMosquitos = 5;

	@Override
	public void create () {
		batch = new SpriteBatch(); // создаём объект, отвечающий за вывод изображений
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		createFont();
		keyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 10);

		screenIntro = new ScreenIntro(this);
		screenGame = new ScreenGame(this);
		screenSettings = new ScreenSettings(this);
		screenAbout = new ScreenAbout(this);

		setScreen(screenIntro);
	}

	void createFont(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("mp_ducky.ttf"));
		//FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter.size = 50;
		parameter.color = Color.CHARTREUSE;
		parameter.borderWidth = 2;
		parameter.borderColor = Color.FOREST;
		font = generator.generateFont(parameter);

		parameter.size = 70;
		fontLarge = generator.generateFont(parameter);

		generator.dispose();
	}

	@Override
	public void dispose () {
		batch.dispose();
		keyboard.dispose();
	}
}
