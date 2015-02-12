package com.monstergoboom.snowday.game;

/**
 * Created by amitrevski on 1/5/15.
 */
public class RedGiftBox extends GiftBox {
    public RedGiftBox(int positionX, int positionY, SnowDayAssetManager asm) {
        super(positionX, positionY, 1.0f, "gift_box", 1, "gift_box", 2,
                asm.getTextureAtlas("misc"));
    }
}
