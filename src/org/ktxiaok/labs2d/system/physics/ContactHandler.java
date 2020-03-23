package org.ktxiaok.labs2d.system.physics;

import org.jbox2d.collision.WorldManifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

public class ContactHandler {
	private Fixture object;
	private Contact contact;
	private WorldManifold worldManifold=null;
	protected ContactHandler(Contact contact,Fixture object) {
		this.contact=contact;
		this.object=object;
	}
	private WorldManifold worldManifold() {
		if(worldManifold==null) {
			worldManifold=new WorldManifold();
			contact.getWorldManifold(worldManifold);	
		}
		return worldManifold;
	}
	public Fixture getObject() {
		return object;
	}
	public boolean isEnable() {
		return contact.isEnabled();
	}
	public void setEnable(boolean b) {
		contact.setEnabled(b);
	}
	public Vec2 getNormal() {
		return worldManifold().normal;
	}
	public Vec2[] getContactPoints() {
		return worldManifold().points;
	}
	public int getContactPointCount() {
		return contact.getManifold().pointCount;
	}
	public void setFriction(float friction) {
		contact.setFriction(friction);
	}
	public void setRestitution(float restitution) {
		contact.setRestitution(restitution);
	}
	public void setTangentSpeed(float speed) {
		contact.setTangentSpeed(speed);
	}
}
