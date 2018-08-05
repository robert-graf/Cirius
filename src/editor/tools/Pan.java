package editor.tools;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;

import basic.DDrawer;
import basic.DPoint;
import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;
import interfaces.Destroyer;

public class Pan implements AbstractTool {

	private DPoint pan;

	public void keyTyped(Matrix m, KeyEvent ev) {
	}

	public void keyReleased(Matrix m, KeyEvent ev) {
	}

	static JDialog d;
	static JSpinner s;
	static JLabel l;

	public void keyPressed(Matrix m, KeyEvent ev) {
//		if (ev.getKeyCode() == KeyEvent.VK_F7) {
//			if (d == null) {
//				d = new JDialog((Frame) m.getPanel().getTopLevelAncestor());
//				JPanel pa = new JPanel();
//				d.add(pa);
//				pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
//				l = new JLabel("Raster: " + Matrix.getRast() + " px");
//				pa.add(l);
//				s = new JSpinner();
//				s.setValue(Matrix.getRast());
//				pa.add(s);
//				JButton b = new JButton("OK");
//				JButton b1 = new JButton("Anwenden");
//				JButton b2 = new JButton("Abbrechen");
//				ActionListener a = new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						Matrix.setRast((Integer) s.getValue());
//						l.setText("Raster: " + Matrix.getRast() + " px");
//					}
//				};
//				ActionListener a1 = new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						d.setVisible(false);
//					}
//				};
//				b.addActionListener(a);
//				b.addActionListener(a1);
//				b1.addActionListener(a);
//				b2.addActionListener(a1);
//				pa.add(b);
//				pa.add(b1);
//				pa.add(b2);
//				d.pack();
//				d.setLocationRelativeTo(m.getPanel());
//				d.setResizable(false);
//
//			}
//			d.setVisible(true);
//		}

	}

	public void mouseReleased(Matrix m, MouseEvent ev) {
		if (oldCursor != null) {
			m.getPanel().setCursor(oldCursor);
			oldCursor = null;
		}

	}

	public void mousePressed(Matrix m, MouseEvent ev) {
		pan = new DPoint(m.getX(ev), m.getY(ev));

	}

	public void mouseExited(Matrix m, MouseEvent ev) {
	}

	public void mouseEntered(Matrix m, MouseEvent ev) {
	}

	public void mouseClicked(Matrix m, MouseEvent ev) {
		if (ev.getClickCount() == 2 && ev.getButton() == MouseEvent.BUTTON2) {
			m.setScale(1f);
		}
		if (ev.getClickCount() == 3 && ev.getButton() == MouseEvent.BUTTON2) {
			m.setTransX(0);
			m.setTransY(0);
		}

	}

	public void mouseMoved(Matrix m, MouseEvent ev) {
	}

	public void mouseDragged(Matrix m, MouseEvent ev) {
		if (isWheelDown(ev, m)) {
			m.setTransX((m.getTransX() + (pan.x - m.getX(ev))));
			m.setTransY(m.getTransY() + (pan.y - m.getY(ev)));
			mousePressed(m, ev);
			if (Matrix.isSmoveRender()) {
				m.getPanel().repaint();
			}
			if (oldCursor == null) {
				oldCursor = m.getPanel().getCursor();
				m.getPanel().setCursor(panCursor);
			}
		}
		;

	}

	private boolean isRightDownAndSTRGDown(MouseEvent ev) {
		int onmask = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON3_DOWN_MASK;
		int offmask = MouseEvent.SHIFT_DOWN_MASK;
		if ((ev.getModifiersEx() & (onmask | offmask)) == onmask) {
			return true;
		}
		return false;
	}

	protected boolean isWheelDown(MouseEvent ev, Matrix m) {
		if (m.getMainTool() == null) {

		} else if (m.getMainTool().equals(this)) {
			// immer bei Maintool;
			return true;
		} else if (m.getMainTool() instanceof Pan) {
			return false;
		}
		int onmask = MouseEvent.BUTTON2_DOWN_MASK;
		int offmask = MouseEvent.CTRL_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK;
		if ((ev.getModifiersEx() & (onmask | offmask)) == onmask) {
			return true;
		}
		return isRightDownAndSTRGDown(ev);
	}

	//int i, j;

	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
		/*
		 * //zentrischer Zoom
		 * i = (int) (p.getWidth()/m.getScale()); j = (int)
		 * (p.getHeight()/m.getScale());
		 * m.setScale(m.getScale()+(ev.getWheelRotation()*0.1f)); i -= (int)
		 * (p.getWidth() /m.getScale()); j -= (int)
		 * (p.getHeight()/m.getScale()); m.setTransX((int)
		 * (m.getTransX()+(i)/2)); m.setTransY((int) (m.getTransY()+(j)/2));
		 */
		int i = (int) (m.getPanel().getWidth() / m.getScale());
		int j = (int) (m.getPanel().getHeight() / m.getScale());
		// m.setScale(m.getScale() * (1 + (ev.getWheelRotation() * 0.01f)));
		if(ev.getWheelRotation() == 0){}else
		if(ev.getWheelRotation()>0){
			m.setScale(DDrawer.round(m.getScale() * (ev.getWheelRotation() * 1.05f),100));
		}else{
			m.setScale(DDrawer.round(m.getScale() / (Math.abs(ev.getWheelRotation()) * 1.05f),100));
		}
		
		if (m.getScale() < 0.005) {
			m.setScale(0.005f);
		}
		if (m.getScale() > 1000000f) {
			m.setScale(1000000f);
		}
		i -= (int) (m.getPanel().getWidth() / m.getScale());
		j -= (int) (m.getPanel().getHeight() / m.getScale());
		float faktorX = (float) ev.getX() / (float) m.getPanel().getWidth();
		float faktorY = (float) ev.getY() / (float) m.getPanel().getHeight();
		m.setTransX((int) (m.getTransX() + (i) * faktorX));
		m.setTransY((int) (m.getTransY() + (j) * faktorY));
	}

	Cursor panCursor;
	Cursor oldCursor;

	public void init(Matrix m) {
		panCursor = new Cursor(Cursor.MOVE_CURSOR);
	}

	public void lost(Matrix m, MouseEvent ev) {
		m.getPanel().setCursor(oldCursor);
	}

	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
	}

	@Override
	public void destroy() {
		Destroyer.destroy(oldCursor);
		oldCursor = null;
		Destroyer.destroyObject(pan);
		pan = null;
		Destroyer.destroyObject(panCursor);
		panCursor = null;
	}

}
