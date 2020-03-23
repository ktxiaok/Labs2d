package org.ktxiaok.labs2d.render.opengl;

public abstract class GLObject {
	private int id;
	private GLContext context;
	private boolean isDisposed=false;
	public GL gl;
	public GLObject() {
		context=GLContext.getCurrentContext();
		gl=GLContext.GL();
	}
	
	protected abstract GLObject getBindingObject();
	protected abstract void setBindingObject(GLObject glObject);
	protected void checkBinding(){
		if(!equals(getBindingObject())) {
			throw new OpenGLOperationException("Must bind OpenGL Object before use it! Object Type:"+getClass().getSimpleName());
		}
	}
	public GLContext getContext() {
		return context;
	}
	public final void bind() {
		if(equals(getBindingObject()))return;
		bindObject();
		setBindingObject(this);
	}
	public final void unbind() {
		if(getBindingObject()==null)return;
		unbindObject(getBindingObject());
		setBindingObject(null);
	}
	protected abstract void unbindObject(GLObject bindingObject);
	protected abstract void bindObject();
	public void dispose() {
		if(isDisposed)return;
		if(equals(getBindingObject())) {
			unbind();
		}
		isDisposed=true;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	protected void setID(int id) {
		this.id=id;
	}
	public int getID() {
		return id;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GLObject) {
		    GLObject glObj=(GLObject)obj;
			if(getID()==glObj.getID()) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return getID();
	}
	
}
