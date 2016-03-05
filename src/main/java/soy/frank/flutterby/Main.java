package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.gfx.Renderer;
import soy.frank.flutterby.input.UserControls;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.fullscreen = true;
        config.title = "Flutterby";
        new LwjglApplication(new Listener(), config);
    }

    public static class Listener implements ApplicationListener {

        private PublishSubject<Float> ticks = PublishSubject.create();
        private Renderer renderer;
        private final Scene staticScene;

        public Listener() {
            staticScene = ImmutableScene
                    .builder()
                    .butterfly(PhysicalEntity.createButterfly(-Butterfly.WIDTH / 2, 0f))
                    .lasers(Collections.singleton(PhysicalEntity.createButterfly(-Laser.WIDTH / 2,  1.5f * Butterfly.HEIGHT)))
                    .dragonflies(Collections.singleton(PhysicalEntity.createButterfly(-DragonFly.WIDTH / 2, 3f * Butterfly.HEIGHT)))
                    .build();
        }


        @Override
        public void create() {
            renderer = new Renderer();

            ticks
                    .map(unused -> UserControls.pollKeysPressed())
                    .scan(staticScene, GameLogic::applyLogic)
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
