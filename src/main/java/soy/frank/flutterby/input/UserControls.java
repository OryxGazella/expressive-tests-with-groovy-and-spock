package soy.frank.flutterby.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.google.common.primitives.Ints;

public class UserControls {

    public static ButterflyControls pollKeysPressed() {
        return ImmutableButterflyControls.builder()
                .moveUp(pressed(Keys.W, Keys.UP))
                .moveRight(pressed(Keys.D, Keys.RIGHT))
                .moveDown(pressed(Keys.S, Keys.DOWN))
                .moveLeft(pressed(Keys.A, Keys.LEFT))
                .fire(pressed(Keys.SPACE))
                .restart(pressed(Keys.R))
                .quit(pressed(Keys.ESCAPE))
                .build();
    }

    private static boolean pressed(int... keys) {
        return Ints.asList(keys).stream()
                .anyMatch(k -> Gdx.input.isKeyPressed(k));
    }
}
