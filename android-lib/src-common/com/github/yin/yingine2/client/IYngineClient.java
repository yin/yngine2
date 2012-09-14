package com.github.yin.yingine2.client;

import com.github.yin.yngine2.common.OpenGL;

public interface IYngineClient {
	public void init();
	public void update();
	public void dispose();
	public void onReshape(int width, int height);
	public void useOGL(OpenGL ogl);
}
