package com.github.yin.yingine2.android;

public class CubeGenerator implements IModelGenerator {
	private IModelHandler handler;

	@Override
	public void setHandler(IModelHandler handler) {
		this.handler = handler;
	}

	@Override
	public IModelHandler getHandler() {
		return handler;
	}

	public void emit() {
		
	}

	public void generate() {
		int i = 0;
		int vertex[] = new int[8];
		for (int z = -1; z <= 1; z += 2) {
			for (int y = -1; y <= 1; y += 2) {
				for (int x = -1; x <= 1; x += 2) {
					vertex[i++] = handler.vertex(new float[] {x, y, z}); 
				}
			}
		}
		square(0, 1, 2, 3);
		square(7, 6, 5, 4);
		square(1, 5, 3, 7);
		square(6, 4, 0, 2);
		square(4, 5, 0, 1);
		square(2, 3, 6, 7);
	}

	private void square(int i, int j, int k, int l) {
		triangle(i, j, k);
		triangle(k, j, l);
	}

	private void triangle(int a, int b, int c) {
		handler.face(a, b, c);
	}
}
