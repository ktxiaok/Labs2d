package org.ktxiaok.labs2d.system.util;

public abstract class ObjectFactory<T> {
    public abstract T create();
    public abstract void init(T obj);
}
