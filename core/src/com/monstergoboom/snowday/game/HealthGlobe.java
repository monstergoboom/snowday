package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by amitrevski on 12/29/14.
 */
public class HealthGlobe extends GlobeContainer {
    public HealthGlobe(int positionX, int positionY, int originalWidth, int originalHeight,
                       int maxSize, int initialValue, SnowDayAssetManager asm) {
        super("misc_2", "snowglobe_top", HelperUtils.regionIndexNone, "snowglobe_top_mask",
                HelperUtils.regionIndexNone, "Health",
                positionX, positionY, originalWidth, originalHeight,
                maxSize, initialValue, Color.RED,
                asm.getAssetManager(),
                asm.getTextureAtlas("misc_2"),
                HelperUtils.emptyTexture,
                asm.getBitmapFont(HelperUtils.displayFontS));
    }
}
