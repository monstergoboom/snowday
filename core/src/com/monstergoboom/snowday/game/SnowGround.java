package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class SnowGround extends Ground {
    public SnowGround(World b2dWorld) {
        super("snow_ground", b2dWorld);
    }
}
