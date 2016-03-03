package soy.frank.flutterby;

import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.input.ButterflyControls;

public class GameLogic {

    public static Scene applyLogic(Scene actors, ButterflyControls controls) {
        float butterflyX = actors.butterfly().position().x() + (controls.moveRight() ?
                Butterfly.VELOCITY : controls.moveLeft() ? -Butterfly.VELOCITY
                : 0.0f);

        float butterflyY = actors.butterfly().position().y() + (controls.moveUp() ?
                Butterfly.VELOCITY
                : controls.moveDown() ? -Butterfly.VELOCITY : 0.0f);

        return ImmutableScene.copyOf(actors)
                .withButterfly(ImmutablePhysicalEntity
                        .copyOf(actors.butterfly())
                        .withPosition(Vector2D.of(butterflyX, butterflyY)));
    }
}
