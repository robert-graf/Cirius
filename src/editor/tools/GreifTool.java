package editor.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPopupMenu;

import basic.DDrawer;
import basic.DPoint;
import editor.AbstractTool;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.Layer;

public class GreifTool implements AbstractTool {

	public static boolean PUNKTFANG = true;

	public static boolean ORTHOGONAL = false;
	public static boolean ORTHOGONAL_OFF_OTHERPOINT = false;
	public static boolean EINGABEHILFE = true;
	public static boolean RASTER = false;
	public static int FANG_SIZE = 20;
	public static boolean PUNKTFANG_ECKE = true;
	public static boolean PUNKTFANG_MITTE = true;
	public static boolean PUNKTFANG_ZENTRUM = false;
	public static boolean PUNKTFANG_RASTER = false;
	public static boolean PUNKTFANG_TILEMAP = false;
	public static GreifItem item;
	public static DPoint nextPoint = new DPoint();
	public static DPoint point = new DPoint(0, 0);

	public double getNearestItem(Matrix m, MouseEvent ev) {

		double distance = Double.MAX_VALUE;
		double x = m.getX(ev);
		double y = m.getY(ev);

		for (Layer l : m.getLayerList()) {
			if (l.isVisible()) {
				for (CObject c : l) {
					if (c.equals(m.getSelectedCObject())) {
						continue;
					}
					distance = c.getNearestPointFang(nextPoint, distance, x, y, item);

				}
			}
		}
		CObject c = m.getSelectedCObject();
		if (c != null && c instanceof editor.cad.CPath || c instanceof editor.cad.CPoly) {

			if (ev == null)
				return distance;
			for (GreifItem gr : c.getGreifItems()) {
				if (gr.equals(item)) {
					continue;
				}
				double currDistance = Math.abs(x - gr.getX()) + Math.abs(y - gr.getY());
				if (currDistance < distance) {
					distance = currDistance;
					nextPoint.setLocation(gr.getLocation());
				}
			}
			;
		}

		return distance;
	}

