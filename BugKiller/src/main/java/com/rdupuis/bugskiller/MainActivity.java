package com.rdupuis.bugskiller;


import com.rdupuis.bugskiller.scenes.Scene01;
import com.rdupuis.gamefactory.components.OpenGLActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends OpenGLActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //

        mGLSurfaceView.setRenderer(new Scene01(this));
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        EditText editText = new EditText(this);
        editText.setText("Bonjour");
        editText.setTextColor(Color.WHITE);
        addContentView(editText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


}
