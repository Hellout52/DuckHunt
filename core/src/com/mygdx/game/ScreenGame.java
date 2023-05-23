package com.mygdx.game;

import static com.mygdx.game.MyGame.SCR_HEIGHT;
import static com.mygdx.game.MyGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class ScreenGame implements Screen {
	MyGame mgg;

	Texture imgDuckAtlas;
	TextureRegion[] imgDuck = new TextureRegion[13]; // ссылка на текстуры (картинки)
	Texture imgBackGround; // фон
	Texture imgBtnMenu;

	Sound[] sndDuck = new Sound[4];
	Sound sndShot;
	Music sndMusic;

	// создание массива ссылок на объекты
	ArrayList<Duck> ducks = new ArrayList<>();
	int kills;
	long timeStart, timeCurrent;
	long timeSpawnDuck, timeSpawnDuckInterval = 500;

	// состояние игры
	public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
	int gameState;

	Player[] players = new Player[5];

	DuckButton btnRestart, btnExit;
	DuckButton btnMenu;


	public ScreenGame (MyGame myGdxGame) {
		mgg = myGdxGame;

		// создаём объекты изображений
		imgDuckAtlas = new Texture ("duck_atlas.png");
		for(int i=0; i<imgDuck.length; i++){
			imgDuck[i] = new TextureRegion(imgDuckAtlas, i*400, 0, 400, 400);
		}
		imgBackGround = new Texture("backgrounds/bg_intro.png");
		imgBtnMenu = new Texture("menu.png");

		// создаём объекты звуков
		for(int i = 0; i< sndDuck.length; i++) {
			sndDuck[i] = Gdx.audio.newSound(Gdx.files.internal("sound/duck"+i+".mp3"));
		}
		sndShot = Gdx.audio.newSound(Gdx.files.internal("sound/huntergun2.mp3"));
		sndMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/soundcrazymosquitos.mp3"));
		sndMusic.setLooping(true);
		sndMusic.setVolume(0.2f);
		if(mgg.musicOn) sndMusic.play();

		// создаём объекты игроков для таблицы рекордов
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player("Noname", 0);
		}
		loadTableOfRecords();

		// создаём кнопки
		btnRestart = new DuckButton(mgg.font, "RESTART", 450, 200);
		btnExit = new DuckButton(mgg.font, "EXIT", 750, 200);
		btnMenu = new DuckButton(SCR_WIDTH-60, SCR_HEIGHT-60, 50, 50);
	}

	@Override
	public void show() {
		gameStart();
	}

	@Override
	public void render(float delta) {// повторяется с частотой 60 fps
		// касания экрана/клики мышью
		if(Gdx.input.justTouched()) {
			mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			mgg.camera.unproject(mgg.touch);
			if(gameState == PLAY_GAME) {
				if(mgg.soundOn) sndShot.play();
				for (int i = ducks.size() - 1; i >= 0; i--) {
					if (ducks.get(i).isAlive) {
						if (ducks.get(i).hit(mgg.touch.x, mgg.touch.y)) {
							kills++;
							if (kills == ducks.size()) {
								gameState = ENTER_NAME;
							}
							break;
						}
					}
				}
			}
			if(gameState == SHOW_TABLE){
				if(btnExit.hit(mgg.touch.x, mgg.touch.y)) {
					mgg.setScreen(mgg.screenIntro);
				}
				if(btnRestart.hit(mgg.touch.x, mgg.touch.y)) {
					gameStart();
				}
			}
			if(gameState == ENTER_NAME){
				if(mgg.keyboard.endOfEdit(mgg.touch.x, mgg.touch.y)){
					gameOver();
				}
			}
			if(btnMenu.hit(mgg.touch.x, mgg.touch.y)){
				mgg.setScreen(mgg.screenIntro);
			}
		}

		// события игры
		spawnDuck();
		for (int i = 0; i < ducks.size(); i++) {
			ducks.get(i).fly();
			if(mgg.soundOn)
				if(ducks.get(i).isKryak)
					sndDuck[MathUtils.random(0,1)].play();
		}
		if(gameState == PLAY_GAME) {
			timeCurrent = TimeUtils.millis() - timeStart;
			if(timeCurrent > 10000){
				gameState = ENTER_NAME;
			}
		}

		// отрисовка всего
		mgg.camera.update();
		mgg.batch.setProjectionMatrix(mgg.camera.combined);
		mgg.batch.begin();
		mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
		for(int i = 0; i< ducks.size(); i++) {
			mgg.batch.draw(imgDuck[ducks.get(i).faza], ducks.get(i).x, ducks.get(i).y,
					ducks.get(i).width/2, ducks.get(i).height/2, ducks.get(i).width, ducks.get(i).height,
					ducks.get(i).isFlip?-1:1, 1, 0);
		}
		mgg.font.draw(mgg.batch, "Duck KILLED: "+kills, 10, SCR_HEIGHT-10);
		mgg.font.draw(mgg.batch, "TIME: "+timeToString(timeCurrent), SCR_WIDTH-500, SCR_HEIGHT-10);
		if(gameState == SHOW_TABLE) {
			mgg.fontLarge.draw(mgg.batch,"Game Over", 0, 600, SCR_WIDTH, Align.center, true);
			for (int i = 0; i < players.length; i++) {
				String s = players[i].name + "......." + players[i].kills;
				mgg.font.draw(mgg.batch, s, 0, 500-i*50, SCR_WIDTH, Align.center, true);
			}
			mgg.font.draw(mgg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
			mgg.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
		}
		if(gameState == ENTER_NAME){
			mgg.keyboard.draw(mgg.batch);
		}
		mgg.batch.draw(imgBtnMenu, btnMenu.x, btnMenu.y, btnMenu.width, btnMenu.height);
		mgg.batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		imgDuckAtlas.dispose();
		for (int i = 0; i < sndDuck.length; i++) {
			sndDuck[i].dispose();
		}
		sndMusic.dispose();
		imgBackGround.dispose();
	}

	String timeToString(long time){
		String min = "" + time/1000/60/10 + time/1000/60%10;
		String sec = "" + time/1000%60/10 + time/1000%60%10;
		return min+":"+sec;
	}

	void gameStart(){
		kills = 0;
		ducks.clear();
		gameState = PLAY_GAME;
		timeStart = TimeUtils.millis();
	}

	void gameOver(){
		gameState = SHOW_TABLE;
		players[players.length-1].name = mgg.keyboard.getText();
		players[players.length-1].kills = kills;
		sortTableOfRecords();
		saveTableOfRecords();
	}

	void saveTableOfRecords(){
		Preferences prefs = Gdx.app.getPreferences("Table Of Records");
		for (int i = 0; i < players.length; i++) {
			prefs.putString("name"+i, players[i].name);
			prefs.putInteger("kills"+i, players[i].kills);
		}
		prefs.flush();
	}

	void loadTableOfRecords(){
		Preferences prefs = Gdx.app.getPreferences("Table Of Records");
		for (int i = 0; i < players.length; i++) {
			if(prefs.contains("name"+i)) players[i].name = prefs.getString("name"+i);
			if(prefs.contains("kills"+i)) players[i].kills = prefs.getInteger("kills"+i);
		}
	}

	void sortTableOfRecords(){

		for (int j = 0; j < players.length-1; j++) {
			for (int i = 0; i < players.length-1; i++) {
				if(players[i].kills<players[i+1].kills){
					int c = players[i].kills;
					players[i].kills = players[i+1].kills;
					players[i+1].kills = c;
					String s = players[i].name;
					players[i].name = players[i+1].name;
					players[i+1].name = s;
					/* Player c = players[i];
					players[i] = players[i+1];
					players[i+1] = c; */
				}
			}
		}
	}

	void clearTableOfRecords(){
		for (int i = 0; i < players.length; i++) {
			players[i].name = "Noname";
			players[i].kills = 0;
		}
	}
	void spawnDuck(){
		if (timeSpawnDuck + timeSpawnDuckInterval < TimeUtils.millis()){
			ducks.add(new Duck(mgg));
			timeSpawnDuck = TimeUtils.millis();
		}
	}
}
