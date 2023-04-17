package com.mygdx.animation_ivan;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ControllerAnimation implements ApplicationListener {
	Animation<TextureRegion> walk_animation;
	Texture player;
	SpriteBatch batch;
	float state_time;
	public TextureRegion[] frames = new TextureRegion[6];
	public float stateTime = 0f;
	Animation<TextureRegion> playerAnim;

	@Override
	public void create() {
		player = new Texture(Gdx.files.internal("spritesheet.png"));

		// per cada frame cal indicar x,y,amplada,alçada
		frames[0] = new TextureRegion(player, 86, 87, 26, 30);
		frames[1] = new TextureRegion(player, 111, 87, 18, 30);
		frames[2] = new TextureRegion(player, 129, 86, 19, 31);
		frames[3] = new TextureRegion(player, 149, 87, 25, 29);
		frames[4] = new TextureRegion(player, 203, 87, 22, 30);
		frames[5] = new TextureRegion(player, 227, 87, 18, 30);
		playerAnim = new Animation<>(0.20f, frames);

		batch = new SpriteBatch();
		state_time = 0f;
	}

	@Override
	public void resize(int width, int height) {  }

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		TextureRegion frame = playerAnim.getKeyFrame(stateTime, true);

		batch.begin();
		batch.draw(frame,
				200, 100,
				0,0,
				frame.getRegionWidth(),
				frame.getRegionHeight(),
				5,5,0);
		batch.end();
	}

	@Override
	public void pause() {  }

	@Override
	public void resume() {  }

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
	}
}
