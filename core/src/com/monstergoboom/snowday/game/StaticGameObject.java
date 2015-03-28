package com.monstergoboom.snowday.game;

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
    protected short filterCategory;
    protected short filterMask;
    protected boolean isSensor;

    private Skeleton skeleton;
    private SkeletonRenderer skeletonRenderer;
    private SkeletonData skeletonData;
    private AnimationState animationState;
    private TextureAtlas textureAtlas;
    private Texture texture;
    private TextureAtlas.AtlasRegion atlasRegion;

    private World world;

    public StaticGameObject(int x, int y, float drawScale,
                            World b2World, Texture ta,
                            int category, int mask, boolean sensor) {
        positionX = x;
        positionY = y;
        scale = drawScale;
        world = b2World;
        texture = ta;

        needsUpdate = true;
        hasAnimations = false;
        hasBoundingBox = false;
        animationRuntime = 0;
        animationSpeed = 1.0f;

        textureAtlas = null;
        skeletonData = null;
        skeleton = null;
        skeletonRenderer = null;
        animationState = null;
        textureAtlas = null;
        atlasRegion = null;
        filterCategory = (short)category;
        filterMask = (short)(mask);
        isSensor = sensor;
    }

    public StaticGameObject(int x, int y, float drawScale,
                            String asset, String region, int index,
                            World b2World, TextureAtlas ta,
                            int category, int mask, boolean sensor) {
        positionX = x;
        positionY = y;
        scale = drawScale;
        textureAtlas = ta;
        assetName = asset;
        regionName = region;
        regionIndex = index;
        world = b2World;

        needsUpdate = true;
        hasAnimations = false;
        hasBoundingBox = false;
        animationRuntime = 0;
        animationSpeed = 1.0f;

        skeletonData = null;
        skeleton = null;
        skeletonRenderer = null;
        animationState = null;
        texture = null;

        filterCategory = (short)category;
        filterMask = (short)(mask);
        isSensor = sensor;

        LoadTexture();
    }

    public StaticGameObject(int x, int y, float drawScale,
                            World b2World,
                            SkeletonData sd,
                            int category, int mask, boolean sensor) {
        positionX = x;
        positionY = y;
        scale = drawScale;
        skeletonData = sd;
        world = b2World;

        needsUpdate = true;
        hasAnimations = false;
        hasBoundingBox = false;
        animationRuntime = 0;
        animationSpeed = 1.0f;

        assetName = null;
        regionName = null;
        regionIndex = HelperUtils.regionIndexNone;
        body = null;
        textureAtlas = null;
        texture = null;
        atlasRegion = null;

        filterCategory = (short)category;
        filterMask = (short)(mask);
        isSensor = sensor;

        LoadSkeleton();
    }

    public void LoadTexture() {
        if (textureAtlas!=null) {
            if(regionIndex >= 0 ) {
                atlasRegion = textureAtlas.findRegion(regionName, regionIndex);
            } else {
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
        if(skeletonData != null) {
            skeletonRenderer = new SkeletonRenderer();

            skeleton = new Skeleton(skeletonData);
            skeleton.setToSetupPose();

            skeleton.setPosition(HelperUtils.convertPixelsToUnits(positionX),
                    HelperUtils.convertPixelsToUnits(positionY));
            skeleton.getRootBone().setScale(scale);

            skeleton.updateWorldTransform();

            animationState = new AnimationState(new AnimationStateData(skeletonData));

            Animation animation = skeletonData.findAnimation("idle");
            assert (animation != null);

            animationState.setAnimation(0, animation, true);
            animationState.setTimeScale(animationSpeed);

            SkeletonBounds bounds = new SkeletonBounds();
            bounds.update(skeleton, true);
            Array<BoundingBoxAttachment> boundingBoxAttachmentArray = bounds.getBoundingBoxes();
            for (BoundingBoxAttachment attachment : boundingBoxAttachmentArray) {
                createBoundingBox(HelperUtils.convertFloatToVector2(attachment.getVertices(), scale));
            }

            skeleton.updateWorldTransform();

            hasAnimations = true;
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
        fixtureDef.filter.categoryBits = filterCategory;
        fixtureDef.filter.maskBits = filterMask;
        fixtureDef.filter.groupIndex = -1;

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
            } else if (texture!=null) {
                batch.draw(texture, HelperUtils.convertPixelsToUnits(positionX),
                        HelperUtils.convertPixelsToUnits(positionY),
                        HelperUtils.convertPixelsToUnits(atlasRegion.originalWidth),
                        HelperUtils.convertPixelsToUnits(atlasRegion.originalHeight));
            }
        }
    }
}
