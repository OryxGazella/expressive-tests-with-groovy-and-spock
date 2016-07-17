package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.actors.*;
import soy.frank.flutterby.gfx.Renderer;
import soy.frank.flutterby.input.UserControls;

import java.util.Random;

class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1920;
        config.height = 1080;
        config.fullscreen = true;
        config.title = "Flutterby";
        new LwjglApplication(new Listener(), config);
    }

    private static class Listener implements ApplicationListener {

        private final GameLogic gameLogic;
        private final PublishSubject<Float> ticks = PublishSubject.create();
        private Renderer renderer;
        private final Scene initialScene;

        Listener() {
            Random random = new Random();
            this.gameLogic = new GameLogic(() -> Math.abs(random.nextInt()));
            initialScene = ImmutableScene
                    .builder()
                    .butterfly(PhysicalEntity.createButterflyAt(-Butterfly.WIDTH / 2, -Butterfly.HEIGHT / 2 - 0.25f))
                    .dragonflies(DragonFly.DOUBlE_HELIX)
                    .build();
        }


        @Override
        public void create() {
            renderer = new Renderer();

            ticks
                    .map(unused -> UserControls.pollKeysPressed())
                    .scan(initialScene, gameLogic::applyLogic)
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
