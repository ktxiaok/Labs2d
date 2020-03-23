package org.ktxiaok.labs2d.system.physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

public class RaycastInfo{
	public RaycastInfo() {

	}
	public Vec2 position;
	public Vec2 normal;
	public float distanceRatio;//碰撞点到起始点占射线总长度的比例
	public Fixture fixture;

}
