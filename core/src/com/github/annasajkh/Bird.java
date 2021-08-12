package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.annasajkh.libraries.NeuralNetwork;

public class Bird implements Comparable<Bird>
{
    public int score;
    Vector2 position;
    Vector2 velocity;
    Sprite sprite;
    NeuralNetwork brain;
    boolean alreadyOverlap = false;
    Pipe pipeOverlaps;
    Rectangle birdRect;
    Pipe closestPipe;

    public Bird(Vector2 position, NeuralNetwork brain)
    {
        this.position = position;
        this.brain = brain;
        velocity = new Vector2(0, 0);
        sprite = Core.bird;
        score = 0;
        birdRect = new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight());
    }

    public void fly()
    {
        velocity.y = 250 * Gdx.graphics.getDeltaTime();
        sprite.rotate(350 * Gdx.graphics.getDeltaTime());
    }

    public void update()
    {
        position.add(velocity);
        velocity.y += Core.gravity * Gdx.graphics.getDeltaTime();
        sprite.setPosition(position.x, position.y);
        sprite.rotate(velocity.y);
        sprite.setRotation(MathUtils.clamp(sprite.getRotation(), -45, 45));

        birdRect.x = position.x;
        birdRect.y = position.y;
        

        closestPipe = Core.pipes[0];
        
        for(Pipe pipe : Core.pipes)
        {
            if( Math.abs(position.x - pipe.pipe1Pos.x + pipe.pipe1Sprite.getWidth()) < 
                Math.abs(position.x - closestPipe.pipe1Pos.x + closestPipe.pipe1Sprite.getWidth()))
            {
                closestPipe = pipe;
            }
        }

        float[] result = brain.process(new float[]{ Math.abs(closestPipe.pipe1Pos.x - position.x) * 0.001f, 
                                                    Math.abs(closestPipe.pipe1Pos.y - position.y) * 0.001f,
                                                    Math.abs(position.y - (closestPipe.pipe1Pos.y - Core.pipeOpenWidth)) * 0.001f});
        if(result[0] > 0.5f)
        {
            fly();
        }
    }

    @Override
    public int compareTo(Bird other)
    {
        return Integer.compare(score, other.score);
    }

}
