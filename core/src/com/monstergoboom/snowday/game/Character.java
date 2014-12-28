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

import java.util.UUID;

/**
 * Created by amitrevski on 12/23/14.
 */
public abstract class Character extends GameObject {
    private UUID id;

    private AssetManager assetManager;
    private Skeleton skeleton;
    private SkeletonRenderer skeletonRenderer;
    private SkeletonData skeletonData;
    private AnimationState animationState;
    private TextureAtlas textureAtlas;
    private TextureAtlasLoader textureAtlasLoader;

    protected int positionX;
    protected int positionY;
    protected float animationSpeed;
    protected float movementSpeed;
    protected float scale;
    protected String assetName;
    protected boolean hasBoundingBox;
    protected boolean needsUpdate;

    protected Body body;
    protected World world;

    public Character(String assetNameParam, float drawScale, int x, int y, World b2World) {
        positionX = x;
        positionY = y;
        assetName = assetNameParam;
        id = UUID.randomUUID();
        animationSpeed = 1.0f;
        movementSpeed = 1.0f;
        scale = HelperUtils.pixelsToUnitsRatio * drawScale;
        assetManager = new AssetManager(new InternalFileHandleResolver());
        textureAtlasLoader = new TextureAtlasLoader(new InternalFileHandleResolver());
        hasBoundingBox = false;
        needsUpdate = true;
        body = null;
        world = b2World;

        LoadTextureAtlas();
        LoadSkeleton();
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
    }

    private void LoadSkeleton() {
        skeletonRenderer = new SkeletonRenderer();
        SkeletonJson json = new SkeletonJson(textureAtlas);
        json.setScale(scale);

        FileHandle fhJson = Gdx.files.internal(assetName + ".json");
        skeletonData = json.readSkeletonData(fhJson);

        skeleton = new Skeleton(skeletonData);
        skeleton.setToSetupPose();
        skeleton = new Skeleton(skeleton);
        skeleton.setPosition(HelperUtils.convertPixelsToUnits(positionX),
                HelperUtils.convertPixelsToUnits(positionY));

        skeleton.updateWorldTransform();

        animationState = new AnimationState(new AnimationStateData(skeletonData));

        Animation animation = skeletonData.findAnimation("idle");
        assert(animation != null);

        animationState.setAnimation(0, animation, true);
        animationState.setTimeScale(animationSpeed);

        SkeletonBounds bounds = new SkeletonBounds();
        bounds.update(skeleton, true);
        Array<BoundingBoxAttachment> boundingBoxAttachmentArray = bounds.getBoundingBoxes();
        for(BoundingBoxAttachment attachment: boundingBoxAttachmentArray) {
            createBoundingBox(HelperUtils.convertFloatToVector2(attachment.getVertices()));
        }

        skeleton.updateWorldTransform();
    }

    public void createBoundingBox(Vector2[] vertices)
    {
        if(body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            Vector2 position = HelperUtils.convertPixelsToUnits(positionX, positionY);
            bodyDef.position.set(position);
            bodyDef.fixedRotation = true;

            body = world.createBody(bodyDef);
            body.setUserData(this);
            body.setTransform(position, 0.0f);
        }

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 1.0f;
        fixtureDef.density = 0.5f;

        body.createFixture(fixtureDef);

        polygonShape.dispose();

        hasBoundingBox = true;
    }

    public UUID getId() {
        return id;
    }

    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;

        Vector2 pos = HelperUtils.convertPixelsToUnits(positionX, positionY);
        skeleton.setPosition(pos.x, pos.y);

        needsUpdate = true;
    }

    public void setPosition(float x, float y) {
        skeleton.setPosition(x,y);
        needsUpdate = true;
    }

    public void update(float delta) {
        animationState.update(delta);
        animationState.apply(skeleton);

        skeleton.updateWorldTransform();

        needsUpdate = false;
    }

    public void draw(Batch batch) {
        skeletonRenderer.draw(batch, skeleton);
    }

    abstract void attack();
    abstract void run();
    abstract void walk();
    abstract void jump();
    abstract void idle();
    abstract void die();
}
