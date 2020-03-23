package org.ktxiaok.labs2d.system;

import org.ktxiaok.labs2d.system.render.AbstractRenderSystem;
import org.ktxiaok.labs2d.system.util.EventManager;
import org.ktxiaok.labs2d.system.util.ObjectGroup;
import org.ktxiaok.labs2d.system.util.TaskProcessor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class World{
    private ObjectGroup<Entity> entityGroup;
    private float deltaTime;//单位为秒
    private List<BaseSystem> systemList;
    private EventManager eventManager;
    private TaskProcessor taskProcessor;
    private AbstractRenderSystem renderSystem=null;
    public World() {
        entityGroup=new ObjectGroup<Entity>();
        systemList =new ArrayList<BaseSystem>();
        eventManager=new EventManager();
        taskProcessor=new TaskProcessor();
    }
    public void addEntity(Entity e) {
        e.setID(entityGroup.put(e));
        e.setWorld(this);
        e.onCreate();
    }
    public Entity findEntity(int id) {
        return entityGroup.get(id);
    }
    public Entity removeEntity(int id) {
        Entity e=entityGroup.remove(id);
        e.setID(Entity.NULL_ID);
        e.onDestroy();
        return e;
    }
    public Iterator<Entity> entityIterator(){
        return entityGroup.iterator();
    }
    public int getEntityCount() {
        return entityGroup.size();
    }

    public <T extends BaseSystem> T createSystem(Class<T> systemClass) {
        T system=null;
        try {
            Constructor<T> constructor=systemClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            system=constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(BaseSystem s0: systemList) {
            if(systemClass.isInstance(s0)) {
                throw new RuntimeException("Only one can be added for each type of system.");
            }
        }
        system.setWorld(this);
        systemList.add(system);
        system.start();
        return system;
    }
    public <T extends BaseSystem> T removeSystem(Class<T> systemClass) {
        T result=null;
        for(int i = 0; i< systemList.size(); i++) {
            BaseSystem s0= systemList.get(i);
            if(systemClass.isInstance(s0)) {
                result=(T)s0;
                systemList.remove(i);
            }
        }
        return result;
    }
    public <T extends BaseSystem> T findSystem(Class<T> systemClass) {
        T result=null;
        for(BaseSystem s0: systemList) {
            if(systemClass.isInstance(s0)) {
                result=(T)s0;
                break;
            }
        }
        return result;
    }

    //	private void initBasicSystem() {
//		addSystem(PhysicsSystem.class);
//		addSystem(ScriptSystem.class);
//	}
    public EventManager getEventManager() {
        return eventManager;
    }

    public TaskProcessor getTaskProcessor() {
        return taskProcessor;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public AbstractRenderSystem getRenderSystem() {
        return renderSystem;
    }

    public void setRenderSystem(AbstractRenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }
    public void update(float deltaTime) {
        this.deltaTime=deltaTime;
        eventManager.handle();
        taskProcessor.run();
//        List<BaseSystem> systemGroup=new ArrayList<BaseSystem>(this.systemList);
//        for(BaseSystem s0:systemGroup) {
//            s0.update();
//        }
        for(int i=0;i<systemList.size();i++){
            BaseSystem baseSystem=systemList.get(i);
            baseSystem.update();
        }
        if(renderSystem!=null){
            renderSystem.update();
        }
    }
    public boolean requestRender(){
        return renderSystem.render();
    }

    public void clean(){
        List<Entity> entityList=new LinkedList<>();
        for(Entity entity:entityGroup){
            entityList.add(entity);
        }
        for(Entity entity:entityList){
            removeEntity(entity.getID());
        }
    }
    public void dispose(){
        for(Entity entity:entityGroup){
            entity.dispose();
        }
    }
}
