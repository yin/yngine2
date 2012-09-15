package com.github.yin.yngine2demo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.github.yin.yingine2.android.render.shaders.IShaderManager;
import com.github.yin.yingine2.client.IYngineClient;
import com.github.yin.yngine2.common.OpenGL;
import com.github.yin.yngine2.common.Yngine;

public class TestClient implements IYngineClient {
	private OpenGL gl;
	public static final float vertexData[] = new float[] {
			-1.0f, -1.0f, 0.0f,
			1.0f, -1.0f, 0.0f,
			-1.0f, -1.0f, 0.0f,
			1.0f, -1.0f, 0.0f };
	private static final int FLOAT_SIZE = 4;
	private static final String TAG = "yngine";
	private static final int VERTICEX_DATA_OFFSET = 0;
	private static final int VERTEX_DATA_STRIDE_BYTES = 3 * FLOAT_SIZE;
	private final String vertexShaderSource =
			"uniform mat4 uMVPMatrix;\n"
			+ "attribute vec4 aPosition;\n"
			+ "void main() {\n"
			+ "  gl_Position = uMVPMatrix * aPosition;\n"
			+ "}\n";

	private final String fragmentShaderSource =
			"precision mediump float;\n"
			+ "void main() {\n"
			+ "  gl_FragColor = vec4(1.0, 0.3, 0.0, 1.0);\n"
			+ "}\n";
	private FloatBuffer vertex;
	private int program;
	private Yngine yngine;
	private int program_aPositionHandle;
	private int program_uMVPMatrixHandle;
	private float[] mMMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mProjMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	public TestClient(Yngine yngine) {
		this.yngine = yngine;
	}

	@Override
	public void init() {
		IShaderManager shaders = yngine.getShaderManager();
		vertex = ByteBuffer.allocateDirect(FLOAT_SIZE * vertexData.length)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertex.put(vertexData);

		program = shaders.createSimpleProgram(vertexShaderSource,
				fragmentShaderSource);
		if (program == 0) {
			return;
		}
		
		program_aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
		checkGlError("glGetAttribLocation aPosition");
		if (program_aPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for aPosition");
		}

		program_uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
		checkGlError("glGetUniformLocation uMVPMatrix");
		if (program_aPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for uMVPMatrix");
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onReshape(int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 100);
	}

	@Override
	public void update() {
		long time = SystemClock.uptimeMillis() % 4000L;
		float angle = 0.090f * ((int) time);
		Matrix.setLookAtM(mVMatrix, 0,
				0.0f, 0.0f, -5.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f);

		Matrix.setRotateM(mMMatrix, 0, angle, 0, 0, 1.0f);
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		GLES20.glUseProgram(program);
		checkGlError("glUseProgram");

		vertex.position(VERTICEX_DATA_OFFSET);
		GLES20.glVertexAttribPointer(program_aPositionHandle, 3, GLES20.GL_FLOAT,
				false, VERTEX_DATA_STRIDE_BYTES, vertex);
		checkGlError("glVertexAttribPointer maPosition");
		
		GLES20.glEnableVertexAttribArray(program_aPositionHandle);
		checkGlError("glEnableVertexAttribArray program_aPositionHandle");

		GLES20.glUniformMatrix4fv(program_uMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		checkGlError("glDrawArrays");
	}

	@Override
	public void useOGL(OpenGL arg0) {
		// TODO Auto-generated method stub

	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
