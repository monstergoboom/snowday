package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
    protected float speed;
    protected float animationTime;
    protected boolean isAwake;

    protected Texture texture;
    protected TextureAtlas.AtlasRegion atlasRegion;

    public Snowflake(int startX, int startY, int particleWidth, int particleHeight,
                     float particleLife, float particleSpeed,
                     Texture t, TextureAtlas.AtlasRegion ar) {
        x = startX;
        y = startY;
        width = particleWidth;
        height = particleHeight;
        life = particleLife;
        speed = particleSpeed;
        animationTime = 0;

        texture = t;
        atlasRegion = ar;
        isAwake = false;
    }

    public void reset(int startX, int startY, float particleLife, float particleSpeed) {
        x = startX;
        y = startY;
        life = particleLife;
        speed = particleSpeed;

        setPosition(x, y);
        setLife(life);
        setSpeed(speed);

        awake();
    }

    public void setPosition(int updateX, int updateY) {
        x = updateX;
        y = updateY;
    }

    public void setLife(float particleLife) {
        life = particleLife;
    }

    public void setSpeed(float particleSpeed) {
        speed = particleSpeed;
    }

    public void awake() {
        isAwake = true;
    }

    public void sleep() {
        isAwake = false;
    }

    public boolean update(float animationDelta) {
        animationTime += animationDelta;
        life -= animationDelta;
        if(life<=0) {
            life = 0;
            return false;
        }

        if(animationTime >= HelperUtils.worldStep) {
            y-=speed;
            if(y<= -height) {
                return false;
            }
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
}
