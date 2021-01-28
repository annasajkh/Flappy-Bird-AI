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

    public Pipe(Vector2 position, Sprite pipe1Sprite, Sprite pipe2Sprite)
    {
        this.pipe1Pos = position;
        pipe2Pos = new Vector2(pipe1Pos.x, pipe1Pos.y - pipe2Sprite.getHeight() - Game.pipeOpenWidth);
        scoreAdder =
                new Rectangle(pipe1Pos.x, pipe1Pos.y - Game.pipeOpenWidth, pipe1Sprite.getWidth(), Game.pipeOpenWidth);
        this.pipe1Sprite = pipe1Sprite;
        this.pipe2Sprite = pipe2Sprite;
        minX = -pipe1Sprite.getWidth() - 52;
        maxX = Gdx.graphics.getWidth() + 104;

    }

    public void update()
    {
        pipe1Pos.x += Game.pipeSpeed * Gdx.graphics.getDeltaTime();
        pipe2Pos.x += Game.pipeSpeed * Gdx.graphics.getDeltaTime();
        scoreAdder.setX((float) (scoreAdder.getX() + Game.pipeSpeed * Gdx.graphics.getDeltaTime()));

        pipe1Sprite.setPosition(pipe1Pos.x, pipe1Pos.y);
        pipe2Sprite.setPosition(pipe2Pos.x, pipe2Pos.y);

        if (pipe1Pos.x < minX)
        {
            pipe1Pos.x = maxX;
            pipe2Pos.x = maxX;
            pipe1Pos.y = MathUtils.random(Game.pipeOpenWidth, Gdx.graphics.getHeight() - Game.pipeOpenWidth);
            pipe2Pos.y = pipe1Pos.y - pipe2Sprite.getHeight() - Game.pipeOpenWidth;
            scoreAdder.setPosition(pipe1Pos.x, pipe1Pos.y - Game.pipeOpenWidth);
        }
        for (int i = Game.birds.size() - 1; i >= 0; i--)
        {
            Bird bird = Game.birds.get(i);

            Rectangle pipe1Rect =
                    new Rectangle(pipe1Pos.x, pipe1Pos.y, pipe1Sprite.getWidth(), pipe1Sprite.getHeight());

            Rectangle pipe2Rect =
                    new Rectangle(pipe2Pos.x, pipe2Pos.y, pipe2Sprite.getWidth(), pipe2Sprite.getHeight());
            Rectangle sprite =
                    new Rectangle(bird.position.x, bird.position.y, bird.sprite.getWidth(), bird.sprite.getHeight());
            if (sprite.overlaps(scoreAdder))
            {
                bird.score += 0.025;
            }

            if (Game.birds.size() == 1)
            {
                if (Game.bestBird == null)
                {
                    Game.bestBird = Game.birds.get(0);
                }
                else if (Game.birds.get(0).score > Game.bestBird.score)
                {
                    Game.bestBird = Game.birds.get(0);
                }
            }

            if (sprite.overlaps(pipe1Rect) || sprite.overlaps(pipe2Rect))
            {
                Game.birds.remove(bird);
                continue;
            }
            if (bird.position.y > Gdx.graphics.getHeight() - bird.sprite.getHeight() / 2)
            {
                if (Game.birds.size() == 1)
                {
                    Game.bestBirdBrain = Game.birds.get(0).brain.clone();
                }
                Game.birds.remove(bird);
                continue;
            }
            if (bird.position.y < bird.sprite.getHeight() / 2)
            {
                if (Game.birds.size() == 1)
                {
                    Game.bestBirdBrain = Game.birds.get(0).brain.clone();
                }
                Game.birds.remove(bird);
                continue;
            }

            if (pipe1Pos.x < bird.position.x + 170 && bird.position.x < pipe1Pos.x + pipe1Sprite.getWidth())
            {
                double[] result = bird.brain.process(new double[]{Math.abs(pipe1Pos.x - bird.position.x) * 0.001,
                        Math.abs(pipe1Pos.y - bird.position.y) * 0.001,
                        Math.abs(bird.position.y - (pipe1Pos.y - Game.pipeOpenWidth)) * 0.001,
                        bird.velocity.y * 0.001});
                if (result[0] > 0.5)
                {
                    bird.fly();
                }
            }
        }

    }

}
