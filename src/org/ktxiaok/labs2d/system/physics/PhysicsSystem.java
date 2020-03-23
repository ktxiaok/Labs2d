package org.ktxiaok.labs2d.system.physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.ktxiaok.labs2d.system.BaseSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PhysicsSystem extends BaseSystem {
	private org.jbox2d.dynamics.World physicsWorld;

	public static final int DEFAULT_VELOCITY_ITERATIONS=10;
	public static final int DEFAULT_POSITION_ITERATIONS=10;
	private int velocityIterations=DEFAULT_VELOCITY_ITERATIONS;
	private int positionIterations=DEFAULT_POSITION_ITERATIONS;

	private Map<Fixture,ContactionListener> contactListenerMap;
	public PhysicsSystem() {
		physicsWorld = new World(new Vec2(0f, 0f));
		contactListenerMap =new HashMap<>();
		physicsWorld.setContactListener(new StandardContactListener());
	}
	private class StandardContactListener implements org.jbox2d.callbacks.ContactListener{
	    private static final int TYPE_BEGIN=0;
	    private static final int TYPE_END=1;
	    private static final int TYPE_PRE_SOLVE=2;
	    private static final int TYPE_POST_SOLVE=3;
		private void handleContact(Contact contact,ContactImpulse impulse,int type){
		    Fixture fixtureA=contact.getFixtureA();
		    Fixture fixtureB=contact.getFixtureB();
		    if(contactListenerMap.containsKey(fixtureA)){
		        handleContact0(contact,impulse,type,fixtureA,fixtureB);
            }
            if(contactListenerMap.containsKey(fixtureB)){
                handleContact0(contact,impulse,type,fixtureB,fixtureA);
            }
        }
        private void handleContact0(Contact contact,ContactImpulse impulse,int type,
                                    Fixture fixture,Fixture object){
		    ContactionListener contactionListener=contactListenerMap.get(fixture);
		    ContactHandler contactHandler=new ContactHandler(contact,object);
		    switch(type){
                case TYPE_BEGIN:
                    contactionListener.beginContact(contactHandler);
                    break;
                case TYPE_END:
                    contactionListener.endContact(contactHandler);
                    break;
                case TYPE_PRE_SOLVE:
                    contactionListener.preSolve(contactHandler);
                    break;
                case TYPE_POST_SOLVE:
                    contactionListener.postSolve(contactHandler,impulse);
                    break;
            }
        }
		@Override
		public void beginContact(Contact contact) {
			handleContact(contact,null,TYPE_BEGIN);
		}

		@Override
		public void endContact(Contact contact) {
			handleContact(contact,null,TYPE_END);
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			handleContact(contact,null,TYPE_PRE_SOLVE);
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			handleContact(contact,impulse,TYPE_POST_SOLVE);
		}
	}
	public org.jbox2d.dynamics.World getPhysicsWorld(){
		return physicsWorld;
	}

	@Override
	public void update() {
		physicsWorld.step(getWorld().getDeltaTime(),velocityIterations,positionIterations);
	}

	public int getVelocityIterations() {
		return velocityIterations;
	}
	public void setVelocityIterations(int velocityIterations) {
		this.velocityIterations = velocityIterations;
	}
	public int getPositionIterations() {
		return positionIterations;
	}
	public void setPositionIterations(int positionIterations) {
		this.positionIterations = positionIterations;
	}
	public void registerContactListener(Fixture fixture,ContactionListener listener) {
		contactListenerMap.put(fixture,listener);
	}
	public void removeContactListener(Fixture fixture) {
		contactListenerMap.remove(fixture);
	}
	private static RigidBodyComponent analyseFixtureAndBodyUserdata(Object userdata) {
		if(userdata==null) {
			return null;
		}
		if(userdata instanceof RigidBodyComponent) {
			return (RigidBodyComponent)userdata;
		}
		return null;
	}
	public static RigidBodyComponent getRigidBody(Fixture fixture) {
		Object userdata=fixture.getUserData();
		return analyseFixtureAndBodyUserdata(userdata);
	}
	public static RigidBodyComponent getRigidBody(Body body) {
		Object userdata=body.getUserData();
		return analyseFixtureAndBodyUserdata(userdata);
	}
	public static RigidBodyComponent getDynamicRigidBody(Fixture fixture){
	    RigidBodyComponent rigidBodyComponent=getRigidBody(fixture);
	    if(rigidBodyComponent==null)return null;
	    if(fixture.isSensor()||fixture.getBody().getType()!= BodyType.DYNAMIC)return null;
	    return rigidBodyComponent;
    }
    public static RigidBodyComponent getDynamicRigidBody(Body body){
	    RigidBodyComponent rigidBodyComponent=getRigidBody(body);
	    if(rigidBodyComponent==null)return null;
	    if(body.getType()!=BodyType.DYNAMIC)return null;
	    return rigidBodyComponent;
    }
	public void removeContactListener(ContactionListener listener) {
		Set<Entry<Fixture, ContactionListener>> entrySet= contactListenerMap.entrySet();
		for(Entry<Fixture, ContactionListener> entry0:entrySet) {
			if(entry0.getValue()==listener) {
				contactListenerMap.remove(entry0.getKey());
				return;
			}
		}
	}
	public List<Fixture> queryAABB(AABB aabb) {
		final List<Fixture> result = new ArrayList<Fixture>();
		QueryCallback query_callback = new QueryCallback() {

			@Override
			public boolean reportFixture(Fixture arg0) {
				result.add(arg0);
				return true;
			}
		};
		physicsWorld.queryAABB(query_callback, aabb);
		return result;
	}
	public void raycast(Vec2 start, Vec2 end, final RaycastCallback raycastCallback){
	    RayCastCallback box2dRaycastCallback=new RayCastCallback() {
            @Override
            public float reportFixture(Fixture fixture, Vec2 vec2, Vec2 vec21, float v) {
                RaycastInfo info=new RaycastInfo();
                info.fixture = fixture;
                info.position = vec2;
                info.normal = vec21;
                info.distanceRatio = v;
                return raycastCallback.report(info);
            }
        };
	    physicsWorld.raycast(box2dRaycastCallback,start,end);
    }
	public List<RaycastInfo> raycast(Vec2 start, Vec2 end) {
		final List<RaycastInfo> result = new LinkedList<>();
		raycast(start, end, new RaycastCallback() {
            @Override
            public float report(RaycastInfo raycastInfo) {
                result.add(raycastInfo);
                return 1;
            }
        });
		return result;
	}

	public RaycastInfo raycastTest(Vec2 start, Vec2 end) {
		final RaycastInfo[] result={null};
		raycast(start, end, new RaycastCallback() {
            @Override
            public float report(RaycastInfo raycastInfo) {
                result[0]=raycastInfo;
                return 0;
            }
        });
		return result[0];
	}

	// 升序排序
	public List<RaycastInfo> raycastWithSort(Vec2 start, Vec2 end) {
		Comparator<RaycastInfo> comparator = new Comparator<RaycastInfo>() {
			@Override
			public int compare(RaycastInfo o1, RaycastInfo o2) {
				return Float.compare(o1.distanceRatio, o1.distanceRatio);
			}
		};
		List<RaycastInfo> result = raycast(start, end);
		Collections.sort(result, comparator);
		return result;
	}

	public RaycastInfo raycastFirst(Vec2 start,Vec2 end){
	    final RaycastInfo[] nearest={null};
	    raycast(start, end, new RaycastCallback() {
            @Override
            public float report(RaycastInfo raycastInfo) {
                if(nearest[0]==null){
                    nearest[0]=raycastInfo;
                }else if(raycastInfo.distanceRatio<nearest[0].distanceRatio){
                    nearest[0]=raycastInfo;
                }
                return nearest[0].distanceRatio;
            }
        });
	    return nearest[0];
    }
}
