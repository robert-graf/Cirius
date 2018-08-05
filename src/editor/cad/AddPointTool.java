package editor.cad;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;
import editor.cad.CPath.PolygonPoint;
import editor.cad.CPath.Tangente;
import editor.tools.GreifTool;

public class AddPointTool implements AbstractTool {
	CPath path = null;
	int id = 0;
	PolygonPoint p;
	Tangente t;
	Tangente superTangete;
	double x;
	double y;

	public void setID(int id, PolygonPoint p) {
		this.id = id;
		this.p = p;
	};

	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {

	};

	public void keyPressed(Matrix m, KeyEvent ev) {
		if (ev.getKeyCode() == KeyEvent.VK_ESCAPE) {
			p.remove();
			path.fixTangenteToLocation = false;
			superTangete = null;
			path = null;
			GreifTool.item = null;
			m.setMainTool(null);

		}

	};

	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
		if (t == null) {
			GreifTool.item = p;
		} else {
			GreifTool.item = t;
		}
	};

	public void mousePressed(Matrix m, MouseEvent ev) {
		if (path != null) {
			path.fixTangenteToLocation = true;
			if (t != null) {
				if (p.syncroniced || p.verbunden) {
					addPoint();
					if (p.verbunden && p.syncroniced){
						superTangete = t;
						x = t.getX();
						y = t.getY();
					}
					t = null;
				} else {
					if (!t.nachher) {
						t = p.getPriviosPoint().tangeteNach;
					} else {
						addPoint();
						t = null;
					}
				}
				return;

			}

			if (p.linie) {
				addPoint();
			} else if ((p.verbunden && !p.syncroniced) || p.ellypse_3Points || p.bogen) {
				t = p.tangeteVor;
			} else if (p.verbunden && p.syncroniced) {
				if (superTangete == null) {
					t = p.tangeteNach;
				} else {
					addPoint();
					st();
				}
			} else if (p.ellypse || p.skip || p.close) {
				addPoint();
			} else {
				if (p.syncroniced) {
					t = p.tangeteNach;
				} else {
					t = p.tangeteVor;
				}
			}
			
		}
	};

	private void addPoint() {
		p = path.addPoint(GreifTool.item.getX() - path.getX(), GreifTool.item.getY() - path.getY(), id);
		id += 1;
	}

	public void mouseExited(Matrix m, MouseEvent ev) {
	};

	public void mouseEntered(Matrix m, MouseEvent ev) {
	};

	public void mouseClicked(Matrix m, MouseEvent ev) {
	};
	private void st(){
		if (superTangete != null) {
			superTangete.setLocation(x, y);
		}
	}
	//
	// mouseMotion
	public void mouseMoved(Matrix m, MouseEvent ev) {
		st();
	};

	public void mouseDragged(Matrix m, MouseEvent ev) {
		st();
	};

	//
	// mouseWheel
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	};

	// custom
	public void init(Matrix m) {
		m.setSelectionAllow(false);
	};

	public void lost(Matrix m, MouseEvent ev) {
		GreifTool.item = null;
		m.setSelectionAllow(true);
	};

	public void draw(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {

	}

	@Override
	public void destroy() {
	}

	public void setCPath(CPath cPath) {
		path = cPath;
	};

}
