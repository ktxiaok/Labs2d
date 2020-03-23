package org.ktxiaok.labs2d.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventManager {
	private Map<Object,ModificationSafeList<EventListener>> sourceToListenerMap;
	private Map<Class<? extends EventObject>,ModificationSafeList<EventListener>> eventToListenerMap;
	private Queue<EventObject> eventQueue;
	public EventManager() {
		sourceToListenerMap=new HashMap<>();
		eventToListenerMap=new HashMap<>();
		eventQueue=new ConcurrentLinkedQueue<>();
	}
	public void post(EventObject e) {
		eventQueue.offer(e);
	}
	public void execute(EventObject e){
	    handleEvent(e);
    }
	public void handle() {
		
		while(!eventQueue.isEmpty()) {
			EventObject event0=eventQueue.poll();
			handleEvent(event0);
		}
	}
	protected void handleEvent(EventObject e) {
		Object eventSource=e.getSource();
		if(eventSource!=null) {
			ModificationSafeList<EventListener> listenerList=sourceToListenerMap.get(eventSource);
			if(listenerList!=null) {
				listenerList.handleRequest();
				for(EventListener listener0:listenerList.getList()) {
					listener0.onEvent(e);
				}
			}
		}
		
		ModificationSafeList<EventListener> listenerList=eventToListenerMap.get(e.getClass());
		if(listenerList!=null) {
			listenerList.handleRequest();
			for(EventListener listener0:listenerList.getList()) {
				listener0.onEvent(e);
			}
		}
	}
	public void registerListenerForEvent(EventListener listener,Class<? extends EventObject> eventClass) {
		if(!eventToListenerMap.containsKey(eventClass)) {
			eventToListenerMap.put(eventClass,new ModificationSafeList<EventListener>(new ArrayList<EventListener>()));
		}
		eventToListenerMap.get(eventClass).requestAdd(listener);
	}
	public void registerListenerForSource(EventListener listener,Object source) {
		if(!sourceToListenerMap.containsKey(source)) {
			sourceToListenerMap.put(source,new ModificationSafeList<EventListener>(new ArrayList<EventListener>()));
		}
		sourceToListenerMap.get(source).requestAdd(listener);
	}
	public void removeListenerForEvent(EventListener listener,Class<? extends EventObject> eventClass) {
		if(eventToListenerMap.containsKey(eventClass)) {
			eventToListenerMap.get(eventClass).requestRemove(listener);
		}
	}
	public void removeListenerForSource(EventListener listener,Object source) {
		if(sourceToListenerMap.containsKey(source)) {
			sourceToListenerMap.get(source).requestRemove(listener);
		}
	}
}
