package soy.frank.flutterby;

import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.input.ButterflyControls;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic {

    public static Scene applyLogic(Scene actors, ButterflyControls controls) {
        Vector2D movedButterflyCoordinates = moveButterfly(actors, controls);
        Stream<PhysicalEntity> laserStream = moveLasers(actors.lasers());

        int resultingCooldown = actors.cooldown() - 1;
        if (resultingCooldown < 0) resultingCooldown = 0;

        if (shouldSpawnLaser(controls, actors.cooldown())) {
            laserStream = appendLaserToLaserStream(movedButterflyCoordinates, laserStream);
            resultingCooldown = Butterfly.COOLDOWN;
        }

        List<PhysicalEntity> movedLasers = laserStream.collect(Collectors.toList());

        return ImmutableScene.copyOf(actors)
                .withLasers(movedLasers)
                .withButterfly(resultingButterfly(movedButterflyCoordinates, actors.butterfly()))
                .withCooldown(resultingCooldown);
    }

    private static Stream<PhysicalEntity> appendLaserToLaserStream(Vector2D movedButterflyCoordinates, Stream<PhysicalEntity> laserStream) {
        laserStream = Stream.concat(laserStream, Stream.of(PhysicalEntity
                .createLaserAt(Vector2D.centeredAt(Butterfly.WIDTH, movedButterflyCoordinates.x(), Laser.WIDTH), movedButterflyCoordinates.y() + Butterfly.HEIGHT)));
        return laserStream;
    }

    private static ImmutablePhysicalEntity resultingButterfly(Vector2D movedButterflyCoordinates, PhysicalEntity initialButterfly) {
        return ImmutablePhysicalEntity
                .copyOf(initialButterfly)
                .withPosition(movedButterflyCoordinates);
    }

    private static boolean shouldSpawnLaser(ButterflyControls controls, int currentCooldown) {
        return controls.fire() && currentCooldown <= 0;
    }

    private static Stream<PhysicalEntity> moveLasers(List<PhysicalEntity> lasers) {
        return lasers.stream()
                .map(l -> {
                    float resultingVelocity = l.velocity() + l.acceleration();
                    return ImmutablePhysicalEntity.copyOf(l)
                            .withPosition(Vector2D.of(l.position().x(), l.position().y() + resultingVelocity))
                            .withVelocity(resultingVelocity);
                });
    }

    private static Vector2D moveButterfly(Scene actors, ButterflyControls controls) {
        final float butterflyX = actors.butterfly().position().x() + (controls.moveRight() ?
                Butterfly.VELOCITY : controls.moveLeft() ? -Butterfly.VELOCITY
                : 0.0f);
        final float butterflyY = actors.butterfly().position().y() + (controls.moveUp() ?
                Butterfly.VELOCITY
                : controls.moveDown() ? -Butterfly.VELOCITY : 0.0f);
        return Vector2D.of(butterflyX, butterflyY);
    }
}
