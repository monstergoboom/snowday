package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.SkeletonData;

/**
 * Created by amitrevski on 12/23/14.
 */
public class Snowman extends NonPlayerCharacter {
    public Snowman(int x, int y, World b2World, SkeletonData sd) {
        super("snowman", 0.35f, x, y, b2World, sd);
    }
}
