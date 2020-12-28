package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

/**
 * Created by amitrevski on 12/28/14.
 */
public class GlobeContainer {
    protected int max;
    protected int current;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int drawHeight;
    protected Color color;
    protected AssetManager assetManager;
    protected TextureAtlas textureAtlas;
    protected Texture texture;
    protected TextureAtlas.AtlasRegion atlasRegionMask;
    protected TextureRegion updatedRegion;
    protected Sprite sprite;
    protected Sprite sprite_base;

    protected String asset;
    protected String region;
    protected int index;
    protected String maskRegion;
    protected int maskIndex;
    protected int originalMaskHeight;
    protected boolean needsUpdate;
    protected float percentComplete;
    protected BitmapFont font;
    protected String text;
    protected String name;

    public GlobeContainer(String assetName, String regionName, int regionIndex,
                          String regionMaskName, int regionMaskIndex, String containerName,
                          int positionX, int positionY, int originalWidth, int originalHeight,
                          int maxSize, int initialValue, Color fillColor,
                          AssetManager asm, TextureAtlas ta, Texture t, BitmapFont f) {
        name = containerName;
        max = maxSize;
        current = initialValue;
        drawHeight = originalHeight;
        x = positionX;
        y = positionY;
        width = originalWidth;
        height = originalHeight;
        asset = assetName;
        region = regionName;
        index = regionIndex;
        maskRegion = regionMaskName;
        maskIndex = regionMaskIndex;
        color = fillColor;
        originalMaskHeight = originalHeight;
        needsUpdate = true;
        percentComplete = current/max;
        font = f;
        text = "";

        assetManager = asm;
        textureAtlas = ta;
        texture = t;

        LoadTextureAtlas();
    }

    public void LoadTextureAtlas() {

        if(texture != null){
            Gdx.app.log("LoadTextureAtlas", "method not supported by texture pass texture atlas");
        }
        else {
            if (textureAtlas != null) {
                if (index >= 0) {
                    sprite = textureAtlas.createSprite(region, index);
                } else {
                    sprite = textureAtlas.createSprite(region);
                }

                sprite.setPosition(HelperUtils.convertPixelsToUnits(x), HelperUtils.convertPixelsToUnits(y));
                sprite.setSize(HelperUtils.convertPixelsToUnits(width), HelperUtils.convertPixelsToUnits(height));

                sprite_base = textureAtlas.createSprite("snowglobe_base");
                sprite_base.setPosition(HelperUtils.convertPixelsToUnits(x), HelperUtils.convertPixelsToUnits(y-5));
                sprite_base.setSize(HelperUtils.convertPixelsToUnits(width), HelperUtils.convertPixelsToUnits(20));

                if (maskIndex >= 0) {
                    atlasRegionMask = textureAtlas.findRegion(maskRegion, maskIndex);
                } else {
                    atlasRegionMask = textureAtlas.findRegion(maskRegion);
                }

                originalMaskHeight = atlasRegionMask.getRegionHeight();
                updatedRegion = new TextureRegion(atlasRegionMask.getTexture(),
                        atlasRegionMask.getRegionX(),
                        atlasRegionMask.getRegionY(),
                        atlasRegionMask.getRegionWidth(),
                        atlasRegionMask.getRegionHeight());
            }
        }
    }

    public void setCurrent(int current) {
        this.current = current;
        needsUpdate = true;
    }

    public void setMax(int max) {
        this.max = max;
        needsUpdate = true;
    }

    public void update(float animationDelta) {
        if (needsUpdate) {
            percentComplete = (float) current / (float) max;
            drawHeight = (int) (originalMaskHeight * percentComplete);

            if (drawHeight > originalMaskHeight) {
                drawHeight = originalMaskHeight;
            }

            updatedRegion = new TextureRegion(atlasRegionMask.getTexture(),
                    atlasRegionMask.getRegionX(),
                    atlasRegionMask.getRegionY(),
                    atlasRegionMask.getRegionWidth(),
                    drawHeight);

            updatedRegion.flip(true, true);

            drawHeight = (int)(height * percentComplete);

            text = String.format("%s", name);

            needsUpdate = false;
        }
    }

    public void drawText(Batch fontBatch) {
        font.draw(fontBatch, text, x, y + (int)(height/2) + font.getLineHeight() + font.getDescent(),
                width, Align.center, true);
    }

    public void draw(Batch batch) {
        Color temp = batch.getColor().cpy();

        batch.setColor(color.r, color.g, color.b, 1.0f);

        batch.draw(updatedRegion,
            HelperUtils.convertPixelsToUnits(x), HelperUtils.convertPixelsToUnits(y),
            HelperUtils.convertPixelsToUnits(width),
            HelperUtils.convertPixelsToUnits(drawHeight));

        batch.setColor(temp);

        sprite.draw(batch);
        sprite_base.draw(batch);
    }
}
