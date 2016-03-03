package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PhysicalEntity {

    @Value.Default
    public Vector2D position() {
        return Vector2D.of(0.0f, 0.0f);
    }

    @Value.Default
    public float width() {
        return 0.0f;
    }

    @Value.Default
    public float height() {
        return 0.0f;
    }

    @Value.Default
    public float velocity() {
        return 0.0f;
    }

    @Value.Default
    public float acceleration() {
        return 0.0f;
    }

    public static PhysicalEntity createButterfly(float x, float y) {
        return createEntity(x, y, Butterfly.WIDTH, Butterfly.HEIGHT);
    }

    public static PhysicalEntity createEntity(float x, float y, float width, float height) {
        return ImmutablePhysicalEntity.builder()
                .width(width)
                .height(height)
                .position(Vector2D.of(x, y))
                .build();
    }
}
