package de.game.objects.geom;

import org.newdawn.slick.geom.Ellipse;

public class COval extends CShape {
	Ellipse c;
	public COval(int x, int y, int r1,int r2) {
		c = new Ellipse(x, y, r1, r2);
	}
	public COval(float x, float y, float r1,float r2) {
		c = new Ellipse(x, y, r1, r2);
	}
	@Override
	public Ellipse getShape() {
		return c;
	}

	@Override
	public String getShapeName() {
		return "Oval";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	public int getRadius1() {
		return (int) c.getRadius1();
	}
	public int getRadius2() {
		return (int) c.getRadius2();
	}
	public void setRadius1(float f) {
		c.setRadius1(f);
	}
	public void setRadius2(float f) {
		c.setRadius2(f);
	}
	@Override
	public void setLocalistaon(int x, int y) {
		c.setCenterX(x);
		c.setCenterY(y);
	}
}
