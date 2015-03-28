package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd) {
        super(assetName, scale, x, y, b2World, sd, 0x0002, 0xffff & ~0x0004);
    }

    @Override
    void attack() {

    }

    @Override
    void run() {

    }

    @Override
    void walk(int direction) {
        Gdx.app.log("PlayerCharacter", "walking");
        setMovementState("walk");
        setMovementDirection(direction);
    }

    @Override
    void jump() {
        Gdx.app.log("PlayerCharacter", "jumping");
        setMovementState("jump");
    }

    @Override
    void idle() {
        Gdx.app.log("PlayerCharacter", "idling");
        setMovementState("idle");
    }

    @Override
    void die() {

    }
}
