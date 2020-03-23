package org.ktxiaok.labs2d.render.opengl;

public class Utils {
    private Utils(){

    }

    public static void runOnRenderThreadOrLocal(Renderer renderer,Runnable task,boolean wait){
        if(renderer==null){
            task.run();
        }else{
            if(wait)renderer.getTaskProcessor().execute(task);
            else renderer.getTaskProcessor().post(task);
        }
    }

}
