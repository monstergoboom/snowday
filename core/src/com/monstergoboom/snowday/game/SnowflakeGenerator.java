package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by amitrevski on 12/26/14.
 */
public class SnowflakeGenerator {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int minSize;
    protected int maxSize;
    protected int minLife;
    protected int maxLife;
    protected int minSpeed;
    protected int maxSpeed;
    protected String generatorAsset;
    protected String generatorRegion;
    protected int generatorIndex;
    protected String asset;
    protected String region;
    protected int regionIndex;
    protected int max;
    protected float frequency;
    protected int density;
    protected float animationTime;
    protected boolean isEmitting;

    protected LinkedList<Snowflake> snowflakes;
    protected LinkedList<Snowflake> emitter;
    
    protected AssetManager assetManager;
    protected TextureAtlasLoader textureAtlasLoader;
    protected TextureAtlas textureAtlas;
    protected Texture texture;
    protected Texture generatorTexture;
    protected TextureAtlas.AtlasRegion generatorAtlasRegion;
    protected TextureAtlas.AtlasRegion atlasRegion;

    protected BitmapFont bitmapFont;

    protected World world;

    public SnowflakeGenerator(String generatorAssetName, String generatorRegionName, int generatorRegionIndex,
                              String assetName, String regionName, int index,
                              int positionX, int positionY, int generatorWidth, int generatorHeight,
                              int maxSnowflakes, float emitterFrequency, int emitterDensity,
                              int particleMinSize, int particleMaxSize,
                              int particleMinLife, int particleMaxLife,
                              int particleMinSpeed, int particleMaxSpeed,
                              World b2World) {
        x = positionX;
        y = positionY;
        width = generatorWidth;
        height = generatorHeight;
        minSize = particleMinSize;
        maxSize = particleMaxSize;
        minLife = particleMinLife;
        maxLife = particleMaxLife;
        minSpeed = particleMinSpeed;
        maxSpeed = particleMaxSpeed;
        generatorAsset = generatorAssetName;
        generatorRegion = generatorRegionName;
        generatorIndex = generatorRegionIndex;
        asset = assetName;
        region = regionName;
        regionIndex = index;
        max = maxSnowflakes;
        frequency = emitterFrequency;
        density = emitterDensity;
        snowflakes = null;
        emitter = null;
        animationTime = emitterFrequency;
        isEmitting = false;
        world = b2World;

        FileHandle fhBitmapFont = Gdx.files.internal("curlz_mt.fnt");
        bitmapFont = new BitmapFont(fhBitmapFont);
        float d = Gdx.graphics.getDensity();
        float th = bitmapFont.getXHeight();
        float d_th = th * d;
        float scaleFactor;
        float s = d_th - 40;
        if( s < 0) {
            scaleFactor = 40/th;
        }
        else {
            scaleFactor = th/40;
        }

        bitmapFont.getData().setScale(scaleFactor);

        assetManager = new AssetManager(new InternalFileHandleResolver());
        textureAtlasLoader = new TextureAtlasLoader(new InternalFileHandleResolver());

        LoadGenerator();
        LoadSnowflake();

        GenerateSnowflakes();
    }

    private void LoadGenerator() {
        if(generatorRegion.isEmpty()) {
            generatorTexture = LoadTexture(generatorAsset);
            generatorAtlasRegion = null;
        }
        else {
            generatorTexture = null;
            generatorAtlasRegion = LoadTextureAtlas(generatorAsset, generatorRegion, generatorIndex);
        }
    }

    private void LoadSnowflake() {
        if(region.isEmpty()) {
            texture = LoadTexture(asset);
            atlasRegion = null;
        }
        else {
            texture = null;
            atlasRegion = LoadTextureAtlas(asset, region, regionIndex);
        }
    }

    private Texture LoadTexture(String assetName) {
        Texture t;

        FileHandle fhTexture = Gdx.files.internal(assetName + ".png");
        t = new Texture(fhTexture);

        return t;
    }

    private TextureAtlas.AtlasRegion LoadTextureAtlas(String assetName, String assetRegion, int regionIndex) {
        TextureAtlas.AtlasRegion t;
        FileHandle fhAtlas = Gdx.files.internal( assetName +".atlas");

        textureAtlasLoader.getDependencies(assetName, fhAtlas,
                new TextureAtlasLoader.TextureAtlasParameter(false));

        FileHandle fhTexture = Gdx.files.internal(assetName + ".png");

        assetManager.load(fhTexture.path(), Texture.class);
        assetManager.finishLoading();

        textureAtlas = textureAtlasLoader.load(assetManager, assetName, fhAtlas,
                new TextureAtlasLoader.TextureAtlasParameter(false));

        if (regionIndex > 0) {
            t = textureAtlas.findRegion(assetRegion, regionIndex);
        } else {
            t = textureAtlas.findRegion(assetRegion);
        }

        return t;
    }

    private void GenerateSnowflakes() {
        snowflakes = new LinkedList<>();
        emitter = new LinkedList<>();

        for(int i = 0; i < max; i++) {

            int snowflakeSize = MathUtils.random(minSize, maxSize);

            Snowflake snowflake = new Snowflake(x, y, snowflakeSize, snowflakeSize,
                    minLife, minSpeed, texture, atlasRegion);
            snowflakes.push(snowflake);
        }
    }

    public void update(float animationDelta) {
        // check snowflake iterations and
        // particle life
        Iterator<Snowflake> it = emitter.iterator();
        while(it.hasNext()){
            Snowflake sf = it.next();
            if(!sf.update(animationDelta)) {
                it.remove();
                sf.sleep();
                snowflakes.push(sf);
            }
        }

        // if we're still emitting particles,
        // check to see if we have any left to emit and
        // add them to list;
        if(isEmitting) {
            animationTime += animationDelta;
            if (animationTime > frequency) {
                animationTime = 0;
                if (snowflakes.size() > 0) {
                    for (int i = 0; i < density; i++) {
                        Snowflake sf = snowflakes.peek();

                        if(sf!=null) {
                            snowflakes.pop();

                            int posX = MathUtils.random(x, width);
                            int posY = y;
                            int life = MathUtils.random(minLife, maxLife);
                            int speed = MathUtils.random(minSpeed, maxSpeed);

                            sf.reset(posX, posY, life, speed);
                            emitter.push(sf);
                        }
                    }
                }
            }
        }
    }

    public void draw(Batch batch) {
        for(Snowflake sf: emitter) {
            sf.draw(batch);
        }

        batch.draw(generatorTexture, HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y),
                HelperUtils.convertPixelsToUnits(width),
                HelperUtils.convertPixelsToUnits(height));
    }

    public void drawText(Batch batch) {
        float posX = x + 10;
        float posY = y + Math.abs((bitmapFont.getDescent() * Gdx.graphics.getDensity()));

        StringBuilder sb = new StringBuilder();

        sb.append("Generator: " + isEmitting);
        sb.append(", Available: " + Integer.toString(snowflakes.size()));
        sb.append(", Emitting: " + Integer.toString(emitter.size()));
        sb.append(", Frequency: " + Float.toString(frequency));
        sb.append(", Time: " + Float.toString(animationTime));

        bitmapFont.draw(batch, sb.toString(), posX, posY);
    }

    public void start() {
        isEmitting = true;
    }

    public void stop(boolean hardStop) {
        isEmitting = false;
        if(hardStop) {
            for(Snowflake sf: emitter) {
                snowflakes.push(sf);
            }

            emitter.clear();
        }
    }
}
