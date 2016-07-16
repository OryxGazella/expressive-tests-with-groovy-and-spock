package soy.frank.flutterby.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import soy.frank.flutterby.actors.Vector2D;

class ExplosionAnimation {
    private ExplosionAnimation(float x, float y) {
        this.x = x;
        this.y = y;
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("explosion.part"), Gdx.files.internal("explosions"));
        particleEffect.scaleEffect(0.15f);
        particleEffect.start();
        particleEffect.allowCompletion();
    }

    ExplosionAnimation(Vector2D position) {
        this(position.getX(), position.getY());
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    ParticleEffect getParticleEffect() {
        return particleEffect;
    }

    private float y;
    private float x;
    private final ParticleEffect particleEffect;
}
