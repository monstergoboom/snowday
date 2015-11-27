package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
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

    protected String movementActionState;
    protected String movementActionStatePrevious;
    protected boolean movementActionStateHasChanged;

    protected float hangTime;
    protected float hangTimeDelta;

    protected short filterCategory;
    protected short filterMask;

    protected boolean hasContact;

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

        movementActionState = "idle";
        movementActionStateHasChanged = false;
        movementActionStatePrevious = "idle";

        movementState = "idle";
        movementStatePrevious = "idle";
        movementStateHasChanged = false;

        filterCategory = (short)category;
        filterMask = (short)(mask);

        hangTime = .5f;
        hangTimeDelta = 0;

        hasContact = false;

        LoadSkeleton();
    }
    public boolean hasGroundContact() {
        return hasContact;
    }

    public void beginContact() {
        Gdx.app.log("Character", "start contact");
        hasContact = true;
    }

    public void endContact() {
        Gdx.app.log("Character", "end contact");
        hasContact = false;
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

    public void setMovementActionState(String value) {
        if (!movementActionState.equalsIgnoreCase(value)) {
            movementActionStatePrevious = movementActionState;
            movementActionState = value;
            movementActionStateHasChanged = true;
            needsUpdate = true;
        }
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

    public void createBoundingBox(Vector2[] vertices) {
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
        body.setUserData(this);

        polygonShape.dispose();

        hasBoundingBox = true;
    }

    public boolean isJumping() {
        return (movementActionState == "jump");
    }

    public boolean isWalking() {
        return (movementState == "walk");
    }

    public boolean isRunning() {
        return (movementState == "run");
    }

    public boolean isAttacking() {
        return (movementState == "attack");
    }

    public boolean isIdle() {
        return (movementState == "idle");
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
        skeleton.setPosition(x, y);
        needsUpdate = true;
    }

    public void update(float delta) {

        movementDelta += delta;

        if(movementStateHasChanged) {
            animationState.setAnimation(0, movementState, true);
            animationState.apply(skeleton);

            movementStateHasChanged = false;
        }
        else {
            if (movementDelta > movementSpeed) {
                if (isWalking()) {
                    Vector2 v = body.getLinearVelocity();

                    if (movementDirection < 0) {
                        if (!skeleton.getFlipX())
                            skeleton.setFlipX(true);
                        v.x = -1;
                    } else {
                        if (skeleton.getFlipX())
                            skeleton.setFlipX(false);
                        v.x = 1;
                    }

                    body.setLinearVelocity(v);
                }

                if (isJumping()) {
                    Vector2 v = body.getLinearVelocity();

                    float desired = 4;
                    float velChange = desired + v.y;

                    float impulse = body.getMass() * velChange;
                    Vector2 c = body.getWorldCenter();
                    body.applyLinearImpulse(new Vector2(0, impulse), c, true);

                    setMovementActionState("idle");
                }

                Vector2 pos = HelperUtils.convertPixelsToUnits(positionX, positionY);

                animationState.update(movementDelta);
                animationState.apply(skeleton);
                skeleton.updateWorldTransform();

                movementDelta = 0;
            }
        }

        needsUpdate = false;
    }

    public void draw(Batch batch) {
        skeletonRenderer.draw(batch, skeleton);
    }

    abstract void attack();
    abstract void run();
    abstract void walk(int direction);
    abstract void jump();
    abstract void doubleJump();
    abstract void idle();
    abstract void die();

    @Override
    public void updateWorldBody(float x, float y, float r) {
        setBodyPosition(x,y,r);
    }
}
