package soy.frank.flutterby;

import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.input.ButterflyControls;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameLogic {

    public static Scene applyLogic(Scene actors, ButterflyControls controls) {
        float butterflyX = actors.butterfly().position().x() + (controls.moveRight() ?
                Butterfly.VELOCITY : controls.moveLeft() ? -Butterfly.VELOCITY
                : 0.0f);

        float butterflyY = actors.butterfly().position().y() + (controls.moveUp() ?
                Butterfly.VELOCITY
                : controls.moveDown() ? -Butterfly.VELOCITY : 0.0f);


        Stream<PhysicalEntity> laserStream = actors.lasers().stream()
                .map(l -> {
                    float resultingVelocity = l.velocity() + l.acceleration();
                    return ImmutablePhysicalEntity.copyOf(l)
                            .withPosition(Vector2D.of(l.position().x(), l.position().y() + resultingVelocity))
                            .withVelocity(resultingVelocity);
                });

        if(controls.fire()) {
            laserStream = Stream.concat(laserStream, Stream.of(PhysicalEntity
                    .createLaserAt(butterflyX + Butterfly.WIDTH / 2 - Laser.WIDTH / 2, butterflyY + Butterfly.HEIGHT)));
        }

        return ImmutableScene.copyOf(actors)
                .withLasers(laserStream.collect(Collectors.toList()))
                .withButterfly(ImmutablePhysicalEntity
                        .copyOf(actors.butterfly())
                        .withPosition(Vector2D.of(butterflyX, butterflyY)));
    }
}
