package com.github.annasajkh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.annasajkh.libraries.Matrix;
import com.github.annasajkh.libraries.NeuralNetwork;

public class Bird
{
    double score = 0;
    Vector2 position;
    Vector2 velocity;
    Sprite sprite;
    NeuralNetwork brain;

    public Bird(Vector2 position, Sprite sprite, NeuralNetwork brain)
    {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.sprite = sprite;
        this.brain = brain;
        brain.setActivationFunction(new NeuralNetwork.ActivationFunction(Matrix.SIGMOID, Matrix.SIGMOID_DERIVATIVE));
        brain.setLearningRate(0.1);
    }

    public void fly()
    {
        velocity.y = 250 * Gdx.graphics.getDeltaTime();
        sprite.rotate(350 * Gdx.graphics.getDeltaTime());
    }


    public void update()
    {
        position.add(velocity);
        velocity.y += Game.gravity * Gdx.graphics.getDeltaTime();
        sprite.setPosition(position.x, position.y);
        sprite.rotate(velocity.y);
        sprite.setRotation(MathUtils.clamp(sprite.getRotation(), -45, 45));
    }


}
