package de.game.objects.geom;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Circle;



public class CKreis extends CShape {
	Circle c;
	
	public CKreis(int x, int y, int radius) {
		this((float)x,(float)y, (float)radius);
	}
	public CKreis(float x, float y, float radius) {
		c = new Circle(x, y, radius);
	}
	@Override
	public Shape getShape() {
		return c;
	}

	@Override
	public String getShapeName() {
		return "Kreis";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	public int getRadius() {
		return (int) c.getRadius();
	}
	public void setRadius(float f) {
		c.setRadius(f);
	}
	@Override
	public void setLocalistaon(int x, int y) {
		c.setCenterX(x);
		c.setCenterY(y);
	}
}
