package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class DesktopInputListener implements InputProcessor {

    private PlayerCharacter playerCharacter;

    public DesktopInputListener(PlayerCharacter character) {
        playerCharacter = character;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log("Keyboard", String.format("Key Down: %d", keycode));

        switch(keycode) {
            case Input.Keys.A:
                playerCharacter.walk(-1);
                break;
            case Input.Keys.F:
                playerCharacter.walk(1);
                break;
            case Input.Keys.SPACE:
                playerCharacter.jump();
                break;
            case Input.Keys.APOSTROPHE:
                playerCharacter.attack();
                break;
            case Input.Keys.SEMICOLON:
                playerCharacter.secondaryAttack();
                break;
            case Input.Keys.R:
                playerCharacter.reloadPrimaryWeapon();
                break;
            case Input.Keys.C:
                playerCharacter.crouch();
                break;
            case Input.Keys.Z:
                playerCharacter.prone();
                break;
            case Input.Keys.UP:
                playerCharacter.switchAmmo("red");
                break;
            case Input.Keys.DOWN:
                playerCharacter.switchAmmo("blue");
                break;
            case Input.Keys.LEFT:
                playerCharacter.switchAmmo("green");
                break;
            case Input.Keys.RIGHT:
                playerCharacter.switchAmmo("yellow");
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log("Keyboard", String.format("Key Up: %d", keycode));

        switch(keycode) {
            case Input.Keys.A:
            case Input.Keys.F:
                playerCharacter.idle();
                break;
            case Input.Keys.C:
            case Input.Keys.Z:
                playerCharacter.stand();
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        Gdx.app.log("Keyboard", String.format("Key Pressed: %s", character));

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
