package com.rdupuis.gamefactory.providers;

import java.io.IOException;
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

public class BitmapProvider {

	private Activity mActivity;
	private ArrayList<Bitmap> bitmapArrayList;
	HashMap<String, String> catalog;

	
	//set activity
	public void setActivity(Activity activity){
		this.mActivity = activity;
	}
	

	//get activity
		public Activity getActivity(){
			return this.mActivity ;
		}

	
	public BitmapProvider(Activity activity) {
		catalog = new HashMap<String, String>();
		bitmapArrayList = new ArrayList<Bitmap>();
		this.setActivity(activity);
	}

	/**
	 * 
	 * @param bitmapName
	 */
	public void add(String folder, int ressourceID) {

		Bitmap bitmap = null;
			
			try {
				bitmap = BitmapFactory.decodeStream(this.getActivity().getAssets().open(
				folder + "/" +	this.getActivity().getString(ressourceID)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
	

		int newindex = catalog.size() + 1;
		catalog.put(String.valueOf(ressourceID), String.valueOf(newindex));
		bitmapArrayList.add(bitmap);
	}

	/**
	 * 
	 * @param bitmapName
	 * @return
	 */
	public Bitmap getBitmapByName(String bitmapName) {
		Bitmap result = null;
		if (catalog.get(bitmapName) == null) {
			Log.e(this.getClass().getName(), "bitmap unknow on Catalog");
		} else {
			result = bitmapArrayList.get(Integer.parseInt(catalog
					.get(bitmapName)) - 1);
		}
		return result;
	}

	// load a texture
	public void linkTexture(int texturerId, GameObject gameobject) {

		Texture texture = new Texture();

		Bitmap bitmap = getBitmapByName(String.valueOf(texturerId));

		texture.width = bitmap.getWidth();
		texture.hight = bitmap.getHeight();

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
		// l'alpha dans le buffer
		
		
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
		texture.texture = wrkTextureBuffer;
		gameobject.setTexture(texture);
		gameobject.textureEnabled = true;
	}

	
	
	public Texture getTexture(int texturerId) {

		Texture texture = new Texture();

		Bitmap bitmap = getBitmapByName(String.valueOf(texturerId));

		texture.width = bitmap.getWidth();
		texture.hight = bitmap.getHeight();

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
		texture.texture = wrkTextureBuffer;
		return texture;
	
	}
	
	// charger la texture mémorisé dans le buffer dans le moteur de rendu comme
	// étant la texture 0,1,2,...
	public void putTextureToGLUnit(Texture texture, int unit) {
		GLES20.glTexImage2D(GL10.GL_TEXTURE_2D, unit, GL10.GL_RGBA,
				texture.width, texture.hight, 0, GL10.GL_RGBA,
				GL10.GL_UNSIGNED_BYTE, texture.texture);

	}

}
