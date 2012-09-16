package com.github.yin.yngine2demo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.FloatMath;
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
		-1.0f, 1.0f, 0.0f,
		1.0f, 1.0f, 0.0f,
		};
	public static final float colorData[] = new float[] {
		0.0f, 0.0f, 1.0f, 1.0f,
		1.0f, 0.0f, 0.0f, 1.0f,
		0.0f, 1.0f, 0.0f, 1.0f,
		1.0f, 1.0f, 0.0f, 1.0f,
		};
	public static final short indexData[] = new short[] {
		0, 1, 2,
		2, 1, 3,
		};
	private final String vertexShaderSource = "uniform mat4 uMVPMatrix;\n"
			+ "attribute vec3 aPosition;\n"
			+ "attribute vec4 aColor;\n"
			+ "varying vec4 c;\n"
			+ "void main() {\n"
			+ "  gl_Position = uMVPMatrix * vec4(aPosition, 1.0);\n"
			+ "  c = aColor;"
			+ "}\n";

	private final String fragmentShaderSource = "precision mediump float;\n"
			+ "varying vec4 c;\n"
			+ "void main() {\n"
			+ "  gl_FragColor = c;\n"
			+ "}\n";
	private static final int SHORT_SIZE = 2;
	private static final int FLOAT_SIZE = 4;
	private static final String TAG = "yngine";
	private static final int VERTEX_DATA_OFFSET = 0;
	private static final int VERTEX_DATA_STRIDE = 3;
	private static final int VERTEX_DATA_STRIDE_BYTES = 3 * FLOAT_SIZE;
	private static final int COLOR_DATA_OFFSET = 0;
	private static final int COLOR_DATA_STRIDE = 4;
	private static final int COLOR_DATA_STRIDE_BYTES = 4 * FLOAT_SIZE;
	private FloatBuffer vertex;
	private ShortBuffer index;
	private int program;
	private Yngine yngine;
	private int program_aPositionHandle;
	private int program_aColorHandle;
	private int program_uMVPMatrixHandle;
	private float[] mMMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mProjMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private IShaderManager shaders;
	private Random r;
	private float ex, ey, ez, tx, ty, tz, ux, uy, uz;
	private long s;
	private FloatBuffer color;
	
	public TestClient(Yngine yngine) {
		this.yngine = yngine;
		shaders = yngine.getShaderManager();
		vertex = ByteBuffer.allocateDirect(FLOAT_SIZE * vertexData.length)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertex.put(vertexData).position(0);
		color = ByteBuffer.allocateDirect(FLOAT_SIZE * colorData.length)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		color.put(colorData).position(0);
		index = ByteBuffer.allocateDirect(SHORT_SIZE * indexData.length)
				.order(ByteOrder.nativeOrder()).asShortBuffer();
		index.put(indexData).position(0);
	}

	@Override
	public void init() {
		program = shaders.createSimpleProgram(vertexShaderSource,
				fragmentShaderSource);
		if (program == 0) {
			return;
		}

		program_aPositionHandle = GLES20.glGetAttribLocation(program,
				"aPosition");
		checkGlError("glGetAttribLocation aPosition");
		if (program_aPositionHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for aPosition");
		}

		program_aColorHandle = GLES20.glGetAttribLocation(program,
				"aColor");
		checkGlError("glGetAttribLocation aColor");
		if (program_aColorHandle == -1) {
			throw new RuntimeException(
					"Could not get attrib location for aColor");
		}

		program_uMVPMatrixHandle = GLES20.glGetUniformLocation(program,
				"uMVPMatrix");
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
		long time = SystemClock.uptimeMillis();
		float angle = 0.090f * ((int) time);

		client_camera(time);
		Matrix.setLookAtM(mVMatrix, 0, ex, ey, ez, tx, ty, tz, ux, uy, uz);

		Matrix.setRotateM(mMMatrix, 0, angle, 0, 0, 1.0f);
		//Matrix.setIdentityM(mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

		GLES20.glUseProgram(program);
		checkGlError("glUseProgram");

		vertex.position(VERTEX_DATA_OFFSET);
		GLES20.glVertexAttribPointer(program_aPositionHandle, vertexData.length / VERTEX_DATA_STRIDE,
				GLES20.GL_FLOAT, false, VERTEX_DATA_STRIDE_BYTES, vertex);
		checkGlError("glVertexAttribPointer aPosition");
		GLES20.glEnableVertexAttribArray(program_aPositionHandle);
		checkGlError("glEnableVertexAttribArray program_aPositionHandle");

		color.position(COLOR_DATA_OFFSET);
		GLES20.glVertexAttribPointer(program_aColorHandle, colorData.length / COLOR_DATA_STRIDE,
				GLES20.GL_FLOAT, false, COLOR_DATA_STRIDE_BYTES, color);
		checkGlError("glVertexAttribPointer aColor");
		GLES20.glEnableVertexAttribArray(program_aColorHandle);
		checkGlError("glEnableVertexAttribArray program_aColorHandle");

		GLES20.glUniformMatrix4fv(program_uMVPMatrixHandle, 1, false,
				mMVPMatrix, 0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, indexData.length, GLES20.GL_UNSIGNED_SHORT, index);
		checkGlError("glDrawArrays");
		s = time / 1000;
	}

	private void client_camera(long time) {
		int a = 0;
		if (a == 0) {
			ex =  FloatMath.sin((float)time / 1000);
			ey =  FloatMath.sin((float)time / 1000);
			ez = -5.0f;
			tx =  0.0f;
			ty =  0.0f;
			tz =  0.0f;
			ux =  0.0f;
			uy =  1.0f;
			uz =  0.0f;
		} else if (time / 1000 != s && a == 1) {
			if (r == null) {
				r = new Random();
			}
			ex = r.nextFloat() * 20 - 10.0f;
			ey = r.nextFloat() * 20 - 10.0f;
			ez = r.nextFloat() * 20 - 10.0f;
			tx = r.nextFloat() * 20 - 10.0f;
			ty = r.nextFloat() * 20 - 10.0f;
			tz = r.nextFloat() * 20 - 10.0f;
			ux = r.nextFloat() * 20 - 10.0f;
			uy = r.nextFloat() * 20 - 10.0f;
			uz = r.nextFloat() * 20 - 10.0f;
			StringBuilder sb = new StringBuilder();
			while (vertex.hasRemaining()) {
				sb.append(vertex.get()).append(':');
			}
			Log.d(TAG, "Reset: " + ex + " " + ey + " " + ez + " " + tx + " "
					+ ty + " " + tz + " " + ux + " " + uy + " " + uz + "@" + sb);
		}
	}

	@Override
	public void useOGL(OpenGL gl) {
		this.gl = gl;
	}

	private void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}
}
