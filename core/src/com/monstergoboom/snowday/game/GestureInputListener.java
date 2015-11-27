package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by amitrevski on 1/11/15.
 */
public class GestureInputListener implements GestureDetector.GestureListener {
    private PlayerCharacter playerCharacter;

    public GestureInputListener(PlayerCharacter pc) {
        playerCharacter = pc;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        Gdx.app.log("gesture listener", "x: " + x + ", y: " + y + ", pointer: " + pointer +
        ", button: " + button);

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        Gdx.app.log("gesture listener", "fling x: " + velocityX + ", fling y: " + velocityY + ", button: " + button);

        if (velocityX > 0 )
            playerCharacter.walk(1);
        else
            playerCharacter.walk(-1);

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

        return false;
    }
}