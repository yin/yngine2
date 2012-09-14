package com.github.yin.yingine2.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.github.yin.yingine2.android.render.AndroidGLES2;
import com.github.yin.yingine2.client.IClientAdapter;
import com.github.yin.yingine2.client.IYngineClient;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class YngineView extends GLSurfaceView {
	public YngineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public class RendererClientInvoker implements GLSurfaceView.Renderer, IClientAdapter {
		IYngineClient client;

		@Override
		public void setClient(IYngineClient client) {
			this.client = client;
		}

		@Override
		public IYngineClient getClient() {
			return client;
		}

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
}
