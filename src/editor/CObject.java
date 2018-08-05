package editor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import basic.CascadingStyleSheet;
import basic.DDrawer;
import basic.DPoint;
import editor.DefaultEditorPanel.Matrix;
import editor.tabelle.ListLine;
import editor.tabelle.NumberLine;
import editor.tabelle.OneLine;
import editor.tools.GreifTool;
import interfaces.DestroyAble;
import interfaces.Destroyer;
import interfaces.HasName;
import interfaces.NameChangeListener;
import interfaces.UserZugang;

public abstract class CObject implements DestroyAble, HasName, UserZugang,Cloneable {
	Matrix m; 
	
	protected CascadingStyleSheet css = new CascadingStyleSheet(this);
	

	OneLine<String> lineName = new OneLine<String>("Name", "") {
		@Override
		public void valueChanges(String value) {
			setName(value);
		}
	};
	protected NumberLine<Double> lineX = new NumberLine<Double>("X", 0d) {
		@Override
		public void valueChanges(Double value) {
			setX(value);
		}
	};
	protected NumberLine<Double> lineY = new NumberLine<Double>("Y", 0d) {
		@Override
		public void valueChanges(Double value) {
			setY(value);
		}
	};
	ListLine<Layer> lineLayer = new ListLine<Layer>("Layer", null, null) {
		public Layer getValue() {
			return getLayer();
		};

//		@Override
//		public Collection<?> getList() {
//			return GUI_Layerbefehle.getMatrix().getLayerList();
//		}

		@Override
		public void valueChanges(Layer value) {
			if (value == null) {
				return;
			}
			setLayer(value);
		}
	};
	protected VerschiebeGriff verschiebeGriff =  new VerschiebeGriff();
	public CObject() {
		addLine(lineName);
		addLine(lineX);
		addLine(lineY);
		addLine(lineLayer);
		griffe.add(verschiebeGriff);

	}

	public abstract String getXMLName();
	protected ArrayList<GreifItem> griffe = new ArrayList<GreifItem>();
	protected static Color paintColor = new Color(0, 0, 255, 100);
	protected static Color fillColor = new Color(100, 100, 100, 100);

	public static Color verschiebeGriffColor = new Color(0, 0, 255);
	public static Color transformGriffColor = new Color(255, 0, 0);

	public void destroyMain() {
		Destroyer.destroy(griffe);
		griffe.clear();
		line.clear();
	}

//	public static ArrayList<Layer> getLayerList() {
//		return GUI_Layerbefehle.getMatrix().getLayerList();
//	}

	public abstract void draw1(Graphics2D g, Graphics2D g2, Matrix m,
			Layer layer, MouseEvent ev);

	public abstract void draw3(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev);

	public abstract void update(Matrix m, int timePassed, int conter);

	// FIXME made contains and intersects
	public abstract Shape getShape();
	
	public abstract double getNearestPointFang(DPoint nextPoint, double distance,double x, double y, GreifItem item);

	public ArrayList<GreifItem> getGreifItems() {
		return griffe;
	}

	public Layer getLayer() {
		for (Layer l : m.getLayerList()) {
			if (l.contains(this)) {
				return l;
			}
		}
		return null;
	}

	public Matrix getMatrix() {
		return m;
	}

	public double getX() {
		return lineX.getValue();
	};

	public double getY() {
		return lineY.getValue();
	}

	public void init(double x, double y) {
		setX(x);
		setY(y);
	}

	public void setLayer(Layer l) {
		getMatrix().addUndo(
				new Undo(Undo.VERSCHIEBENLAYER, getLayer(), l, this));
		getLayer().remove(this, false);
		l.add(CObject.this);
	}

	public void setMatrix(Matrix m) {
		this.m = m;
	}

	public final void setName(String s) {
		System.err.println("In Script wird noch nichts Umbenannt!");
		//TODO
		setName(s, true);
	}

	public final void setName(String s, boolean redo) {
		if (s != null && !s.isEmpty()) {
			s = s.replace(" ", "_");
			s = s.replace(".", "_");
		}
		if (getMatrix() == null) {
			for(NameChangeListener list : nameChangeListeners){
				list.nameChance(this.lineName.getValue(), s);
			}
			lineName.setValue(s);
			
			return;
		}
		String nameAlt = lineName.getValue();
		String name = GUI_Layerbefehle.getUniqueLayerName(s,
				lineName.getValue(), m);
		for(NameChangeListener list : nameChangeListeners){
			list.nameChance(this.lineName.getValue(), s);
		}
		lineName.setValue(name);
		if (name != null && name.equals(s)) {
			return;
		}
		if (redo) {
			getMatrix().addUndo(
					new Undo(Undo.NAME, nameAlt, lineName.getValue(), this));
		}
	}
	
	Vector<NameChangeListener> nameChangeListeners = new Vector<>();
	@Override
	public void addNameChangeListener(NameChangeListener list) {
		nameChangeListeners.add(list);
	}

	@Override
	public void removeNameChangeListener(NameChangeListener list) {
		nameChangeListeners.remove(list);
	}
	
	
	public HasName getLayerOrObject(String tupel) {
		if (m == null) {
			return null;
		}
		for (Layer l : m.getLayerList()) {
			if (l.getName().equals(tupel)) {
				return l;
			}
			for (CObject c : l) {
				if (c.getName().equals(tupel)) {
					return c;
				}
			}
		}
		return null;

	}

