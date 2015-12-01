package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 1/5/15.
 */
public class ChristmasTree extends StaticGameObject {
    public ChristmasTree(int positionX, int positionY, World b2World, SnowDayAssetManager asm) {
        super(positionX, positionY, 1.0f,
                "christmas_tree", "background",
                b2World,
                asm.getSkeletonData("tree"),
                0x0004, 0xffff & ~0x0002,
                false);
    }
}
