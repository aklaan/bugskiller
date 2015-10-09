package com.rdupuis.gamefactory.utils;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;



public class Tools {

    /** read an asset file as Text File and return a string */
    public static String readShaderFile(Context context, String filename) {
		return filename;
      
    	/***
    	try {
            InputStream iStream = context.getAssets().open(
            		   		context.getString(R.string.shaderfolder) +"/" +            		
            		filename);
            return readStringInput(iStream);
        } catch (IOException e) {
            Log.e("Testgame", "Shader " + filename + " cannot be read");
            return "";
        }
*/    }

    /** read string input stream */
    public  static String readStringInput(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[4096];
        for (int n; (n = in.read(buffer)) != -1;) {
            sb.append(new String(buffer, 0, n));
        }
        return sb.toString();
    }

	// retourne un float aléatoire entre 0 et 1
	public static float getRamdom() {
		float value = (float) (Math.random() * 2. - 1.);
		return value;
	}

	
	

}
