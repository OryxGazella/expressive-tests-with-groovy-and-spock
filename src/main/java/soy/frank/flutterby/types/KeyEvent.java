package soy.frank.flutterby.types;

import org.immutables.value.Value;

@Value.Immutable(intern = true)
public interface KeyEvent {
    int keyCode();
    boolean downEvent();
}
