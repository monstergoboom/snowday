package com.monstergoboom.snowday.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.input.GestureDetector;

public class SnowDay extends ApplicationAdapter{
    private PolygonSpriteBatch spriteBatch;
    private SpriteBatch fontSpriteBatch;
    private Camera camera;
    private Camera fontCamera;

    private PhysicsSystem physicsSystem;
    private SnowDayAssetManager snowDayAssetManager;

    private SantaClause santaClause;

    private Elf elf;
    private Snowman snowman;
    private Reindeer reindeer;

    private SnowGround snowGround;
    private Background background;
    private SnowflakeGenerator snowflakeGenerator;
    private PlayerHud playerHud;
    private ChristmasTree tree1;
    private OrnamentBlaster ornamentBlaster;

    private float bulletTime = 0.0f;

    private Controller controller;
    private PlayerControllerListener playerControllerListener;

    Sound winterWind;

    public void setup() {
        HelperUtils.updateUnitRatios(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 8f);

        fontCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fontCamera.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
        fontCamera.update();

        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        fontSpriteBatch = new SpriteBatch();
        fontSpriteBatch.setProjectionMatrix(normalProjection);

        camera = new OrthographicCamera(HelperUtils.viewPortWidth, HelperUtils.viewPortHeight);
        camera.translate(HelperUtils.viewPortWidth / 2, HelperUtils.viewPortHeight / 2, 0);
        camera.update();

        spriteBatch = new PolygonSpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);

        physicsSystem = new PhysicsSystem(camera, false);
    }

    public void initialize() {
        winterWind = Gdx.audio.newSound(Gdx.files.internal("sounds/winter-wind.ogg"));
        winterWind.loop(.5f);

        background = new Background("backdrop_1", physicsSystem.getWorld(),
                snowDayAssetManager.getTexture("backdrop_1"));

        santaClause = new SantaClause(75, 100, physicsSystem.getWorld(),
                snowDayAssetManager.getSkeletonData("santa"), snowDayAssetManager);


        elf = new Elf(400, 100, physicsSystem.getWorld(),
                snowDayAssetManager);

        //snowman = new Snowman(600,100, physicsSystem.getWorld(),
                //snowDayAssetManager.getSkeletonData("snowman"));

        //reindeer = new Reindeer(700, 100, physicsSystem.getWorld(),
                //snowDayAssetManager.getSkeletonData("reindeer"));

        tree1 = new ChristmasTree(900, 75, physicsSystem.getWorld(),
                snowDayAssetManager);

        snowGround = new SnowGround(physicsSystem.getWorld());
        snowflakeGenerator = new SnowflakeGenerator(
                "snowflake_generator", "", -1,
                "misc", "snow_flake", 2,
                0, Gdx.graphics.getHeight() + 100, Gdx.graphics.getWidth(), 60,
                500, 0.1f, 10,
                8, 16,
                5, 20,
                1, 3,
                physicsSystem.getWorld());

        snowflakeGenerator.start();

        TextureAtlas miscTextureAtlas = snowDayAssetManager.getTextureAtlas("misc");

        ornamentBlaster = new OrnamentBlaster(new RedOrnamentBullet(600, 600,
                physicsSystem.getWorld(),
                miscTextureAtlas.createSprite("red_ornament")),
                snowDayAssetManager);

        santaClause.setPrimaryWeapon(ornamentBlaster);

        playerHud = new PlayerHud(santaClause, snowDayAssetManager);

        Array<Controller> a = Controllers.getControllers();

        if (a.size > 0) {
            controller = Controllers.getControllers().first();
            if (controller != null) {
                controller = Controllers.getControllers().get(0);

                playerControllerListener = new PlayerControllerListener(santaClause);
                playerControllerListener.register(controller);
            }
        }
        else {
            // Gdx.input.setInputProcessor(new GestureDetector(new GestureInputListener(santaClause)));
            Gdx.input.setInputProcessor(new DesktopInputListener(santaClause));
            Gdx.app.log("Controllers", "no controllers attached to device");
        }
    }

    public void load() {
        snowDayAssetManager = new SnowDayAssetManager();
        snowDayAssetManager.load();
    }

    public void draw() {
        spriteBatch.begin();

        background.draw(spriteBatch);
        snowGround.draw(spriteBatch);

        tree1.draw(spriteBatch);
        elf.draw(spriteBatch);
        //snowman.draw(spriteBatch);
        //reindeer.draw(spriteBatch);
        santaClause.draw(spriteBatch);

        snowflakeGenerator.draw(spriteBatch);
        playerHud.draw(spriteBatch);

        spriteBatch.end();

        fontSpriteBatch.begin();
        snowflakeGenerator.drawText(fontSpriteBatch);
        playerHud.drawText(fontSpriteBatch);
        fontSpriteBatch.end();

        physicsSystem.debugInfo();
    }

    public void update(float animationDelta) {
        physicsSystem.update(animationDelta);

        background.update(animationDelta);
        snowGround.update(animationDelta);

        elf.update(animationDelta);
        //snowman.update(animationDelta);
        //reindeer.update(animationDelta);
        tree1.update(animationDelta);
        santaClause.update(animationDelta);
        snowflakeGenerator.update(animationDelta);
        playerHud.update(animationDelta);
    }

    @Override
    public void create () {
        setup();
        load();
    }

    @Override
    public void resize(int width, int height) {
        //camera.viewportHeight = (VIEWPORT_WIDTH / width) * height;
        //camera.update();
    }

    @Override
	public void render () {
        super.render();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearDepthf(1.0f);

        float animationDelta = Gdx.graphics.getDeltaTime();

        camera.update();
        fontCamera.update();

        if(!snowDayAssetManager.isFinishedLoading()) {
            snowDayAssetManager.update();
            if(snowDayAssetManager.getProgress() >= 1 ) {
                Gdx.app.log("SnowDay", "Assets loaded successfully.");
                initialize();
            } else {
                Gdx.app.log("SnowDay", "Loading assets: " + Float.toString(snowDayAssetManager.getProgress()) + " complete.");
            }
        } else {
            update(animationDelta);
            draw();
        }
	}

    @Override
    public void dispose() {
        super.dispose();

        winterWind.dispose();

        spriteBatch.dispose();
        fontSpriteBatch.dispose();
        snowDayAssetManager.dispose();
        physicsSystem.dispose();
    }
}
