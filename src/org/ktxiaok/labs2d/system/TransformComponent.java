package org.ktxiaok.labs2d.system;

import org.joml.Vector2f;

public final class TransformComponent extends Component{
	private TransformStrategy transformStrategy;
	public class DefaultTransformStrategy implements TransformStrategy{
	    private float x=0;
	    private float y=0;
	    private float sx=1;
	    private float sy=1;
	    private float angle=0;
		@Override
		public void setPosition(float x, float y) {
			this.x=x;
			this.y=y;
		}

		@Override
		public void getPosition(Vector2f v){
		    v.x=x;
		    v.y=y;
        }

		@Override
		public void setScale(float sx, float sy) {
			this.sx=sx;
			this.sy=sy;
		}

		@Override
		public void getScale(Vector2f v){
		    v.x=sx;
		    v.y=sy;
        }

		@Override
		public void setAngle(float a) {
			this.angle=a;
		}

		@Override
		public float getAngle() {
			return angle;
		}

	}
	public TransformComponent() {
		transformStrategy=new DefaultTransformStrategy();
	}
	public TransformComponent(float x,float y) {
		this();
		setPosition(x, y);
	}
	public TransformStrategy getTransformStrategy() {
		return transformStrategy;
	}
	public void replaceTransformStrategy(TransformStrategy transformStrategy) {
//		float[] pos=this.transformStrategy.getPosition();
//		float[] scale=this.transformStrategy.getScale();
        Vector2f pos=new Vector2f();
        Vector2f scale=new Vector2f();
        this.transformStrategy.getPosition(pos);
        this.transformStrategy.getScale(scale);
		float angle=this.transformStrategy.getAngle();
		transformStrategy.setPosition(pos.x,pos.y);
		transformStrategy.setScale(scale.x,scale.y);
		transformStrategy.setAngle(angle);
		this.transformStrategy=transformStrategy;
	}
	public void resetTransformStrategy() {
		replaceTransformStrategy(new DefaultTransformStrategy());
	}
	public final void setPosition(float x,float y) {
		transformStrategy.setPosition(x, y);
	}
	public final void getPosition(Vector2f v) {
		transformStrategy.getPosition(v);
	}
	
	public void translate(float x,float y) {
		//float[] pos=getPosition();
        Vector2f pos=new Vector2f();
        getPosition(pos);
		pos.x+=x;
		pos.y+=y;
		setPosition(pos.x,pos.y);
	}
//	public void setPositionX(float x) {
//		setPosition(x,getPosition()[1]);
//	}
//    public void setPositionY(float y) {
//    	setPosition(getPosition()[0],y);
//    }
    
    public void getScale(Vector2f v){
    	transformStrategy.getScale(v);
    }
    public void setScale(float sx,float sy) {
    	transformStrategy.setScale(sx, sy);
    }
    public void scale(float sx,float sy) {
    	//float[] scale=getScale();
    	Vector2f scale=new Vector2f();
    	getScale(scale);
    	setScale(sx*scale.x, sy*scale.y);
    }
//    public void setScaleX(float sx) {
//    	setScale(sx, getScale()[1]);
//    }
//    public void setScaleY(float sy) {
//    	setScale(getScale()[0],sy);
//    }
//
    public float getAngle() {
    	return transformStrategy.getAngle();
    }
    public void setAngle(float a) {
    	transformStrategy.setAngle(a);
    }
	
    public void rotate(float a) {
    	transformStrategy.setAngle(transformStrategy.getAngle()+a);
    }
}
