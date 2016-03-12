package soy.frank.flutterby.actors;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class Scene {
    public abstract PhysicalEntity butterfly();
    public abstract List<PhysicalEntity> dragonflies();
    public abstract List<PhysicalEntity> lasers();

    @Value.Default
    public int cooldown() {
        return 0;
    }
}
