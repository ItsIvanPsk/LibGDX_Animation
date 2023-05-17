package com.mygdx.animation_ivan;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

import org.json.JSONObject;

public class ControllerAnimation extends ApplicationAdapter {

	Texture player, background;
	SpriteBatch spriteBatch;
	TextureRegion[] frames = new TextureRegion[6];
	private OrthographicCamera camera;
	Animation<TextureRegion> playerAnim;
	float stateTime;
	SpriteBatch batch;
	float posx, posy;

	Rectangle up, down, left, right;
	final int IDLE = 0, UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4;
	float lastSend = 0f;

	WebSocket socket;
	String address = "localhost";
	int port = 8888;

	@Override
	public void create() {

		if (Gdx.app.getType() == Application.ApplicationType.Android)
			// en Android el host és accessible per 10.0.2.2
			address = "10.0.2.2";
		socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(address, port));
		socket.setSendGracefully(false);
		socket.addListener((WebSocketListener) new MyWSListener());
		socket.connect();
		socket.send("Enviar dades");

		background = new Texture(Gdx.files.internal("background.png"));
		posx = 750;
		posy = 450;
		player = new Texture("spritesheet.png");
		frames[0] = new TextureRegion(player, 86, 87, 26, 30);
		frames[1] = new TextureRegion(player, 111, 87, 18, 30);
		frames[2] = new TextureRegion(player, 129, 86, 19, 31);
		frames[3] = new TextureRegion(player, 149, 87, 25, 29);
		frames[4] = new TextureRegion(player, 203, 87, 22, 30);
		frames[5] = new TextureRegion(player, 227, 87, 18, 30);
		playerAnim = new Animation<>(0.15f, frames);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 2900, 1480);
		batch = new SpriteBatch();
		stateTime = 0f;

		up = new Rectangle(0, 1480 * 2 / 3f, 2900, 2900 / 3f);
		down = new Rectangle(0, 0, 2900, 1480 / 3f);
		left = new Rectangle(0, 0, 2900 / 3f, 1480);
		right = new Rectangle(2900 * 2 / 3f, 0, 2900 / 3f, 1480);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = playerAnim.getKeyFrame(stateTime, true);

		batch.begin();
		batch.draw(background, 0, 0, 2900, 1480);
		background.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		int direction = virtual_joystick_control();

		switch (direction){
			case 0:
				break;
			case 1:
				Player.transform[1] += Player.speed;
				break;
			case 2:
				Player.transform[1] -= Player.speed;
				break;
			case 3:
				Player.transform[0] -= Player.speed;
				break;
			case 4:
				Player.transform[0] += Player.speed;
			default: // IDLE
				break;
		}

		batch.draw(frame, Player.transform[0], Player.transform[1], 0, 0, frame.getRegionWidth(), frame.getRegionHeight(), 5, 5, 0);

		batch.end();


		if (stateTime - lastSend > 1.0f) {
			lastSend = (int) stateTime;
			JSONObject json = new JSONObject();
			json.put("player_x", Player.transform[0]);
			json.put("player_y", Player.transform[1]);
			socket.send(json.toString());
		}
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

	protected int virtual_joystick_control() {
		for (int i = 0; i < 10; i++)
			if (Gdx.input.isTouched(i)) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				camera.unproject(touchPos);
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

	class MyWSListener implements WebSocketListener {

		@Override
		public boolean onOpen(WebSocket webSocket) {
			System.out.println("Opening...");
			return false;
		}

		@Override
		public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
			System.out.println("Closing...");
			return false;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, String packet) {
			System.out.println("Message:");
			return false;
		}

		@Override
		public boolean onMessage(WebSocket webSocket, byte[] packet) {
			System.out.println("Message:");
			return false;
		}

		@Override
		public boolean onError(WebSocket webSocket, Throwable error) {
			System.out.println("ERROR:" + error.toString());
			return false;
		}
	}
}
