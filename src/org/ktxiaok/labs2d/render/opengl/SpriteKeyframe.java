package org.ktxiaok.labs2d.render.opengl;

import org.ktxiaok.labs2d.system.render.AnimationPlayer;
import org.ktxiaok.labs2d.system.render.Keyframe;

public class SpriteKeyframe extends Keyframe {
    private SpriteObject sprite;
    private int textureAtlasIndex;
    public SpriteKeyframe(SpriteObject sprite,int textureAtlasIndex){
        this.sprite=sprite;
        this.textureAtlasIndex=textureAtlasIndex;
    }
    @Override
    public void applyFrame(AnimationPlayer animationPlayer) {
        sprite.setTextureAtlasIndex(textureAtlasIndex);
    }
}
