package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by amitrevski on 11/28/15.
 */
public class OrnamentBlaster extends Weapon {
    private int maxCapacity;
    private int currentCount;

    public OrnamentBlaster() {
        super("projectile", "Santa's Ornament Hand Blaster");
        maxCapacity = 10;
        currentCount = maxCapacity;
    }

    @Override
    public void fire() {
        if (currentCount > 0 ) {
            currentCount -= 1;
        }

        if (currentCount <= 0)
            Gdx.app.log("weapon", "no ammo, must reload");
        else
            Gdx.app.log("weapon", String.format("weapon fired, ammo left: %d", currentCount));
    }

    @Override
    public int reload(int count) {
        int reloadCount = maxCapacity - currentCount;

        if (reloadCount > count) {
            currentCount += count;
            reloadCount = count;
        }
        else {
            currentCount = maxCapacity;
        }

        Gdx.app.log("weapon", String.format("reloading weapon with %d ammo", reloadCount));

        return reloadCount;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void beginContact(GameObject contactWith) {

    }

    @Override
    public void endContact(GameObject contactWith) {

    }
}
