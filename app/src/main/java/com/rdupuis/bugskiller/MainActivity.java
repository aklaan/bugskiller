package com.rdupuis.bugskiller;



import com.rdupuis.bugskiller.scenes.Scene01;
import com.rdupuis.gamefactory.components.OpenGLActivity;

import android.os.Bundle;

public class MainActivity extends OpenGLActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// 
		mGLSurfaceView.setRenderer(new Scene01(this));
	
		
	}

	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	

}
