package com.monstergoboom.snowday.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by amitrevski on 12/26/14.
 */
public class HelperUtils {
    public static float viewPortWidth = 7f;
    public static float viewPortHeight = 4.05f;
    public static float pixelsToUnitsRatio = 0.00366f;
    public static float unitsToPixelsRatio = 273.224f;

    public static final float gravity = -9.81f;
    public static final float worldStep = 1/60f;

    public static final int velocityIterations = 6;
    public static final int positionIterations = 2;
    public static final int regionIndexNone = -1;

    public static String displayFontM = "curlz_mt";
    public static String displayFontS = "curlz_mt_16";

    public static final Texture emptyTexture = null;
    public static final String playerScoreDefaultFormat = "%06d";

    public static void updateUnitRatios(float pixelWidth, float pixelHeight, float unitWidth)
    {
        float unitsRatio = pixelWidth / unitWidth;
        float pixelRatio = 1 / unitsRatio;

        HelperUtils.viewPortHeight = pixelRatio * pixelHeight;
        HelperUtils.viewPortWidth = unitWidth;
        HelperUtils.pixelsToUnitsRatio = pixelRatio;
        HelperUtils.unitsToPixelsRatio = unitsRatio;
    }

    public static int convertUnitsToPixel(float units) {
        return (int)(units * HelperUtils.unitsToPixelsRatio);
    }

    public static float convertPixelsToUnits(int pixels) {
        return pixels * HelperUtils.pixelsToUnitsRatio;
    }

    public static Vector2 convertPixelsToUnits(int x, int y) {
        return new Vector2(HelperUtils.convertPixelsToUnits(x), HelperUtils.convertPixelsToUnits(y));
    }

    public static Vector2[] convertFloatToVector2(float[] vertices, float scale) {
        java.util.ArrayList<Vector2> result = new java.util.ArrayList<>();
        for (int i = 0; i < vertices.length/2; i++) {
            float x = vertices[i * 2];
            float y = vertices[i * 2 + 1];

            Vector2 v = new Vector2(x,y);
            v.scl(scale);

            result.add(v);
        }

        return result.toArray(new Vector2[result.size()]);
    }

    public static BodyDef.BodyType convertStringToBodyType(String bodyType) {
        switch(bodyType) {
            case "static":
                return BodyDef.BodyType.StaticBody;
            case "k":
                return BodyDef.BodyType.KinematicBody;
            case "dynamic":
            default:
                return BodyDef.BodyType.DynamicBody;
        }
    }
}
