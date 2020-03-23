package org.ktxiaok.labs2d.system;

public abstract class ScriptComponent extends Component{
	public ScriptComponent() {
		
	}
	
	public abstract void start();
	public abstract boolean update();
	private ScriptSystem scriptSystem() {
		return (getWorld().findSystem(ScriptSystem.class));
	}
	@Override
	protected void onCreate() {
		super.onCreate();
		scriptSystem().register(this);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		scriptSystem().remove(this);
	}
}
