package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

import java.util.UUID;

/**
 * Created by amitrevski on 1/5/15.
 */
public class RedOrnamentBullet extends Bullet {
    public RedOrnamentBullet(int posX, int posY, World world, Sprite sprite) {
        super(posX, posY, "red_ornament", "bullet",
                world, sprite);
    }

    @Override
    public void beginContact(GameObject contactWith) {

    }

    @Override
    public void endContact(GameObject contactWith) {

    }

    @Override
    public Bullet copy() {
        return new RedOrnamentBullet(x, y, world, new Sprite(sprite));
    }

    @Override
    protected RedOrnamentBullet clone() {
        try {
            return (RedOrnamentBullet)super.clone();
        } catch (CloneNotSupportedException e) {
            return (RedOrnamentBullet) copy();
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    @Override
    public void update(float animationDelta) {
        super.update(animationDelta);
    }
}
