package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class NonPlayerCharacter extends Character {
    public NonPlayerCharacter(String assetName, float scale, int x, int y, World b2World) {
        super(assetName, scale, x, y, b2World);
    }

    @Override
    void attack() {

    }

    @Override
    void run() {

    }

    @Override
    void walk() {

    }

    @Override
    void jump() {

    }

    @Override
    void idle() {

    }

    @Override
    void die() {

    }
}
