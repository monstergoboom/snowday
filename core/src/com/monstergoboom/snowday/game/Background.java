package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    final static float PIXEL_TO_METERS_RATIO = 0.00366f;

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
            batch.draw(texture, positionX * PIXEL_TO_METERS_RATIO, positionY * PIXEL_TO_METERS_RATIO,
                Gdx.graphics.getWidth() * PIXEL_TO_METERS_RATIO,
                    Gdx.graphics.getHeight() * PIXEL_TO_METERS_RATIO);
        }
        else if(atlasRegion != null) {
            batch.draw(atlasRegion, positionX * PIXEL_TO_METERS_RATIO, positionY * PIXEL_TO_METERS_RATIO,
                    atlasRegion.originalWidth * PIXEL_TO_METERS_RATIO, atlasRegion.originalHeight * PIXEL_TO_METERS_RATIO);
        }
        batch.enableBlending();
    }
}
