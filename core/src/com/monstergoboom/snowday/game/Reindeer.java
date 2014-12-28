package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class Reindeer extends NonPlayerCharacter {
    public Reindeer(int x, int y, World b2World) {
        super("reindeer", 0.45f, x, y, b2World);
    }
}
