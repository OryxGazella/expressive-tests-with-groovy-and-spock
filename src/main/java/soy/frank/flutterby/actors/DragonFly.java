package soy.frank.flutterby.actors;

import java.util.Arrays;
import java.util.List;

public class DragonFly {
    public static final float WIDTH = 0.075f;
    public static final float HEIGHT = 0.025f;
    public static final float LASER_VELOCITY = -Laser.VELOCITY;
    public static final float LASER_ACCELERATION = -Laser.ACCELERATION;
    public static final List<PhysicalEntity> DOUBlE_HELIX = Arrays.asList(
            PhysicalEntity.createDragonflyAt(0f, 0f),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f, 60),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + HEIGHT, 10),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + HEIGHT, 70),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 2 * HEIGHT, 20),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 2 * HEIGHT, 80),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 3 * HEIGHT, 30),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 3 * HEIGHT, 90),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 4 * HEIGHT, 40),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 4 * HEIGHT, 100),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 5 * HEIGHT, 50),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 5 * HEIGHT, 110),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 6 * HEIGHT, 60),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 6 * HEIGHT, 120),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 7 * HEIGHT, 70),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 7 * HEIGHT, 10),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 8 * HEIGHT, 80),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 8 * HEIGHT, 20),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 9 * HEIGHT, 90),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 9 * HEIGHT, 30),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 10 * HEIGHT, 100),
            PhysicalEntity.createDragonflyWithPhase(0f, 0f + 10 * HEIGHT, 40));

    private DragonFly() {
    }
}
