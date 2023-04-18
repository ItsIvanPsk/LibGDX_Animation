package com.mygdx.animation_ivan;

import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.UP;
import static java.awt.Event.DOWN;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.awt.Rectangle;

public class ControllerAnimation implements ApplicationListener {
	private static final int SCR_HEIGHT = 480;
	private static final int SCR_WIDTH = 800;

	Animation<TextureRegion> walk_animation;
	Texture player;
	Texture background;

	TextureRegion bgRegion;

	Rectangle up, down, left, right, fire;
	private int UP = 1, DOWN = 0, LEFT = 2, RIGHT = 3, IDLE = 4;

	SpriteBatch batch;
	float state_time;
	public TextureRegion[] frames = new TextureRegion[6];
	public float stateTime = 0f;
	Animation<TextureRegion> playerAnim;

	@Override
	public void create() {
		player = new Texture(Gdx.files.internal("spritesheet.png"));
		// facilities per calcular el "touch"
		up = new Rectangle(0, SCR_HEIGHT * 2 / 3, SCR_WIDTH, SCR_HEIGHT / 3);
		down = new Rectangle(0, 0, SCR_WIDTH, SCR_HEIGHT / 3);
		left = new Rectangle(0, 0, SCR_WIDTH / 3, SCR_HEIGHT);
		right = new Rectangle(SCR_WIDTH * 2 / 3, 0, SCR_WIDTH / 3, SCR_HEIGHT);
		// per cada frame cal indicar x,y,amplada,alçada
		frames[0] = new TextureRegion(player, 86, 87, 26, 30);
		frames[1] = new TextureRegion(player, 111, 87, 18, 30);
		frames[2] = new TextureRegion(player, 129, 86, 19, 31);
		frames[3] = new TextureRegion(player, 149, 87, 25, 29);
		frames[4] = new TextureRegion(player, 203, 87, 22, 30);
		frames[5] = new TextureRegion(player, 227, 87, 18, 30);
		playerAnim = new Animation<>(0.20f, frames);

		batch = new SpriteBatch();

		background = new Texture(Gdx.files.internal("background.png"));
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		bgRegion = new TextureRegion(background);

		state_time = 0f;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = playerAnim.getKeyFrame(stateTime, true);
		bgRegion.setRegion(100, 100, 800, 480);

		batch.begin();
		batch.draw(bgRegion, 0, 0);
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		int direction = virtual_joystick_control();

		switch (direction){
			case 0:
				Player.transform[1] += Player.speed;
				break;
			case 1:
				Player.transform[1] -= Player.speed;
				break;
			case 2:
				Player.transform[0] += Player.speed;
				break;
			case 3:
				Player.transform[0] += Player.speed;
				break;
			default: // IDLE
				break;
		}

		batch.draw(frame, Player.transform[0], Player.transform[1], 0, 0, frame.getRegionWidth(), frame.getRegionHeight(), 5, 5, 0);

		batch.end();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
	}

	protected int virtual_joystick_control() {
		// iterar per multitouch
		// cada "i" és un possible "touch" d'un dit a la pantalla
		for (int i = 0; i < 10; i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				// traducció de coordenades reals (depen del dispositiu) a 800x480
				// game.camera.unproject(touchPos);
				if (up.contains(touchPos.x, touchPos.y)) {
					return UP;
				} else if (down.contains(touchPos.x, touchPos.y)) {
					return DOWN;
				} else if (left.contains(touchPos.x, touchPos.y)) {
					return LEFT;
				} else if (right.contains(touchPos.x, touchPos.y)) {
					return RIGHT;
				}
			}
		return IDLE;
	}

}
