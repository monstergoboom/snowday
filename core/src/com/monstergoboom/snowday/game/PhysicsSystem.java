package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by amitrevski on 1/6/15.
 */
public class PhysicsSystem {
    private FPSLogger fpsLogger;
    private float animationTime;
    private boolean debug;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Camera camera;

    public PhysicsSystem(Camera c, boolean debugFlag) {
        animationTime = 0;

        Box2D.init();
        world = new World(new Vector2(0, HelperUtils.gravity), true);
        world.setAutoClearForces(false);

        camera = c;
        debug = debugFlag;

        configureDebug();

        world.setContactListener(new ContactSystemListener());
    }

    public World getWorld() {
        return world;
    }

    public void configureDebug() {
        fpsLogger = new FPSLogger();
        box2DDebugRenderer = new Box2DDebugRenderer(true, false, false, false, true, false);

        Gdx.app.log("Game", "display width: " + Integer.toString(Gdx.graphics.getWidth()) +
                ", display height: " + Integer.toString(Gdx.graphics.getHeight()));
    }

    public void debugInfo() {
        if(debug) {
            box2DDebugRenderer.render(world, camera.combined);
            fpsLogger.log();
        }
    }

    public void update(float animationDelta) {
        animationTime += animationDelta;
        if(animationTime > HelperUtils.worldStep) {
            Array<Body> bodies = new Array<>();
            world.getBodies(bodies);
            for (Body b : bodies) {
                Object obj = b.getUserData();
                if (obj instanceof IPhysicsComponent) {
                    ((IPhysicsComponent) obj).updateWorldBody(b.getPosition().x,
                            b.getPosition().y, b.getAngle());
                }
            }

            animationTime = 0;
        }

        world.step(HelperUtils.worldStep, HelperUtils.velocityIterations, HelperUtils.positionIterations);
        world.clearForces();
    }

    public void dispose() {
        box2DDebugRenderer.dispose();
        world.dispose();
    }
}
