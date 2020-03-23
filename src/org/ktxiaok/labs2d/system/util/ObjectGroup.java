package org.ktxiaok.labs2d.system.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ObjectGroup<T> implements Iterable<T>{
	private Map<Integer,T> map;
    private Queue<Integer> freeIDQueue;
    private int maxID=0;
	public ObjectGroup() {
		map=new HashMap<Integer,T>();
		freeIDQueue=new LinkedList<Integer>();
	}
	public int put(T t) {
		int id;
		if(freeIDQueue.isEmpty()) {
			id=maxID;
			maxID++;
		}else {
			id=freeIDQueue.poll();
		}
		map.put(id,t);
		return id;
	}
	public T get(int id) {
		return map.get(id);
	}
	public T remove(int id) {
		T result=map.remove(id);
		if(result==null) {
			return null;
		}
		freeIDQueue.offer(id);
		return result;
	}
	@Override
	public Iterator<T> iterator() {
		return map.values().iterator();
	}
	public int size() {
		return map.size();
	}
}
