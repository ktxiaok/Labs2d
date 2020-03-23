
package org.ktxiaok.labs2d.render.opengl;

import java.io.Serializable;

public class Color implements Serializable{

	public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
	public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	public static final Color YELLOW = new Color(1.0f, 1.0f, 0, 1.0f);
	public static final Color RED = new Color(1.0f, 0, 0, 1.0f);
	public static final Color BLUE = new Color(0, 0, 1.0f, 1.0f);
	public static final Color GREEN = new Color(0, 1.0f, 0, 1.0f);
	public static final Color BLACK = new Color(0, 0, 0, 1.0f);
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f, 1.0f);
	public static final Color CYAN = new Color(0, 1.0f, 1.0f, 1.0f);
	public static final Color DARK_GRAY = new Color(0.3f, 0.3f, 0.3f, 1.0f);
	public static final Color LIGHT_GRAY = new Color(0.7f, 0.7f, 0.7f, 1.0f);
	public static final Color PINK = new Color(255, 175, 175, 255);
	public static final Color ORANGE = new Color(255, 200, 0, 255);
	public static final Color MAGENTA = new Color(255, 0, 255, 255);
	
	private float r;
	private float g;
	private float b;
	private float a;

	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	public Color(float r, float g, float b) {
		this(r, g, b, 1f);
	}
	public Color(Color color) {
		this(color.r, color.g, color.b, color.a);
	}
	public Color(int value) {
		int r = (value & 0x00FF0000) >> 16;
		int g = (value & 0x0000FF00) >> 8;
		int b = (value & 0x000000FF);
		int a = (value & 0xFF000000) >> 24;
		if (a < 0)
			a += 256;
		if (a == 0)
			a = 255;
		this.r = r / 255.0f;
		this.g = g / 255.0f;
		this.b = b / 255.0f;
		this.a = a / 255.0f;
	}
	public int toIntBits() {
		int color = ((int) (255 * a) << 24) | ((int) (255 * b) << 16) | ((int) (255 * g) << 8)
				| ((int) (255 * r));
		return color;
	}
	public void set(Color color) {
		set(color.r, color.g, color.b, color.a);
	}
	public void set(float r, float g, float b, float a) {
		set(r, g, b);
		this.a = a;
	}
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public float getRed() {
		return r;
	}
	public float getGreen() {
		return g;
	}
	public float getBlue() {
		return b;
	}
	public float getAlpha() {
		return a;
	}
	public void setRed(float _r) {
		r=_r;
	}
	public void setBlue(float _b) {
		b=_b;
	}
	public void setGreen(float _g) {
		g=_g;
	}
	public void setAlpha(float _a) {
		a=_a;
	}
	public float[] toArray() {
		return new float[] {r,g,b,a};
	}
	public boolean equals(Object other) {
		if (other instanceof Color) {
			Color o = (Color) other;
			return ((o.r == r) && (o.g == g) && (o.b == b) && (o.a == a));
		}
		return false;
	}
	public String toString() {
		return "Color (" + r + "," + g + "," + b + "," + a + ")";
	}
}
