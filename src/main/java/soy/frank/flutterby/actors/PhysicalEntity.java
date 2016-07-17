package soy.frank.flutterby.actors;

import org.immutables.value.Value;

@Value.Immutable
public abstract class PhysicalEntity {

    @Value.Default
    public Vector2D getPosition() {
        return Vector2D.of(0.0f, 0.0f);
    }

    @Value.Default
    public float getWidth() {
        return 0.0f;
    }

    @Value.Default
    public float getHeight() {
        return 0.0f;
    }

    @Value.Default
    public float getVelocity() {
        return 0.0f;
    }

    @Value.Default
    public float getAcceleration() {
        return 0.0f;
    }

    @Value.Default
    public int getPhase() {return 0;}

    public static PhysicalEntity createButterflyAt(float x, float y) {
        return createPhysicalEntityAt(x, y, ImmutablePhysicalEntity.builder()
                .width(Butterfly.WIDTH)
                .height(Butterfly.HEIGHT)
        );
    }

    public static PhysicalEntity createLaserAt(float x, float y) {
        return createPhysicalEntityAt(x, y, ImmutablePhysicalEntity
                .builder()
                .width(Laser.WIDTH)
                .height(Laser.HEIGHT)
                .velocity(Laser.VELOCITY)
                .acceleration(Laser.ACCELERATION)
        );
    }

    private static PhysicalEntity createPhysicalEntityAt(float x, float y, ImmutablePhysicalEntity.Builder builder) {
        return builder
                .position(Vector2D.of(x, y))
                .build();
    }

    public static PhysicalEntity createDragonflyAt(float x, float y) {
        return createDragonflyWithPhase(x, y, 0);
    }

    static PhysicalEntity createDragonflyWithPhase(float x, float y, int phase) {
        return createPhysicalEntityAt(x, y, ImmutablePhysicalEntity.builder()
                .width(DragonFly.WIDTH)
                .height(DragonFly.HEIGHT)
                .phase(phase));
    }
}
