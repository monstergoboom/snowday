package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class SantaClause extends PlayerCharacter {
    public SantaClause(int x, int y, World b2World, SkeletonData sd) {
        super("santa", 0.25f, x, y, b2World, sd);
    }
}
