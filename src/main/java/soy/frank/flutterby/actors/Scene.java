package soy.frank.flutterby.actors;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Scene {
    public abstract PhysicalEntity butterfly();
    public abstract List<PhysicalEntity> dragonflies();
    public abstract List<PhysicalEntity> lasers();
    public abstract List<Explosion> explosions();

    @Value.Default
    public int cooldown() {
        return 0;
    }

    @Value.Default
    public int dragonflyCooldown() {
        return 60;
    }

    @Value.Default
    public int lives() {
        return 3;
    }
}
