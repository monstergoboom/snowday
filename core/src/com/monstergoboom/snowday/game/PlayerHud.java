package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by amitrevski on 12/28/14.
 */
public class PlayerHud {
    protected MagicGlobe magicGlobe;
    protected HealthGlobe healthGlobe;
    protected PlayerScore playerScore;
    protected RedGiftBox giftBox1;
    protected RedGiftBox giftBox2;
    protected RedGiftBox giftBox3;
    protected PlayerCharacter player;
    protected TextureRegion textureRegion;
    private BitmapFont font;

    public PlayerHud(PlayerCharacter player, SnowDayAssetManager asm) {
        this.player = player;

        healthGlobe = new HealthGlobe(25, Gdx.graphics.getHeight() - 150, 128, 128,
                player.getMaxHealth(),player.getCurrentHealth(),
                asm);

        magicGlobe = new MagicGlobe(125, Gdx.graphics.getHeight() - 150, 128, 128,
                player.getMaxMagic(),player.getCurrentMagic(),
                asm);

        playerScore = new PlayerScore(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 50, .1f,
                HelperUtils.playerScoreDefaultFormat, asm);

        giftBox1 = new RedGiftBox(25, Gdx.graphics.getHeight() - 240,
                asm);

        giftBox2 = new RedGiftBox(100, Gdx.graphics.getHeight() - 240,
                asm);

        giftBox3 = new RedGiftBox(175, Gdx.graphics.getHeight() - 240,
                asm);

        TextureAtlas.AtlasRegion weapons = asm.getTextureAtlas("misc_2").findRegion("green_ornament");

        textureRegion = new TextureRegion(weapons.getTexture(),
                weapons.getRegionX(), weapons.getRegionY(),
                weapons.getRegionWidth(), weapons.getRegionHeight());

        font = asm.getBitmapFont(HelperUtils.displayFontS);
        font.getData().scale(.1f);
    }

    public void update(float animationDelta) {
        healthGlobe.setCurrent(player.getCurrentHealth());
        healthGlobe.update(animationDelta);

        magicGlobe.setCurrent(player.getCurrentMagic());
        magicGlobe.update(animationDelta);

        playerScore.update(animationDelta);
        giftBox1.update(animationDelta);
        giftBox2.update(animationDelta);
        giftBox3.update(animationDelta);
    }

    public void draw(Batch batch) {
        healthGlobe.draw(batch);
        magicGlobe.draw(batch);

        giftBox1.draw(batch);
        giftBox2.draw(batch);
        giftBox3.draw(batch);

        batch.draw(textureRegion,
                HelperUtils.convertPixelsToUnits(25),
                HelperUtils.convertPixelsToUnits(10),
                HelperUtils.convertPixelsToUnits(30),
                HelperUtils.convertPixelsToUnits(30));
    }

    public void drawText(Batch fontBatch) {
        healthGlobe.drawText(fontBatch);
        magicGlobe.drawText(fontBatch);

        playerScore.drawText(fontBatch);

        font.draw(fontBatch,
                String.format("x %d", player.getItemInventoryCount("green_ornament") +
                        player.getPrimaryWeapon().getCurrentAmmo()),
                60,
                35);
    }
}
