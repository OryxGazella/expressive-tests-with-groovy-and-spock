package soy.frank.flutterby;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import rx.Observable;
import rx.subjects.PublishSubject;
import soy.frank.flutterby.types.ButterflyControls;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1024;
        new LwjglApplication(new Listener(), config);
    }

    public static class Listener implements ApplicationListener {

        private PublishSubject<Float> ticks = PublishSubject.create();
        private OrthographicCamera camera;
        private SpriteBatch batch;
        private Texture texture;
        private Sprite sprite;

        @Override
        public void create() {
            initSprites();

            UserControls userControls = new UserControls();
            Observable<ButterflyControls> controlsObservable = userControls.controlsObservable();
            Gdx.input.setInputProcessor(userControls);

            Observable.combineLatest(controlsObservable, ticks, (c, t) -> c)
                    .subscribe((c) -> {
                        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                        batch.setProjectionMatrix(camera.combined);
                        batch.begin();
                        sprite.translate(
                                c.moveRight() ? 0.005f : c.moveLeft() ? -0.005f : 0.0f,
                                c.moveUp() ? 0.005f : c.moveDown() ? -0.005f : 0.0f);

                        sprite.draw(batch);
                        batch.end();
                    });
        }

        private void initSprites() {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

            camera = new OrthographicCamera(1, h / w);
            batch = new SpriteBatch();

            texture = new Texture(Gdx.files.internal("butterfly.png"));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

            TextureRegion region = new TextureRegion(texture, 0, 0, 512, 512);

            sprite = new Sprite(region);
            sprite.setSize(0.08f, 0.08f);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
            sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
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
