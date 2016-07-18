package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.actors.Scene;
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

        Listener() {
            Random random = new Random();
            this.gameLogic = new GameLogic(() -> Math.abs(random.nextInt()));
        }


        @Override
        public void create() {
            renderer = new Renderer();

            ticks
                    .map(unused -> UserControls.pollKeysPressed())
                    .scan(GameLogic.INITIAL_SCENE, gameLogic::applyLogic)
                    .takeWhile(Scene::isRunning)
                    .subscribe(renderer::render,
                            (e) -> System.out.println("An error has occurred: " + e.getMessage()),
                            () -> Gdx.app.exit());
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
