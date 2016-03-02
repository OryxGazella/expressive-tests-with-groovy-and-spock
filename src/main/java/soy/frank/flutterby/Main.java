package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rx.Observable;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.actors.ImmutableScene;
import soy.frank.flutterby.actors.PhysicalEntity;
import soy.frank.flutterby.actors.Scene;
import soy.frank.flutterby.gfx.Renderer;
import soy.frank.flutterby.input.UserControls;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        config.fullscreen = true;
        config.title = "Flutterby";
        new LwjglApplication(new Listener(), config);
    }

    public static class Listener implements ApplicationListener {

        private PublishSubject<Float> ticks = PublishSubject.create();
        private Renderer renderer;
        private final Scene initialScene;

        public Listener() {
            initialScene = ImmutableScene
                    .builder()
                    .butterfly(PhysicalEntity.createButterfly())
                    .build();
        }


        @Override
        public void create() {
            UserControls userControls = new UserControls();
            Gdx.input.setInputProcessor(userControls);

            renderer = new Renderer();

            Observable.combineLatest(userControls.controlsObservable(), ticks, (c, t) -> c)
                    .scan(initialScene, GameLogic::applyLogic)
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
