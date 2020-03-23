package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.render.AnimationClip;

import java.util.LinkedList;
import java.util.List;

public class TextureAtlas {

    public static final int INVALID_INDEX=-1;

    private TextureRegionEntry[] textureRegionEntries;

    public static class TextureRegionEntry{
        public String name;
        public TextureRegion textureRegion;
    }

    public TextureAtlas(TextureRegionEntry[] textureRegionEntries) {
        this.textureRegionEntries = textureRegionEntries;
    }

    public int findIndex(String name){
        for(int i=0;i<textureRegionEntries.length;i++){
            if(textureRegionEntries[i].name.equals(name)){
                return i;
            }
        }
        return INVALID_INDEX;
    }
    public TextureRegion getTextureRegion(int index){
        return textureRegionEntries[index].textureRegion;
    }
    public int getSize(){
        return textureRegionEntries.length;
    }

    public List<Integer> getIndexSequence(String prefix){
        List<Integer> sequence=new LinkedList<>();
        int sequenceIndex=0;
        while(true){
            String name=prefix+'_'+sequenceIndex;
            int atlasIndex=findIndex(name);
            if(atlasIndex==INVALID_INDEX){
                break;
            }
            sequence.add(atlasIndex);
            sequenceIndex++;
        }
        return sequence;
    }
    public AnimationClip createAnimationClip(SpriteObject sprite,String prefix){
        List<Integer> indexSequence=getIndexSequence(prefix);
        int size=indexSequence.size();
        if(size==0)return null;
        AnimationClip animationClip=new AnimationClip(size);
        int animationIndex=0;
        for(int atlasIndex:indexSequence){
            SpriteKeyframe keyframe=new SpriteKeyframe(sprite,atlasIndex);
            animationClip.putKeyframe(animationIndex,keyframe);
            animationIndex++;
        }
        return animationClip;
    }
//    public AnimationClip createAnimationClip(SpriteObject sprite, String prefix){
//        int num=0;
//        List<SpriteKeyframe> keyframeList=new ArrayList<>();
//        while(true){
//            String name=prefix+'_'+num;
//            int index=findIndex(name);
//            if(index==INVALID_INDEX){
//                break;
//            }
//            SpriteKeyframe keyframe=new SpriteKeyframe(sprite,index);
//            keyframeList.add(keyframe);
//            num++;
//        }
//        if(num==0){
//            return null;
//        }
//        int length=keyframeList.size();
//        AnimationClip animationClip =new AnimationClip(length);
//        for(int i=0;i<length;i++){
//            animationClip.putKeyframe(i,keyframeList.get(i));
//        }
//        return animationClip;
//    }
}
