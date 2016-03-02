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
        private Texture butterflyTexture;
        private Sprite butterfly;
        private Sprite clouds;
        private Texture cloudTexture;

        @Override
        public void create() {
            initSprites();

            UserControls userControls = new UserControls();
            Observable<ButterflyControls> controlsObservable = userControls.controlsObservable();
            Gdx.input.setInputProcessor(userControls);

            Observable.combineLatest(controlsObservable, ticks, (c, t) -> c)
                    .scan(new float[]{0.0f, 0.0f}, GameLogic::applyLogic)
                    .subscribe((xy) -> {
                        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
                        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                        batch.setProjectionMatrix(camera.combined);
                        batch.begin();
                        butterfly.setX(xy[0]);
                        butterfly.setY(xy[1]);
                        clouds.translate(0.0f, -0.001f);
                        clouds.draw(batch);
                        if(clouds.getY() < -3.0f) clouds.setY(-0.33f);
                        butterfly.draw(batch);
                        batch.end();
                    });
        }

        private void initSprites() {
            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();

            camera = new OrthographicCamera(1, h / w);
            batch = new SpriteBatch();

            butterflyTexture = new Texture(Gdx.files.internal("butterfly.png"));
            butterflyTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

            TextureRegion butterflyRegion = new TextureRegion(butterflyTexture, 0, 0, 512, 512);

            butterfly = new Sprite(butterflyRegion);
            butterfly.setSize(0.08f, 0.08f);
            butterfly.setOrigin(butterfly.getWidth() / 2, butterfly.getHeight() / 2);
            butterfly.setPosition(-butterfly.getWidth() / 2, -butterfly.getHeight() / 2);

            cloudTexture = new Texture(Gdx.files.internal("clouds.png"));
            cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            cloudTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

            TextureRegion cloudRegion = new TextureRegion(cloudTexture, 0, 0, 1440, 4968);
            clouds = new Sprite(cloudRegion);
            clouds.setSize(1.0f, 3.44305555556f);
            clouds.setPosition(-0.5f, -0.33f);
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
