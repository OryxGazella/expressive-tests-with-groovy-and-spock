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

    public static PhysicalEntity createButterflyAt(float x, float y) {
        return createPhysicalEntityAt(x, y, ImmutablePhysicalEntity.builder().width(Butterfly.WIDTH).height(Butterfly.HEIGHT));
    }

    public static PhysicalEntity createLaserAt(float x, float y) {
        return createPhysicalEntityAt(x, y, ImmutablePhysicalEntity.builder().width(Laser.WIDTH).height(Laser.HEIGHT));
    }

    private static PhysicalEntity createPhysicalEntityAt(float x, float y, ImmutablePhysicalEntity.Builder builder) {
        return builder
                .position(Vector2D.of(x, y))
                .build();
    }
}
