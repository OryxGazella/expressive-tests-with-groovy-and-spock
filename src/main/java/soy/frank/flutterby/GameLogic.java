package soy.frank.flutterby;

import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.input.ButterflyControls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

class GameLogic {

    private static final double TAU = Math.PI * 2;
    private static final int TICKS_PER_CYCLE = 120;

    private final RandomNumberGenerator randomNumberGenerator;

    public GameLogic(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;

    }
    public Scene applyLogic(Scene actors, ButterflyControls controls) {
        Vector2D movedButterflyCoordinates = moveButterfly(actors, controls);
        Stream<PhysicalEntity> laserStream = moveLasers(actors.lasers());

        int resultingCooldown = actors.cooldown() - 1;
        if (resultingCooldown < 0) resultingCooldown = 0;

        if (shouldSpawnLaser(controls, actors.cooldown())) {
            laserStream = appendLaserToLaserStream(movedButterflyCoordinates, laserStream);
            resultingCooldown = Butterfly.COOLDOWN;
        }

        int resultingDragonflyCooldown = actors.dragonflyCooldown() - 1;
        if (resultingDragonflyCooldown < 0) resultingDragonflyCooldown = 0;

        Map<Boolean, List<PhysicalEntity>> remainingLasers = laserStream.collect(partitioningBy(l -> !actors.dragonflies().stream().anyMatch(d -> CollisionDetector.collides(l, d))));

        List<PhysicalEntity> movedLasers = remainingLasers.get(true);

        List<PhysicalEntity> collidingLasers = remainingLasers.get(false);

        Stream<PhysicalEntity> dragonflies = actors.dragonflies().stream();

        dragonflies = moveDragonflies(dragonflies);

        if(actors.dragonflyCooldown() <= 0) {
            dragonflies = Stream.concat(dragonflies, Stream.of(PhysicalEntity.createDragonflyAt(0f, (randomNumberGenerator.randomInteger() % 14 - 2) * DragonFly.HEIGHT)));
            resultingDragonflyCooldown = randomNumberGenerator.randomInteger() % 180 + 120;
        }

        dragonflies = dragonflies.filter(df -> collidingLasers.stream().noneMatch(l -> CollisionDetector.collides(df, l)));

        ImmutablePhysicalEntity resultingButterfly = resultingButterfly(movedButterflyCoordinates, actors.butterfly());
        Map<Boolean, List<PhysicalEntity>> dragonfliesThatCollideWithButterfly = dragonflies.collect(partitioningBy(df -> !CollisionDetector.collides(df, resultingButterfly)));
        Stream<ImmutableExplosion> explosions = collidingLasers.stream().map(cl -> ImmutableExplosion.builder().position(cl.position()).build());
        int resultingLives = actors.lives();
        if(dragonfliesThatCollideWithButterfly.get(false).size() > 0) {
            resultingLives -= 1;
            explosions = Stream.concat(explosions, dragonfliesThatCollideWithButterfly.get(false).stream().map(dr -> ImmutableExplosion.builder().position(dr.position()).build()));
        }
        return ImmutableScene.copyOf(actors)
                .withLasers(movedLasers)
                .withButterfly(resultingButterfly)
                .withDragonflies(dragonfliesThatCollideWithButterfly.get(true))
                .withCooldown(resultingCooldown)
                .withDragonflyCooldown(resultingDragonflyCooldown)
                .withExplosions(explosions.collect(Collectors.toList()))
                .withLives(resultingLives);
    }

    private Stream<PhysicalEntity> moveDragonflies(Stream<PhysicalEntity> dragonflies) {
        return dragonflies.map(df -> ImmutablePhysicalEntity
                .copyOf(df)
                .withPhase((df.phase() + 1 <= TICKS_PER_CYCLE ? df.phase() + 1 : 0) % TICKS_PER_CYCLE)
                .withPosition(
                        Vector2D.of(
                                (float)(Math.sin((TAU / TICKS_PER_CYCLE) * df.phase())) * 0.1f,
                                df.position().y())));
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
