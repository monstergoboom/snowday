package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amitrevski on 12/23/14.
 */
public class PlayerCharacter extends Character {
    SnowDayAssetManager assetManager;

    protected float magicRefreshRate;
    protected float magicRefreshDelta;
    protected float magicRefreshAmountPerRefresh;

    protected float healthRefreshRate;
    protected float healthRefreshDelta;
    protected float healthRefreshAmountPerRefresh;

    protected float primaryWeaponMagicCost;
    protected float secondaryWeaponMagicCost;
    protected float actionMagicCost;

    protected Map<String, Integer> inventory;

    public PlayerCharacter(String assetName, float scale, int x, int y, World b2World, SkeletonData sd, SnowDayAssetManager assetManager) {
        super(assetName, scale, x, y, "player_character", "character", b2World, sd, 0x0002, 0xffff & ~0x0004);
        this.assetManager = assetManager;
        magicRefreshRate = 1.5f;
        magicRefreshAmountPerRefresh = 2.1f;
        magicRefreshDelta = 0f;

        healthRefreshRate = 1.5f;
        healthRefreshAmountPerRefresh = 5f;
        healthRefreshDelta = 0f;

        primaryWeaponMagicCost = 5f;
        secondaryWeaponMagicCost = 2f;

        actionMagicCost = 12.2f;

        inventory = new HashMap<>();
    }

    protected void addItemToInventory(String itemName, int count) {
        if (count > 0) {
            inventory.put(itemName, getItemInventoryCount(itemName) + count);
        }
    }

    protected void removeItemFromInventory(String itemName, int count) {
        if (count > 0) {
            int itemInventoryCount = getItemInventoryCount(itemName);
            if (count > itemInventoryCount) {
                inventory.put(itemName, 0);
            } else {
                inventory.put(itemName, itemInventoryCount - count);
            }
        }
    }

    protected int getItemInventoryCount(String itemName) {
        return inventory.getOrDefault(itemName, 0);
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
    protected void crouch() {
        super.crouch();
    }

    @Override
    protected void stand() {
        super.stand();
    }

    @Override
    protected void prone() {
        super.prone();
    }

    @Override
    protected void switchAmmo(String ammoName) {
        super.switchAmmo(ammoName);
    }

    public void reloadPrimaryWeapon() {
        if (getItemInventoryCount("green_ornament") > 0) {

            int reloaded = getPrimaryWeapon()
                    .reload(getItemInventoryCount("green_ornament"));

            removeItemFromInventory("green_ornament", reloaded);
        }
    }

    @Override
    public Weapon getPrimaryWeapon() {
        return null;
    }

    @Override
    public void update(float delta) {

        updateMagic(delta);
        updateHealth(delta);

        super.update(delta);
    }

    private void updateHealth(float delta) {
        if(currentHealth < maxHealth ) {
            healthRefreshDelta += delta;
            if (healthRefreshDelta > magicRefreshRate) {
                float v = healthRefreshDelta / healthRefreshRate;
                float t = v * healthRefreshAmountPerRefresh;

                healthRefreshDelta = 0;

                currentHealth += (int) t;

                if (currentHealth > maxHealth) {
                    currentHealth = maxHealth;
                }
            }
        }
    }

    private void updateMagic(float delta) {
        if (currentMagic < maxMagic) {
            magicRefreshDelta += delta;
            if (magicRefreshDelta > magicRefreshRate) {
                float v = magicRefreshDelta / magicRefreshRate;
                float t = v * magicRefreshAmountPerRefresh;

                magicRefreshDelta = 0;

                currentMagic += (int) t;

                if (currentMagic > maxMagic) {
                    currentMagic = maxMagic;
                }
            }
        }
    }
}
