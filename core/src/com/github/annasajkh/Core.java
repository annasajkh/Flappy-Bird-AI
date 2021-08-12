package com.github.annasajkh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.annasajkh.libraries.NeuralNetwork;

public class Core extends ApplicationAdapter
{
    static SpriteBatch spriteBatch;
    static Texture birdT;
    static Sprite bird;
    static Texture pipeT;
    static Sprite pipeUp;
    static Sprite pipeDown;
    static Texture backgroundT;
    static Sprite background;
    static double pipeSpeed = -50;
    static float gravity = -15;
    static float pipeOpenWidth = 100;
    static boolean running = true;
    static List<Bird> birds;
    static List<Bird> birdsDead;
    static Pipe[] pipes;
    static BitmapFont font;
    static int score = 0;
    static int populationSize = 1000;
    static int bestSize = (int)(populationSize * 0.4f);
    static int generation = 0;

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
        birdsDead = new ArrayList<>();
        font = new BitmapFont();

        for(int i = 0; i < populationSize; i++)
        {
            birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), new NeuralNetwork(3, 5, 1, 2)));
        }

        pipes = new Pipe[3];
        for(int i = 0; i < pipes.length; i++)
        {
            pipes[i] = new Pipe(new Vector2(Gdx.graphics.getWidth() + (i * 208), MathUtils.random(pipeOpenWidth, Gdx.graphics.getHeight() - pipeOpenWidth)), pipeDown, pipeUp);
        }

        spriteBatch = new SpriteBatch();
        pipeDown.flip(false, true);
    }

    @Override
    public void render()
    {

        if(birds.isEmpty())
        {
            score = 0;
            running = false;
        }
        
        ScreenUtils.clear(0,0,0,1);
        
        if(running)
        {

            spriteBatch.begin();

            background.draw(spriteBatch);
            for(Pipe pipe : pipes)
            {
                pipe.update();
                
                pipe.pipe1Sprite.draw(spriteBatch);
                pipe.pipe2Sprite.draw(spriteBatch);
            }
            for(Bird bird : birds)
            {
                bird.update();
                bird.sprite.draw(spriteBatch);
                
                if(bird.score > score)
                {
                    score = bird.score;
                }
            }
            font.draw(spriteBatch, "Score : " + score, 10, Gdx.graphics.getHeight() - 10);
            font.draw(spriteBatch, "Generation : " + generation, 10, Gdx.graphics.getHeight() - 30);

            spriteBatch.end();
        }

        if(!running)
        {
            Collections.sort(birdsDead);
            
            Random random = new Random();
            
            List<Bird> bestBirds = birdsDead.subList(birdsDead.size() - bestSize,birdsDead.size());
            List<Bird> bestBirdsBreedAndMutates = new ArrayList<>(bestBirds);

            for(Bird bestBirdsBreedAndMutate : bestBirdsBreedAndMutates)
            {
                Bird otherBirds = bestBirdsBreedAndMutates.get(random.nextInt(bestBirdsBreedAndMutates.size()));
                
                bestBirdsBreedAndMutate.brain.crossover(otherBirds.brain);
                bestBirdsBreedAndMutate.brain.mutate(0.1f);
            }
            
            bestBirds.addAll(bestBirdsBreedAndMutates);
            
            for(int i = 0; i < bestSize / 2; i++)
            {
                bestBirds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), new NeuralNetwork(3, 5, 1, 2)));
            }
            
            
            for(Bird bird : bestBirds)
            {
                birds.add(new Bird(new Vector2(Gdx.graphics.getWidth() / 2f, MathUtils.random(50, 550)), bird.brain));
            }
            
            birdsDead.clear();
            
            for(int i = 0; i < pipes.length; i++)
            {
                pipes[i] = new Pipe(new Vector2(Gdx.graphics.getWidth() + (i * 208), MathUtils.random(pipeOpenWidth, Gdx.graphics.getHeight() - pipeOpenWidth)), pipeDown, pipeUp);
            }
            
            running = true;
            generation++;
        }

    }

    @Override
    public void dispose()
    {
        spriteBatch.dispose();
    }
}
