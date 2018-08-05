package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import basic.DPoint;
import database.XMLCObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.rpg.CSprite;
import editor.tabelle.NumberLine;
import editor.tabelle.OneLine;
import editor.tools.GreifTool;
import editor.tools.PopupShowOnHover;

public class CPoly extends CShape {
	ArrayList<PrivatePoint> polygon = new ArrayList<>();
	ArrayList<PolygonPoint> ppunkte = new ArrayList<>();
	NumberLine<Integer> selectedPoint;
	OneLine<Boolean> evenOdd = new OneLine<Boolean>("WindingRule", true){
		@Override
		public void valueChanges(Boolean value) {
			setEvenOdd(value);
		}
	};
	OneLine<Boolean> close = new OneLine<Boolean>("Geschlossen", true){
		@Override
		public void valueChanges(Boolean value) {
			setClose(value);
		}
	};
	JPopupMenu menu = new JPopupMenu();
	JMenuItem menuitem = new JMenuItem("Entfernen");
	
	public CPoly(DPoint p1, DPoint p2,boolean close) {
		init(p1.x, p1.y);
		addPoint(0, 0, 0);
		setName("Polylinie", false);
		addPoint(p2.x - getX(), p2.y - getY());
		setClose(close);
		init();
	}
	public CPoly(int x, int y, String polygonList, boolean close) {
		init(x, y);
		String[] s = null;
		if (polygonList.contains(";")) {
			s = polygonList.replace(",", ";").split(";");
		} else {
			s = polygonList.replace(",", " ").split(" ");
		}
		for (int i = 0; i < s.length; i+=2) {
			int x1 = 0;
			int y1 = 0;
			try {
				x1 = Integer.parseInt(s[i]);
				y1 = Integer.parseInt(s[i+1]);
			} catch (Exception e) {
				//y1 == null --> 0
			}
			addPoint(x1, y1);
		}
		setClose(close);
		init();
	}

	public CPoly(String list, boolean close) {
		PolygonByList(list);
		setClose(close);
		init();
	}

	public CPoly(int x1, int y1, int x2, int y2) {
		this(new DPoint(x1, y1), new DPoint(x2, y2),true);
	}

