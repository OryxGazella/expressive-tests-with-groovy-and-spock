package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PhysicalEntity {

    public static final float BUTTERFLY_WIDTH = 0.08f;
    public static final float BUTTERFLY_HEIGHT = 0.08f;

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

    public static PhysicalEntity createButterfly() {
        return ImmutablePhysicalEntity.builder()
                .width(BUTTERFLY_WIDTH)
                .height(BUTTERFLY_HEIGHT)
                .position(Vector2D.of(-BUTTERFLY_WIDTH / 2, -BUTTERFLY_HEIGHT / 2))
                .build();
    }
}
