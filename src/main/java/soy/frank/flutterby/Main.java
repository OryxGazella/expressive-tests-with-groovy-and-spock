package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rx.Observable;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.gfx.Renderer;
import soy.frank.flutterby.input.ButterflyControls;
import soy.frank.flutterby.input.UserControls;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        new LwjglApplication(new Listener(), config);
    }

    public static class Listener implements ApplicationListener {

        private PublishSubject<Float> ticks = PublishSubject.create();
        private Renderer renderer;


        @Override
        public void create() {
            UserControls userControls = new UserControls();
            Observable<ButterflyControls> controlsObservable = userControls.controlsObservable();
            Gdx.input.setInputProcessor(userControls);

            renderer = new Renderer();

            Observable.combineLatest(controlsObservable, ticks, (c, t) -> c)
                    .scan(new float[]{0.0f, 0.0f}, GameLogic::applyLogic)
                    .subscribe(renderer::render);
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
            renderer.dispose();
        }
    }
}
