package com.github.yin.yngine2demo.util;

import android.opengl.Matrix;

public class YngineUtil {
	public String[] transformVertexData(float[] vertexData, int stride, float[] mvpMatrix) {
		int l = vertexData.length;
		int n = l / stride;
		String ret[] = new String[n];
		float m[] = new float[16];
		float v[] = new float[4];
		for (int i = 0; i < n; i++) {
			int p = i * stride;
			v[0] = vertexData[p];
			v[1] = vertexData[p + 1];
			v[2] = vertexData[p + 2];
			v[3] = 1;
			Matrix.multiplyMV(m, 0, mvpMatrix, 0, v, 0);
			StringBuilder sb = new StringBuilder();
			for (float e : m) {
				sb.append(e).append(':');
			}
			ret[i/stride] = "n:" + ((i / stride) % 3) + " m:" + sb.toString();
		}
		return ret;
	}
}
