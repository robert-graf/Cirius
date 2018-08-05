package de.game.objects.geom;

import org.newdawn.slick.geom.Rectangle;

public class CRect extends CShape {
	Rectangle r;
	public CRect(int x, int y, int width, int height) {
		r = new Rectangle(x, y, width, height);
	}
	public CRect(float x, float y, float width, float height) {
		r = new Rectangle(x, y, width, height);
	}
	@Override
	public Rectangle getShape() {
		return r;
	}

	@Override
	public String getShapeName() {
		return "Rect";
	}

	@Override
	public void init() {
	}
	public void setHeight(int height){
		r.setHeight(height);
	}
	public int getHeight(){
		return (int) r.getHeight();
	}
	public void setWidth(int width) {
		r.setWidth(width);
	}
	public int getWidth(){
		return (int) r.getWidth();
	}
	@Override
	public void setLocalistaon(int x, int y) {
		r.setLocation(x, y);
	}
}
