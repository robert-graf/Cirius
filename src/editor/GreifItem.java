package editor;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPopupMenu;

import basic.DPoint;
import editor.DefaultEditorPanel.Matrix;

public abstract class GreifItem {
	DPoint p = new DPoint();

	public abstract int getID();

	public abstract double getX();

	public abstract double getY();

	public DPoint getLocation() {
		p.setLocation(getX(), getY());
		return p;
	};

	public void setLocation(DPoint point) {
		setLocation(point.x, point.y);
	}

	public abstract void setLocation(double x, double y);

	// public abstract void setX(int x);
	// public abstract void setY(int y);
	public abstract Rectangle getBounds();

	public abstract void draw(Graphics2D g1, Graphics2D g2, Matrix m);

	// retun true wenn object gelöscht werden soll
	public abstract boolean remove();

	public abstract JPopupMenu showContext();
}
