package com.github.yin.yngine2.common;


public interface OpenGL extends OpenGLES {
	public void lookAt(float eyeX, float eyeY, float eyeZ, float centerX,
			float centerY, float centerZ, float upX, float upY, float upZ);

	public void perspective(float fov, float aspect, float near, float far);
}
