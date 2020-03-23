package org.ktxiaok.labs2d.system.render;

public abstract class Keyframe {
    private int length=1;
    private KeyFrameScript script=null;
    public abstract void applyFrame(AnimationPlayer animationPlayer);
    public void enterFrame(AnimationPlayer animationPlayer){
        if(script!=null){
            script.enterFrame(animationPlayer);
        }
    }
    public void onFrame(AnimationPlayer animationPlayer){
        if(script!=null){
            script.onFrame(animationPlayer);
        }
    }
    public Keyframe() {
    }

    public Keyframe(int length) {
        this.length = length;
    }

    public KeyFrameScript getScript() {
        return script;
    }
    public void setScript(KeyFrameScript script) {
        this.script = script;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
