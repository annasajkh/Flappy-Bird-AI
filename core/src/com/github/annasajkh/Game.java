package com.github.annasajkh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.annasajkh.libraries.NeuralNetwork;

import java.util.ArrayList;

public class Game extends ApplicationAdapter
{
    SpriteBatch spriteBatch;
    Texture birdT;
    Sprite bird;
    Texture pipeT;
    Sprite pipeUp;
    Sprite pipeDown;
    Texture backgroundT;
    Sprite background;
    static double pipeSpeed = -50;
    static float gravity = -15;
    static float pipeOpenWidth = 100;
    static boolean running = true;
    static NeuralNetwork bestBirdBrain;
    static ArrayList<Bird> birds;
    static Bird bestBird;
    static Pipe[] pipes;
    static BitmapFont font;
    static int score = 0;

    @Override
    public void create()
    {
        birdT = new Texture("bird.png");
        bird = new Sprite(birdT);
        pipeT = new Texture("pipe.png");
        pipeUp = new Sprite(pipeT);
        pipeUp.setSize(pipeUp.getWidth(), Gdx.graphics.getHeight());
        pipeDown = new Sprite(pipeT);
        pipeDown.setSize(pipeUp.getWidth(), Gdx.graphics.getHeight());
        backgroundT = new Texture("background.png");
        background = new Sprite(backgroundT);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        birds = new ArrayList<>();
        font = new BitmapFont();

        for (int i = 0; i < 400; i++)
        {
            birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), bird,
                    new NeuralNetwork(4, 4, 1)));
        }

        pipes = new Pipe[3];
        for (int i = 0; i < pipes.length; i++)
        {
            pipes[i] = new Pipe(new Vector2(Gdx.graphics.getWidth() + (i * 208),
                    MathUtils.random(pipeOpenWidth, Gdx.graphics.getHeight() - pipeOpenWidth)), pipeDown, pipeUp);
        }

        spriteBatch = new SpriteBatch();
        pipeDown.flip(false, true);
    }

    @Override
    public void render()
    {

        if (Game.birds.isEmpty())
        {
            score = 0;
            running = false;
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        background.draw(spriteBatch);
        if (running)
        {
            for (Pipe pipe : pipes)
            {
                pipe.update();
                pipe.pipe1Sprite.draw(spriteBatch);
                pipe.pipe2Sprite.draw(spriteBatch);
            }
            for (Bird bird : birds)
            {
                bird.update();
                bird.sprite.draw(spriteBatch);
                if (bird.score > score)
                {
                    score = (int) bird.score;
                }
            }
            font.draw(spriteBatch, "Score : " + score, 10, Gdx.graphics.getHeight() - 10);
        }

        if (!running)
        {
            bestBirdBrain = bestBird.brain.clone();
            for (int i = 0; i < 400; i++)
            {
                if (MathUtils.randomBoolean())
                {
                    NeuralNetwork mutateWeights = bestBirdBrain.clone().mutateWeights(0.01);
                    birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), bird,
                            mutateWeights));
                    NeuralNetwork mutateBiases = bestBirdBrain.clone().mutateBiases(0.01);
                    birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), bird,
                            mutateBiases));
                }
                else
                {
                    birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), bird,
                            new NeuralNetwork(4, 4, 1)));
                }
            }
            for (int i = 0; i < pipes.length; i++)
            {
                pipes[i] = new Pipe(new Vector2(Gdx.graphics.getWidth() + (i * 208),
                        MathUtils.random(pipeOpenWidth, Gdx.graphics.getHeight() - pipeOpenWidth)), pipeDown, pipeUp);
            }
            running = true;
        }


        spriteBatch.end();
    }

    @Override
    public void dispose()
    {
        spriteBatch.dispose();
    }
}