	@Override
	public void keyPressed(Matrix m, KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (item != null) {
				item.setLocation(point);
				item = null;
			}
		}
		if (ev.getKeyCode() == KeyEvent.VK_DELETE) {
			m.remove();
		}
	}

	JPopupMenu menu;

	@Override
	public void mousePressed(Matrix m, MouseEvent ev) {
		if (menu != null) {
			menu.setVisible(false);
			menu = null;
		}
		if (ev.getButton() == MouseEvent.BUTTON2) {
			return;
		}
		CObject c = m.getSelectedCObject();
		if (c != null && item == null) {

			for (int i = 0; i < c.getGreifItems().size(); i += 1) {
				GreifItem gr = c.getGreifItems().get(i);
				if (gr.getBounds().contains(ev.getX(), ev.getY())) {
					if (ev.getButton() == MouseEvent.BUTTON3) {

						menu = gr.showContext();
						if (menu == null)
							return;
						menu.setLocation(ev.getXOnScreen(), ev.getYOnScreen());
						menu.setVisible(true);
						return;
					}
					if (ev.getButton() == MouseEvent.BUTTON1) {
						item = gr;
						point.setLocation(item.getX(), item.getY());
					}
				}
			}
		} else if (item != null && ev.getButton() == MouseEvent.BUTTON1) {
			item = null;
		}
	}

	public void mouseMoved(Matrix m, MouseEvent ev) {
		// if(point == null) point = new Point();
		if (item != null) {

			double x = m.getX(ev);
			double y = m.getY(ev);
			x = DDrawer.round(x);
			y = DDrawer.round(y);
			double fromX = point.x;
			double fromY = point.y;
			if (PUNKTFANG) {
				double currDistance = getNearestItem(m, ev);
				if (nextPoint != null) {

					if (currDistance < FANG_SIZE) {
						item.setLocation(nextPoint);
						return;
					}
				}

			}
			
			if (ORTHOGONAL) {
				if (Math.abs(x - fromX) < Math.abs(y - fromY)) {
					item.setLocation(fromX, y);
				} else {
					item.setLocation(x, fromY);
				}
			} else {
				item.setLocation(x, y);
			}
		}
	}

	@Override
	public void keyTyped(Matrix m, KeyEvent ev) {
	}

	@Override
	public void keyReleased(Matrix m, KeyEvent ev) {

	}

	@Override
	public void mouseReleased(Matrix m, MouseEvent ev) {
		mouseMoved(m, ev);
	}

	@Override
	public void mouseExited(Matrix m, MouseEvent ev) {
	}

	@Override
	public void mouseEntered(Matrix m, MouseEvent ev) {
	}

	@Override
	public void mouseClicked(Matrix m, MouseEvent ev) {
	}

	@Override
	public void mouseDragged(Matrix m, MouseEvent ev) {
	}

	@Override
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	}

	@Override
	public void init(Matrix m) {
	}

	@Override
	public void lost(Matrix m, MouseEvent ev) {
	}

	Stroke strich = new BasicStroke(1.5f, // Width
			BasicStroke.CAP_SQUARE, // End cap
			BasicStroke.JOIN_MITER, // Join style
			10.0f, // Miter limit
			new float[] { 4.0f, 6.0f }, // Dash pattern
			0.0f);
	Stroke linie = new BasicStroke();

	Color gr = new Color(100, 100, 100, 100);

	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		if (RASTER) {
			g1.setStroke(linie);
			g1.setColor(gr);
			int w = (int) (m.getTransX() + m.getVisibleRectangle().getWidth());
			int h = (int) (m.getTransY() + m.getVisibleRectangle().getHeight());
			for (double x = m.getTransX() / Matrix.getRast() * Matrix.getRast(); x < w; x += Matrix.getRast()) {
				DDrawer.drawLine(x, m.getTransY(), x, h, g1);
			}
			for (double y = m.getTransY() / Matrix.getRast() * Matrix.getRast(); y < h; y += Matrix.getRast()) {
				DDrawer.drawLine(m.getTransY(), y, w, y, g1);
			}
		}

		if (ev == null || item == null || point == null)
			return;
		Stroke stroke = g1.getStroke();
		g1.setStroke(strich);
		g1.setColor(Color.BLUE);
		DDrawer.drawLine(point.x, point.y, item.getX(), item.getY(), g1);
		g1.setStroke(stroke);

		if (EINGABEHILFE) {
			int i = g2.getFontMetrics().getHeight() - 2;
			String s = item.getX() - point.x + "," + (item.getY() - point.y);

			g2.setColor(Color.lightGray);
			g2.fillRect(m.getRealX(item.getX()), m.getRealY(item.getY()) - i, g2.getFontMetrics().stringWidth(s), i);
			g2.setColor(Color.white);
			g2.drawRect(m.getRealX(item.getX()), m.getRealY(item.getY()) - i, g2.getFontMetrics().stringWidth(s), i);

			g2.setColor(Color.black);
			g2.drawString(s, m.getRealX(item.getX()), m.getRealY(item.getY()) - 1);
		}

		// GreifItem gr = getNearestItem(m, ev, item);
		// if(gr == null)return;
		// g1.drawOval(gr.getX()-20, gr.getY()-20, 40, 40);

	}

	@Override
	public void destroy() {

	}

	public static double getDistance(DPoint p1, DPoint p2) {
		return getDistance(p1.x, p1.y, p2.x, p2.y);
	}

	public static double getDistance(double x, double y, double x2, double y2) {
		return Math.abs(x - x2) + Math.abs(y - y2);
	}

	public static double getDistanceAndOverWirte(DPoint overwrite, double distance, double x, double y, DPoint calc) {
		double i = getDistance(calc.x, calc.y, x, y);
		if (i < distance) {
			overwrite.setLocation(calc);
			return i;
		}
		return distance;
	}

}
