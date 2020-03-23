package org.ktxiaok.labs2d.system;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {
	private Entity entity=null;
	private Component[] dependentComponents=null;
	private Object attachment=null;
	private boolean active=false;
	public Component() {
		
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean b) {
		active=b;
	}
	public Object getAttachment() {
		return attachment;
	}
	public void setAttachment(Object attachment) {
		this.attachment=attachment;
	}
	private void prepareDependentComponents() {
		Class<? extends Component>[] dependentComponentClasses=getDependentComponentClasses();
		dependentComponents=new Component[dependentComponentClasses.length];
		@SuppressWarnings("rawtypes")
		List<Class> lackClasses=new ArrayList<Class>();
		for(int i=0;i<dependentComponentClasses.length;i++) {
			Class<? extends Component> class0=dependentComponentClasses[i];
			Component c0=getEntity().findComponent(class0);
			dependentComponents[i]=c0;
			if(c0==null) {
				lackClasses.add(class0);
			}
		}
		if(!lackClasses.isEmpty()) {
			StringBuilder stringBuilder=new StringBuilder();
			stringBuilder.append("While adding the "+getClass().getName()+" .The following components are missing:");
			for(@SuppressWarnings("rawtypes") Class class0:lackClasses) {
				stringBuilder.append(class0.getName()+"\n");
			}
			throw new RuntimeException(stringBuilder.toString());
		}
		
	}
	/*
	 * Called when the component is added to a entity.
	 */
	protected void onAdd() {
		prepareDependentComponents();
	}
	/*
	 * Called the component is removed from a entity.
	 */
	protected void onRemove() {
		
	}
	/*
	 * Called the component is added to a world.
	 */
	protected void onCreate() {
		active=true;
	}
	/*
	 * Called the component is removed from a world.
	 */
	protected void onDestroy() {
		active=false;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends Component>[] getDependentComponentClasses(){
		return new Class[] {};
	}
	public final <T extends Component> T getDependentComponent(int index) {
		return (T)dependentComponents[index];
	}
	
	public Entity getEntity() {
		return entity;
	}
	protected void setEntity(Entity e) {
		entity=e;
	}
	public World getWorld() {
		return entity.getWorld();
	}
	public void dispose(){}
}
