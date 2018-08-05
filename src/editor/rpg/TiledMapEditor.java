package editor.rpg;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;

import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;
import editor.Layer;
import gui.Messages;

@SuppressWarnings("serial")
public class TiledMapEditor extends JComponent implements ComponentListener {
	File map;
	Teilbar exampel;

	final static TiledMapManager manager = TiledMapManager.THAT;
	// TODO TileMap und Ref
	ArrayList<Teilbar> listOfObject = new ArrayList<>();
	ArrayList<JButton> buttons = new ArrayList<>();
	ArrayList<JButton> buttons2 = new ArrayList<>();
	ArrayList<JButton> buttons3 = new ArrayList<>();
	int firstColunm = 100;
	int firstRow = 100;
	int border = 10;
	int space = border + 5;

	Color lightred = new Color(255, 100, 100);

	public TiledMapEditor() {
		setLayout(null);
		addComponentListener(this);
	}

	// List update
	public void init() {
		map = (File) manager.box.getSelectedItem();
		// Information List
		listOfObject.clear();
		Matrix m = GUI_Layerbefehle.getMatrix();
		for (Layer l : m.getLayerList()) {
			for (CObject c : l) {
				// TODO TileMap und Ref "getMap()"
				if (c instanceof CTileMap) {
					if (((CTileMap) c).getFile().equals(map)) {
						listOfObject.add((CTileMap) c);
					}
				}
				if (c instanceof CRef) {
					if (((CRef) c).getFile().equals(map)) {
						listOfObject.add((CRef) c);
					}
				}
			}
		}
		if (listOfObject.size() != 0) {
			exampel = listOfObject.get(0);
		}
		// Buttons for editing
		removeAll();
		for (int i = buttons.size(); i < listOfObject.size(); i++) {
			JButton b = new JButton();
			b.addMouseMotionListener(new TMEListener(i, true));
			buttons.add(b);
			JButton b2 = new JButton();
			b2.addMouseMotionListener(new TMEListener(i, false));
			buttons2.add(b2);
			JButton b3 = new JButton();
			if (i != 0) {
				b3.addMouseMotionListener(new TMEListenerDouble(i, b));
			}
			buttons3.add(i, b3);
		}
		// add all Buttons TODO Only if needed
		for (int i = 0; i != listOfObject.size(); i++) {
			add(buttons3.get(i));
		}
		for (int i = 0; i != listOfObject.size(); i++) {
			add(buttons.get(i));
			add(buttons2.get(i));
		}
	}

	@Override
	protected void paintComponent(Graphics go) {
		Graphics2D g = (Graphics2D) go;
		if (map == null) {
			init();
		}
		if (buttons.size() != listOfObject.size()) {
			init();
		}
		if (listOfObject.size() == 0) {
			g.drawString(
					Messages.getString("TiledMapEditor.NoObjectIsFound"), 20, 20); //$NON-NLS-1$
			return;
		}
		// calc new Frame
		int textheigt = g.getFont().getSize() / 2;
		cellWidth = (getWidth() - firstColunm - border)
				/ exampel.getLayerCount();
		cellHeight = ((getHeight() - firstRow - border) / listOfObject.size());
		// 3d Recht background
		g.setColor(getBackground());
		g.fill3DRect(border, border, getWidth() - 2 * border, getHeight() - 2
				* border, true);
		// Solid red. See what you get
		g.setColor(lightred);
		for (int n = 0; n != listOfObject.size(); n++) {
			Teilbar c = listOfObject.get(n);
			// TODO Calc "getStart" "getEnd"
			g.fillRect((int) calcColunm(c.getStart()), (int) calcRow(n),
					((cellWidth()) * (c.getEnd() - c.getStart() + 1)),
					cellHeight());
			// System.out.println(c.start + "\t" + c.ende);
		}
		// Text Layer and Grid1
		g.setColor(getForeground());
		for (int n = 0; n != exampel.getLayerCount(); n++) {
			drawRotatedString(exampel.getLayerName(n), g,
					((int) calcColunm(n + 0.5)) - textheigt, space, Math.PI / 2);
			int i = (int) calcColunm(n);
			g.drawLine(i, border, i, getHeight() - border - 1);
		}
		// Text Object and Grid2
		for (int n = 0; n != listOfObject.size(); n++) {
			g.drawString(listOfObject.get(n).getName(), space, calcRow(n + 0.5)
					+ textheigt);
			int i = (int) calcRow(n);
			g.drawLine(border, i, getWidth() - border - 1, i);
		}
		// Place buttons
		for (int n = 0; n != listOfObject.size(); n++) {
			JButton b1 = buttons.get(n);
			JButton b2 = buttons2.get(n);
			JButton b3 = buttons3.get(n);
			Teilbar c = listOfObject.get(n);
			Teilbar c2 = null;
			if (n != 0) {
				c2 = listOfObject.get(n - 1);
				if (c2.getStart() != c.getEnd() + 1) {
					c2 = null;
				}
			}
			if (!b1.hasFocus()) {
				b1.setLocation((int) calcColunm(c.getStart()) - 10,
						(int) calcRow(n));
				b1.setSize(20, cellHeight());
			}
			if (!b2.hasFocus()) {
				b2.setLocation((int) calcColunm(c.getEnd() + 1) - 10,
						(int) calcRow(n));
				b2.setSize(20, cellHeight());
			}
			if (!b3.hasFocus()) {
				if (c2 == null) {
					b3.setVisible(false);
				} else {
					int size = 15;
					if (b2.getHeight() / 2 < size) {
						size = b2.getHeight() / 2;
					}
					b3.setVisible(true);
					b3.setLocation((int) calcColunm(c2.getStart()) - size,
							(int) calcRow(n) - size);
					b3.setSize(size * 2, size * 2);
				}
			}
		}

	}

