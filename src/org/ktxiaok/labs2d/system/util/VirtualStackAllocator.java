package org.ktxiaok.labs2d.system.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualStackAllocator<T> {

    private ObjectFactory<T> objectFactory;
    private List<VirtualStack<T>> stackList;
    private int pointer=0;

    private static Map<Thread,Map<Class<?>,VirtualStackAllocator<?>>> allocatorMap=new ConcurrentHashMap<>(4);

    public static <T> VirtualStackAllocator<T> get(Class<T> objectClass,Thread thread){
        Map<Class<?>,VirtualStackAllocator<?>> map=allocatorMap.get(thread);
        if(map==null)return null;
        @SuppressWarnings("unchecked")
        VirtualStackAllocator<T> result=(VirtualStackAllocator<T>)map.get(objectClass);
        return result;
    }
    public static <T> void register(ObjectFactory<T> objectFactory,Thread thread){
        Map<Class<?>,VirtualStackAllocator<?>> map=allocatorMap.get(thread);
        if(map==null) {
            map=new HashMap<>();
            allocatorMap.put(thread,map);
        }
        Class<T> objectClass=getTClass(objectFactory);
        map.put(objectClass,new VirtualStackAllocator<>(objectFactory));
    }
    public static <T> VirtualStackAllocator<T> get(Class<T> objectClass){
        return get(objectClass,Thread.currentThread());
    }
    public static <T> void register(ObjectFactory<T> objectFactory) {
        register(objectFactory,Thread.currentThread());
    }
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getTClass(ObjectFactory<T> objectFactory){
        Type genType = objectFactory.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<T>)params[0];
    }
    private VirtualStackAllocator(ObjectFactory<T> objectFactory) {
        this.objectFactory=objectFactory;
        stackList=new ArrayList<>();
    }
    public VirtualStack<T> push() {
        VirtualStack<T> result;
        if(stackList.size()<(pointer+1)) {
            VirtualStack<T> virtualStack=new VirtualStack<>(objectFactory);
            stackList.add(virtualStack);
            result=virtualStack;
        }else {
            result=stackList.get(pointer);
            result.reset();
        }
        pointer++;
        return result;
    }
    public void pop() {
        pointer--;
    }
    public void reset() {
        pointer=0;
    }

}
