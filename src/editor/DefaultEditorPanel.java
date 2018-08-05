package editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JColorChooser;
import javax.swing.JDesktopPane;

import toolbars.DialogCAD;
import editor.tabelle.ObjectTableGUI;
import editor.tools.GreifTool;
import interfaces.DestroyAble;
import interfaces.Destroyer;
import interfaces.MatrixListener;
import interfaces.Undo;
import interfaces.updateable;

@SuppressWarnings("serial")
public class DefaultEditorPanel extends javax.swing.JDesktopPane implements
		KeyListener, MouseListener, MouseMotionListener, MouseWheelListener,
		DestroyAble {
	private ArrayList<AbstractTool> passiveTools;
	public Matrix m;
	static MouseEvent e;
	boolean destroy = false;
	long timeSinceLastUpdate;
	int timePassed = 1;
	int framenumber = 0;
	// boolean debugg = true;

	// FPS Messung
	private int frames;
	private int sekundenzähler;
	private int fps;

	public DefaultEditorPanel() {
		setBackground(new Color(0, 0, 0, 0));
		m = new Matrix(this);
		passiveTools = m.passiveTools;
		/** Add Default MainTool */
		// tool = new AddCEntity();
		// tool.init(this);
		/** Add Default PassiveTools */
		passiveTools.add(new GreifTool());
		passiveTools.add(DialogCAD.pan);
		/** All about initial Layer */
		m.addLayer(m.defaultLayer);
		/** All Listeners */
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		this.setFocusable(true);
		requestFocus();
		/** init demo stuff */
		// Removed
		/** repaint Thread */
		new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (true) {
					try {
						if (timeSinceLastUpdate != 0) {
							framenumber++;
							timePassed = (int) (System.currentTimeMillis() - timeSinceLastUpdate);
						}
						timeSinceLastUpdate = System.currentTimeMillis();
						if (m == null)
							return;
						try{
						for (Layer l : m.getLayerList()) {
							if (l.isVisible()) {
								for (CObject c : l) {
									c.update(m, timePassed, framenumber);
								}
							}
						}
						}catch(Exception ex){
							ex.printStackTrace();
							System.out.println("#314159 DefaultEditorPanel meldet " + ex.getMessage());
						}
						if (Matrix.doUpdate) {
							repaint();
						}

						if (m != null && i % 7 == 0 && m.getPanel().isShowing()) {
							// long[] times = new
							// long[Matrix.updateList.size()];
							// long lasttime = System.nanoTime();
							// System.nanoTime();
							for (int n = 0; n != Matrix.updateList.size(); n++) {

								updateable u = Matrix.updateList.get(n);
								u.update(m);
								// times[n] = System.nanoTime()-lasttime;
								// lasttime = System.nanoTime();

								// if(debugg){
								// System.out.println(u.getClass().getName());
								// }
							}
							// for(int n = 0; n!= times.length; n++){
							// System.out.print(times[n] + "\t");
							// }
							// System.out.println();
							// debugg = false;
						}

						i++;
						if (!isShowing() || !Matrix.doUpdate) {
							Thread.sleep(100);
						}
						if (destroy) {
							break;
						}
						Thread.sleep(20);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/** KEYLISTENER */
	public void keyTyped(KeyEvent e) {
		if (getMainTool() != null)
			getMainTool().keyTyped(m, e);
		for (AbstractTool tool : passiveTools) {
			tool.keyTyped(m, e);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (getMainTool() != null)
			getMainTool().keyReleased(m, e);
		for (AbstractTool tool : passiveTools) {
			tool.keyReleased(m, e);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (getMainTool() != null)
			getMainTool().keyPressed(m, e);
		for (AbstractTool tool : passiveTools) {
			tool.keyPressed(m, e);
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			m.setSelectedCObject(null, m.getLayer(0));
			m.multiselection.clear();
			for(MatrixListener list: m.listner){
				list.selectChacheEvent(m);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F9) {
			if (background == Color.BLACK) {
				background = Color.WHITE;
			} else if (background.equals(Color.white)) {
				background = Color.gray;
			} else if (background.equals(Color.gray)) {
				background = Color.LIGHT_GRAY;
			} else if (background.equals(Color.LIGHT_GRAY)) {
				background = Color.cyan;
			} else if (background.equals(Color.cyan)) {
				background = Color.pink;
			} else if (background.equals(Color.pink)) {
				background = Color.black;
			} else {
				background = Color.BLACK;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F10) {
			background = JColorChooser.showDialog(this,
					"Wähle neue Hintergrundfarbe", background);
			if (background == null) {
				background = Color.BLACK;
			}
		}
	}

	/** MOUSELITENER */
	public void mouseReleased(MouseEvent ev) {
		if (getMainTool() != null)
			getMainTool().mouseReleased(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mouseReleased(m, ev);
		}
	}

	public void mousePressed(MouseEvent ev) {
		requestFocus();
		if (getMainTool() != null)
			getMainTool().mousePressed(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mousePressed(m, ev);
		}
	}

	public void mouseExited(MouseEvent ev) {
		if (getMainTool() != null)
			getMainTool().mouseExited(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mouseExited(m, ev);
		}
	}

	public void mouseEntered(MouseEvent ev) {
		e = ev;
		if (getMainTool() != null)
			getMainTool().mouseEntered(m, ev);
		else {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}
		for (AbstractTool tool : passiveTools) {
			tool.mouseEntered(m, ev);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (getMainTool() != null)
			getMainTool().mouseClicked(m, e);
		for (AbstractTool tool : passiveTools) {
			tool.mouseClicked(m, e);
		}
	}

	/** MOUSEMOTIONLISTENER */
	public void mouseMoved(MouseEvent ev) {
		e = ev;
		if (getMainTool() != null)
			getMainTool().mouseMoved(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mouseMoved(m, ev);
		}
		m.updateInfobar(ev);
	}

	public void mouseDragged(MouseEvent ev) {
		if (getMainTool() != null)
			getMainTool().mouseDragged(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mouseDragged(m, ev);
		}
		mouseMoved(ev);
	}

	/** MOUSEWHEELLISTENER */
	@Override
	public void mouseWheelMoved(MouseWheelEvent ev) {
		if (getMainTool() != null)
			getMainTool().mouseWheelMoved(m, ev);
		for (AbstractTool tool : passiveTools) {
			tool.mouseWheelMoved(m, ev);
		}
		m.updateInfobar(ev);
	}

	private Layer oneLayer;
	private boolean doShowFPS = true;
	public boolean isShowFPS() {
		return doShowFPS;
	}

	public void setShowFPS(boolean doShowFPS) {
		this.doShowFPS = doShowFPS;
	}

	private static Color background = Color.BLACK;
	/** DRAW */
	public void paint(Graphics g0) {
		// Graphics stuff
		((Graphics2D)g0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D g1 = (Graphics2D) g0;
		Graphics2D g2 = (Graphics2D) g1.create();
		g1.setColor(background);
		g1.fillRect(0, 0, getWidth(), getHeight());
		g1.scale(m.getScale(), m.getScale());
		g1.translate(-m.getTransX(), -m.getTransY());
		// ROMOVE!?
		g1.setColor(Color.WHITE);
		g1.drawLine(-5, 0, 5, 0);
		g1.drawLine(0, -5, 0, 5);
		// First Loop
		LayerLoop loop = m.getLayers(true);
		CObject o;
		while (loop.hasnext()) {
			oneLayer = loop.next();
			if (oneLayer.isVisible()) {

				g1.setColor(Color.WHITE);
				for (int n = oneLayer.size() - 1; n != -1; n--) {
					o = oneLayer.get(n);
					o.draw1(g1, g2, m,oneLayer, e);
					o.setMatrix(m);
				}
			}
		}
		loop.restart();
		while (loop.hasnext()) {
			oneLayer = loop.next();
			if (oneLayer.isVisible()) {
				g1.setColor(Color.WHITE);
				for (int n = oneLayer.size() - 1; n != -1; n--) {
					o = oneLayer.get(n);
					o.draw3(g1, g2, m, e);
				}
			}
		}
		frames++;
		if(doShowFPS){
		sekundenzähler += timePassed;
		if (sekundenzähler > 1000) {
			fps = frames;
			frames = 0;
			sekundenzähler -= 1000;
		}
		g2.setColor(Color.WHITE);
		
		g2.drawString("FPS: " + fps, 50, 50);
		}
		try {
			// SCHÖNER
			if (m.getSelectedCObject() != null) {
				for (int i = 0; i <m.getSelectedCObject().getGreifItems().size();i+=1 ) {
					m.getSelectedCObject().getGreifItems().get(i).draw(g1, g2, m);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		g1.setColor(Color.white);
		if (getMainTool() != null)
			getMainTool().draw(g1, g2, m, e);
		for(int i = passiveTools.size()-1; i >= 0; i-=1){
			passiveTools.get(i).draw(g1, g2, m, e);
		}
		
		super.paint(g2);
		try{
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, getHeight()-25, 25+(int)(g2.getFontMetrics().getStringBounds(m.label, g2).getWidth()), 50);
			g2.setColor(Color.BLACK);
			g2.drawString(m.label, 10, getHeight()-5);
		}catch(Exception ex){
			
		}
		g2.dispose();
		g1.dispose();
		
	}

	private AbstractTool getMainTool() {
		return m.getMainTool();
	}

	/** Easy Loop class forward or backward */
	public static class LayerLoop {
		private ArrayList<Layer> l;
		private int cont = -1;
		private int jump = 1;

		public LayerLoop(ArrayList<Layer> l, boolean isInverted) {
			this(l);
			if (!isInverted)
				return;
			cont = l.size();
			jump = -1;
		}

		public LayerLoop(ArrayList<Layer> l) {
			this.l = l;
		}

		public Layer next() {
			cont += jump;
			return l.get(cont);
		}

		public void restart() {
			if (jump < 0) {
				cont = l.size();
			} else {
				cont = -1;
			}
		}

		public boolean hasnext() {
			if (jump > 0) {
				if (l.size() - jump <= cont) {
					return false;
				}
				return true;
			} else {
				if (cont + jump < 0) {
					return false;
				}
				return true;
			}
		}
	}

	/** all my nice variables */
	public static class Matrix {
		/** Variable */
		DefaultEditorPanel selbstbezug;
		private double scale = 1;
		private double transX = 0;
		private double transY = 0;
		private static int Rast = 16;
		private static boolean smoveRender = true;
		private String text = "";
		public ArrayList<AbstractTool> passiveTools = new ArrayList<AbstractTool>();
		protected static ArrayList<updateable> updateList = new ArrayList<updateable>();
		protected ArrayList<CObject> multiselection = new ArrayList<CObject>();
		private ArrayList<Layer> layer = new ArrayList<Layer>();
		private AbstractTool mainTool = null;
		private Layer defaultLayer = new Layer("Default", this);
		int objectIndex = 0;
		public static boolean doUpdate = true;
		boolean selectionAllow = true;
		boolean multiSelectionAllow = false;
		public String label;
		private int version = 0;

		public Matrix(DefaultEditorPanel d) {
			selbstbezug = d;
		}
		DecimalFormat df = new DecimalFormat("0.00");//.format(1.199);
		// SCHÖNER
		public void updateInfobar(MouseEvent ev) {
			
			label=getX(ev) + "," + getY(ev) + " | Zoom: " + df.format(getScale())
					 + " | " + text;
		}

		/** variablien getter und setter */
		public double getScale() {
			return scale;
		}

		public void setScale(double d) {
			this.scale = d;
		}

		public double getTransX() {
			return transX;
		}

		public void setTransX(double transX) {
			this.transX = transX;
		}

		public double getTransY() {
			return transY;
		}

		public void setTransY(double transY) {
			this.transY = transY;
		}

		public static int getRast() {
			return Rast;
		}

		public static void setRast(int rast) {
			Rast = rast;
		}

		public static boolean isSmoveRender() {
			return smoveRender;
		}

		public static void setSmoveRender(boolean smoveRender) {
			Matrix.smoveRender = smoveRender;
		}

		public void setInfo(String s) {
			text = s;
		}

		public int getTimePassed() {
			return (int) ((DefaultEditorPanel) getPanel()).timePassed;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		/** math getter */
		public static double rastern(double x) {
			if(!GreifTool.PUNKTFANG_RASTER){
				return x;
			}
			return (x + Rast / 2) / Rast * Rast;
		}

		private double getXTrue(MouseEvent ev) {
			if (ev == null) {
				ev = e;
			}
			return  ((ev.getX()) / scale + transX);
		}

		private double getYTrue(MouseEvent ev) {
			if (ev == null) {
				ev = e;
			}
			return ((ev.getY()) / scale + transY);
		}

		public int getRealX(double x) {
			return (int)((x - transX) * scale);
		}

		public int getRealY(double y) {
			return (int) ((y - transY) * scale);
		}

		public double getX(MouseEvent e) {
			if(GreifTool.PUNKTFANG_RASTER && GreifTool.PUNKTFANG){
				return rastern(getXTrue(e));
			}
			return getXTrue(e);
		}

		public double getY(MouseEvent e) {
			if(GreifTool.PUNKTFANG_RASTER && GreifTool.PUNKTFANG){
				return rastern(getYTrue(e));
			}
			return getYTrue(e);
		}

		/** Tool stuff */
		public AbstractTool getMainTool() {
			return mainTool;
		}

		public void setMainTool(AbstractTool mainTool) {
			if (mainTool != this.mainTool && this.mainTool != null) {
				this.mainTool.lost(this, e);
			}
			this.mainTool = mainTool;
		}

		/** Layer stuff */

		public LayerLoop getLayers(boolean inverted) {
			return new LayerLoop(layer, inverted);
		}

		public Collection<Layer> getLayerList() {
			return layer;
		}
		public void addLayer(Layer l) {
			layer.add(l);
			l.setMatrix(this);
			for(MatrixListener list: listner){
				list.addToMatrixEvent(this, l);
			}
		}

		public void addLayer(int i, Layer l) {
			layer.add(i, l);
			l.setMatrix(this);
			for(MatrixListener list: listner){
				list.addToMatrixEvent(this, l);
			}
		}

		public void removeLayer(Layer l) {
			layer.remove(l);
			for(MatrixListener list: listner){
				list.removedToMatrixEvent(this, l);
			}
		}

		public void removeLayer(int i) {
			for(MatrixListener list: listner){
				list.removedToMatrixEvent(this, layer.get(i));
			}
			layer.remove(i);
		}

		public Layer getLayer(int i) {
			if (layer == null) {
				return null;
			}
			if(i >= getLayerList().size()){
				return null;
			}
			return layer.get(i);
		}

		public Layer getLayer(String s) {
			for (Layer l : layer) {
				if (l.getName().equals(s)) {
					return l;
				}
			}
			for (Layer l : layer) {
				if (l.getName().contains(s)) {
					return l;
				}
			}
			return null;
		}

		public boolean containsLayer(Layer l) {
			return layer.contains(l);
		}

		public int getIndexOfLayer(Layer l) {
			return layer.indexOf(l);
		}

		public int getLayerSize() {
			return layer.size();
		}

		/** Add CObject */
		public void addCObject(int i, CObject c) {
			addCObject(i, c, true);
		}

		public void addCObject(int i, CObject c, boolean b) {
			// c.setMatrix(this);
			if (defaultLayer == null) {
				defaultLayer = layer.get(0);
			}
			defaultLayer.add(i, c, b);
		}

		public void addCObject(CObject c) {
			// c.setMatrix(this);
			if (defaultLayer == null) {
				defaultLayer = getLayer(0);
			}
			defaultLayer.add(0,c);
		}

		/** return current ObjectList multiselection */
		public ArrayList<CObject> getSelectedList() {
			for(MatrixListener list: listner){
				list.selectChacheEvent(this);
			}
			return multiselection;
		}

		/** add current ObjectList multiselection */
		public void addSelectedCObject(CObject c) {
			if (c != null && !multiselection.contains(c)) {
				if(!multiSelectionAllow){
					multiselection.clear();
				}
				multiselection.add(c);
			}
			for(MatrixListener list: listner){
				list.selectChacheEvent(this);
			}
		}

		/** add current ObjectList multiselection */
		public void removeSelectedCObject(CObject c) {
			if (c != null) {
				multiselection.remove(c);
			}
			for(MatrixListener list: listner){
				list.selectChacheEvent(this);
			}
		}

		/** return current Object */
		public CObject getSelectedCObject() {
			if (defaultLayer == null || defaultLayer.size() <= objectIndex
					|| objectIndex == -1 || defaultLayer == null) {
				return null;
			}
			return defaultLayer.get(objectIndex);
		}

		public Layer getSelectedLayer() {
			return defaultLayer;
		};

		public int getSelectedLayerIndex() {
			if (defaultLayer == null) {
				return -1;
			}
			return layer.indexOf(defaultLayer);
		}

		public int getSelectedCObjectIndex() {
			return objectIndex;
		}

		private Rectangle visibleRechtangel = new Rectangle();

		/** SichtbarkeitsRechteck */
		public Rectangle getVisibleRectangle() {
			visibleRechtangel.setBounds((int)getTransX(),(int) getTransY(),
					(int) (getPanel().getWidth() / getScale()),
					(int) (getPanel().getHeight() / getScale()));
			return visibleRechtangel;
		}

		/** set current Object */
		public void setSelectedCObject(CObject c, Layer l) {
			ObjectTableGUI.THAT.update(c);
			defaultLayer = l;
			if (l == null || !l.contains(c)) {
				objectIndex = -1;
			} else if (layer.contains(l)) {
				objectIndex = l.indexOf(c);
			} else {
				objectIndex = -1;
				System.out.println("Dieser Layer ist nicht auffindbar");
			}
		}
		public void setLayer(String text) {
			defaultLayer = getLayer(text);
			objectIndex = -1;
		}
		public void setSelectedLayer(int index) {
			defaultLayer = getLayer(index);
			objectIndex = -1;
		}
		/** Remove */
		public void remove() {
			if (GreifTool.item == null || GreifTool.item.remove()) {
				if (multiselection.size() == 0) {
					getSelectedLayer().remove(objectIndex);
				} else {
					for (CObject c : multiselection) {
						c.getLayer().remove(c);
					}
					multiselection.clear();
				}
				setSelectedCObject(null, getSelectedLayer());
			}
			for(MatrixListener list: listner){
				list.selectChacheEvent(this);
			}
		}

		/** Undo */
		public static int undoID = 0;
		LinkedList<Undo> redos = new LinkedList<>();
		LinkedList<Undo> undos = new LinkedList<>();

		public Undo getFirstUndo() {
			if (undos.size() == 0) {
				return null;
			}
			return undos.get(0);
		}

		public void addUndo(Undo undo) {
			if (undos.size() != 0 && undos.get(0).getID() >= 0
					&& undos.get(0).getID() == undo.getID()
					&& undos.get(0).getObject().equals(undo.getObject())) {
				undos.set(0, undo);
			} else {
				undos.addFirst(undo);
				redos.clear();
			}
		}

		public void exeRedo() {
			Undo r = redos.pollFirst();
			if (r == null) {
				return;
			}
			r.doRedo();
			undos.addFirst(r);
		}

		public void exeUndo() {
			Undo r = undos.pollFirst();
			if (r == null) {
				return;
			}
			r.doUndo();
			redos.addFirst(r);
		}

		/** Panelbezug */
		public JDesktopPane getPanel() {
			return selbstbezug;
		}

		public static void addUpdate(int arg0, updateable arg1) {
			updateList.add(arg0, arg1);
		}

		public static boolean addUpdate(updateable arg0) {
			return updateList.add(arg0);
		}

		public static updateable getUpdate(int arg0) {
			try {
				return updateList.get(arg0);
			} catch (Exception e) {
				return null;
			}
		}

		public static updateable removeUpdate(int arg0) {
			return updateList.remove(arg0);
		}

		public static boolean removeUpdate(Object arg0) {
			return updateList.remove(arg0);
		}

		public boolean isSelectionAllow() {
			return selectionAllow;
		}

		public void setSelectionAllow(boolean allow) {
			selectionAllow = allow;
		}
		public boolean isMultiSelectionAllow() {
			return multiSelectionAllow;
		}

		public void setMultiSelectionAllow(boolean allow) {
			multiSelectionAllow = allow;
		}
		Vector<MatrixListener> listner = new Vector<>();
		public void addMatrixListener(MatrixListener matrixListener) {
			listner.add(matrixListener);
		}
		public void removeMatrixListener(MatrixListener matrixListener) {
			listner.remove(matrixListener);
		}

		
	}

//	@Override
//	protected void finalize() throws Throwable {
//		System.out.println("Bereinigt " + this.getClass().getName());
//		super.finalize();
//	}

	@Override
	public void destroy() {
		ObjectTableGUI.THAT.update(null);
		destroy = true;
		m.selbstbezug = null;
		Destroyer.destroyList(m.layer);
		m.layer = null;
		Destroyer.destroyList(passiveTools);
		passiveTools = null;
		Destroyer.destroyObject(m.mainTool);
		m.mainTool = null;
		Destroyer.destroyList(m.defaultLayer);
		m.defaultLayer = null;
		oneLayer = null;
		passiveTools = null;
		m.label = null;
		m = null;
	}
}
