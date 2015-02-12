package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by amitrevski on 12/30/14.
 */
public class GiftBox  {
    private int x;
    private int y;
    private float scale;
    private TextureAtlas textureAtlas;
    private boolean isOn;

    private Sprite spriteOn;
    private Sprite spriteOff;

    private String spriteOnRegion;
    private int spriteOnIndex;
    private String spriteOffRegion;
    private int spriteOffIndex;

    private boolean needsUpdate;

    public GiftBox(int positionX, int positionY,
                   float objectScale,
                   String onRegionName, int onRegionIndex,
                   String offRegionName, int offRegionIndex,
                   TextureAtlas ta) {
        x = positionX;
        y = positionY;
        scale = objectScale;
        isOn = false;

        spriteOnRegion = onRegionName;
        spriteOnIndex = onRegionIndex;
        spriteOffRegion = offRegionName;
        spriteOffIndex = offRegionIndex;

        textureAtlas = ta;

        needsUpdate = true;

        LoadTextures();
    }

    public void setOn() {
        needsUpdate = true;
        isOn = true;
    }

    public void setOff() {
        needsUpdate = true;
        isOn = false;
    }

    private void LoadTextures() {
        if(textureAtlas!=null) {
            if(spriteOnIndex >= 0) {
                spriteOn = textureAtlas.createSprite(spriteOnRegion, spriteOnIndex);
            } else {
                spriteOn = textureAtlas.createSprite(spriteOnRegion);
            }

            if(spriteOffIndex>=0) {
                spriteOff = textureAtlas.createSprite(spriteOffRegion, spriteOffIndex);
            } else {
                spriteOff = textureAtlas.createSprite(spriteOffRegion);
            }
        }
    }

    public void update(float animationDelta) {
        if(needsUpdate) {
            needsUpdate = false;

            spriteOn.setPosition(HelperUtils.convertPixelsToUnits(x),
                    HelperUtils.convertPixelsToUnits(y));
            spriteOn.setSize(HelperUtils.convertPixelsToUnits(64),
                    HelperUtils.convertPixelsToUnits(64));

            spriteOff.setPosition(HelperUtils.convertPixelsToUnits(x),
                    HelperUtils.convertPixelsToUnits(y));
            spriteOff.setSize(HelperUtils.convertPixelsToUnits(64),
                    HelperUtils.convertPixelsToUnits(64));
        }
    }

    public void draw(Batch batch) {
        if(isOn) {
            spriteOn.draw(batch);
        }
        else {
            spriteOff.draw(batch);
        }
    }
}
