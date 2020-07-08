package com.ayushmalla.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.awt.Label;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
//	ShapeRenderer shapeRenderer;
	BitmapFont font;

	Texture gameover;

	Texture[] birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;

	int gameState = 0;
	float gravity = 1;

	Texture topTube;
	Texture bottomTube;

	//making game harder creating tubes gap
	float gap = 950;

	float maxTubeOffSet;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffSet = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
//		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		gameover = new Texture("gameOver.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffSet = Gdx.graphics.getHeight() /2 - gap /2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		startGame();


	}
	public void startGame(){
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() /2 ;

        for(int i = 0; i < numberOfTubes; i++){

            tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            //tubes gonna be half a distance away
            tubeX[i] = Gdx.graphics.getWidth() /2 - topTube.getWidth() /2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
            topTubeRectangle[i] = new Rectangle();
            bottomTubeRectangle[i] = new Rectangle();
        }
    }

	@Override
	public void render () {
        //drawing background first so that other doesnot get overlaped
		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//when we play game
        if (gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;
				if(scoringTube < numberOfTubes - 1){
					scoringTube++;

				}else{
					scoringTube = 0;
				}
			}
			if (Gdx.input.justTouched()) {
                velocity = -18;

            }
			for(int i = 0; i < numberOfTubes; i++) {
				if(tubeX[i]  <- topTube.getWidth()){

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					//tube goes outside the screen moving right
					tubeOffSet[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],208, 1900  );
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffSet[i],
						208, 1900 );

				topTubeRectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],
                        208, 1900);
				bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight()/2 -  gap / 2 - bottomTube.getHeight() + tubeOffSet[i],
						208, 1900);

			}

			if(birdY > 0 ) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }else{
			    gameState = 2;
            }
        } else if(gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }

        }else if(gameState == 2){

            //when game is over
            batch.draw(gameover, Gdx.graphics.getWidth() /2 - gameover.getWidth() ,
                        Gdx.graphics.getHeight() /2 - gameover.getHeight() /2, 700, 400);

			font.draw(batch, String.valueOf(score), 500, 1160);


            if (Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}
        }


        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

       	batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 500, 2060);

		birdCircle.set(Gdx.graphics.getWidth() /2, birdY + birds[flapState].getHeight() /2, birds[flapState].getWidth() /2 );


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x , birdCircle.y, birdCircle.radius);

        for(int i = 0 ; i < numberOfTubes; i++){
//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],
//					topTube.getWidth(),topTube.getHeight());
//			shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight()/2 -  gap / 2 - bottomTube.getHeight() + tubeOffSet[i],
//					bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangle[i])){
					gameState = 2;
			}
		}
		batch.end();

//        shapeRenderer.end();
    }
}