	private void init() {
		menu.add(menuitem);
		menuitem.addMouseListener(PopupShowOnHover.That);
		menuitem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removePoint(selectedPoint.getValue());
			}
		});
		selectedPoint = new NumberLine<Integer>("Aktueller Punkt", 0, 0, 1, 1) {
			@Override
			public void valueChanges(Integer value) {
				setSelectedPoint(value);
			}
		};
		addLine(selectedPoint);
		addLine(new OneLine<Double>("X", null) {
			@Override
			public Double getValue() {
				return getCurrendPointX();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointX(value);
			}
		});
		addLine(new OneLine<Double>("Y", null) {
			@Override
			public Double getValue() {
				return getCurrendPointY();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointY((Double) value);
			}
		});
		addLine(new OneLine<Double>("X Koordinate", null) {
			@Override
			public Double getValue() {
				return getCurrendPointXKordinate();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointXKordinate(value);
			}
		});
		addLine(new OneLine<Double>("Y Koordinate", null) {
			@Override
			public Double getValue() {
				return getCurrendPointYKordinate();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointYKordinate(value);
			}
		});
		addLine(evenOdd);
		addLine(close);
	}

	public PrivatePoint addPoint(double i, double j) {
		return addPoint(i, j, polygon.size());
	}

	private PrivatePoint addPoint(double x, double y, int id) {
		PrivatePoint point = new PrivatePoint(Matrix.rastern(x), Matrix.rastern(y));
		polygon.add(id,point);
		PolygonPoint p = new PolygonPoint(id);
		ppunkte.add(id, p);
		getGreifItems().add(p);
		getGreifItems().add(1, new AddPoint(polygon.size() - 1));
		int cont = 0;
		for (PolygonPoint pp : ppunkte) {
			pp.id = cont++;
		}
		return point;
	}

	public void removePoint() {
		removePoint(polygon.size() - 1);
		setSelectedPoint(0);
	}

	public void removePoint(int id) {
		polygon.remove(id);
		getGreifItems().remove(ppunkte.get(id));
		ppunkte.remove(id);
		getGreifItems().remove(0);
		int cont = 0;
		for (PolygonPoint pp : ppunkte) {
			pp.id = cont++;
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getXMLName() {
		if (polygon.size() == 2) {
			return XMLCObject.line;
		} else if (isClose()) {
			return XMLCObject.polygon;
		} else {
			return XMLCObject.polyline;
		}
	}

	GeneralPath path = new GeneralPath();

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		selectedPoint.setMaximum(polygon.size() - 1);
		path.reset();
		if(isEvenodd()){
			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
		}else{
			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
		}
		boolean first = true;
		for (PrivatePoint p : polygon) {
			if (first) {
				path.moveTo(p.getX() + getX(), p.getY() + getY());
				first = false;
			} else {
				path.lineTo(p.getX() + getX(), p.getY() + getY());
			}
		}
		if (isClose()) {
			path.closePath();
		}
	}

	@Override
	public Shape getShape() {
		return path;
	}

	public int getSelectedPoint() {
		int i = selectedPoint.getValue();
		if (i == -1 || i == polygon.size()) {
			return 0;
		}
		return selectedPoint.getValue();
	}

	public void setSelectedPoint(int selectedPoint) {
		this.selectedPoint.setValue(selectedPoint);
	}

	public double getCurrendPointX() {
		return polygon.get(getSelectedPoint()).getX();
	}

	public double getCurrendPointY() {
		return polygon.get(getSelectedPoint()).getY();
	}

	public void setCurrendPointX(double x) {
		polygon.get(getSelectedPoint()).setX(x);
	}

	public void setCurrendPointY(double y) {
		polygon.get(getSelectedPoint()).setY(y);
	}

	public double getCurrendPointXKordinate() {
		return polygon.get(getSelectedPoint()).getX()+ getX();
	}

	public double getCurrendPointYKordinate() {
		return polygon.get(getSelectedPoint()).getY() + getY();
	}

	public void setCurrendPointXKordinate(double x) {
		polygon.get(getSelectedPoint()).setX(x - getX());
	}

	public void setCurrendPointYKordinate(double y) {
		polygon.get(getSelectedPoint()).setY(y - getY());
	}
	private DPoint calc = new DPoint();
	
	@Override
	public double getNearestPointFang(DPoint Overwrite, double distance, double x, double y,
			GreifItem withOut) {
//		public static boolean PUNKTFANG_ECKE = true;
		int Xid = -1;
		if(withOut instanceof PolygonPoint){
			Xid = withOut.getID();
		}
		if(GreifTool.PUNKTFANG_ECKE){
			int i = 0;
			for(PrivatePoint p : polygon){
				i+=1;
				if(Xid == i-1){
					continue;
				}
				calc.setLocation(p.getX()+getX(),p.getY()+getY());
				distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			}
		}
//		public static boolean PUNKTFANG_MITTE = true;
		if (GreifTool.PUNKTFANG_MITTE) {
			for (int id = 0; id != polygon.size(); id += 1) {
				calc.setLocation(
						CPoly.this.getX()
								+ (polygon.get(id).getX() + polygon.get(
										id == 0 ? polygon.size() - 1 : id - 1)
										.getX()) / 2,
						CPoly.this.getY()
								+ (polygon.get(id).getY() + polygon.get(
										id == 0 ? polygon.size() - 1 : id - 1)
										.getY()) / 2);
				distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			}

		}
//		public static boolean PUNKTFANG_ZENTRUM = false;
		if(GreifTool.PUNKTFANG_ZENTRUM){
			int mx = 0,my = 0;
			for(PrivatePoint p : polygon){
				mx+= p.getX();
				my+= p.getY();
			}
			mx /= polygon.size();
			my /= polygon.size();
			calc.setLocation(mx,my);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
		return distance;
	}
	/** Interne Classen */
	class PolygonPoint extends GreifItem {
		int id;

		public PolygonPoint(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public double getX() {
			return polygon.get(id).getX() + CPoly.this.getX();
		}

		public double getY() {
			return polygon.get(id).getY() + CPoly.this.getY();
		}

		public void setLocation(double x, double y) {
			try {
				// X
				polygon.get(id).setX(Matrix.rastern(x - CPoly.this.getX()));
				// Y
				polygon.get(id).setY(Matrix.rastern(y - CPoly.this.getY()));
				setSelectedPoint(id);
			} catch (Exception e) {
				GreifTool.item = null;
				System.err.println("entfernt CPoly error");

			}
		}

		Color ausgewählt = new Color(255, 255, 0);

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			r.setBounds(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2,
					i, i);
			if (getID() != getSelectedPoint()) {
				g2.setColor(CSprite.verschiebeGriffColor);
			} else {
				g2.setColor(ausgewählt);
			}
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
			removePoint(getID());
			GreifTool.item = null;
			return false;
		}

		@Override
		public JPopupMenu showContext() {
			setSelectedPoint(id);
			return menu;
		}

	}

	class AddPoint extends GreifItem {
		int id;
		final Color ColorGriff = new Color(0, 255, 0);

		public AddPoint(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public double getX() {
			return CPoly.this.getX()
					+ (polygon.get(id).getX() + polygon
							.get(id == 0 ? polygon.size() - 1 : id - 1).getX()) / 2;
		}

		public double getY() {
			return CPoly.this.getY()
					+ (polygon.get(id).getY() + polygon
							.get(id == 0 ? polygon.size() - 1 : id - 1).getY()) / 2;
		}

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			if(getID() == polygon.size()){
				getGreifItems().remove(this); return;
			}
			g.setColor(Color.WHITE);
			int i = 10;
			r.setBounds(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2,
					i, i);
			g2.setColor(ColorGriff);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		public void setLocation(double x, double y) {
			addPoint(getX() - CPoly.this.getX(), getY() - CPoly.this.getY(),id);
			GreifTool.item = ppunkte.get(id);
		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	public class PrivatePoint{
		private double x1;
		private double y1;
		public double getX() {
			return x1;
		}

		public double getY() {
				return y1;
		}
		public void setX(double x) {
			this.x1 = x;
		}

		public void setY(double y) {
			this.y1 = y;
		}
		public PrivatePoint(double x, double y) {
			this.x1 = x;
			this.y1 = y;
		}
		@Override
		public String toString() {
			return x1 + "\t" + y1 + "\t";
		}
	}
	public String getPointListString() {
		String s = "";
		for (PrivatePoint p : polygon) {
			s += (p.getX()+getX()) + "," + (p.getY()+getY()) + " ";
		}
		return s;
	}

	private void PolygonByList(String list) {

		String[] s = list.split(" ");
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int[] x = new int[s.length];
		int[] y = new int[s.length];

		for (int i = 0; i < s.length; i++) {
			String[] point = s[i].split(",");
			x[i] = Integer.parseInt(point[0]);
			y[i] = Integer.parseInt(point[1]);
			if (x[i] < minX) {
				minX = x[i];
			}
			if (y[i] < minY) {
				minY = y[i];
			}
		}
		init(minX, minY);
		for (int i = 0; i < x.length; i++) {
			addPoint(x[i] - minX, y[i] - minY);
		}
	}

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.geom.CPoly.class;
	}

	@Override
	public CPoly clone1() throws CloneNotSupportedException {
		CPoly c = new CPoly(getPointListString(), isClose());
		c.setCSS(this.getCSS().clone1(this));
		return c;
	}

	public boolean isClose() {
		return close.getValue();
	}

	public void setClose(boolean close) {
		this.close.setValue(close);
	}

	public boolean isEvenodd() {
		return evenOdd.getValue();
	}
	public void setEvenOdd(boolean evenOdd) {
		this.evenOdd.setValue(evenOdd);
	}
	
}