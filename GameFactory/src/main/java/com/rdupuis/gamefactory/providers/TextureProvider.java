package com.rdupuis.gamefactory.providers;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.Texture;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

public class TextureProvider {

    private Activity mActivity;

    public ArrayList<Texture> getTextureList() {
        return textureList;
    }

    public void setTextureList(ArrayList<Texture> textureList) {
        this.textureList = textureList;
    }

    private ArrayList<Texture> textureList;

    //set activity
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    //get activity
    public Activity getActivity() {
        return this.mActivity;
    }

    /**
     * Constructeur
     *
     * @param activity
     */
    public TextureProvider(Activity activity) {
        this.setTextureList(new ArrayList<Texture>());
        this.setActivity(activity);
    }


    public void initialize() {
        this.initGlTextureParam();
        this.initGlBuffer();
    }

    /**
     * add : Ajouter une nouvelle texture
     *
     * @param bitmapRessourceID
     */
    public void add(int bitmapRessourceID) {

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(this.getActivity().getAssets().open(
                    this.getActivity().getString(bitmapRessourceID)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Texture texture = new Texture();
        texture.setWidth(bitmap.getWidth());
        texture.setHeight(bitmap.getHeight());
        texture.setRessourceId(bitmapRessourceID);

        // on défini un buffer contenant tous les points de l'image
        // il en a (longeur x hauteur)
        // pour chaque point on a 4 bytes . 3 pour la couleur RVB et 1 pour
        // l'alpha
        ByteBuffer wrkTextureBuffer;
        wrkTextureBuffer = ByteBuffer.allocateDirect(bitmap.getHeight()
                * bitmap.getWidth() * 4);

        // on indique que les bytes dans le buffer doivent
        // être enregistré selon le sens de lecture natif de l'architecture CPU
        // (de gaucha a droite ou vice et versa)
        wrkTextureBuffer.order(ByteOrder.nativeOrder());

        byte buffer[] = new byte[4];
        // pour chaque pixel composant l'image, on mémorise sa couleur et
        // l'alpha
        // dans le buffer
        for (int i = 0; i < bitmap.getHeight(); i++) {
            for (int j = 0; j < bitmap.getWidth(); j++) {
                int color = bitmap.getPixel(j, i);
                buffer[0] = (byte) Color.red(color);
                buffer[1] = (byte) Color.green(color);
                buffer[2] = (byte) Color.blue(color);
                buffer[3] = (byte) Color.alpha(color);
                wrkTextureBuffer.put(buffer);
            }
        }
        // on se place a la position 0 du buffer - près à être lu plus tard
        wrkTextureBuffer.position(0);
        texture.setBufferTexture(wrkTextureBuffer);
        this.getTextureList().add(texture);

    }

    /**
     * @param ressourceId
     * @return
     */
    public Texture getTextureById(int ressourceId) {

        for (Texture texture : this.getTextureList()) {
            if (texture.getRessourceId() == ressourceId) return texture;
        }

        return null;
    }


    private void initGlBuffer() {

        int nbTextures = this.getTextureList().size();

        //on crée autant de buffer openGl que de textures
        int[] indexBuffer = new int[nbTextures];
        GLES20.glGenTextures(nbTextures, indexBuffer, 0);

        // on charge chaque texture dans un buffer Opengl et
        // on associe l'Id du buffer a la Texture
        int indx = 0;
        for (Texture texture : this.getTextureList()) {

            //on assigne un id de buffer à la texture
            texture.setGlBufferId(indexBuffer[indx]);

            //on recharge l'image
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(this.getActivity().getAssets().open(
                        this.getActivity().getString(texture.getRessourceId())));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // on défini un buffer contenant tous les points de l'image
            // il en a (longeur x hauteur)
            // pour chaque point on a 4 bytes . 3 pour la couleur RVB et 1 pour
            // l'alpha
            ByteBuffer wrkTextureBuffer;
            wrkTextureBuffer = ByteBuffer.allocateDirect(bitmap.getHeight()
                    * bitmap.getWidth() * 4);

            // on indique que les bytes dans le buffer doivent
            // être enregistrés selon le sens de lecture natif de l'architecture CPU
            // (de gauche a droite ou vice et versa)
            wrkTextureBuffer.order(ByteOrder.nativeOrder());

            byte buffer[] = new byte[4];
            // pour chaque pixel composant l'image, on récupère sa couleur et
            // l'alpha et on l'écrit dans le buffer
            for (int i = 0; i < bitmap.getHeight(); i++) {
                for (int j = 0; j < bitmap.getWidth(); j++) {
                    int color = bitmap.getPixel(j, i);
                    buffer[0] = (byte) Color.red(color);
                    buffer[1] = (byte) Color.green(color);
                    buffer[2] = (byte) Color.blue(color);
                    buffer[3] = (byte) Color.alpha(color);
                    wrkTextureBuffer.put(buffer);
                }
            }
            // on se place au debut du buffer
            wrkTextureBuffer.rewind();

            //on se positionne sur le buffer texture
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture.getGlBufferId());

            //--------------------------------------------------------------
            // définition des paramètres de magnification et minification des
            // texture
            // on indique GL_NEAREST pour dire que l'on doit prendre le pixel qui se
            // rapporche le plus
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // paramétrage du dépassement des coordonées de texture
            // GL_CLAMP_TO_EDGE = on étire la texture pour recouvrir la forme
            // on peu aussi mettre un paramètre pour répéter la texture ou bien
            // effectuer un mirroir

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                    GLES20.GL_CLAMP_TO_EDGE);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                    GLES20.GL_CLAMP_TO_EDGE);


            //------------------------------------------------------------


            //on ecrit dans le buffer
            GLES20.glTexImage2D(GL10.GL_TEXTURE_2D, texture.getGlBufferId(), GL10.GL_RGBA,
                    texture.getWidth(), texture.getHeight(), 0, GL10.GL_RGBA,
                    GL10.GL_UNSIGNED_BYTE, wrkTextureBuffer);

            indx++;
        }

    }

    public void initGlTextureParam() {

        // on active le texturing 2D
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        //on active l'unité de traitement des textures 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);



    }

    // charger la texture mémorisé dans le buffer dans le moteur de rendu comme
    // étant la texture 0,1,2,...
    public void putTextureToGLUnit(Texture texture, int unit) {
        GLES20.glTexImage2D(GL10.GL_TEXTURE_2D, unit, GL10.GL_RGBA,
                texture.getWidth(), texture.getHeight(), 0, GL10.GL_RGBA,
                GL10.GL_UNSIGNED_BYTE, texture.getBufferTexture());

    }

}
