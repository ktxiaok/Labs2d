package org.ktxiaok.labs2d.system.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ModificationSafeList<T> {
	private List<T> list;
	private static final int TYPE_ADD=0;
	private static final int TYPE_REMOVE_OBJECT=1;
	private static final int TYPE_REMOVE_INDEX=2;
	private Queue<Object> requestQueue;
	public ModificationSafeList(List<T> list){
		this(list,false);
	}
	public ModificationSafeList(List<T> list,boolean async) {
		this.list=list;
		if(async){
			requestQueue=new ConcurrentLinkedQueue<>();
		}else{
			requestQueue=new LinkedList<>();
		}
	}
	public List<T> getList(){
		return list;
	}
    @SuppressWarnings("unchecked")
	public void handleRequest() {
    	while(!requestQueue.isEmpty()) {
    		Object[] r0=(Object[])requestQueue.poll();
    		int type=((Integer)r0[0]).intValue();
    		switch(type) {
    		case TYPE_ADD:
    			list.add((T)r0[1]);
    			break;
    		case TYPE_REMOVE_INDEX:
    			list.remove(((Integer)r0[1]).intValue());
    			break;
    		case TYPE_REMOVE_OBJECT:
    			list.remove((T)r0[1]);
    			break;
    		}
    	}
    }
    public void requestAdd(T element) {
    	requestQueue.add(new Object[] {new Integer(TYPE_ADD),element});
    }
    public void requestRemove(int index) {
    	requestQueue.add(new Object[] {new Integer(TYPE_REMOVE_INDEX),new Integer(index)});
    }
    public void requestRemove(T element) {
    	requestQueue.add(new Object[] {new Integer(TYPE_REMOVE_OBJECT),element});
    }
    
}
