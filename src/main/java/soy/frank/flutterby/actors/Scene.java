package soy.frank.flutterby.actors;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Scene {
    public abstract PhysicalEntity getButterfly();
    public abstract List<PhysicalEntity> getDragonflies();
    public abstract List<PhysicalEntity> getLasers();
    public abstract List<Explosion> getExplosions();
    public abstract List<PhysicalEntity> getDragonflyLasers();

    @Value.Default
    public int cooldown() {
        return 0;
    }

    @Value.Default
    public int dragonflyCooldown() {
        return 60;
    }

    @Value.Default
    public int getLives() {
        return 3;
    }

    @Value.Default
    public int getScore() {
        return 0;
    }

    @Value.Default
    public boolean isRunning() {
        return true;
    }

    @Value.Default
    public boolean isPaused() {
        return false;
    }
}
