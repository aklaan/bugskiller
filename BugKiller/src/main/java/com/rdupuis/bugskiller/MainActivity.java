package com.rdupuis.bugskiller;


import com.rdupuis.bugskiller.scenes.Scene01;
import com.rdupuis.gamefactory.components.OpenGLActivity;

import android.app.ActionBar;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends OpenGLActivity {

    public TextView tv;
    public String texte;
    public Handler mHandler;


    public MainActivity() {
        super();



        //je crée un Handler et je reféfini la méthose handleMessage
        //de cette manière, je peux capter des infromations qui sont émises par d'autres
        //thread pour pouvoireffectuer des actions dans cette scène.
        //notamemnt, la mise à jour des View (textView...etc..)
        //seul le Thread de la scène peu mettre à jour les vue de la scène
        mHandler = new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(Message inputMessage) {
                // Gets the image task from the incoming Message object.
                //PhotoTask photoTask = (PhotoTask) inputMessage.obj;

                switch (inputMessage.what) {

                    default:
                        tv.setText(texte);

                }

            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //

        // mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        EditText editText = new EditText(this);
        editText.setText("Bonjour");
        editText.setTextColor(Color.WHITE);
//        addContentView(editText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        View toto = getLayoutInflater().inflate(R.layout.activity_main, null);

        addContentView(toto, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.tv = (TextView) this.findViewById(R.id.boxx);

        mGLSurfaceView.setRenderer(new Scene01(this));

    }


    public void test() {
        this.tv.setText(texte);

    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }


}
