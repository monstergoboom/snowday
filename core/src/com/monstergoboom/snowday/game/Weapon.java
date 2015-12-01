package com.monstergoboom.snowday.game;

/**
 * Created by amitrevski on 11/28/15.
 */
public abstract class Weapon extends GameObject {
    private String weaponType;
    private String weaponName;

    public Weapon(String weaponType, String weaponName) {
        super("weapon", "weapon");

        this.weaponType = weaponType;
        this.weaponName = weaponName;
    }

    abstract void fire();
    abstract int reload(int count);
    abstract boolean isReady();

    @Override
    public void setPosition(int x, int y) {
    }

    public String getWeaponType() {
        return weaponType;
    }

    public String getWeaponName() {
        return weaponName;
    }
}
