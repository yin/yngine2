package com.github.yin.yingine2.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.yin.yingine2.android.render.AndroidGLES2;
import com.github.yin.yingine2.client.IClientAdapter;
import com.github.yin.yingine2.client.IYngineClient;
import com.github.yin.yingine2.client.IYngineClient.Touch;

public class YngineView extends GLSurfaceView implements IClientAdapter {
	IYngineClient client;

	public YngineView(Context context) {
		super(context);
		init();
	}

	public YngineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setEGLContextClientVersion(2);
	}

	@Override
	public void setClient(IYngineClient client) {
		this.client = client;
		setRenderer(new RendererClientAdapter());
		setOnTouchListener(new TouchAdapter());
	}

	@Override
	public IYngineClient getClient() {
		return client;
	}

	public class RendererClientAdapter implements GLSurfaceView.Renderer {
		@Override
		public void onDrawFrame(GL10 gl) {
			client.update();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			client.onReshape(width, height);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			client.init();
			client.useOGL(new AndroidGLES2());
		}
	}

	public class TouchAdapter implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (client == null)
				return false;

			Touch touch = client.getTouch();
			if (touch != null) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					touch.down(0, event.getX(), event.getY());
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					touch.up(0, event.getX(), event.getY());
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					touch.move(0, event.getX(), event.getY());
				}
			}
			return true;
		}
	}
}
