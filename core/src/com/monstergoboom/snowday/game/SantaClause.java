package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/23/14.
 */
public class SantaClause extends PlayerCharacter {
    public SantaClause(int x, int y, World b2World) {
        super("santa", 0.5f, x, y, b2World);
    }
}
