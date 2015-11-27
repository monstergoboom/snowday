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

        Object fixtureB = contact.getFixtureB().getBody().getUserData();

        if (fixtureB != null) {
            if (fixtureB instanceof Character) {
                ((Character) fixtureB).beginContact();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Object fixtureB = contact.getFixtureB().getBody().getUserData();

        if (fixtureB != null) {
            if (fixtureB instanceof Character) {
                ((Character) fixtureB).endContact();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
