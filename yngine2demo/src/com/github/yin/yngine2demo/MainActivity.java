package com.github.yin.yngine2demo;

import android.app.Activity;
import android.os.Bundle;

import com.github.yin.yingine2.android.YngineView;
import com.github.yin.yingine2.client.AndroidYngine;

public class MainActivity extends Activity {
	private YngineView view;
	private AndroidYngine yngine;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yngine = new AndroidYngine();
        view = new YngineView(getApplication());
        view.setClient(new TestClient(yngine));
        setContentView(view);
    }

	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
	}
}
