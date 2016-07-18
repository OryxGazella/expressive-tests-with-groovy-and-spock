package soy.frank.flutterby.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import soy.frank.flutterby.actors.*;

import java.util.*;
import java.util.stream.Collectors;

public class Renderer implements Disposable {

    private static final float CLOUD_HEIGHT = (float) (2697 / 1920);
    private static final float VIEWPORT_HEIGHT = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
    private static final float SCROLLING_HEIGHT = -0.001f;
    private static final float LIFE_HEIGHT = Butterfly.WIDTH / 2;
    private static final float LIFE_WIDTH = Butterfly.WIDTH / 2;
    private final SpriteBatch batch;
    private final Sprite clouds;
    private final Sprite laser;
    private final BitmapFont font;
    private final Matrix4 simpleProjectionMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

    private final Map<String, Texture> textures = new HashMap<>();
    private final Sprite dragonfly;
    private final Sprite butterfly;
    private final Sprite life;

    private List<ExplosionAnimation> explosionAnimations = Collections.emptyList();
    private final OrthographicCamera camera;

    public Renderer() {

        float bottom = -VIEWPORT_HEIGHT / 2;
        camera = new OrthographicCamera( 1f, VIEWPORT_HEIGHT);
        batch = new SpriteBatch();
        butterfly = createSprite("butterfly.png", 96, 96, Butterfly.WIDTH, Butterfly.HEIGHT);
        clouds = createSprite("clouds.png", 1920, 2697, 1.0f, CLOUD_HEIGHT);
        laser = createSprite("laser.png", 12, 72, Laser.WIDTH, Laser.HEIGHT);
        dragonfly = createSprite("dragonfly.png", 144, 48, DragonFly.WIDTH, DragonFly.HEIGHT);
        life = createSprite("butterfly_life.png", 48, 48, LIFE_WIDTH, LIFE_HEIGHT);

        clouds.setPosition(-0.500f, bottom);
        font = new BitmapFont();
        Gdx.input.setCursorCatched(true);
        Gdx.input.setCursorPosition(Gdx.graphics.getHeight(), Gdx.graphics.getWidth());
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
        explosionAnimations.addAll(actors.getExplosions().stream().map(e -> new ExplosionAnimation(e.getPosition())).collect(Collectors.toList()));
        boolean gameOver = actors.lives() < 0;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        moveBackground();

        drawExplosions(explosionAnimations);
        moveExplosions(explosionAnimations);
        explosionAnimations = removeExplosionsThatAreOffTheScreen(explosionAnimations);

        drawSprites(actors);
        batch.end();

        batch.setProjectionMatrix(simpleProjectionMatrix);
        batch.begin();
        drawScore(actors.getScore());
        if(gameOver) {
            drawGameOver();
        }
        if(actors.isPaused()) {
            drawPaused();
        }
        batch.end();
    }

    private void drawGameOver() {
        font.setColor(Color.FIREBRICK);
        font.getData().setScale(2.0f);
        font.draw(batch,
                "Game Over\n" +
                        "Press 'R' to Restart\n" +
                        "Press <Esc> to quit",
                Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2);
    }

    private void drawPaused() {
        font.setColor(Color.FIREBRICK);
        font.getData().setScale(2.0f);
        font.draw(batch,
                "Paused\n" +
                        "Press 'Fire' to Quit\n" +
                        "Press any movement key to continue",
                Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2);
    }

    private void drawScore(int score) {
        font.setColor(Color.BLACK);
        font.getData().setScale(1.5f);
        font.getData().setScale(1.0f);
        int width = Gdx.graphics.getWidth();
        float padding = 0.005f * width;
        float margin = 0.01f * width;
        font.draw(batch, "Score: " + score,
                width - 3 * LIFE_WIDTH * width - 2 * padding - margin,
                Gdx.graphics.getHeight() - (LIFE_HEIGHT * width) - (padding) - font.getLineHeight() - margin / 2);
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
        drawSpriteAtPosition(actors.getButterfly().getPosition(), butterfly);
        drawLives(actors.lives());
        actors.getLasers().forEach(l -> drawSpriteAtPosition(l.getPosition(), laser));
        actors.getDragonflies().forEach(df -> drawSpriteAtPosition(df.getPosition(), dragonfly));
    }

    private void drawLives(int lives) {
        float margin = 0.01f;
        float padding = 0.005f;
        for(int i = 0; i < lives; i++) {
            drawSpriteAtPosition(
                    Vector2D.of(0.5f - LIFE_HEIGHT - (LIFE_HEIGHT + padding) * i - margin, VIEWPORT_HEIGHT / 2 - LIFE_HEIGHT - margin),
                    life);
        }
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
        sprite.setX(position.getX());
        sprite.setY(position.getY());
        sprite.draw(batch);
    }


    @Override
    public void dispose() {
        textures.values().forEach(Texture::dispose);
        font.dispose();
    }
}
