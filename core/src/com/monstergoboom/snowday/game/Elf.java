package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class Elf extends NonPlayerCharacter {
    public Elf(int x, int y, World b2World, SnowDayAssetManager asm) {
        super("elf", 0.25f, x, y, b2World, asm.getSkeletonData("elf"));
    }
}
