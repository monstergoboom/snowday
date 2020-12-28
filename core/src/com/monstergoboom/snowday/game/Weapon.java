package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by amitrevski on 11/28/15.
 */
public abstract class Weapon extends GameObject {
    private String weaponType;
    private String weaponName;
    protected int x;
    protected int y;
    protected int direction;

    public Weapon(String weaponType, String weaponName, String resourceName, String resourceCategory) {
        super(resourceName, resourceCategory);

        this.weaponType = weaponType;
        this.weaponName = weaponName;
    }

    abstract void fire();
    abstract int reload(int count);
    abstract boolean isReady();

    public String getWeaponType() {
        return weaponType;
    }

    public String getWeaponName() {
        return weaponName;
    }

    abstract void update(float animationDelta);
    abstract void draw(Batch batch);

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

    abstract public int getCurrentAmmo();
}
