package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 1/5/15.
 */
public class RedOrnamentBullet extends Bullet {
    public RedOrnamentBullet(int posX, int posY, World w, SnowDayAssetManager asm) {
        super(posX, posY, "red_ornament", -1, w, asm.getTextureAtlas("misc"));
    }
}
