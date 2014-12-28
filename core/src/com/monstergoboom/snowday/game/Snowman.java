package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class Snowman extends NonPlayerCharacter {
    public Snowman(int x, int y, World b2World) {
        super("snowman", 0.75f, x, y, b2World);
    }
}
