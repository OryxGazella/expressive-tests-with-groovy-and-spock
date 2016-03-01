package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import rx.Observable;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.types.ButterflyControls;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 480;
        config.width = 800;
        new LwjglApplication(new Listener(), config);
    }

    public static class Listener implements ApplicationListener {

        private PublishSubject<Float> ticks = PublishSubject.create();

        @Override
        public void create() {
            UserControls userControls = new UserControls();
            Observable<ButterflyControls> controlsObservable = userControls.controlsObservable();
            Gdx.input.setInputProcessor(userControls);

            Observable.combineLatest(controlsObservable, ticks, (c, t) -> c)
                    .subscribe((c) -> {
                        // Sets the clear screen color to: Cornflower Blue
                        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
                        // Clears the screen
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                    });
            controlsObservable.distinctUntilChanged().subscribe(System.out::println);
        }

        @Override
        public void resize(int width, int height) {

        }

        @Override
        public void render() {
            ticks.onNext(Gdx.graphics.getDeltaTime());
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }

        @Override
        public void dispose() {
            ticks.onCompleted();
        }
    }
}
