package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    SnowDayAssetManager assetManager;

    protected float magicRefreshRate;
    protected float magicRefreshDelta;
    protected float magicRefreshAmountPerRefresh;
    protected float primaryWeaponMagicCost;
    protected float secondaryWeaponMagicCost;
    protected float actionMagicCost;

    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd, SnowDayAssetManager assetManager) {
        super(assetName, scale, x, y, "player_character", "character", b2World, sd, 0x0002, 0xffff & ~0x0004);
        this.assetManager = assetManager;
        magicRefreshRate = 1.5f;
        magicRefreshDelta = 0.0f;
        magicRefreshAmountPerRefresh = 2.1f;

        primaryWeaponMagicCost = 5f;
        secondaryWeaponMagicCost = 2f;

        actionMagicCost = 12.2f;
    }

    @Override
    protected void attack() {
        if (currentMagic >= primaryWeaponMagicCost) {
            super.attack();
            currentMagic -= primaryWeaponMagicCost;
        }
    }

    @Override
    protected void secondaryAttack() {
        if (currentMagic >= secondaryWeaponMagicCost) {
            super.secondaryAttack();
            currentMagic -= secondaryWeaponMagicCost;
        }
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
        if (currentMagic >= actionMagicCost) {
            super.jump();
            currentMagic -= actionMagicCost;
        }
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

    @Override
    public void update(float delta) {

        magicRefreshDelta += delta;
        if (magicRefreshDelta>magicRefreshRate) {
            float v = magicRefreshDelta / magicRefreshRate;
            float t = v * magicRefreshAmountPerRefresh;

            magicRefreshDelta = 0;

            currentMagic += (int)t;

            if (currentMagic > maxMagic) {
                currentMagic = maxMagic;
            }
        }

        super.update(delta);

        Gdx.app.log("PlayerCharacter", String.format("current magic: %d", currentMagic));
    }
}
