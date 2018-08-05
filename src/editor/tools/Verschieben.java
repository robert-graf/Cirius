package editor.tools;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import editor.AbstractToolAdapter;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import interfaces.UndoObject;

public class Verschieben extends AbstractToolAdapter {
	Point basispunkt = new Point();
	int timeline = 0;
	ArrayList<Infoclass> data = new ArrayList<>();

	void setObjectPossision(Matrix m, MouseEvent ev) {
		double x = m.getX(ev) - basispunkt.x;
		double y = m.getY(ev) - basispunkt.y;
		for (Infoclass i : data) {
			i.setLocal(x, y);
		}
	}

	// public void destroy() {}
	// public void keyTyped(Matrix m, KeyEvent ev) {}
	// public void keyReleased(Matrix m, KeyEvent ev) {}
	// public void keyPressed(Matrix m, KeyEvent ev) {}
	// public void mouseReleased(Matrix m, MouseEvent ev) {}
	public void mousePressed(Matrix m, MouseEvent ev) {
		if (timeline == 0) {
			m.setSelectionAllow(false);
			basispunkt.setLocation(m.getX(ev), m.getY(ev));
			data.clear();
			for (CObject c : m.getSelectedList()) {
				data.add(new Infoclass(c, c.getX(), c.getY()));
			}
			timeline++;
			m.setInfo("Zielpunkt wählen");
		} else if (timeline == 1) {
			setObjectPossision(m, ev);
			double x = m.getX(ev) - basispunkt.x;
			double y = m.getY(ev) - basispunkt.y;
			m.addUndo(new UndoVerschieben(data, x, y));
			timeline = 0;
			m.setMainTool(null);
		}
	}

	// public void mouseExited(Matrix m, MouseEvent ev) {}
	// public void mouseEntered(Matrix m, MouseEvent ev) {}
	// public void mouseClicked(Matrix m, MouseEvent ev) {}
	public void mouseMoved(Matrix m, MouseEvent ev) {
		if (timeline == 1) {
			setObjectPossision(m, ev);
		}
	}

	// public void mouseDragged(Matrix m, MouseEvent ev) {}
	// public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {}
	public void init(Matrix m) {
		m.setInfo("Basispunkt wählen");
	}

	@Override
	public void lost(Matrix m, MouseEvent ev) {
		m.setSelectionAllow(true);
		timeline = 0;
		m.setInfo("-");
	}

	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub

	}

	private class Infoclass {
		private CObject c;
		double x, y;

		public Infoclass(CObject c, double x, double y) {
			this.c = c;
			this.x = x;
			this.y = y;
		}

		public void setLocal(double x2, double y2) {
			c.setX(x2 + x, false);
			c.setY(y2 + y, false);
		}

		public void undo() {
			c.setX(x, false);
			c.setY(y, false);
		}
	}

	private class UndoVerschieben extends UndoObject {
		ArrayList<Infoclass> data;
		double x, y;

		@SuppressWarnings("unchecked")
		public UndoVerschieben(ArrayList<Infoclass> data, double x2, double y2) {
			super(-1, null, null, data.clone());
			this.data = (ArrayList<Infoclass>) getObject();
			this.x = x2;
			this.y = y2;
		}

		@Override
		public void doRedo() {
			for (Infoclass i : data) {
				i.setLocal(x, y);
			}
		}

		@Override
		public void doUndo() {
			for (Infoclass i : data) {
				i.undo();
			}
		}

		@Override
		public String getName() {
			return "Verschieben";
		}

	}
}
