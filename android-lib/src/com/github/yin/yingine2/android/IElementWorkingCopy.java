package com.github.yin.yingine2.android;

import java.util.Collection;

public interface IElementWorkingCopy<T extends IElement> {
	public void work(T element);
	public void set(String id, Object value);
	public void get(String id);
	public Collection<String> getIds();
	public T finish();
}
