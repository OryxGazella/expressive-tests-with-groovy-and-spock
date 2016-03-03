package soy.frank.flutterby.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import soy.frank.flutterby.actors.*;

import java.util.HashMap;
import java.util.Map;

public class Renderer implements Disposable {

    private final SpriteBatch batch;
    private final Sprite clouds;
    private final Sprite laser;

    private final Map<String, Texture> textures = new HashMap<>();

    private final Sprite dragonfly;
    private final Sprite butterfly;

    public Renderer() {

        OrthographicCamera camera = new OrthographicCamera(1, (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        butterfly = createSprite("butterfly.png", 512, 512, Butterfly.WIDTH, Butterfly.HEIGHT);
        clouds = createSprite("clouds.png", 1440, 4968, 1.0f, 3.44305555556f);
        laser = createSprite("laser.png", 16, 121, Laser.WIDTH, Laser.HEIGHT);
        dragonfly = createSprite("dragonfly.png", 512, 207, DragonFly.WIDTH, DragonFly.HEIGHT);

        clouds.setPosition(-0.5f, -0.33f);
    }

    private Sprite createSprite(String imagePath, int imageWidth, int imageHeight, float width, float height) {
        Sprite sprite;
        Texture texture = new Texture(Gdx.files.internal(imagePath));
        textures.put("imagePath", texture);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion region = new TextureRegion(texture, 0, 0, imageWidth, imageHeight);
        sprite = new Sprite(region);
        sprite.setSize(width, height);

        return sprite;
    }

    public void render(Scene actors) {
        clearScreen();

        batch.begin();

        moveBackground();
        drawSprites(actors);

        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void drawSprites(Scene actors) {
        drawSpriteAtPosition(actors.butterfly().position(), butterfly);
        actors.lasers().forEach(l -> drawSpriteAtPosition(l.position(), laser));
        actors.dragonflies().forEach(df -> drawSpriteAtPosition(df.position(), dragonfly));
    }

    private void moveBackground() {
        clouds.translate(0.0f, -0.001f);
        clouds.draw(batch);
        if (clouds.getY() < -3.0f) clouds.setY(-0.33f);
    }

    private void drawSpriteAtPosition(Vector2D position, Sprite sprite) {
        sprite.setX(position.x());
        sprite.setY(position.y());
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        textures.values().forEach(Texture::dispose);
    }
}
