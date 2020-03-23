 package org.ktxiaok.labs2d.system;

import java.util.ArrayList;
import java.util.Iterator;

import org.ktxiaok.labs2d.system.util.ModificationSafeList;

public class ScriptSystem extends BaseSystem{
	private ModificationSafeList<ScriptComponent> scriptGroup;
	public ScriptSystem() {
		scriptGroup=new ModificationSafeList<>(new ArrayList<ScriptComponent>());
	}
	@Override
	public void update() {
		scriptGroup.handleRequest();
		Iterator<ScriptComponent> scriptGroupItr=scriptGroup.getList().iterator();
		while(scriptGroupItr.hasNext()) {
			ScriptComponent script0=scriptGroupItr.next();
			if(script0.isActive()) {
				boolean flag=script0.update();
				if(!flag) {
					scriptGroupItr.remove();
				}
			}
		}
	}
	protected void register(ScriptComponent script) {
		script.start();
		scriptGroup.requestAdd(script);
	}
	protected void remove(ScriptComponent script) {
		scriptGroup.requestRemove(script);
	}
}
