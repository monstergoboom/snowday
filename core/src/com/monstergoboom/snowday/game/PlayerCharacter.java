package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd) {
        super(assetName, scale, x, y, b2World, sd);
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