	@Override
	public final String getName() {
		return lineName.getValue();
	}
	public final CascadingStyleSheet getCSS(){
		return css;
	}
	public void setCSS(CascadingStyleSheet css) {
		this.css = css;
	}
	public void setX(double value) {
		setX(value, true);
	}

	public void setX(double value, boolean undo) {
		value = DDrawer.round(value);
		if (undo && getMatrix() != null) {
			getMatrix().addUndo(
					new Undo(Undo.VERSCHIEBEN, new DPoint(getX(), getY()),
							new DPoint(value, getY()), this));
		}
		lineX.setValue(value);
	}

	public void setY(double y) {
		setY(y, true);
	}

	public void setY(double y, boolean undo) {
		y = DDrawer.round(y);
		if (undo && getMatrix() != null) {
			getMatrix().addUndo(
					new Undo(Undo.VERSCHIEBEN, new DPoint(getX(), getY()),
							new DPoint(getX(), y), this));
		}
		lineY.setValue(y);
	}

	private ArrayList<OneLine<?>> line = new ArrayList<>();

	public void addLine(OneLine<?> l) {
		line.add(l);
	}

	public void addLine(int i, OneLine<?> l) {
		line.add(i, l);
	}

	
	protected static JPopupMenu getMenu() {
		return menu;
	}

	

	public int indexOfLine(OneLine<?> l) {
		return line.indexOf(l);
	}

	public void removeLine(OneLine<?> l) {
		line.remove(l);
	}

	@Override
	public ArrayList<OneLine<?>> getLines() {
		return line;

	}

	public double getWidth() {
		if(getShape() != null){
			return getShape().getBounds().getWidth();
		}
		return 0;
	}

	public double getHeight() {
		return getShape().getBounds().getHeight();
	}

	@Override
	public void destroy() {
		css = null;
	}
	@Override
	public String toString() {
		String s = getName();
		if(s == null || s == ""){
			return "[unnamed]";
		}
		return s;
	}

	/** interne Klassen */
	private static class Undo extends interfaces.UndoObject {
		CObject obj;
		public static final int NAME = Matrix.undoID++;
		public static final int VERSCHIEBEN = Matrix.undoID++;
		public static final int VERSCHIEBENLAYER = Matrix.undoID++;

		public Undo(int id, Object old, Object neu, CObject obj) {
			super(id, old, neu, obj);
			this.obj = obj;
		}

		@Override
		public void doRedo() {
			if (getID() == NAME) {
				obj.setName((String) neu, false);
			} else if (getID() == VERSCHIEBEN) {
				obj.setX(((Point) neu).x, false);
				obj.setY(((Point) neu).y, false);
			} else if (getID() == VERSCHIEBENLAYER) {
				((Layer) old).remove(obj, false);
				((Layer) neu).add(obj, false);
			}
		}

		@Override
		public void doUndo() {
			if (getID() == NAME) {
				obj.setName((String) old, false);
			} else if (getID() == VERSCHIEBEN) {
				obj.setX(((Point) old).x, false);
				obj.setY(((Point) old).y, false);
			} else if (getID() == VERSCHIEBENLAYER) {
				((Layer) neu).remove(obj, false);
				((Layer) old).add(obj, false);
			}
		}

		@Override
		public String getName() {
			if (getID() == NAME) {
				return "Umbenennen Object";
			} else if (getID() == VERSCHIEBEN) {
				return "Verschieben";
			} else if (getID() == VERSCHIEBENLAYER) {
				return "Nach Layer Verschieben";
			}
			return null;
		}

	}// end class redo
	private static JPopupMenu menu;
	static{
		menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("klonen");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CObject c = GUI_Layerbefehle.getMatrix().getSelectedCObject();
				if(c != null){
					try {
						CObject c1 = (CObject) c.clone1();
						GUI_Layerbefehle.getMatrix().addCObject(c1);
						GreifTool.item = c1.griffe.get(0);
					} catch (CloneNotSupportedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		getMenu().add(item);
	}
	protected String script;
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	public class VerschiebeGriff extends GreifItem {
		Rectangle r = new Rectangle();

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			// if(i == 0){i = 1;}
			r.setBounds((int)m.getRealX(getX()) - i / 2,(int)m.getRealY(getY()) - i / 2,i, i);
			g2.setColor(transformGriffColor);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		@Override
		public Rectangle getBounds() {
			return r;
		}

		public int getID() {
			return 0;
		}

		public double getX() {
			int verschieben = 0;
			if (GreifTool.item == null || !GreifTool.item.equals(this)) {
				verschieben = 0;
				for (int n = 0; n != getGreifItems().size(); n++) {
					GreifItem i = getGreifItems().get(n);
					try{
					if (!i.equals(this)) {

						if (CObject.this.getX() - 10 + verschieben < i.getX()
								&& i.getX() < CObject.this.getX() + 10
										+ verschieben
								&& CObject.this.getY() - 10 < i.getY()
								&& i.getY() < CObject.this.getY() + 10) {
							verschieben += 12;
							n = 0;
						}
					}
					}catch(NullPointerException ex){
						System.out.println(i);
						ex.printStackTrace();
					}
				}
			}
			return CObject.this.getX() + verschieben;
		}

		public double getY() {
			return CObject.this.getY();
		}

		@Override
		public void setLocation(double x, double y) {
			CObject.this.setX(x);
			CObject.this.setY(y);
		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return getMenu();
		}
	}// Ende class VerschiebeGriff
	
}// ende class
