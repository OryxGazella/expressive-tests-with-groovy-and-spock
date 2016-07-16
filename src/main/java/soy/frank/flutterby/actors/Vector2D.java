package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable(intern = true)
public interface Vector2D {
    float getX();
    float getY();

    static float centeredAt(float width, float centeredAtWidth, float centeredWithWidth) {
        return centeredAtWidth + width / 2 - centeredWithWidth / 2;
    }

    static Vector2D of(float x, float y) {
        return ImmutableVector2D.builder().x(x).y(y).build();
    }
}
