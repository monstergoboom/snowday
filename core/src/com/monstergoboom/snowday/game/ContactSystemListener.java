package com.monstergoboom.snowday.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by amitrevski on 11/26/15.
 */
public class ContactSystemListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Object fixtureA = contact.getFixtureA().getBody().getUserData();

        GameObject gameObjectA = null;
        GameObject gameObjectB = null;

        if (fixtureA != null) {
            if (fixtureA instanceof GameObject) {
                gameObjectA = ((GameObject) fixtureA);
            }
        }

        Object fixtureB = contact.getFixtureB().getBody().getUserData();

        if (fixtureB != null) {
            if (fixtureB instanceof GameObject) {
                gameObjectB = ((GameObject) fixtureB);
            }
        }

        if (gameObjectA != null && gameObjectB != null) {
            gameObjectA.beginContact(gameObjectB);
            gameObjectB.beginContact(gameObjectA);
        }
    }

    @Override
    public void endContact(Contact contact) {

        Object fixtureA = contact.getFixtureA().getBody().getUserData();

        GameObject gameObjectA = null;
        GameObject gameObjectB = null;

        if (fixtureA != null) {
            if (fixtureA instanceof GameObject) {
                gameObjectA = ((GameObject) fixtureA);
            }
        }

        Object fixtureB = contact.getFixtureB().getBody().getUserData();

        if (fixtureB != null) {
            if (fixtureB instanceof GameObject) {
                gameObjectB = ((GameObject) fixtureB);
            }
        }

        if (gameObjectA != null && gameObjectB != null) {
            gameObjectA.endContact(gameObjectB);
            gameObjectB.endContact(gameObjectA);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
