package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    SnowDayAssetManager assetManager;

    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd, SnowDayAssetManager assetManager) {
        super(assetName, scale, x, y, "player_character", "character", b2World, sd, 0x0002, 0xffff & ~0x0004);
        this.assetManager = assetManager;
    }

    @Override
    protected void attack() {
        super.attack();
    }

    @Override
    protected void secondaryAttack() {
        super.secondaryAttack();
    }

    @Override
    protected void run() {
        super.run();
    }

    @Override
    protected void walk(int direction) {
        super.walk(direction);
    }

    @Override
    protected void jump() {
        super.jump();
    }

    @Override
    protected void doubleJump() {
        super.doubleJump();
    }

    @Override
    protected void idle() {
        super.idle();
    }

    @Override
    protected void die() {
        super.die();
    }

    @Override
    public Weapon getPrimaryWeapon() {
        return null;
    }
}
