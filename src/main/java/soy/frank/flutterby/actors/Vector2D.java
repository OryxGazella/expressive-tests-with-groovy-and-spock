package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable(intern = true)
public interface Vector2D {
    float x();
    float y();

    static Vector2D of(float x, float y) {
        return ImmutableVector2D.builder().x(x).y(y).build();
    }
}
