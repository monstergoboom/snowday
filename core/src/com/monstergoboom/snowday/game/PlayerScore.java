package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by amitrevski on 12/29/14.
 */
public class PlayerScore {
    private int score;
    private int x;
    private int y;
    private float scale;
    private int renderX;
    private int renderY;
    private String scoreText;
    private String format;
    private BitmapFont font;
    private boolean needsUpdate;
    SnowDayAssetManager assetManager;

    public PlayerScore(int positionX, int positionY, float fontScale, String formatString,
                       SnowDayAssetManager asm) {
        assetManager = asm;
        score = 0;
        font = assetManager.getBitmapFont(HelperUtils.displayFontM);

        x = positionX;
        y = positionY;
        renderX = x;
        renderY = y;

        scale = fontScale;

        format = formatString;

        needsUpdate = true;
    }

    public void changeFont(String fontName) {
        font = assetManager.getBitmapFont(fontName);
    }

    public void updateScore(int value) {
        score += value;
        if(score<0)
            score = 0;
    }

    public void update(float animationDelta) {
        if(needsUpdate) {
            scoreText = String.format(format, score);
            font.scale(scale);
            BitmapFont.TextBounds bounds = font.getBounds(scoreText);

            renderX = x - (int)(bounds.width/2);
            renderY = y;

            needsUpdate = false;
        }
    }

    public void drawText(Batch fontBatch) {
        font.draw(fontBatch,scoreText,
                renderX,
                renderY);
    }
}
