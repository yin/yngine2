package com.github.yin.yingine2.android;

import java.util.HashMap;
import java.util.Map;

public class ModelElement implements IElement, Cloneable {
	private Map<String, Object> properties = new HashMap<String, Object>();

	public ModelElement() {
	}

	@Override
	public Object get(String id) {
		return null;
	}

}
