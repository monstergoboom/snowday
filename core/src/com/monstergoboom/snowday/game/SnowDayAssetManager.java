package com.monstergoboom.snowday.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.SkeletonData;

import java.util.HashMap;

/**
 * Created by amitrevski on 1/4/15.
 */
public class SnowDayAssetManager {
    private AssetManager assetManager;
    protected HashMap<String, String> assetManagerMap;
    protected boolean isLoaded;

    public SnowDayAssetManager() {
        assetManager = new AssetManager(new InternalFileHandleResolver());
        assetManager.setLoader(SkeletonData.class, new SkeletalDataAssetLoader(new InternalFileHandleResolver()));
        assetManagerMap = new HashMap<>();
        isLoaded = false;

        Texture.setAssetManager(assetManager);
    }

    public void load() {
        if(!isLoaded) {
            String[] textureAtlases = new String[] {"snow_ground", "santa", "misc", "elf"};
            String[] textures = new String[] {"backdrop_1", "snowflake_generator"};
            String[] fonts = new String[] { "curlz_mt", "curlz_mt_16", "display_font", "display_font_16" };
            String[] skeletalData = new String[] {"santa", "elf"};


            // load texture atlases
            for(String ta: textureAtlases) {
                FileHandle fhAtlas = new FileHandle(ta + ".atlas");
                assetManager.load(fhAtlas.path(), TextureAtlas.class);
                assetManagerMap.put(formatName(ta, TextureAtlas.class), fhAtlas.path());
            }

            // load textures
            for(String t: textures) {
                FileHandle fhTexture = new FileHandle(t + ".png");
                assetManager.load(fhTexture.path(), Texture.class);
                assetManagerMap.put(formatName(t, Texture.class), fhTexture.path());
            }

            // load bitmap fonts
            for(String f: fonts) {
                FileHandle fhFont = new FileHandle(f + ".fnt");
                assetManager.load(fhFont.path(), BitmapFont.class);
                assetManagerMap.put(formatName(f, BitmapFont.class), fhFont.path());
            }

            // load skeletal data
            for(String d: skeletalData) {
                FileHandle fhSkeleton = new FileHandle(d + ".json");
                assetManager.load(fhSkeleton.path(), SkeletonData.class,
                        new SkeletalDataAssetLoader.SkeletalDataAssetLoaderParameter(d + ".atlas"));
                assetManagerMap.put(formatName(d, SkeletonData.class), fhSkeleton.path());
            }
        }
    }

    private String formatName(String name, Class c) {
        return name + "#" + c.getSimpleName();
    }

    public boolean isFinishedLoading() {
        if(!isLoaded) {
            if (assetManager.getProgress() >= 1) {
                isLoaded = true;
            }
        }

        return isLoaded;
    }

    public TextureAtlas getTextureAtlas(String name) {
        return assetManager.get(assetManagerMap.get(formatName(name, TextureAtlas.class)),
                TextureAtlas.class);
    }

    public Texture getTexture(String name) {
        return assetManager.get(assetManagerMap.get(formatName(name, Texture.class)),
                Texture.class);
    }

    public BitmapFont getBitmapFont(String name) {
        return assetManager.get(assetManagerMap.get(formatName(name, BitmapFont.class)),
                BitmapFont.class);
    }

    public SkeletonData getSkeletonData(String name) {
        return assetManager.get(assetManagerMap.get(formatName(name, SkeletonData.class)),
                SkeletonData.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public void dispose() {
        assetManager.clear();
        assetManager.dispose();

        assetManagerMap.clear();
    }
}
