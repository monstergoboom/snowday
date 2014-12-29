package com.monstergoboom.snowday.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class SnowDay extends ApplicationAdapter implements ControllerListener {
    private PolygonSpriteBatch spriteBatch;

    private SantaClause santaClause;
    private Elf elf;
    private Snowman snowman;
    private Reindeer reindeer;
    private SnowGround snowGround;
    private StaticGameObject tree1;
    private Background background;
    private SnowflakeGenerator snowflakeGenerator;

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Camera camera;

    private Camera fontCamera;
    private SpriteBatch fontSpriteBatch;

    private Array<Controller> controllers;

    @Override
	public void create () {
        controllers = Controllers.getControllers();

        Box2D.init();
        fontCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fontCamera.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
        fontCamera.update();

        fontSpriteBatch = new SpriteBatch();

        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        fontSpriteBatch.setProjectionMatrix(normalProjection);

        world = new World(new Vector2(0, HelperUtils.gravity), true);
        camera = new OrthographicCamera(HelperUtils.viewPortWidth, HelperUtils.viewPortHeight);

        // Reset the camera view to 0,0 bottom,left of the screen
        camera.translate(HelperUtils.viewPortWidth / 2, HelperUtils.viewPortHeight / 2, 0);
        camera.update();

        spriteBatch = new PolygonSpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);

        background = new Background("backdrop_1");

        santaClause = new SantaClause(100, 100, world);
        elf = new Elf(400, 100, world);
        snowman = new Snowman(600,100, world);
        reindeer = new Reindeer(700, 100, world);

        tree1 = new StaticGameObject("tree","tree", -1, 1200, 75, 1.0f, world);

        snowGround = new SnowGround(world);
        snowflakeGenerator = new SnowflakeGenerator(
                "snowflake_generator", "", -1,
                "misc", "snow_flake", 2,
                0, Gdx.graphics.getHeight() - 60, Gdx.graphics.getWidth(), 60,
                60, 0.25f, 1,
                16, 48,
                5,20,
                world);

        snowflakeGenerator.start();

        box2DDebugRenderer = new Box2DDebugRenderer(true, false, false, false, true, false);

        Controllers.addListener(this);

        for(Controller controller: controllers) {
            Gdx.app.log("snow day controller test", controller.getName());
        }

        Gdx.app.log("Game", "display width: " + Integer.toString(Gdx.graphics.getWidth()) +
        ", display height: " + Integer.toString(Gdx.graphics.getHeight()));
    }

    @Override
    public void resize(int width, int height) {
        //camera.viewportHeight = (VIEWPORT_WIDTH / width) * height;
        //camera.update();
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float animationTime = Gdx.graphics.getDeltaTime();

        camera.update();
        fontCamera.update();

        updateGameObjects(animationTime);

        drawGameObjects();

        postDraw();

        updateWorldBodies();

        debugInfo();
	}

    public void postDraw() {
        world.step(HelperUtils.worldStep, HelperUtils.velocityIterations, HelperUtils.positionIterations);
        world.clearForces();
    }

    public void debugInfo() {
        box2DDebugRenderer.render(world, camera.combined);
    }

    public void drawGameObjects() {
        spriteBatch.begin();

        background.draw(spriteBatch);

        snowGround.draw(spriteBatch);
        tree1.draw(spriteBatch);

        santaClause.draw(spriteBatch);
        elf.draw(spriteBatch);
        snowman.draw(spriteBatch);
        reindeer.draw(spriteBatch);

        snowflakeGenerator.draw(spriteBatch);

        spriteBatch.end();

        fontSpriteBatch.begin();
        snowflakeGenerator.drawText(fontSpriteBatch);
        fontSpriteBatch.end();
    }

    public void updateWorldBodies() {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for(Body b: bodies) {
            Object obj = b.getUserData();
            if(obj instanceof GameObject) {
                GameObject o = (GameObject) obj;

                if (o != null) {
                    o.setPosition(b.getPosition().x, b.getPosition().y);
                }
            }
            else if(obj instanceof Snowflake) {
                Snowflake sf = (Snowflake) obj;
                if(sf!= null) {
                    sf.setBodyPosition(b.getPosition().x, b.getPosition().y, b.getAngle());
                }
            }
        }
    }

    public void updateGameObjects(float animationTime) {
        background.update(animationTime);
        santaClause.update(animationTime);
        elf.update(animationTime);
        snowman.update(animationTime);
        reindeer.update(animationTime);
        snowGround.update(animationTime);
        tree1.update(animationTime);
        snowflakeGenerator.update(animationTime);
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();

        spriteBatch.dispose();
        fontSpriteBatch.dispose();
    }
}