	private float calcRow(double n) {
		return (float) (cellHeight() * (n) + firstRow);
	}

	private int cellHeight;

	private int cellHeight() {
		return cellHeight;
	}

	private float calcColunm(double n) {
		return (float) (cellWidth() * n + firstColunm);
	}

	private int calcCellnumberByColum(int i) {
		return (i + cellWidth() / 2 - firstColunm) / cellWidth();
	}

	private int cellWidth;

	private int cellWidth() {
		return cellWidth;
	}

	public static void drawRotatedString(String text, Graphics2D g2, float x,
			float y, double winkel) {
		AffineTransform alt = g2.getTransform();
		AffineTransform rotieren = AffineTransform.getRotateInstance(winkel, x,
				y);
		g2.transform(rotieren);
		g2.drawString(text, x, y);
		g2.setTransform(alt);
	}

	private class TMEListener extends MouseMotionAdapter {
		int row;
		boolean start;

		public TMEListener(int row, boolean start) {
			this.row = row;
			this.start = start;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			try {
				// Dragg button
				JComponent c = (JComponent) e.getSource();
				c.setLocation(e.getX() + c.getX(), c.getY());
				int i = calcCellnumberByColum(c.getX());
				// Set infomation
				if (start) {
					if (listOfObject.get(row).getEnd() >= i && i > -1) {
						listOfObject.get(row).setStart(i);
						repaint();
					} else {
						c.setLocation((int) calcColunm(listOfObject.get(row)
								.getStart()) - 10, c.getY());
					}
				} else {
					i--;
					if (listOfObject.get(row).getStart() <= i
							&& i < exampel.getLayerCount()) {
						listOfObject.get(row).setEnd(i);
						repaint();
					} else {
						c.setLocation((int) calcColunm(listOfObject.get(row)
								.getEnd() + 1) - 10, c.getY());
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private class TMEListenerDouble extends MouseMotionAdapter {
		int row;
		JButton b;

		public TMEListenerDouble(int row, JButton b) {
			this.row = row;
			this.b = b;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			try {
				int size = 15;
				if (b.getHeight() / 2 < size) {
					size = b.getHeight() / 2;
				}
				// Dragg button
				JComponent c = (JComponent) e.getSource();
				c.setLocation(e.getX() + c.getX(), c.getY());
				Teilbar map1 = listOfObject.get(row);
				Teilbar map2 = listOfObject.get(row - 1);
				int i = calcCellnumberByColum(c.getX());
				if (map2.getEnd() < i || map1.getStart() > i - 1) {
					int n = (int) calcColunm(map2.getStart());
					c.setLocation(n - size, c.getY());
					return;
				}
				map2.setStart(i);
				map1.setEnd(i - 1);
				repaint();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		requestFocus();
		// TODO Rechnungen für bessere Perfomenc hier einfügen?
		// TODO Undo und Redo
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	public static interface Teilbar {
		public int getLayerCount();

		public void setEnd(int i);

		public void setStart(int i);

		public String getName();

		public String getLayerName(int n);

		public int getEnd();

		public int getStart();
	}
}
