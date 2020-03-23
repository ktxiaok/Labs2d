package org.ktxiaok.labs2d.system.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskProcessor {
    private Queue<Task> taskQueue;
    private Queue<ConstantTask> constantTaskAddQueue;
    private List<ConstantTask> constantTaskList;
    public TaskProcessor(){
        taskQueue=new ConcurrentLinkedQueue<>();
        constantTaskAddQueue=new ConcurrentLinkedQueue<>();
        constantTaskList=new ArrayList<>();
    }
    private static class Task{
        public Runnable runnable;
        public Object finishLock=null;
    }
    private static class ConstantTask{
        public Runnable runnable;
        public boolean removed=false;
    }
    public void run(){
        while(!taskQueue.isEmpty()){
            Task task=taskQueue.poll();
            task.runnable.run();
            if(task.finishLock!=null){
                synchronized (task.finishLock){
                    task.finishLock.notify();
                }
            }
        }
        while(!constantTaskAddQueue.isEmpty()){
            ConstantTask constantTask=constantTaskAddQueue.poll();
            constantTaskList.add(constantTask);
        }
        Iterator<ConstantTask> itr=constantTaskList.iterator();
        while(itr.hasNext()){
            ConstantTask constantTask=itr.next();
            if(constantTask.removed){
                itr.remove();
                continue;
            }
            constantTask.runnable.run();
        }
    }
    public void post(Runnable task){
        Task task0=new Task();
        task0.runnable=task;
        taskQueue.offer(task0);
    }
    public void execute(Runnable task){
        Task task0=new Task();
        task0.runnable=task;
        task0.finishLock=new Object();
        taskQueue.offer(task0);
        synchronized (task0.finishLock){
            try {
                task0.finishLock.wait();
            } catch (InterruptedException e) {

            }
        }
    }
    public Object register(Runnable task){
        ConstantTask task0=new ConstantTask();
        task0.runnable=task;
        constantTaskAddQueue.offer(task0);
        return task0;
    }
    public boolean remove(Object taskHandler){
        if(taskHandler.getClass()==ConstantTask.class){
            ConstantTask task=(ConstantTask)taskHandler;
            task.removed=true;
            return true;
        }
        return false;
    }
}
