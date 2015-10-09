

package com.rdupuis.gamefactory.scene01.components;




import com.rdupuis.gamefactory.R;
import com.rdupuis.gamefactory.components.GameObject;
import com.rdupuis.gamefactory.components.shapes.Rectangle2D;
import com.rdupuis.gamefactory.enums.DrawingMode;

public class PetitRobot extends Rectangle2D {

    private int sens = 1;
	public PetitRobot() {

		super(DrawingMode.FILL);
		this.setTagName(R.string.petit_robot);
		this.isStatic=false;		
	}

	@Override
	public void onUpdate(){
		float limit_y = this.getScene().getHeight();
		//Log.i("debug",String.valueOf(activity.mGLSurfaceView.getHeight()));
		//Log.i("debug",String.valueOf(i));
		
		float inc = 0.f;
		
		if (this.getCoordY()>limit_y || this.getCoordY()<-limit_y){
			sens = sens*-1;
		}  
		
		inc=inc * sens;
		
		this.Y = Y+inc;

	
		if (!this.mCollideWithList.isEmpty()){
			this.setHeight(this.getHeight()+5.f);
			if (this.getHeight() > 700){this.setHeight(0);}
		}
		//test des colisions
				for (GameObject go : this.mCollideWithList) {
					//Log.i("petitrobot", "i'm collide with : " + go.getTagName());

				}

	
	
	}
	
}
