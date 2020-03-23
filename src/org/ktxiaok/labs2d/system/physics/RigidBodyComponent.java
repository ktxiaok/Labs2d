package org.ktxiaok.labs2d.system.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;
import org.ktxiaok.labs2d.system.Component;
import org.ktxiaok.labs2d.system.TransformComponent;
import org.ktxiaok.labs2d.system.TransformStrategy;

public final class RigidBodyComponent extends Component{
	private transient Body body=null;
	
//	private transient BodyDef bodyDef;
//	private transient FixtureDef[] fixtureDefs;
    private transient RigidBodyDef rigidBodyDef=null;
	private transient PhysicsSystem physicsSystem=null;
	
	public static final int BODY_TYPE_STATIC=0;
	public static final int BODY_TYPE_KINEMATIC=1;
	public static final int BODY_TYPE_DYNAMIC=2;
	
	private float scaleX=1;
	private float scaleY=1;
	
	public class RigidBodyTransformStrategy implements TransformStrategy{

		@Override
		public void setPosition(float x, float y) {
			body.setTransform(new Vec2(x,y), body.getAngle());
		}

		@Override
		public void getPosition(Vector2f v){
		    Vec2 pos=body.getTransform().p;
		    v.x=pos.x;
		    v.y=pos.y;
        }

		@Override
		public void setScale(float sx, float sy) {
			RigidBodyComponent.this.setScale(sx, sy);
		}

		@Override
		public void getScale(Vector2f v){
		    v.x=scaleX;
		    v.y=scaleY;
        }

		@Override
		public void setAngle(float a) {
			body.setTransform(body.getPosition(),a);
		}

		@Override
		public float getAngle() {
			return body.getAngle();
		}
		
		
	}
	public RigidBodyComponent() {
		this(new RigidBodyDef());
	}
	public RigidBodyComponent(RigidBodyDef rigidBodyDef) {
	    this.rigidBodyDef=rigidBodyDef;
	}
	public static class RigidBodyDef{
	    public BodyDef bodyDef;
	    public FixtureDef[] fixtureDefs;
	    public RigidBodyDef(){
	        bodyDef=new BodyDef();
	        fixtureDefs=new FixtureDef[]{};
        }
        public RigidBodyDef(BodyDef bodyDef, FixtureDef[] fixtureDefs) {
            this.bodyDef = bodyDef;
            this.fixtureDefs = fixtureDefs;
        }
    }
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Component>[] getDependentComponentClasses() {
		return new Class[] {TransformComponent.class};
	}
	
	public TransformComponent transformComponent() {
		return getDependentComponent(0);
	}
	public PhysicsSystem physicsSystem() {
		if(physicsSystem==null) {
			physicsSystem=getWorld().findSystem(PhysicsSystem.class);
		}
		return physicsSystem;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		body=physicsSystem().getPhysicsWorld().createBody(rigidBodyDef.bodyDef);
		body.setUserData(this);
		for(FixtureDef fixtureDef0:rigidBodyDef.fixtureDefs) {
			createFixture(fixtureDef0);
		}
		transformComponent().replaceTransformStrategy(new RigidBodyTransformStrategy());
		rigidBodyDef=null;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		transformComponent().resetTransformStrategy();
		physicsSystem().getPhysicsWorld().destroyBody(body);
	}
	public Fixture createFixture(FixtureDef fixtureDef) {
		Fixture fixture=body.createFixture(fixtureDef);
		fixture.setUserData(this);
		return fixture;
	}
	public Body getBody() {
		return body;
	}
	
	protected void setScale(float sx,float sy) {
		scale(sx/scaleX,sy/scaleY);
		scaleX=sx;
		scaleY=sy;
	}
	protected void scale(float sx,float sy) {
		Fixture fixture0=body.getFixtureList();
		while(fixture0!=null) {
			Shape shape0=fixture0.getShape();
			if(shape0.getType()==ShapeType.POLYGON) {
				PolygonShape polygonShape0=(PolygonShape)shape0;
				Vec2[] vertexes0=polygonShape0.getVertices();
				for(Vec2 v0:vertexes0) {
					v0.x*=sx;
					v0.y*=sy;
				}
			}
			fixture0=fixture0.getNext();
		}
	}
}
