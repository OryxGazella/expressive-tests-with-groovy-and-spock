package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable
public interface Explosion {
    Vector2D getPosition();
}
