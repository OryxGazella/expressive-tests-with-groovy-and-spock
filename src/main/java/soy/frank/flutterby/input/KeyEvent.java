package soy.frank.flutterby.input;

import org.immutables.value.Value;

@Value.Immutable(intern = true)
public interface KeyEvent {
    int keyCode();
    boolean downEvent();
}
