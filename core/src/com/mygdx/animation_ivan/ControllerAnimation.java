package com.mygdx.animation_ivan;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.animation_ivan.utils.AppConstants;

public class ControllerAnimation implements ApplicationListener {
	Animation<TextureRegion> walk_animation;
	Texture walk_sheet;
	SpriteBatch batch;
	float state_time;
	@Override
	public void create() {
		walk_sheet = new Texture(Gdx.files.internal("sprite_sheet.png"));

		TextureRegion[][] txt_region = TextureRegion.split(
				walk_sheet,
				walk_sheet.getWidth() / AppConstants.FRAME_COLS,
				walk_sheet.getHeight() / AppConstants.FRAME_ROWS
		);

		TextureRegion[] walkFrames = new TextureRegion[AppConstants.FRAME_COLS * AppConstants.FRAME_ROWS];
		int index = 0;
		for (int it = 0; it < AppConstants.FRAME_ROWS; it++) {
			for (int it2 = 0; it2 < AppConstants.FRAME_COLS; it2++) {
				walkFrames[index++] = txt_region[it][it2];
			}
		}

		walk_animation = new Animation<>(0.05f, walkFrames);
		batch = new SpriteBatch();
		state_time = 0f;
	}

	@Override
	public void resize(int width, int height) {  }

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		state_time += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = walk_animation.getKeyFrame(state_time, true);
		batch.begin();
		batch.draw(currentFrame,100, 100, 400, 400);
		batch.end();
	}

	@Override
	public void pause() {  }

	@Override
	public void resume() {  }

	@Override
	public void dispose() {
		batch.dispose();
		walk_sheet.dispose();
	}
}
