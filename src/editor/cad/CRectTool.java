package editor.cad;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;

public class CRectTool implements AbstractTool {
	Point p1 = new Point(), p2 = null;
	Point set = p1;

	// Key
	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
	};

	public void keyPressed(Matrix m, KeyEvent ev) {
	};

	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
	};

	public void mousePressed(Matrix m, MouseEvent ev) {
		if (p2 == null) {
			p2 = new Point();
			set = p2;
		} else {
			Point a = p1;
			Point b = p2;
			m.addCObject(new CRect(a.x < b.x ? a.x : b.x,
					a.y < b.y ? a.y : b.y, Math.abs(a.x - b.x) - 1, Math
							.abs(a.y - b.y) - 1));
			p2 = null;
			set = p1;
			m.setMainTool(null);
		}
	};

	public void mouseExited(Matrix m, MouseEvent ev) {
	};

	public void mouseEntered(Matrix m, MouseEvent ev) {
	};

	public void mouseClicked(Matrix m, MouseEvent ev) {
	};

	//
	// mouseMotion
	public void mouseMoved(Matrix m, MouseEvent ev) {
		set.setLocation(m.getX(ev), m.getY(ev));
	};

	public void mouseDragged(Matrix m, MouseEvent ev) {
	};

	//
	// mouseWheel
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	};

	// custom
	public void init(Matrix m) {
	};

	public void lost(Matrix m, MouseEvent ev) {
	};

	public void draw(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {
		if (p2 == null) {
			return;
		}
		Point a = p1;
		Point b = p2;
		Rectangle r = new Rectangle(a.x < b.x ? a.x : b.x, a.y < b.y ? a.y
				: b.y, Math.abs(a.x - b.x) - 1, Math.abs(a.y - b.y) - 1);
		g.setColor(CShape.fillDefault);
		g.fill(r);
		g.setColor(CShape.paintColor);
		if (m.getSelectedCObject() != null
				&& m.getSelectedCObject().equals(this)) {
			g.setColor(CShape.paintColorSelect);
		}
		g.draw(r);
	}

	@Override
	public void destroy() {
	};

}
