package org.ktxiaok.labs2d.system.render;

public class AnimationPlayer {
    public static final float DEFAULT_FPS=30f;

    private boolean loadFlag=false;
    private float fps=DEFAULT_FPS;
    private int currentIndex=0;
    private float frameCounter=0;

    private boolean play=true;

    private boolean loop=true;

    private AnimationClip animationClip;

    public AnimationPlayer(AnimationClip animationClip){
        this.animationClip=animationClip;
    }

    public void setAnimationClip(AnimationClip animationClip) {
        this.animationClip = animationClip;
    }
    public AnimationClip getAnimationClip() {
        return animationClip;
    }


    public float getFPS() {
        return fps;
    }
    public void setFPS(float fps) {
        this.fps = fps;
    }

    private int nextIndex(int index){
        if(index==animationClip.getLength()-1){
            return 0;
        }
        return (index+1);
    }

    public void update(float deltaTime){
        if(!play)return;
        Keyframe currentKeyframe=animationClip.getKeyframe(currentIndex);
        float frameTime=1f/fps;
        frameCounter+=(deltaTime/frameTime);

        while(true){
            if(!loadFlag){
                currentKeyframe.enterFrame(this);
                currentKeyframe.applyFrame(this);
            }
            loadFlag=true;
            currentKeyframe.onFrame(this);
            if(!play){
                break;
            }

            if(frameCounter>currentKeyframe.getLength()){
                frameCounter-=currentKeyframe.getLength();
                loadFlag=false;
                currentIndex=nextIndex(currentIndex);
                currentKeyframe=animationClip.getKeyframe(currentIndex);

                if(!loop){
                    if(currentIndex==animationClip.getLength()-1){
                        play=false;
                    }
                }
            }else{
                break;
            }
        }

    }

    public Keyframe getCurrentKeyframe(){
        return animationClip.getKeyframe(currentIndex);
    }
    public int getCurrentIndex(){
        return currentIndex;
    }

    public boolean isPlay() {
        return play;
    }
    public void setPlay(boolean play) {
        this.play = play;
    }
    public void jump(int index){
        currentIndex=index;
    }
    public void reset(){
        play=true;
        currentIndex=0;
    }

    public boolean isLoop() {
        return loop;
    }
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
}
