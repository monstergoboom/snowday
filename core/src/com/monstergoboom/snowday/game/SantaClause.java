package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class SantaClause extends PlayerCharacter {
    private Weapon primaryWeapon;

    public SantaClause(int x, int y, World b2World, SkeletonData sd, SnowDayAssetManager assetManager) {
        super("santa", .35f, x, y, b2World, sd, assetManager);
        speed = 3.5f;
        primaryWeapon = null;

        maxHealth = 150;
        currentHealth = 75;

        maxMagic = 100;
        currentMagic = 75;

        healthRefreshRate = 2.5f;
        healthRefreshAmountPerRefresh = 5.0f;

        magicRefreshRate = 1.0f;
        magicRefreshAmountPerRefresh = 5.0f;

        actionMagicCost = 8f;
        primaryWeaponMagicCost = 2.5f;
        secondaryWeaponMagicCost = 5.0f;

        addItemToInventory("red_ornament", 10);
        addItemToInventory("green_ornament", 10);
        addItemToInventory("blue_ornament", 10);
        addItemToInventory("yellow_ornament", 10);
    }

    public void setPrimaryWeapon(Weapon weapon) {
        primaryWeapon = weapon;
        primaryWeapon.setPosition(positionX, positionY);
    }

    public Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    @Override
    protected void attack() {
        if (canAttack()) {
            super.attack();

            if (primaryWeapon != null) {
                primaryWeapon.setPosition(positionX, positionY + (150));
                primaryWeapon.setDirection(movementDirection);
                primaryWeapon.fire();
            }
        }
    }

    private boolean canAttack() {
        return currentMagic >= 5;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (primaryWeapon != null) {
            primaryWeapon.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if ( primaryWeapon != null) {
            primaryWeapon.update(delta);
        }
    }
}
