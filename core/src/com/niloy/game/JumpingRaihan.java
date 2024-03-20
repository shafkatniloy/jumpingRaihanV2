package com.niloy.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class JumpingRaihan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture[] background;
	ShapeRenderer shapeRenderer;

	Texture[] raihans;
	int flapState=0;
	float raihanY;
	float velocity=0;
	Circle raihancircle;
	float gamestate=0;
	float gravity=2;
	Texture toptube;
	Texture bottomtube;
	float gap=480;
	float maxtubeoffset;
	Random randomg;

	float tubevelocity=4;
	int numberoftube=4;
	float tubex[]=new float[numberoftube];
	float[] tubeoffset = new float[numberoftube];
	float distancebetweentubes;
	Rectangle[] toptuberec;
	Rectangle[] bottomtuberec;
	int score=0,scoringtube=0;
	BitmapFont font;
	Texture gameover;

	int bgchange;
	Music go;
	Music flap;
	Music startmusic;
    Music playmusic;







	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture[2];
		background[0]= new Texture("bg0.png");
		background[1]= new Texture("bg.png");
		bgchange=0;
		raihans = new Texture[4];
		raihans[0]=new Texture("r1.png");
		raihans[1]=new Texture("r11.png");
		raihans[2]=new Texture("r2.png");
		raihans[3]=new Texture("r22.png");

		toptube = new Texture("toptube.png");
		bottomtube= new Texture("bottomtube.png");
		maxtubeoffset=bottomtube.getHeight()/2;
		randomg= new Random();

		distancebetweentubes=Gdx.graphics.getWidth()* 3/4;

		raihancircle=new Circle();
		shapeRenderer=new ShapeRenderer();

		toptuberec= new Rectangle[numberoftube];
		bottomtuberec=new Rectangle[numberoftube];

		score=0;
		scoringtube=0;
		font=new BitmapFont();
		font.setColor(Color.RED);
		font.getData().setScale(10);

		// music
		 go=Gdx.audio.newMusic(Gdx.files.internal("go.mp3"));
		go.setVolume(.8f);
		go.setLooping(false);

		startmusic=Gdx.audio.newMusic(Gdx.files.internal("startm.mp3"));
		startmusic.setVolume(.5f);

        playmusic=Gdx.audio.newMusic(Gdx.files.internal("play.mp3"));
        startmusic.setVolume(.4f);

		flap=Gdx.audio.newMusic(Gdx.files.internal("flap.mp3"));
		flap.setVolume(1f);




		gameover=new Texture("gameover2.png");
		startgame();


	}

	public void startgame()
	{
		raihanY=Gdx.graphics.getHeight()/2-raihans[flapState].getHeight()/2;
		for(int i=0;i<numberoftube;i++)
		{
			tubeoffset[i] = (randomg.nextFloat()-0.5f)*maxtubeoffset;
			tubex[i] = Gdx.graphics.getWidth()/2-toptube.getWidth()/2+ Gdx.graphics.getWidth()  +i * distancebetweentubes;

			toptuberec[i]= new Rectangle();
			bottomtuberec[i]=new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();

		batch.draw(background[bgchange],0,0 , Gdx.graphics.getWidth() , Gdx.graphics.getHeight() );
		if (gamestate==0)
		{
			startmusic.play();
		}
		else startmusic.stop();

		if (gamestate==1)
		{
			playmusic.play();
		}
		else playmusic.stop();

		if (Gdx.input.justTouched() && gamestate==0)
		{
			bgchange=1;
		}

		if (gamestate==1) {


			if(tubex[scoringtube] < Gdx.graphics.getWidth()/2)
			{
				Gdx.app.log("score", String.valueOf(score));
				score++;
				if(scoringtube < numberoftube-1) scoringtube++;
				else scoringtube=0;
			}


			for(int i=0;i<numberoftube;i++) {
				if(tubex[i] < -toptube.getWidth())
				{
					tubex[i] += numberoftube * distancebetweentubes;
					tubeoffset[i] = (randomg.nextFloat()-0.5f)*maxtubeoffset;
				}
				else {
					tubex[i] -= tubevelocity;

				}

				batch.draw(toptube, tubex[i], Gdx.graphics.getWidth() / 2 + gap / 2 + bottomtube.getHeight() / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubex[i], Gdx.graphics.getWidth() / 2 - gap / 2 - bottomtube.getHeight() / 2 + tubeoffset[i]);
				toptuberec[i]=new Rectangle(tubex[i],Gdx.graphics.getWidth() / 2 + gap / 2 + bottomtube.getHeight() / 2 + tubeoffset[i], toptube.getWidth(),toptube.getHeight());
				bottomtuberec[i]=new Rectangle(tubex[i],Gdx.graphics.getWidth() / 2 - gap / 2 - bottomtube.getHeight() / 2 + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			}

			if (Gdx.input.justTouched()) {
				gamestate = 1;
				velocity=-30;
				bgchange=1;
				flap.play();
				if(flapState==0) flapState=1;
				else flapState=0;

			}

if(raihanY>0 ){
	//gravity=2;
	velocity+=gravity;
	raihanY-=velocity;
}
else {
	gamestate=2;
}


		}
		else if(gamestate==0){
			if (Gdx.input.justTouched()) {
				gamestate = 1;}
		}
		else if(gamestate==2)
		{
			//music
			go.play();
			go.setLooping(false);

			batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
			if(Gdx.input.justTouched())
			{
				go.stop();
				bgchange=0;
				gamestate = 0;
				startgame();
				score = 0;
				scoringtube = 0;
				velocity = 0;

			}
		}





		batch.draw(raihans[flapState], Gdx.graphics.getWidth()/2-raihans[flapState].getWidth()/2, raihanY);
		font.draw(batch, String.valueOf(score),100,200);
		batch.end();

		raihancircle.set(Gdx.graphics.getWidth()/2 , raihanY+raihans[flapState].getHeight()/2 , raihans[flapState].getWidth()/2 - 50);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.CLEAR);
//		shapeRenderer.circle(raihancircle.x , raihancircle.y , raihancircle.radius);
		for(int i=0;i<numberoftube;i++)
		{
			//shapeRenderer.rect(tubex[i],Gdx.graphics.getWidth() / 2 + gap / 2 + bottomtube.getHeight() / 2 + tubeoffset[i], toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubex[i],Gdx.graphics.getWidth() / 2 - gap / 2 - bottomtube.getHeight() / 2 + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			if(Intersector.overlaps(raihancircle, toptuberec[i]) || Intersector.overlaps(raihancircle,bottomtuberec[i]))
			{
				gamestate=2;
			}
		}

//		shapeRenderer.end();

	}
	@Override
	public void dispose () {
		batch.dispose();
		background[1].dispose();
	}
}
