package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by amitrevski on 12/29/14.
 */

public class MagicGlobe extends GlobeContainer {
    public MagicGlobe(int positionX, int positionY, int originalWidth, int originalHeight,
                       int maxSize, int initialValue, SnowDayAssetManager asm) {
        super("misc", "snowglobe_top", HelperUtils.regionIndexNone, "snowglobe_top_mask",
                HelperUtils.regionIndexNone, "Magic",
                positionX, positionY, originalWidth, originalHeight,
                maxSize, initialValue, Color.GREEN,
                asm.getAssetManager(),
                asm.getTextureAtlas("misc"),
                HelperUtils.emptyTexture,
                asm.getBitmapFont(HelperUtils.displayFontS));
    }
}
