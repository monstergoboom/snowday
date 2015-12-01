package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd) {
        super(assetName, scale, x, y, "player_character", "character", b2World, sd, 0x0002, 0xffff & ~0x0004);
    }

    @Override
    void attack() {
        Gdx.app.log("PlayerCharacter", "attacking");
        setMovementCombatState("attack");
    }

    @Override
    void run() {
        Gdx.app.log("PlayerCharacter", "running");
        setMovementState("run");
    }

    @Override
    void walk(int direction) {
        Gdx.app.log("PlayerCharacter", "walking");
        setMovementState("walk");
        setMovementDirection(direction);
    }

    @Override
    void jump() {
        if(!isJumping() && hasGroundContact()) {
            Gdx.app.log("PlayerCharacter", "jumping");
            setMovementActionState("jump");
        }
    }

    @Override
    void doubleJump() {

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
