package soy.frank.flutterby.input;

import org.immutables.value.Value;

@Value.Immutable(intern = true)
public interface ButterflyControls {
    boolean moveUp();
    boolean moveRight();
    boolean moveDown();
    boolean moveLeft();
    boolean fire();
    boolean restart();
}
