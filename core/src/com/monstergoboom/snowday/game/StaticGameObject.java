package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;

/**
 * Created by amitrevski on 12/23/14.
 */
public class StaticGameObject {
    protected int positionX;
    protected int positionY;
    protected String assetName;
    protected String regionName;
    protected int regionIndex;
    protected float scale;
    protected float animationSpeed;
    protected Body body;
    protected boolean needsUpdate;
    protected float animationRuntime;
    protected boolean hasAnimations;
    protected boolean hasBoundingBox;

    private AssetManager assetManager;
    private Skeleton skeleton;
    private SkeletonRenderer skeletonRenderer;
    private SkeletonData skeletonData;
    private AnimationState animationState;
    private TextureAtlas textureAtlas;
    private TextureAtlas.AtlasRegion atlasRegion;
    private TextureAtlasLoader textureAtlasLoader;

    private World world;

    public StaticGameObject(String asset, String region, int index,
                            int x, int y, float drawScale, World b2World) {
        assetName = asset;
        regionName = region;
        regionIndex = index;
        positionX = x;
        positionY = y;
        needsUpdate = true;
        animationRuntime = 0;
        scale = HelperUtils.pixelsToUnitsRatio * drawScale;
        animationSpeed = 1.0f;
        hasAnimations = false;
        hasBoundingBox = false;
        body = null;
        world = b2World;

        assetManager = new AssetManager(new InternalFileHandleResolver());
        textureAtlasLoader = new TextureAtlasLoader(new InternalFileHandleResolver());

        LoadTextureAtlas();
        LoadSkeleton();
    }

    private void LoadTextureAtlas() {
        FileHandle fhAtlas = Gdx.files.internal( assetName +".atlas");
        if(fhAtlas.exists()) {
            textureAtlasLoader.getDependencies(assetName, fhAtlas,
                    new TextureAtlasLoader.TextureAtlasParameter(false));

            FileHandle fhTexture = Gdx.files.internal(assetName + ".png");

            assetManager.load(fhTexture.path(), Texture.class);
            assetManager.finishLoading();

            textureAtlas = textureAtlasLoader.load(assetManager, assetName, fhAtlas,
                    new TextureAtlasLoader.TextureAtlasParameter(false));

            if(regionIndex > 0) {
                atlasRegion = textureAtlas.findRegion(regionName, regionIndex);
            }
            else {
                atlasRegion = textureAtlas.findRegion(regionName);
            }
        }
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

    private void LoadSkeleton() {
        FileHandle fhJson = Gdx.files.internal(assetName + ".json");
        if(fhJson.exists()) {
            skeletonRenderer = new SkeletonRenderer();
            SkeletonJson json = new SkeletonJson(textureAtlas);
            json.setScale(scale);

            skeletonData = json.readSkeletonData(fhJson);

            skeleton = new Skeleton(skeletonData);
            skeleton.setToSetupPose();
            skeleton = new Skeleton(skeleton);
            skeleton.setPosition(positionX, positionY);
            skeleton.updateWorldTransform();

            animationState = new AnimationState(new AnimationStateData(skeletonData));

            Animation animation = skeletonData.findAnimation("idle");
            assert (animation != null);

            animationState.setAnimation(0, animation, true);
            animationState.setTimeScale(animationSpeed);

            hasAnimations = true;

            SkeletonBounds bounds = new SkeletonBounds();
            bounds.update(skeleton, true);
            Array<BoundingBoxAttachment> boundingBoxAttachmentArray = bounds.getBoundingBoxes();
            for(BoundingBoxAttachment attachment: boundingBoxAttachmentArray) {
                createBoundingBox(HelperUtils.convertFloatToVector2(attachment.getVertices()));
            }
        }
    }

    public void update(float delta) {
        if(hasAnimations) {
            animationState.update(delta);
            animationState.apply(skeleton);
            skeleton.updateWorldTransform();
        }

        if(needsUpdate) {
            Vector2 pos = HelperUtils.convertPixelsToUnits(positionX, positionY);

            if(hasAnimations) {
                skeleton.setPosition(pos.x, pos.y);
                skeleton.updateWorldTransform();
            }

            needsUpdate = false;
        }
    }

    public void createBoundingBox(Vector2[] vertices)
    {
        if(body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(HelperUtils.convertPixelsToUnits(positionX, positionY));

            body = world.createBody(bodyDef);
        }

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 1.0f;

        body.createFixture(fixtureDef);

        polygonShape.dispose();

        hasBoundingBox = true;
    }

    public void draw(Batch batch) {
        if(hasAnimations) {
            skeletonRenderer.draw(batch, skeleton);
        }
        else {
            if (atlasRegion!=null) {
                batch.draw(atlasRegion, HelperUtils.convertPixelsToUnits(positionX),
                        HelperUtils.convertPixelsToUnits(positionY),
                        HelperUtils.convertPixelsToUnits(atlasRegion.originalWidth),
                        HelperUtils.convertPixelsToUnits(atlasRegion.originalHeight));
            }
        }
    }
}
