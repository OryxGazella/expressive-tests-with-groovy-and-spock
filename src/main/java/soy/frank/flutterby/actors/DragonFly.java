package soy.frank.flutterby.actors;

import java.util.Arrays;
import java.util.List;

public class DragonFly {
    public static final float WIDTH = 0.075f;
    public static final float HEIGHT = 0.025f;
    public static final float LASER_VELOCITY = -Laser.VELOCITY;
    public static final float LASER_ACCELERATION = -Laser.ACCELERATION;
    public static final List<PhysicalEntity> DOUBlE_HELIX = Arrays.asList(
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(0)
                    .shotCooldown(120)
                    .position(Vector2D.of(0f, 0f))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(60)
                    .shotCooldown(180)
                    .position(Vector2D.of(0f, 0f))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(10)
                    .shotCooldown(60)
                    .position(Vector2D.of(0f, 0f + HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(70)
                    .shotCooldown(240)
                    .position(Vector2D.of(0f, 0f + HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(20)
                    .shotCooldown(350)
                    .position(Vector2D.of(0f, 0f + 2 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(80)
                    .shotCooldown(400)
                    .position(Vector2D.of(0f, 0f + 2 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(30)
                    .shotCooldown(250)
                    .position(Vector2D.of(0f, 0f + 3 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(90)
                    .shotCooldown(500)
                    .position(Vector2D.of(0f, 0f + 3 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(40)
                    .position(Vector2D.of(0f, 0f + 4 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(100)
                    .shotCooldown(720)
                    .position(Vector2D.of(0f, 0f + 4 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(50)
                    .shotCooldown(300)
                    .position(Vector2D.of(0f, 0f + 5 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(110)
                    .shotCooldown(890)
                    .position(Vector2D.of(0f, 0f + 5 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(60)
                    .shotCooldown(1234)
                    .position(Vector2D.of(0f, 0f + 6 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(120)
                    .shotCooldown(887)
                    .position(Vector2D.of(0f, 0f + 6 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(70)
                    .shotCooldown(273)
                    .position(Vector2D.of(0f, 0f + 7 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(10)
                    .shotCooldown(432)
                    .position(Vector2D.of(0f, 0f + 7 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(80)
                    .shotCooldown(763)
                    .position(Vector2D.of(0f, 0f + 8 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(20)
                    .shotCooldown(321)
                    .position(Vector2D.of(0f, 0f + 8 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(90)
                    .shotCooldown(325)
                    .position(Vector2D.of(0f, 0f + 9 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(30)
                    .shotCooldown(873)
                    .position(Vector2D.of(0f, 0f + 9 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(100)
                    .shotCooldown(1234)
                    .position(Vector2D.of(0f, 0f + 10 * HEIGHT))
                    .build(),
            ImmutablePhysicalEntity.builder()
                    .width(WIDTH)
                    .height(HEIGHT)
                    .phase(40)
                    .shotCooldown(900)
                    .position(Vector2D.of(0f, 0f + 10 * HEIGHT))
                    .build());

    private DragonFly() {
    }
}
