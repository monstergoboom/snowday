package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class Elf extends NonPlayerCharacter {
    public Elf(int x, int y, World b2World) {
        super("elf", 0.50f, x, y, b2World);
    }
}
