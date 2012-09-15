package com.github.yin.yingine2.client;

import com.github.yin.yingine2.android.render.shaders.IShaderManager;
import com.github.yin.yingine2.android.render.shaders.ShaderManager;
import com.github.yin.yngine2.common.Yngine;

public class AndroidYngine implements Yngine {
	private ShaderManager shaderManager;

	@Override
	public IShaderManager getShaderManager() {
		if (shaderManager == null) {
			shaderManager = new ShaderManager();
		}
		return shaderManager;
	}
}
