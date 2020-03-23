package org.ktxiaok.labs2d.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Entity {
	/*
	 * If the entity is not in a world,the id will be NULL_ID(-1).
	 */
	public static final int NULL_ID=-1;
	private int id=NULL_ID;
	
	private List<Component> componentList;
	
	private transient World world=null;
	
	private Object attachment=null;
	
	private boolean active=false;
	public Entity() {
		componentList =new LinkedList<Component>();
	}
	public Object getAttachment() {
		return attachment;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean b) {
		active=b;
	}
	public void setAttachment(Object attachment) {
		this.attachment=attachment;
	}
	protected void setWorld(World world) {
		this.world=world;
	}
	public World getWorld() {
		return world;
	}
	protected void setID(int id) {
		this.id=id;
	}
	public int getID() {
		return id;
	}
	public void addComponent(Component c0) {
		c0.setEntity(this);
		c0.onAdd();
		if(world!=null) {
			c0.onCreate();
		}
		componentList.add(c0);
	}
	private void removeComponent0(Component c0) {
		c0.onRemove();
		if(world!=null) {
			c0.onDestroy();
		}
	}
	public void removeComponent(Component c0) {
		removeComponent0(c0);
		componentList.remove(c0);
	}
	public void removeComponentAll(Class<? extends Component> componentClass) {
		Iterator<Component> itr= componentList.iterator();
		while(itr.hasNext()) {
			Component c0=itr.next();
			if(c0.getClass()==componentClass) {
				itr.remove();
			}
		}
	}
	public <T extends Component> T findComponent(Class<T> componentClass) {
		for(Component c0: componentList) {
			if(componentClass.isInstance(c0)) {
				return (T)c0;
			}
		}
		return null;
	}
	public <T extends Component> List<T> findComponentAll(Class<T> componentClass) {
		List<T> result=new ArrayList<>();
		for(Component c0: componentList) {
			if(componentClass.isInstance(c0)) {
				result.add((T)c0);
			}
		}
		return result;
	}
	public Iterator<Component> componentIterator(){
		return componentList.iterator();
	}
	/*
	 * Called when the entity is added to a world.
	 */
	protected void onCreate() {
		for(Component c0: componentList) {
			c0.onCreate();
		}
	}
	/*
	 * Called when the entity is removed from a world.
	 */
	protected void onDestroy() {
		for(Component c0: componentList) {
			c0.onDestroy();
		}
	}

	public void dispose(){
	    for(Component c0: componentList){
	        c0.dispose();
        }
    }
}
