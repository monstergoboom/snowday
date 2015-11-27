package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class NonPlayerCharacter extends Character {
    public NonPlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd) {
        super(assetName, scale, x, y, b2World, sd, (short)0x0008, (short)0x0000);
    }

    @Override
    void attack() {

    }

    @Override
    void run() {

    }

    @Override
    void walk(int direction) {

    }

    @Override
    void jump() {

    }

    @Override
    void doubleJump() {

    }

    @Override
    void idle() {

    }

    @Override
    void die() {

    }
}
