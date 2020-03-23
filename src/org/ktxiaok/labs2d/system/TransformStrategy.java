package org.ktxiaok.labs2d.system;

import org.joml.Vector2f;

public interface TransformStrategy {
	void setPosition(float x,float y);
	void getPosition(Vector2f v);
	void setScale(float sx,float sy);
	void getScale(Vector2f v);
	void setAngle(float a);
	float getAngle();
}
