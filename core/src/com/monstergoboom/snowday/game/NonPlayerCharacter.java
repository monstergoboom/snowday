package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class NonPlayerCharacter extends Character {
    public NonPlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd) {
        super(assetName, scale, x, y, "non_player_character", "character", b2World, sd, 0x0008, 0xffff & ~0x0004);
    }

    @Override
    protected void attack() {

    }

    @Override
    protected void run() {

    }

    @Override
    protected void walk(int direction) {

    }

    @Override
    protected void jump() {

    }

    @Override
    protected void doubleJump() {

    }

    @Override
    protected void idle() {

    }

    @Override
    protected void die() {

    }

    @Override
    public Weapon getPrimaryWeapon() {
        return null;
    }
}
