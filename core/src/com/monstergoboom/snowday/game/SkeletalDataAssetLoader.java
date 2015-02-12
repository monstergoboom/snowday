package com.monstergoboom.snowday.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

/**
 * Created by amitrevski on 1/5/15.
 */
public class SkeletalDataAssetLoader extends AsynchronousAssetLoader<SkeletonData, SkeletalDataAssetLoader.SkeletalDataAssetLoaderParameter> {

    public static class SkeletalDataAssetLoaderParameter extends AssetLoaderParameters<SkeletonData> {
        public String textureAtlasRef = "";
        public SkeletalDataAssetLoaderParameter(String name) {
            textureAtlasRef = name;
        }
    }

    public SkeletalDataAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletalDataAssetLoaderParameter parameter) {
    }

    @Override
    public SkeletonData loadSync(AssetManager manager, String fileName, FileHandle file, SkeletalDataAssetLoaderParameter parameter) {
        SkeletonJson skeletonJson = new SkeletonJson(manager.get(parameter.textureAtlasRef, TextureAtlas.class));
        skeletonJson.setScale(HelperUtils.pixelsToUnitsRatio);
        return skeletonJson.readSkeletonData(file);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletalDataAssetLoaderParameter parameter) {
        return null;
    }
}
