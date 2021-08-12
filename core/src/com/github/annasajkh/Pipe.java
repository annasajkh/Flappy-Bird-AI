package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Pipe
{
    Vector2 pipe1Pos;
    Vector2 pipe2Pos;
    Rectangle scoreAdder;
    Sprite pipe1Sprite;
    Sprite pipe2Sprite;
    float minX;
    float maxX;
    Rectangle pipe1Rect;
    Rectangle pipe2Rect;

    public Pipe(Vector2 position, Sprite pipe1Sprite, Sprite pipe2Sprite)
    {
        this.pipe1Pos = position;
        pipe2Pos = new Vector2(pipe1Pos.x, pipe1Pos.y - pipe2Sprite.getHeight() - Core.pipeOpenWidth);
        scoreAdder = new Rectangle(pipe1Pos.x, pipe1Pos.y - Core.pipeOpenWidth, pipe1Sprite.getWidth(), Core.pipeOpenWidth);
        
        this.pipe1Sprite = pipe1Sprite;
        this.pipe2Sprite = pipe2Sprite;
        
        minX = -pipe1Sprite.getWidth() - 52;
        maxX = Gdx.graphics.getWidth() + 104;
        
        pipe1Rect = new Rectangle(pipe1Pos.x, pipe1Pos.y, pipe1Sprite.getWidth(), pipe1Sprite.getHeight());
        pipe2Rect = new Rectangle(pipe2Pos.x, pipe2Pos.y, pipe2Sprite.getWidth(), pipe2Sprite.getHeight());
    }

    public void update()
    {
        pipe1Pos.x += Core.pipeSpeed * Gdx.graphics.getDeltaTime();
        pipe2Pos.x += Core.pipeSpeed * Gdx.graphics.getDeltaTime();
        scoreAdder.setX((float) (scoreAdder.getX() + Core.pipeSpeed * Gdx.graphics.getDeltaTime()));

        pipe1Sprite.setPosition(pipe1Pos.x, pipe1Pos.y);
        pipe2Sprite.setPosition(pipe2Pos.x, pipe2Pos.y);

        if(pipe1Pos.x < minX)
        {
            pipe1Pos.x = maxX;
            pipe2Pos.x = maxX;
            
            pipe1Pos.y = MathUtils.random(Core.pipeOpenWidth, Gdx.graphics.getHeight() - Core.pipeOpenWidth);
            pipe2Pos.y = pipe1Pos.y - pipe2Sprite.getHeight() - Core.pipeOpenWidth;
            
            scoreAdder.setPosition(pipe1Pos.x, pipe1Pos.y - Core.pipeOpenWidth);
        }
        
        for(int i = Core.birds.size() - 1; i >= 0; i--)
        {
            Bird bird = Core.birds.get(i);

            if(bird.birdRect.overlaps(scoreAdder) && !bird.alreadyOverlap)
            {
                bird.score += 1;
                bird.alreadyOverlap = true;
                bird.pipeOverlaps = this;
            }
            
            if(!bird.birdRect.overlaps(scoreAdder) && bird.pipeOverlaps == this)
            {
                bird.alreadyOverlap = false;
            }

            if(bird.birdRect.overlaps(pipe1Rect) || bird.birdRect.overlaps(pipe2Rect))
            {
                Core.birdsDead.add(Core.birds.remove(Core.birds.indexOf(bird)));
                continue;
            }
            if(bird.position.y > Gdx.graphics.getHeight() - bird.sprite.getHeight() / 2)
            {
                Core.birdsDead.add(Core.birds.remove(Core.birds.indexOf(bird)));
                continue;
            }
            if(bird.position.y < bird.sprite.getHeight() / 2)
            {
                Core.birdsDead.add(Core.birds.remove(Core.birds.indexOf(bird)));
                continue;
            }
            

        }
        
        
        pipe1Rect.x = pipe1Pos.x;
        pipe1Rect.y = pipe1Pos.y;

        pipe2Rect.x = pipe2Pos.x;
        pipe2Rect.y = pipe2Pos.y;

    }

}
