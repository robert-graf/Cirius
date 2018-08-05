package editor.cad;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.AbstractTool;
import editor.CObject;
import editor.DefaultEditorPanel.LayerLoop;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;

public class SelectByFrame implements AbstractTool {
	Layer oneLayer;
	double x, y;

	// Key
	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
	};

	public void keyPressed(Matrix m, KeyEvent e) {
		if ((e != null && e.getKeyCode() == KeyEvent.VK_SPACE)) {
			if (m.getSelectedLayer() == null) {
				if (m.getLayer(0).size() != 0) {
					m.setSelectedCObject(m.getLayer(0).get(0), m.getLayer(0));
				}
				return;
			}
			int indexObjekt = m.getSelectedCObjectIndex();
			int indexLayer = m.getSelectedLayerIndex();
			indexObjekt++;
			if (indexObjekt == m.getSelectedLayer().size()) {
				indexObjekt = 0;
				indexLayer = m.getSelectedLayerIndex() + 1;
				if (indexLayer == m.getLayerSize()) {
					indexLayer = 0;
				}
			}
			m.setSelectedCObject(m.getLayer(indexLayer).get(indexObjekt),
					m.getLayer(indexLayer));
		}
	};

	//
	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
	};

	public void mousePressed(Matrix m, MouseEvent ev) {
		x = m.getX(ev);
		y = m.getY(ev);
		LayerLoop loop = m.getLayers(false);
		// Auswählen
		boolean stopp = false;
		while (loop.hasnext()) {
			oneLayer = loop.next();
			if (oneLayer.isVisible()) {
				// for(int o = oneLayer.size()-1;o!=-1;o--){
				for (CObject o : oneLayer) {
					if (o instanceof CShape) {
						CShape s = (CShape) o;
						if (!s.getShape().contains(x - 5, y - 5, 10, 10)
								&& s.getShape()
										.intersects(x - 5, y - 5, 10, 10)) {
							m.setSelectedCObject(s, oneLayer);
							stopp = true;
							break;
						}
					}
				}
			}
			if (stopp) {
				break;
			}
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

	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
	}

	@Override
	public void destroy() {
		oneLayer = null;

	};

}
