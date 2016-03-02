package soy.frank.flutterby.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.google.common.collect.ImmutableSet;
import rx.Observable;
import rx.subjects.PublishSubject;

public class UserControls extends InputAdapter {

    private Observable<ButterflyControls> controlsObservable;
    private PublishSubject<KeyEvent> keyEvents = PublishSubject.create();

    public static final ButterflyControls initialControls = ImmutableButterflyControls.builder()
            .moveUp(false)
            .moveRight(false)
            .moveDown(false)
            .moveLeft(false)
            .fire(false)
            .build();

    private static final ImmutableSet<Integer> allowedKeys = ImmutableSet
            .of(
                    Keys.UP,
                    Keys.RIGHT,
                    Keys.DOWN,
                    Keys.LEFT,
                    Keys.SPACE);

    public synchronized Observable<ButterflyControls> controlsObservable() {
        if (controlsObservable != null) return controlsObservable;
        controlsObservable = keyEvents
                .filter(ke -> allowedKeys.contains(ke.keyCode()))
                .scan(initialControls, (controls, event) -> {
                    switch (event.keyCode()) {
                        case Keys.UP:
                            return ImmutableButterflyControls.copyOf(controls).withMoveUp(event.downEvent());
                        case Keys.RIGHT:
                            return ImmutableButterflyControls.copyOf(controls).withMoveRight(event.downEvent());
                        case Keys.DOWN:
                            return ImmutableButterflyControls.copyOf(controls).withMoveDown(event.downEvent());
                        case Keys.LEFT:
                            return ImmutableButterflyControls.copyOf(controls).withMoveLeft(event.downEvent());
                        case Keys.SPACE:
                            return ImmutableButterflyControls.copyOf(controls).withFire(event.downEvent());
                    }
                    return controls;
                })
                .startWith(initialControls);
        return controlsObservable;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyEvents.onNext(ImmutableKeyEvent.builder()
                .downEvent(true)
                .keyCode(keycode)
                .build());
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyEvents.onNext(ImmutableKeyEvent.builder()
                .downEvent(false)
                .keyCode(keycode)
                .build());
        return true;
    }
}
