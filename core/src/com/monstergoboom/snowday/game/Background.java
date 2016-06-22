package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/25/14.
 */
public class Background {
    protected int positionX;
    protected int positionY;
    protected int centerX;
    protected int centerY;
    protected String assetName;
    protected String regionName;
    protected int regionIndex;
    protected TextureAtlas textureAtlas;
    protected Texture texture;
    protected TextureAtlas.AtlasRegion atlasRegion;
    protected boolean needsUpdate;
    protected World world;

    public Background(String asset, String region, int index, World w, TextureAtlas ta) {
        positionX = 0;
        positionY = 0;
        centerX = 0;
        centerY = 0;
        texture = null;
        atlasRegion = null;
        assetName = asset;
        regionName = region;
        regionIndex = index;
        needsUpdate = true;
        world = w;
        textureAtlas = ta;
        texture = null;
    }

    public Background(String asset, String region, World w, TextureAtlas ta) {
        positionX = 0;
        positionY = 0;
        centerX = 0;
        centerY = 0;
        texture = null;
        atlasRegion = null;
        assetName = asset;
        regionName = region;
        regionIndex = -1;
        needsUpdate = true;
        world = w;
        textureAtlas = ta;
        texture = null;
    }

    public Background(String asset, World w, Texture t) {
        positionX = 0;
        positionY = 0;
        centerX = 0;
        centerY = 0;
        texture = null;
        assetName = asset;
        regionName = "";
        regionIndex = -1;
        needsUpdate = true;
        world = w;
        texture = t;
        textureAtlas = null;
    }

    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
        needsUpdate = true;
    }

    public void setPositionX(int value) {
        positionX = value;
        needsUpdate = true;
    }

    public void setPositionY(int value) {
        positionY = value;
        needsUpdate = true;
    }

    public void update(float delta) {
        needsUpdate = false;
    }

    public void draw(Batch batch) {
        batch.disableBlending();

        if(texture != null) {
            batch.draw(texture,
                    HelperUtils.convertPixelsToUnits(positionX),
                    HelperUtils.convertPixelsToUnits(positionY),
                    HelperUtils.convertPixelsToUnits(Gdx.graphics.getWidth()),
                    HelperUtils.convertPixelsToUnits(Gdx.graphics.getHeight()));
        }
        else if(atlasRegion != null) {
            batch.draw(atlasRegion,
                    HelperUtils.convertPixelsToUnits(positionX),
                    HelperUtils.convertPixelsToUnits(positionY),
                    HelperUtils.convertPixelsToUnits(atlasRegion.originalWidth),
                    HelperUtils.convertPixelsToUnits(atlasRegion.originalHeight));
        }

        batch.enableBlending();
    }
}
