package org.ktxiaok.labs2d.system.util;

public abstract class EventObject {
	private Object source;
	public EventObject(Object source) {
		this.source=source;
	}
	public Object getSource() {
		return source;
	}
}
