package org.ktxiaok.labs2d.system;

public abstract class BaseSystem{
	private World world;
	public BaseSystem() {
		
	}
	protected void setWorld(World world) {
		this.world=world;
	}
	public World getWorld() {
		return world;
	}

	public void start(){

    }
	public void update(){

    }
}
