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
import android.opengl.GLUtils;
import android.util.Log;

/**
 * TextureProvider : class servant à gérer les textures
 */
public class TextureProvider {

    private Activity mActivity;
    private ArrayList<Texture> textureList;

    /**
     * Constructeur
     *
     * @param activity
     */
    public TextureProvider(Activity activity) {
        this.setTextureList(new ArrayList<Texture>());
        this.setActivity(activity);
    }

    public ArrayList<Texture> getTextureList() {
        return textureList;
    }

    public void setTextureList(ArrayList<Texture> textureList) {
        this.textureList = textureList;
    }

    //set activity
    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    //get activity
    public Activity getActivity() {
        return this.mActivity;
    }

    public void initialize() {
        this.initGlTextureParam();
        this.initGlBuffer();
    }

    /**
     * intialisation générales à ne faire d'une fois au démarrage
     */
    public void initGlTextureParam() {

        // on active le texturing 2D
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
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

        this.getTextureList().add(texture);

    }





    /**
     * Recherche d'une texture via son ID dans la liste des textures
     * @param ressourceId
     * @return
     */
    public Texture getTextureById(int ressourceId) {

        for (Texture texture : this.getTextureList()) {
            if (texture.getRessourceId() == ressourceId) return texture;
        }
        //si la texture n'a pas été trouvé, on retourne null
        throw new RuntimeException("la Texture recherché n'a pas été ajouté dans la liste du TextureProvider " + String.valueOf(ressourceId));
      //  return null;
    }

    /**
     * Initialisation des buffer OpenGl
     * -> pour chaques texture, on charge l'image dans le buffer OpenGl
     *    et on mémorise l'index du buffer dans lequel on a enregistré la texture
     *
     *    cette initialisation ne doit se faire qu'une fois au moment du surfaceCreated
     *    il faut donc que toutes les textures utilisées par la la scène soient référencées en amont
     *    dans le provider.
     */
    private void initGlBuffer() {

        //On récupère le nombre de textures à traiter
        int nbTextures = this.getTextureList().size();

        //on crée autant de buffer openGl que de textures
        int[] indexBuffer = new int[nbTextures];
        GLES20.glGenTextures(nbTextures, indexBuffer, 0);

        // on charge chaque textures dans un buffer Opengl et
        // on associe l'Id du buffer a la Texture
        int indx = 0;
        for (Texture texture : this.getTextureList()) {

            //on assigne un id de buffer à la texture
            texture.setGlBufferId(indexBuffer[indx]);

            //on charge l'image qui va servir à la texture
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(this.getActivity().getAssets().open(
                        this.getActivity().getString(texture.getRessourceId())));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

             //on se positionne sur le buffer texture sur lequel on souhaite écrire
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
            //on écrit dans le buffer
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);

            //on supprime l'image de la mémoire
            bitmap.recycle();
            indx++;
        }

    }


    // charger la texture mémorisé dans le buffer dans le moteur de rendu comme
    // étant la texture 0,1,2,...
    public void putTextureToGLUnit(Texture texture, int unit) {
        GLES20.glTexImage2D(GL10.GL_TEXTURE_2D, unit, GL10.GL_RGBA,
                texture.getWidth(), texture.getHeight(), 0, GL10.GL_RGBA,
                GL10.GL_UNSIGNED_BYTE, texture.getBufferTexture());

    }

}
