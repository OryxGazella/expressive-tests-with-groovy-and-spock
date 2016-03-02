package soy.frank.flutterby.actors;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface Scene {
    PhysicalEntity butterfly();
    List<PhysicalEntity> dragonflies();
    List<PhysicalEntity> lasers();
}
