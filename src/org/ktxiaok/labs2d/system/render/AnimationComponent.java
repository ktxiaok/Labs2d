package org.ktxiaok.labs2d.system.render;

import org.ktxiaok.labs2d.system.Component;

public class AnimationComponent extends Component {
    private AnimationPlayer animationPlayer;

    public AnimationComponent(){
        this(null);
    }
    public AnimationComponent(AnimationClip animationClip){
        animationPlayer=new AnimationPlayer(animationClip);
    }

    public AnimationPlayer getAnimationPlayer() {
        return animationPlayer;
    }

    public void setAnimationPlayer(AnimationPlayer animationPlayer) {
        this.animationPlayer = animationPlayer;
    }
    public void replaceAnimationClip(AnimationClip animationClip){
        animationPlayer.setAnimationClip(animationClip);
        animationPlayer.reset();
    }

    public AbstractRenderSystem renderSystem(){
        return getWorld().getRenderSystem();
    }
    public void update(){
        animationPlayer.update(getWorld().getDeltaTime());
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        renderSystem().registerAnimationComponent(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        renderSystem().removeAnimationComponent(this);
    }
}
