package com.monstergoboom.snowday.game;

/**
 * Created by amitrevski on 12/26/14.
 */
public abstract class GameObject {
    protected String reference_name;
    protected String reference_category;

    public GameObject(String name, String category) {
        reference_category = category;
        reference_name = name;
    }

    public String getReferenceName() {
        return reference_name;
    }

    public String getReferenceCategory() {
        return reference_category;
    }

    public abstract void setPosition(int x, int y);
    public abstract void beginContact(GameObject contactWith);
    public abstract void endContact(GameObject contactWith);
}
