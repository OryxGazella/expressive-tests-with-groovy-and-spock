package soy.frank.flutterby.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class Renderer implements Disposable {

    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Sprite butterfly;
    private final Sprite clouds;
    private final Sprite laser;
    private final Texture cloudTexture;
    private final Texture butterflyTexture;
    private final Texture laserTexture;
    private final Texture dragonflyTexture;
    private final Sprite dragonfly;

    public Renderer() {
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

        cloudTexture = new Texture(Gdx.files.internal("clouds.png"));
        cloudTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        cloudTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion cloudRegion = new TextureRegion(cloudTexture, 0, 0, 1440, 4968);
        clouds = new Sprite(cloudRegion);
        clouds.setSize(1.0f, 3.44305555556f);
        clouds.setPosition(-0.5f, -0.33f);

        laserTexture = new Texture(Gdx.files.internal("laser.png"));
        laserTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        laserTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion laserRegion = new TextureRegion(laserTexture, 0, 0, 16, 121);
        laser = new Sprite(laserRegion);
        laser.setSize(0.01f, 0.075625f);
        laser.setPosition(butterfly.getWidth() / 2 - (laser.getWidth() / 2), butterfly.getHeight());
        laser.setOrigin(laser.getWidth() / 2, laser.getHeight() / 2);

        dragonflyTexture = new Texture(Gdx.files.internal("dragonfly.png"));
        dragonflyTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        dragonflyTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion dragonflyRegion = new TextureRegion(dragonflyTexture, 0, 0, 512, 207);
        dragonfly = new Sprite(dragonflyRegion);
        dragonfly.setSize(0.08f, 0.03234375f);
        dragonfly.setPosition(butterfly.getWidth() * 3 / 2 - (dragonfly.getWidth() / 2), butterfly.getHeight() * 2);
        dragonfly.setOrigin(dragonfly.getWidth() / 2, dragonfly.getHeight() / 2);
    }

    public void render(float[] xy) {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        butterfly.setX(xy[0]);
        butterfly.setY(xy[1]);
        clouds.translate(0.0f, -0.001f);
        clouds.draw(batch);
        if (clouds.getY() < -3.0f) clouds.setY(-0.33f);
        butterfly.draw(batch);
        laser.draw(batch);
        dragonfly.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        cloudTexture.dispose();
        butterflyTexture.dispose();
        laserTexture.dispose();
        dragonflyTexture.dispose();
    }
}
