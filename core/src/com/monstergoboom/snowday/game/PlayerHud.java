package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

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

    public PlayerHud(SnowDayAssetManager asm) {
        healthGlobe = new HealthGlobe(25, Gdx.graphics.getHeight() - 150, 128, 128, 100,100,
                asm);

        magicGlobe = new MagicGlobe(125, Gdx.graphics.getHeight() - 150, 100, 100, 100,100,
                asm);

        playerScore = new PlayerScore(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - 50, .1f,
                HelperUtils.playerScoreDefaultFormat, asm);

        giftBox1 = new RedGiftBox(25, Gdx.graphics.getHeight() - 250,
                asm);

        giftBox2 = new RedGiftBox(100, Gdx.graphics.getHeight() - 250,
                asm);

        giftBox3 = new RedGiftBox(175, Gdx.graphics.getHeight() - 250,
                asm);
    }

    public void update(float animationDelta) {
        healthGlobe.update(animationDelta);
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
    }

    public void drawText(Batch fontBatch) {
        healthGlobe.drawText(fontBatch);
        magicGlobe.drawText(fontBatch);

        playerScore.drawText(fontBatch);
    }
}
