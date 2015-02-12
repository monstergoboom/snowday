package com.monstergoboom.snowday.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;

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

    private RedOrnamentBullet bullet;
    private float bulletTime = 0.0f;

    public void setup() {
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

        Gdx.input.setInputProcessor(new GestureDetector(new GestureInputListener()));
    }

    public void initialize() {
        background = new Background("backdrop_1", physicsSystem.getWorld(),
                snowDayAssetManager.getTexture("backdrop_1"));

        santaClause = new SantaClause(100, 100, physicsSystem.getWorld(),
                snowDayAssetManager.getSkeletonData("santa"));
        elf = new Elf(400, 100, physicsSystem.getWorld(),
                snowDayAssetManager);
        snowman = new Snowman(600,100, physicsSystem.getWorld(),
                snowDayAssetManager.getSkeletonData("snowman"));
        reindeer = new Reindeer(700, 100, physicsSystem.getWorld(),
                snowDayAssetManager.getSkeletonData("reindeer"));

        tree1 = new ChristmasTree(1200, 75, physicsSystem.getWorld(),
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

        playerHud = new PlayerHud(snowDayAssetManager);
        bullet = new RedOrnamentBullet(300, 1200, physicsSystem.getWorld(),
                snowDayAssetManager);
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

        santaClause.draw(spriteBatch);
        elf.draw(spriteBatch);
        snowman.draw(spriteBatch);
        reindeer.draw(spriteBatch);

        snowflakeGenerator.draw(spriteBatch);

        playerHud.draw(spriteBatch);

        bullet.draw(spriteBatch);

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
        santaClause.update(animationDelta);
        elf.update(animationDelta);
        snowman.update(animationDelta);
        reindeer.update(animationDelta);
        snowGround.update(animationDelta);
        tree1.update(animationDelta);
        playerHud.update(animationDelta);
        snowflakeGenerator.update(animationDelta);
        bullet.update(animationDelta);

        if(bulletTime > 5) {
            bullet.shoot(0.00001f,0.01f);
            bulletTime = 0f;
        }
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

        spriteBatch.dispose();
        fontSpriteBatch.dispose();
        snowDayAssetManager.dispose();
        physicsSystem.dispose();
    }
}
