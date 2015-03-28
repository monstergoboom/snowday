package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.attachments.BoundingBoxAttachment;

import java.util.UUID;

/**
 * Created by amitrevski on 12/23/14.
 */
public abstract class Character extends GameObject implements IPhysicsComponent {
    private UUID id;

    protected Skeleton skeleton;
    protected SkeletonRenderer skeletonRenderer;
    protected SkeletonData skeletonData;
    protected AnimationState animationState;

    protected int positionX;
    protected int positionY;
    protected float animationSpeed;
    protected float movementSpeed;
    protected float movementDelta;
    protected float scale;
    protected String assetName;
    protected boolean hasBoundingBox;
    protected boolean needsUpdate;

    protected Body body;
    protected World world;

    protected String movementState;
    protected String movementStatePrevious;
    protected int movementDirection;
    protected boolean movementStateHasChanged;
    protected float hangTime;
    protected float hangTimeDelta;

    protected short filterCategory;
    protected short filterMask;

    public Character(String assetNameParam, float drawScale, int x, int y,
                     World b2World, SkeletonData sd,
                     int category, int mask) {
        positionX = x;
        positionY = y;
        assetName = assetNameParam;
        id = UUID.randomUUID();
        animationSpeed = 1.0f;
        movementSpeed = 0.01678f;
        movementDelta = 0;
        scale = drawScale;
        hasBoundingBox = false;
        needsUpdate = true;
        body = null;
        world = b2World;
        skeletonData = sd;
        skeletonRenderer = new SkeletonRenderer();

        movementState = "idle";
        movementStateHasChanged = false;

        filterCategory = (short)category;
        filterMask = (short)(mask);

        hangTime = .5f;
        hangTimeDelta = 0;

        LoadSkeleton();
    }

    public void setMovementState(String value) {
        if(!movementState.equalsIgnoreCase(value)) {
            movementStatePrevious = movementState;
            movementState = value;
            movementStateHasChanged = true;
            needsUpdate = true;
        }
    }

    public void setMovementDirection(int direction) {
        movementDirection = direction;
    }

    private void LoadSkeleton() {
        if(skeletonData != null ) {
            skeleton = new Skeleton(skeletonData);
            skeleton.setToSetupPose();

            skeleton.setPosition(HelperUtils.convertPixelsToUnits(positionX),
                    HelperUtils.convertPixelsToUnits(positionY));
            skeleton.getRootBone().setScale(scale);

            skeleton.updateWorldTransform();

            animationState = new AnimationState(new AnimationStateData(skeletonData));

            animationState.setAnimation(0, "idle", true);
            animationState.setTimeScale(animationSpeed);

            SkeletonBounds bounds = new SkeletonBounds();
            bounds.update(skeleton, true);
            Array<BoundingBoxAttachment> boundingBoxAttachmentArray = bounds.getBoundingBoxes();
            for (BoundingBoxAttachment attachment : boundingBoxAttachmentArray) {
                createBoundingBox(HelperUtils.convertFloatToVector2(attachment.getVertices(), scale));
            }

            skeleton.updateWorldTransform();
        }
    }

    public void createBoundingBox(Vector2[] vertices)
    {
        if(body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            Vector2 position = HelperUtils.convertPixelsToUnits(positionX, positionY);
            bodyDef.position.set(position);
            bodyDef.fixedRotation = true;
            bodyDef.linearDamping = 0f;
            bodyDef.linearVelocity.y = -5f;

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
        fixtureDef.density = 25f;
        fixtureDef.filter.categoryBits = filterCategory;
        fixtureDef.filter.maskBits = filterMask;
        fixtureDef.filter.groupIndex = -1;

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

    public void setBodyPosition(float x, float y, float r) {
        skeleton.setPosition(x,y);
        needsUpdate = true;
    }

    public void update(float delta) {
        movementDelta += delta;

        if(movementStateHasChanged) {
            if(movementState == "jump") {
                animationState.setAnimation(1, movementState, false);
            }
            else {
                animationState.setAnimation(0, movementState, true);
            }

            movementStateHasChanged = false;
        }

        if(movementDelta > movementSpeed) {
            Vector2 pos = HelperUtils.convertPixelsToUnits(positionX, positionY);
            if (movementState == "idle") {
                body.setLinearVelocity(0f, 0f);
            } else if (movementState == "walk") {
                if(movementDirection > 0) {
                    body.setLinearVelocity(1.5f, 0f);
                    skeleton.setFlipX(false);
                }
                else {
                    body.setLinearVelocity(-1.5f, 0f);
                    skeleton.setFlipX(true);
                }
            } else if (movementState == "jump") {
                float x = 0.0f;
                if(movementStatePrevious == "walk")
                    if(movementDirection > 0)
                        x = 1.f;
                    else
                        x = -1.5f;

                hangTimeDelta += delta;
                float y = .75f;
                if(hangTimeDelta >= hangTime) {
                    y = -5f;
                    hangTimeDelta = 0;
                    setMovementState(movementStatePrevious);
                }

                body.setLinearVelocity(x, y);
            }

            movementDelta = 0;
        }

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
    abstract void walk(int direction);
    abstract void jump();
    abstract void idle();
    abstract void die();

    @Override
    public void updateWorldBody(float x, float y, float r) {
        setBodyPosition(x,y,r);
    }
}
