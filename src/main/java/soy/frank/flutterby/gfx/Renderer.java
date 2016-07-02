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

import java.util.*;
import java.util.stream.Collectors;

public class Renderer implements Disposable {

    private static final float CLOUD_HEIGHT = (float) (2697 / 1920);
    private static final float VIEWPORT_HEIGHT = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
    private static final float SCROLLING_HEIGHT = -0.001f;
    private final SpriteBatch batch;
    private final Sprite clouds;
    private final Sprite laser;

    private final Map<String, Texture> textures = new HashMap<>();
    private final Sprite dragonfly;
    private final Sprite butterfly;

    private List<ExplosionAnimation> explosionAnimations = Collections.emptyList();

    public Renderer() {

        float bottom = -VIEWPORT_HEIGHT / 2;
        OrthographicCamera camera = new OrthographicCamera(1f, VIEWPORT_HEIGHT);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        butterfly = createSprite("butterfly.png", 96, 96, Butterfly.WIDTH, Butterfly.HEIGHT);
        clouds = createSprite("clouds.png", 1920, 2697, 1.0f, CLOUD_HEIGHT);
        laser = createSprite("laser.png", 12, 72, Laser.WIDTH, Laser.HEIGHT);
        dragonfly = createSprite("dragonfly.png", 144, 48, DragonFly.WIDTH, DragonFly.HEIGHT);

        clouds.setPosition(-0.500f, bottom);

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
        explosionAnimations.addAll(actors.explosions().stream().map(e -> new ExplosionAnimation(e.position())).collect(Collectors.toList()));

        batch.begin();

        moveBackground();

        drawExplosions(explosionAnimations);
        moveExplosions(explosionAnimations);
        explosionAnimations = removeExplosionsThatAreOffTheScreen(explosionAnimations);

        drawSprites(actors);

        batch.end();
    }

    private List<ExplosionAnimation> removeExplosionsThatAreOffTheScreen(List<ExplosionAnimation> explosionAnimations) {
        return explosionAnimations.stream()
                .filter(e -> e.getY() > -(VIEWPORT_HEIGHT / 2 + 0.3) && !e.getParticleEffect().isComplete())
                .collect(Collectors.toList());
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f / 255.0f, 168f / 255.0f, 240f / 255.0f, 0xff / 255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void drawSprites(Scene actors) {
        drawSpriteAtPosition(actors.butterfly().position(), butterfly);
        actors.lasers().forEach(l -> drawSpriteAtPosition(l.position(), laser));
        actors.dragonflies().forEach(df -> drawSpriteAtPosition(df.position(), dragonfly));
    }

    private void drawExplosions(Collection<ExplosionAnimation> explosionAnimations) {
        explosionAnimations.forEach(e -> {
            e.getParticleEffect().setPosition(e.getX(), e.getY());
            e.getParticleEffect().draw(batch);
        });
    }

    private void moveExplosions(Collection<ExplosionAnimation> explosionAnimations) {
        explosionAnimations.forEach(e -> {
            e.setY(e.getY() + SCROLLING_HEIGHT );
            e.getParticleEffect().update(0.0128f);
        });
    }

    private void moveBackground() {
        clouds.translate(0.0f, SCROLLING_HEIGHT);
        clouds.draw(batch);
        if (clouds.getY() < -(CLOUD_HEIGHT + VIEWPORT_HEIGHT / 2)) clouds.setY(VIEWPORT_HEIGHT / 2);
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
