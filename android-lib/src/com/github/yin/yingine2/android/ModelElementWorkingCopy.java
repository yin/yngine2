package com.github.yin.yingine2.android;

import java.util.Collection;

public class ModelElementWorkingCopy implements IElementWorkingCopy<ModelElement> {
	private ModelElement original = null;

	@Override
	public void work(ModelElement element) {
		this.original = element;
		
	}

	@Override
	public void set(String id, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ModelElement finish() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void newCopy() {
		
	}
}
