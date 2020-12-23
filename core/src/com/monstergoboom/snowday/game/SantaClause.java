package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
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
    }

    public void setPrimaryWeapon(Weapon weapon) {
        primaryWeapon = weapon;
        primaryWeapon.setPosition(positionX, positionY);
    }

    public Weapon getPrimaryWeapon() {
        return primaryWeapon;
    }

    @Override
    void attack() {
        super.attack();

        if (primaryWeapon != null) {
            primaryWeapon.setPosition(positionX, positionY + (100));
            primaryWeapon.setDirection(movementDirection);
            primaryWeapon.fire();
            Gdx.app.log("Santa Clause", "attacking");
        }
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
            Gdx.app.log("santa", String.format("position: %d, %d", positionX, positionY));
        }
    }
}
