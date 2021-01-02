package com.monstergoboom.snowday.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by amitrevski on 11/28/15.
 */
public class OrnamentBlaster extends Weapon {
    private int maxCapacity;
    private int currentCount;
    private Bullet bullet;
    private Queue<Bullet> magazine;
    private Queue<Bullet> fired;
    private SnowDayAssetManager assetManager;

    Sound sound;
    int soundId;

    public OrnamentBlaster(Bullet bullet, SnowDayAssetManager assetManager) {
        super("projectile", "Santa's Ornament Hand Blaster", "blaster", "weapon");
        this.assetManager = assetManager;
        maxCapacity = 15;
        currentCount = maxCapacity;
        magazine = new LinkedList<>();
        this.bullet = bullet;
        this.direction = 1;

        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/hollowthud.ogg"));

        magazine = Stream.generate(() -> {
            RedOrnamentBullet redOrnamentBullet = new RedOrnamentBullet(0, 0, bullet.world,
                    assetManager.getTextureAtlas("misc")
                            .createSprite("green_ornament"));
            redOrnamentBullet.hide();
            return redOrnamentBullet;
        })
                .limit(maxCapacity).collect(Collectors.toCollection(LinkedList::new));

        fired = new LinkedList<>();
    }

    @Override
    public int getCurrentAmmo() {
        return currentCount;
    }

    @Override
    public void fire() {
        if (currentCount > 0 ) {
            sound.play(1.0f);

            currentCount -= 1;

            Bullet item = magazine.poll();

            item.show();
            item.setPosition(x,y);
            item.shoot(25f, 1.5f, direction);

            fired.offer(item);
        }
    }

    @Override
    public int reload(int count) {
        if (count == 0) {
            return count;
        }

        int loaded = 0;

        if (currentCount < maxCapacity ) {
            int reload = maxCapacity - currentCount;
            int totalFired = fired.size();

            if (count < reload) {
                reload = count;
            }

            while (!fired.isEmpty() && (loaded < reload)) {
                Bullet bullet = fired.poll();
                bullet.hide();
                bullet.setPosition(0, 0);
                magazine.offer(bullet);
                loaded ++;
            }

            currentCount += loaded;
        }

        return loaded;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void beginContact(GameObject contactWith) {

    }

    @Override
    public void endContact(GameObject contactWith) {

    }

    @Override
    public void update(float animationDelta) {
        fired.forEach(item -> item.update(animationDelta));
        magazine.forEach(item ->
        {
            item.update(animationDelta);
        });
    }

    @Override
    public void draw(Batch batch) {
        fired.forEach(item -> {
            item.draw(batch);
        });
        magazine.forEach(item -> item.draw(batch));
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }
}
