package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

/**
 * Created by amitrevski on 1/5/15.
 */
public abstract class Bullet extends GameObject implements PhysicsComponent, Cloneable {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float angle;
    protected boolean needsUpdate;
    protected boolean isVisible;

    protected World world;
    protected Body body;
    protected Fixture fixture;

    protected float animationTime;

    protected Sprite sprite;
    static Random random = new Random(Instant.now().getEpochSecond());

    public Bullet(int posX, int posY, String gameObjectName, String gameObjectCategory, String regionName, int regionIndex, World w, TextureAtlas ta) {
        super(gameObjectName + "_" + random.nextInt(100), gameObjectCategory);

        x = posX;
        y = posY;
        world = w;
        animationTime = 0;
        isVisible = true;

        if( regionIndex >= 0 ) {
            sprite = ta.createSprite(regionName, regionIndex);
        }
        else {
            sprite = ta.createSprite(regionName);
        }

        sprite.setPosition(HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y));
        sprite.setSize(HelperUtils.convertPixelsToUnits((int)sprite.getWidth()),
                HelperUtils.convertPixelsToUnits((int)sprite.getHeight()));

        createBody();

        needsUpdate = true;
    }

    public Bullet(int posX, int posY, String gameObjectName, String gameObjectCategory, World w, Texture t) {
        super(gameObjectName + "_" + random.nextInt(100), gameObjectCategory);

        x = posX;
        y = posY;
        world = w;
        animationTime = 0;
        isVisible = true;

        sprite = new Sprite(t);

        sprite.setPosition(HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y));
        sprite.setSize(HelperUtils.convertPixelsToUnits((int)sprite.getWidth()),
                HelperUtils.convertPixelsToUnits((int)sprite.getHeight()));

        createBody();

        needsUpdate = true;
    }

    public Bullet(int posX, int posY, String gameObjectName, String gameObjectCategory, World w, Sprite s) {
        super(gameObjectName + "_" + random.nextInt(100), gameObjectCategory);

        x = posX;
        y = posY;
        world = w;
        animationTime = 0;
        isVisible = true;

        sprite = s;

        sprite.setPosition(HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y));
        sprite.setSize(HelperUtils.convertPixelsToUnits((int)sprite.getWidth()),
                HelperUtils.convertPixelsToUnits((int)sprite.getHeight()));

        createBody();

        needsUpdate = true;
    }

    protected void createBody() {
        width = sprite.getRegionWidth();
        height = sprite.getRegionHeight();

        BodyDef bodyDef = new BodyDef();
        bodyDef.bullet = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(HelperUtils.convertPixelsToUnits(x + width/2, y + height/2));
        bodyDef.linearDamping = 1.7f;

        body = world.createBody(bodyDef);
        body.setUserData(this);

        MassData massData = new MassData();
        massData.mass = 0.04f;

        body.setMassData(massData);

        CircleShape shape = new CircleShape();
        shape.setRadius(HelperUtils.convertPixelsToUnits(width/2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.01f;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.0f;
        fixtureDef.filter.groupIndex = -1;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void shoot(float impulseX, float impulseY, int movementDirection) {
        body.setLinearVelocity(impulseX * movementDirection, impulseY);
        needsUpdate = true;
    }

    public void setBodyPosition(float x, float y, float r) {
        this.x = HelperUtils.convertUnitsToPixel(x) - width/2;
        this.y = HelperUtils.convertUnitsToPixel(y) - height/2;
        angle = MathUtils.radiansToDegrees * r;
        needsUpdate = true;
    }

    public void update(float animationDelta) {
        setBodyPosition(body.getPosition().x, body.getPosition().y, 0);

        sprite.setPosition(HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y));
    }

    public void draw(Batch batch) {
        if (isVisible) {
            sprite.draw(batch);
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;

        body.setTransform(HelperUtils.convertPixelsToUnits(x, y), 0);
    }

    @Override
    public void updateWorldBody(float x, float y, float r) {
        setBodyPosition(x,y,r);
    }

    @Override
    public void hide() {
        isVisible = false;
    }

    @Override
    public void show() {
        isVisible = true;
    }

    abstract Object copy();
}
