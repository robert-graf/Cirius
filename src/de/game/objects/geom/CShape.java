package de.game.objects.geom;

import org.newdawn.slick.Graphics;

import de.game.objects.CObject;

public abstract class CShape extends CObject {
	
	public abstract org.newdawn.slick.geom.Shape getShape();
	public abstract String getShapeName();

	@Override
	public String getXMLName() {
		return "Shape";
	}
	@Override
	public void draw(int x, int y, Graphics g) {
		setLocalation(x, y);
		g.setAntiAlias(true);
		g.draw(this.getShape());
		g.setAntiAlias(false);
//		g.draw(getShape());
	}
	@Override
	protected void update(int delta,int index) {
	}

	public abstract void setLocalistaon(int x,int y);
	
}
