package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPopupMenu;

import basic.DDrawer;
import basic.DPoint;
import editor.AbstractTool;
import editor.GreifItem;
import editor.DefaultEditorPanel.Matrix;
import editor.tools.GreifTool;

public class CPathTool implements AbstractTool {
	DPoint alt;
	CPath poly = null;

	GreifItem greif2 = new GreifItem() {
		DPoint p = new DPoint();
		Color ausgewählt = Color.CYAN;

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			r.setBounds(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2, i, i);
			g2.setColor(ausgewählt);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		@Override
		public boolean remove() {
			// GreifTool.item = null;
			return false;
		}

		@Override
		public JPopupMenu showContext() {
			return null;
		}

		@Override
		public int getID() {
			return 0;
		}

		@Override
		public double getX() {
			return p.x;
		}

		@Override
		public double getY() {
			return p.y;
		}

		@Override
		public void setLocation(double x, double y) {
			p.setLocation(x, y);
		}
	};
	GreifItem greif = greif2;

	// Key
	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (poly == null) {
				alt = null;
				m.setMainTool(null);
				return;
			}
			alt = null;
			poly.setClose(true);
			poly.removePoint();

			poly = null;
			greif = greif2;
			GreifTool.item = greif;

		}

	};

	public void keyPressed(Matrix m, KeyEvent ev) {
	};

	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
		GreifTool.item = greif;

	};

	public void mousePressed(Matrix m, MouseEvent ev) {

		double x = greif.getX();
		double y = greif.getY();
		GreifTool.point.x = x;
		GreifTool.point.y = y;
		if (poly != null) {
			poly.addPoint(x - poly.getX(), y - poly.getY());
			greif = poly.getGreifItems().get(poly.getGreifItems().size() - 1);
		} else if (alt == null) {
			alt = new DPoint(x, y);
		} else {
			poly = new CPath(alt, new DPoint(x, y), false, m);
			poly.addPoint(y - poly.getX(), x - poly.getY());
			m.addCObject(poly);
			greif = poly.getGreifItems().get(poly.getGreifItems().size() - 1);
			// m.setSelectedCObject(poly, m.getSelectedLayer());
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
		if (alt == null) {
			GreifTool.point.x = m.getX(ev);
			GreifTool.point.y = m.getY(ev);

		}
		if (poly != null) {
			poly.resetCurrendTangenten();
		}
	};

	public void mouseDragged(Matrix m, MouseEvent ev) {
	};

	//
	// mouseWheel
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	};

	// custom
	public void init(Matrix m) {
		GreifTool.item = greif;
		m.setSelectionAllow(false);
	};

	public void lost(Matrix m, MouseEvent ev) {
		GreifTool.item = null;
		m.setSelectionAllow(true);
	};

	public void draw(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {
		
		greif.draw(g, g2, m);
		if (alt == null) {
			return;
		}
		DDrawer.drawLine(alt.x, alt.y, greif.getX(), greif.getY(),g);

	}

	@Override
	public void destroy() {
	}

	

}
