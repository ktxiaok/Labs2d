package org.ktxiaok.labs2d.system.util;

import java.util.ArrayList;
import java.util.List;

public class VirtualStack<T> {
    private ObjectFactory<T> objectFactory;
    private List<T> objectList;
    private int pointer=0;
    protected VirtualStack(ObjectFactory<T> objectFactory) {
        this.objectFactory=objectFactory;
        objectList=new ArrayList<>(4);
    }
    public T create() {
        T result;
        if(objectList.size()<(pointer+1)) {
            result=objectFactory.create();
            objectList.add(result);
        }else {
            result=objectList.get(pointer);
            objectFactory.init(result);
        }
        pointer++;
        return result;
    }
    protected void reset() {
        pointer=0;
    }
}
