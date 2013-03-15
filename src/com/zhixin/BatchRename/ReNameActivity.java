package com.zhixin.BatchRename;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
public class ReNameActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rename);
		Intent i= this.getIntent();
		this.setTitle(i.getStringExtra("path"));
	}
}
