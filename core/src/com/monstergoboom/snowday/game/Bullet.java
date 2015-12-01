package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 1/5/15.
 */
public abstract class Bullet extends GameObject implements IPhysicsComponent {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float angle;
    protected boolean needsUpdate;

    protected World world;
    protected Body body;
    protected Fixture fixture;

    protected float animationTime;

    Sprite sprite;

    public Bullet(int posX, int posY, String gameObjectName, String gameObjectCategory, String regionName, int regionIndex, World w, TextureAtlas ta) {
        super(gameObjectName, gameObjectCategory);

        x = posX;
        y = posY;
        world = w;
        animationTime = 0;

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
        super(gameObjectName, gameObjectCategory);

        x = posX;
        y = posY;
        world = w;
        animationTime = 0;

        sprite = new Sprite(t);

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
        bodyDef.linearDamping = 5.0f;

        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.setTransform(HelperUtils.convertPixelsToUnits(x + width/2, y + height/2), 0.0f);

        CircleShape shape = new CircleShape();
        shape.setRadius(HelperUtils.convertPixelsToUnits(width/2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.1f;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.1f;

        fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void shoot(float impulseX, float impulseY) {
        body.applyLinearImpulse(impulseX, impulseY,
                HelperUtils.convertPixelsToUnits(x),
                HelperUtils.convertPixelsToUnits(y),
                true);
    }

    public void setBodyPosition(float x, float y, float r) {
        this.x = HelperUtils.convertUnitsToPixel(x) - width/2;
        this.y = HelperUtils.convertUnitsToPixel(y) - height/2;
        angle = MathUtils.radiansToDegrees * r;
        angle = 0;
        needsUpdate = true;
    }

    public void update(float animationDelta) {
        if(animationTime > HelperUtils.worldStep) {
            if (needsUpdate) {
                sprite.setPosition(HelperUtils.convertPixelsToUnits(x),
                        HelperUtils.convertPixelsToUnits(y));
                sprite.setRotation(angle);
            }

            needsUpdate = false;
            animationTime = 0;
        } else {
            animationTime += animationDelta;
        }
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void updateWorldBody(float x, float y, float r) {
        setBodyPosition(x,y,r);
    }
}
