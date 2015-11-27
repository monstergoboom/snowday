package com.monstergoboom.snowday.game;

/**
 * Created by Alek on 3/24/2015.
 */
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;

public class PlayerControllerListener implements ControllerListener {
    private PlayerCharacter playerCharacter;
    public static final int MOVE_LEFT = 21; // dpad left
    public static final int MOVE_RIGHT = 22; // dpad right
    public static final int JUMP = 99; // button
    public static final int ATTACK = 96; // a button
    public static final int PICK_UP = 97; // b button
    public static final int USE_MAGIC = 100; // y button
    public static final int HOLD_UP = 19; // dpad up
    public static final int KNEEL = 20;  // dpad down

    public PlayerControllerListener(PlayerCharacter pc) {
        playerCharacter = pc;
    }

    public void register(Controller controller) {
        controller.addListener(this);
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        Gdx.app.log("PlayerControllerListener", "Button Down: " + buttonCode);

        // Movement
        if(buttonCode == MOVE_LEFT) {
            playerCharacter.walk(-1);
        }
        else if (buttonCode == MOVE_RIGHT) {
            playerCharacter.walk(1);
        }

        // Actions
        if (buttonCode == JUMP) {
            playerCharacter.jump();
        }

        // Combat
        if (buttonCode == ATTACK) {
            playerCharacter.attack();
        }

        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if( buttonCode == MOVE_LEFT || buttonCode == MOVE_RIGHT )
            playerCharacter.idle();

        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Gdx.app.log("PlayerControllerListener", "Axis Moved " + axisCode);
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Gdx.app.log("PlayerControllerListener", "Pov Moved " + povCode);
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
}
