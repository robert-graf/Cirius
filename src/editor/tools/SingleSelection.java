package editor.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.AbstractTool;
import editor.CObject;
import editor.DefaultEditorPanel.LayerLoop;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;

public class SingleSelection implements AbstractTool {
	// Key
	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
	};

	public void keyPressed(Matrix m, KeyEvent e) {
		if (!m.isSelectionAllow()) {
			return;
		}
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
		if (m.isSelectionAllow() && ev.getButton() == MouseEvent.BUTTON1
				&& !ev.isShiftDown() && !ev.isAltDown()) {
			if (ev.isControlDown()) {
				m.removeSelectedCObject(getCurrendMousoverObject(m, ev, false));
			} else {
				getCurrendMousoverObject(m, ev, true);
			}
		}
	}

	public void mouseExited(Matrix m, MouseEvent ev) {
	};

	public void mouseEntered(Matrix m, MouseEvent ev) {
	};

	public void mouseClicked(Matrix m, MouseEvent ev) {
	};

	CObject lightingUpObject;
	boolean thread1 = true;

	// mouseMotion
	public void mouseMoved(final Matrix m, final MouseEvent ev) {
		if (thread1) {
			thread1 = false;
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(100);
						lightingUpObject = getCurrendMousoverObject(m, ev,	false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					thread1 = true;
				}
			}.start();
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
	};

	public void lost(Matrix m, MouseEvent ev) {
	};

	Stroke strichpunkt = new BasicStroke(1.5f, // Width
			BasicStroke.CAP_SQUARE, // End cap
			BasicStroke.JOIN_BEVEL, // Join style
			10.0f, // Miter limit
			new float[] { 10.5f, 4.5f, 1.5f, 4.5f }, 0.0f); // Dash phase
	Stroke strich = new BasicStroke(1.5f, // Width
			BasicStroke.CAP_SQUARE, // End cap
			BasicStroke.JOIN_MITER, // Join style
			10.0f, // Miter limit
			new float[] { 4.0f, 6.0f }, // Dash pattern
			0.0f);

	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		if (m.isSelectionAllow()) {
			Stroke stroke = g1.getStroke();
			g1.setStroke(strich);
			g1.setColor(Color.BLUE);
			for (CObject c : m.getSelectedList()) {
				g1.draw(c.getShape());
				if(lightingUpObject != null && lightingUpObject.equals(c)){
					lightingUpObject = null;
				}
				
			}
			if (lightingUpObject == null) {
				return;
			}
			
			g1.setColor(Color.YELLOW);
			g1.setStroke(strichpunkt);
			g1.draw(lightingUpObject.getShape());
			g1.setStroke(stroke);
			// g1.draw(lightingUpObject.getShape());
		}
	}

	@Override
	public void destroy() {};

	public static CObject getCurrendMousoverObject(Matrix m, MouseEvent ev,boolean select) {
		Layer oneLayer;
		double x, y;
		x = m.getX(ev);
		y = m.getY(ev);
		LayerLoop loop = m.getLayers(false);
		// Auswählen
		while (loop.hasnext()) {
			oneLayer = loop.next();
			if (oneLayer.isVisible()) {
				// for(int o = oneLayer.size()-1;o!=-1;o--){
				for (CObject o : oneLayer) {
					if (!o.getShape().contains(x - 5, y - 5, 10, 10)
							&& o.getShape().intersects(x - 5, y - 5, 10, 10)) {
						if (select) {
							m.addSelectedCObject(o);
							m.setSelectedCObject(o, oneLayer);
						}
						return o;
					}
				}

			}
		}
		loop.restart();
		while (loop.hasnext()) {
			oneLayer = loop.next();
			if (oneLayer.isVisible()) {
				// for(int o = oneLayer.size()-1;o!=-1;o--){
				for (CObject o : oneLayer) {
					if (o.getShape().contains(x, y)) {
						if (select) {
							m.addSelectedCObject(o);
							m.setSelectedCObject(o, oneLayer);
						}
						return o;
					}
				}
			}
		}
		return null;
	}
}
