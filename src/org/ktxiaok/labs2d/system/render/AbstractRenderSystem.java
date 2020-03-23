package org.ktxiaok.labs2d.system.render;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRenderSystem {
    private List<AnimationComponent> animationComponentList;

    public AbstractRenderSystem(){
        animationComponentList=new ArrayList<>();
    }

    public abstract boolean render();

    protected void registerAnimationComponent(AnimationComponent animationComponent){
        animationComponentList.add(animationComponent);
    }
    protected void removeAnimationComponent(AnimationComponent animationComponent){
        animationComponentList.remove(animationComponent);
    }

    public void update(){
        for(AnimationComponent animationComponent:animationComponentList){
            animationComponent.update();
        }
    }
}
