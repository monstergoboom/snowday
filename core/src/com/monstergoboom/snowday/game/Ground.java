package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;


/**
 * Created by amitrevski on 12/23/14.
 */
public class Ground extends GameObject {
    protected int overlapOffset;
    protected String assetName;
    protected boolean loopIndex;
    protected int segmentLength;
    protected boolean lockIndex;
    protected int lockedIndex;
    protected int currentIndex;
    protected int startIndex;
    protected int totalRegions;
    protected int displayWidth;
    protected int virtualWidth;
    protected int positionX;
    protected int positionY;
    protected int totalRegionWidth;
    protected int totalRegionLoops;
    protected int startOffset;
    protected int totalRegionLoopWidth;
    protected int startRegion;
    protected float scale;
    protected boolean needsUpdate;
    protected World world;
    protected BodyDef bodyDef;
    protected Body body;
    protected AtlasBoxing.AtlasBoxDef boxingDef;

    private AssetManager assetManager;
    private TextureAtlasLoader textureAtlasLoader;
    private TextureAtlas textureAtlas;
    private Array<AtlasRegion> regionArray;
    private Array<AtlasRegion> drawableRegionArray;

    protected short filterCategory;
    protected short filterMask;

    @Override
    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }

    @Override
    public void beginContact(GameObject contactWith) {

    }

    @Override
    public void endContact(GameObject contactWith) {

    }

    public static class GroundDef {
        public int positionIndex;
        public int atlasIndex;
        public int startLocation;
        public int endLocation;

        public GroundDef(int position, int atlas, int start, int end) {
            positionIndex = position;
            atlasIndex = atlas;
            startLocation = start;
            endLocation = end;
        }
    }

    private Array<GroundDef> groundDefs;

    public Ground(String name, World b2dWorld) {
        super(name, "platform");

        overlapOffset = 0;
        assetName = name;
        loopIndex = true;
        segmentLength = 5;
        lockIndex = false;
        lockedIndex = 0;
        currentIndex = 1;
        startIndex = 1;
        totalRegions = 0;
        displayWidth = Gdx.graphics.getWidth();
        virtualWidth = 9000;
        positionX = 0;
        positionY = 0;
        groundDefs = new Array<>();
        drawableRegionArray = new Array<>();
        totalRegionWidth = 0;
        totalRegionLoops = 0;
        startOffset = 0;
        totalRegionLoopWidth = 0;
        startRegion = 1;
        needsUpdate = true;
        world = b2dWorld;
        scale = HelperUtils.pixelsToUnitsRatio;

        assetManager = new AssetManager(new InternalFileHandleResolver());
        textureAtlasLoader = new TextureAtlasLoader(new InternalFileHandleResolver());

        filterCategory = (short)0x0001;
        filterMask = (short)0xffff;

        LoadTextureAtlas();
        LoadBoxingDefinition();
        SetFixtures();
    }

    private void SetFixtures() {
        if(!boxingDef.hasError) {
            bodyDef = new BodyDef();

            Vector2 bodyDefPos = HelperUtils.convertPixelsToUnits(boxingDef.x - positionX, boxingDef.y - positionY);

            bodyDef.position.set(bodyDefPos);
            bodyDef.type = HelperUtils.convertStringToBodyType(boxingDef.bodyType);

            body = world.createBody(bodyDef);

            Array<Vector2> vertices = new Array<>();

            // for each start region and each
            for(GroundDef def: groundDefs) {
                AtlasBoxing.AtlasBox box = boxingDef.get(assetName, def.atlasIndex);
                if(box!=null) {
                    Vector2 xy = HelperUtils.convertPixelsToUnits(def.startLocation - 1, box.y);
                    Vector2 dx_dy = HelperUtils.convertPixelsToUnits(def.endLocation + 1, box.y);

                    vertices.add(xy);
                    vertices.add(dx_dy);
                }
            }

            ChainShape chainShape = new ChainShape();

            Vector2[] t = vertices.toArray(Vector2.class);
            chainShape.createChain(t);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.friction = 1.0f;
            fixtureDef.restitution = 0.0f;
            fixtureDef.shape = chainShape;
            fixtureDef.filter.categoryBits = filterCategory;
            fixtureDef.filter.maskBits = filterMask;

            body.createFixture(fixtureDef);
            body.setUserData(this);

            chainShape.dispose();

            body.setTransform(bodyDefPos, 0.0f);
        }
    }

    private void LoadBoxingDefinition() {
        try {
            FileHandle fhBoxingDef = Gdx.files.internal(assetName + ".boxing.json");
            boxingDef = AtlasBoxing.load(fhBoxingDef);
        }
        catch(Exception ex) {
            boxingDef = new AtlasBoxing.AtlasBoxDef();
            boxingDef.hasError = true;
            boxingDef.message = ex.getLocalizedMessage();

            Gdx.app.log("Ground", "invalid json file. exception handled: " + ex.getLocalizedMessage());
        }

        Gdx.app.log("Ground", boxingDef.debug());
    }

    private void LoadTextureAtlas() {
        FileHandle fhAtlas = Gdx.files.internal( assetName +".atlas");
        textureAtlasLoader.getDependencies(assetName, fhAtlas,
                new TextureAtlasLoader.TextureAtlasParameter(false));

        FileHandle fhTexture = Gdx.files.internal(assetName + ".png");

        assetManager.load(fhTexture.path(), Texture.class);
        assetManager.finishLoading();

        textureAtlas = textureAtlasLoader.load(assetManager, assetName, fhAtlas,
                new TextureAtlasLoader.TextureAtlasParameter(false));

        regionArray = textureAtlas.getRegions();
        totalRegions = regionArray.size;

        for(int regionIndex = 0; regionIndex < totalRegions; regionIndex++) {
            totalRegionLoopWidth += regionArray.get(regionIndex).originalWidth;
        }

        int positionIndex = 0;
        while (totalRegionWidth < virtualWidth) {
            ++ totalRegionLoops;
            for(int regionIndex = 0; regionIndex < totalRegions; regionIndex++) {
                int width = regionArray.get(regionIndex).originalWidth;
                groundDefs.add(new GroundDef(positionIndex, regionIndex, totalRegionWidth - overlapOffset, totalRegionWidth + width - overlapOffset ));
                totalRegionWidth+= width;
                positionIndex++;
                if(totalRegionWidth >= virtualWidth) {
                    break;
                }
            }
        }

        Gdx.app.log("Ground", "display width: " + Integer.toString(displayWidth)
                + ", virtual width: " + Integer.toString(virtualWidth));
        Gdx.app.log("Ground", "total width: " + Integer.toString(totalRegionWidth));
        Gdx.app.log("Ground", "total regions: " + Integer.toString(totalRegions));
        Gdx.app.log("Ground", "total region loops: " + Integer.toString(totalRegionLoops));
        Gdx.app.log("Ground", "total region loop width: " + Integer.toString(totalRegionLoopWidth));
    }

    public void update(float delta) {
        if(needsUpdate) {
            drawableRegionArray.clear();

            // find the starting region based on the current x position and the total
            // width of all regions
            int posX = positionX;
            int posDX = positionX + displayWidth;

            int posXIndex = 0;
            int posDXIndex = 0;

            for(GroundDef def : groundDefs) {
                if(posX < def.startLocation) {
                    posXIndex = def.positionIndex;
                    break;
                }
            }

            for(GroundDef def : groundDefs) {
                if(posDX < def.startLocation) {
                    posDXIndex = def.positionIndex;
                    break;
                }
            }

            for (int k = posXIndex; k <= posDXIndex; k++) {
                GroundDef def = groundDefs.get(k);
                drawableRegionArray.add(regionArray.get(def.atlasIndex));
            }

            int regionStartPosition = groundDefs.get(posXIndex).startLocation;
            startOffset = (regionStartPosition - positionX) - drawableRegionArray.first().originalWidth;

            needsUpdate = false;
        }
    }

    public void draw(Batch batch) {
        int x = startOffset;
        int y = 0;

        for(AtlasRegion workingRegion: drawableRegionArray) {
            batch.draw(workingRegion, HelperUtils.convertPixelsToUnits(x), HelperUtils.convertPixelsToUnits(y),
                    HelperUtils.convertPixelsToUnits(workingRegion.originalWidth),
                    HelperUtils.convertPixelsToUnits(workingRegion.originalHeight));

            x += workingRegion.originalWidth;
        }
    }
}
