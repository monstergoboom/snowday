package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.Skin.SkinEntry;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by amitrevski on 12/23/14.
 */
public abstract class Character extends GameObject implements PhysicsComponent {
    private UUID id;

    protected Skeleton skeleton;
    protected SkeletonRenderer skeletonRenderer;
    protected SkeletonData skeletonData;
    protected AnimationState animationState;

    protected int positionX;
    protected int positionY;
    protected float animationSpeed;
    protected float movementSpeed;
    protected float speed;
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

    protected short filterCategory;
    protected short filterMask;

    protected boolean hasGroundContact;

    protected int maxHealth;
    protected int currentHealth;
    protected int maxMagic;
    protected int currentMagic;

    public Character(String assetNameParam, float drawScale, int x, int y,
                     String gameObjectName, String gameObjectCategory,
                     World b2World, SkeletonData sd,
                     int category, int mask) {

        super(gameObjectName, gameObjectCategory);

        positionX = x;
        positionY = y;
        assetName = assetNameParam;
        id = UUID.randomUUID();
        animationSpeed = 1.7f;
        movementSpeed = 0.03f;
        speed = 15.0f;
        scale = drawScale;
        hasBoundingBox = false;
        needsUpdate = true;
        body = null;
        world = b2World;
        skeletonData = sd;
        skeletonRenderer = new SkeletonRenderer();
        animationState = null;

        movementState = "idle";
        movementStatePrevious = "not_set";
        movementStateHasChanged = true;

        filterCategory = (short) category;
        filterMask = (short) (mask);

        hasGroundContact = false;

        LoadSkeleton();
    }

    public boolean hasGroundContact() {
        return hasGroundContact;
    }

    public void beginContact(GameObject contactWith) {
        if (contactWith.getReferenceCategory() == "snow_ground") {
            hasGroundContact = true;
        }
    }

    public void endContact(GameObject contactWith) {
        if (contactWith.getReferenceCategory() == "snow_ground") {
            hasGroundContact = false;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxMagic() {
        return maxMagic;
    }

    public int getCurrentMagic() {
        return currentMagic;
    }

    public void setMovementState(String value) {
        if (!movementState.equalsIgnoreCase(value)) {
            movementStatePrevious = movementState;
            movementState = value;
            movementStateHasChanged = true;
            needsUpdate = true;
        }
    }

    public void setMovementDirection(int direction) {
        if ( movementDirection != direction ) {
            movementDirection = direction;

            if (movementDirection < 0) {
                skeleton.getRootBone().setScaleX(-skeleton.getRootBone().getScaleX());
            } else {
                skeleton.getRootBone().setScaleX(Math.abs(skeleton.getRootBone().getScaleX()));
            }
        }
    }

    private void LoadSkeleton() {
        if (skeletonData != null) {
            skeleton = new Skeleton(skeletonData);
            skeleton.setToSetupPose();

            skeleton.setPosition(HelperUtils.convertPixelsToUnits(positionX),
                    HelperUtils.convertPixelsToUnits(positionY));
            skeleton.getRootBone().setScale(scale);

            skeleton.updateWorldTransform();

            animationState = new AnimationState(new AnimationStateData(skeletonData));

            animationState.setAnimation(0, "idle", true);
            animationState.setTimeScale(animationSpeed);

            List<SkinEntry> attachments = Arrays.asList(skeleton.getData().getDefaultSkin()
                    .getAttachments().toArray());

            attachments.forEach(skinEntry -> {
                if (skinEntry.getAttachment() instanceof RegionAttachment) {
                    RegionAttachment regionAttachment = (RegionAttachment) skinEntry.getAttachment();
                    createBoundingBox(regionAttachment.getUVs());
                }
            });

            skeleton.updateWorldTransform();
        }
    }

    public void createBoundingBox(float[] vertices) {
        if (body == null) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            Vector2 position = HelperUtils.convertPixelsToUnits(positionX, positionY);
            bodyDef.position.set(position);
            bodyDef.fixedRotation = true;
            bodyDef.linearDamping = 0.0f;

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
        fixtureDef.density = 1.26f;
        fixtureDef.filter.categoryBits = filterCategory;
        fixtureDef.filter.maskBits = filterMask;
        fixtureDef.filter.groupIndex = -1;

        MassData massData = new MassData();
        massData.mass = 75f;

        body.setMassData(massData);
        body.createFixture(fixtureDef);
        body.setUserData(this);

        polygonShape.dispose();

        hasBoundingBox = true;
    }

    public boolean isJumping() {
        return (movementState.equalsIgnoreCase("jump")
                || movementState.equalsIgnoreCase("double_jump"));
    }

    public boolean isPreviousJumping() {
        return (movementStatePrevious.equalsIgnoreCase("jump")
                || movementStatePrevious.equalsIgnoreCase("double_jump"));
    }

    public boolean isWalking() {
        return (movementState.equalsIgnoreCase("walk"));
    }

    public boolean isRunning() {
        return (movementState.equalsIgnoreCase("run"));
    }

    public boolean isAttacking() {
        return (movementState.equalsIgnoreCase("attack")
                || movementState.equalsIgnoreCase("secondary_attack"));
    }

    public boolean isIdle() {
        return (movementState.equalsIgnoreCase("idle"));
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
        Vector2 linearVelocity = body.getLinearVelocity();

        if (isJumping()) {
            body.setLinearVelocity(linearVelocity.x, 5f);
            setMovementState(movementStatePrevious);
        }

        if ((isWalking() || isAttacking()) && movementStatePrevious != "jump") {
            body.setLinearVelocity(movementDirection * speed, linearVelocity.y);
        }

        if (isAttacking()) {
            setMovementState(movementStatePrevious);
        }

        animationState.update(delta);
        animationState.apply(skeleton);

        skeleton.updateWorldTransform();

        positionX = HelperUtils.convertUnitsToPixel(body.getPosition().x);
        positionY = HelperUtils.convertUnitsToPixel(body.getPosition().y);
    }

    public void draw(Batch batch) {
        skeletonRenderer.draw(batch, skeleton);
    }

    protected void attack() {
        if (!isAttacking()) {
            animationState.setAnimation(1, "shoot", false);
            setMovementState("attack");
        }
    }

    protected void secondaryAttack() {
        if (!isAttacking()) {
            animationState.setAnimation(1, "attack", false);
            setMovementState("secondary_attack");
        }
    }

    protected void run() {
        setMovementState("run");
    }

    protected void walk(int direction) {
        if (!isWalking()) {
            animationState.setAnimation(0, "walk", true);
            setMovementState("walk");
            setMovementDirection(direction);
        }
    }

    protected void jump() {
        if ( !isJumping()) {
            animationState.setAnimation(0, "jump", false);
            setMovementState("jump");
        }
    }

    protected void doubleJump() {
        setMovementState("double_jump");
    }

    protected void idle() {
        if (!isIdle()) {
            animationState.setAnimation(0, "idle", true);
            setMovementState("idle");
        }
    }

    protected void die() {
        setMovementState("die");
    }

    @Override
    public void updateWorldBody(float x, float y, float r) {
        setBodyPosition(x, y, r);
    }

    @Override
    public void hide() {
    }

    @Override
    public void show() {
    }

    public abstract Weapon getPrimaryWeapon();
}
