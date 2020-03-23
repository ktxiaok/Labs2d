package org.ktxiaok.labs2d.system.render;

public class AnimationClip {
    private Keyframe[] keyframeSequence;
    public AnimationClip(int length){
        keyframeSequence=new Keyframe[length];
    }
    public int getLength(){
        return keyframeSequence.length;
    }

    public void putKeyframe(int index,Keyframe keyframe){
        keyframeSequence[index]=keyframe;
    }
    public Keyframe getKeyframe(int index){
        return keyframeSequence[index];
    }
}
