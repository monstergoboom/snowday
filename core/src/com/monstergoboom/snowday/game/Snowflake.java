package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by amitrevski on 12/27/14.
 */
public class Snowflake {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected float angle;
    protected float life;

    protected Texture texture;
    protected TextureAtlas.AtlasRegion atlasRegion;

    protected Body body;
    protected World world;
    protected Fixture fixture;

    public Snowflake(int startX, int startY, int particleWidth, int particleHeight,
                     float particleLife,
                     Texture t, TextureAtlas.AtlasRegion ar, World w) {
        x = startX;
        y = startY;
        width = particleWidth;
        height = particleHeight;
        life = particleLife;

        texture = t;
        atlasRegion = ar;
        world = w;
        body = null;

        AssignFixture();
    }

    public void AssignFixture() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(HelperUtils.convertPixelsToUnits(x + width/2,y + height/2));
        bodyDef.active = false;
        bodyDef.awake = false;
        bodyDef.allowSleep = true;
        bodyDef.linearDamping = 1.0f;
        bodyDef.gravityScale = 0.05f;
        bodyDef.bullet = true;

        body = world.createBody(bodyDef);
        body.setUserData(this);
        body.setTransform(HelperUtils.convertPixelsToUnits(x + width/2,y + height/2), 0.0f);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(HelperUtils.convertPixelsToUnits(width/2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.restitution = 0.0f;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 1.0f;
        fixtureDef.isSensor = true;

        fixture = body.createFixture(fixtureDef);

        sleep();

        circleShape.dispose();
    }

    public void reset(int startX, int startY, int particleLife) {
        x = startX;
        y = startY;
        life = particleLife;

        setPosition(x, y, true);
        setLife(life);

        awake();
    }

    public void setPosition(int updateX, int updateY, boolean updateBody) {
        x = updateX;
        y = updateY;

        Vector2 v = new Vector2(HelperUtils.convertPixelsToUnits(x, y));
        if(updateBody)
            body.setTransform(v, 0.0f);
    }

    public void setBodyPosition(float updateX, float updateY, float rotation) {
        x = HelperUtils.convertUnitsToPixel(updateX);
        y = HelperUtils.convertUnitsToPixel(updateY);

        x = x - width/2;
        y = y - width/2;

        angle = rotation;
    }

    public void setLife(float particleLife) {
        life = particleLife;
    }

    public void awake() {
        body.setAwake(true);
        body.setActive(true);
    }

    public void sleep() {
        body.setAwake(false);
        body.setActive(false);
    }

    public boolean update(float animiationTime) {
        life -= animiationTime;
        if(life<=0) {
            life = 0;
            return false;
        }

        return true;
    }

    public void draw(Batch batch) {
        if(life>0) {
            if(texture!=null) {
                batch.draw(texture, HelperUtils.convertPixelsToUnits(x),
                        HelperUtils.convertPixelsToUnits(y),
                        HelperUtils.convertPixelsToUnits(width),
                        HelperUtils.convertPixelsToUnits(height));
            } else {
                if (atlasRegion != null) {
                    batch.draw(atlasRegion, HelperUtils.convertPixelsToUnits(x),
                            HelperUtils.convertPixelsToUnits(y),
                            HelperUtils.convertPixelsToUnits(width),
                            HelperUtils.convertPixelsToUnits(height));
                }
            }
        }
    }

    public void cleanUp() {
        body.destroyFixture(fixture);
    }
}
