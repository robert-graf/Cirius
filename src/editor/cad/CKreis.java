package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import basic.DPoint;
import database.XMLCObject;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;
import editor.GreifItem;
import editor.rpg.CSprite;
import editor.tabelle.NumberLine;
import editor.tools.GreifTool;

public class CKreis extends CShape {

	Ellipse2D r = new Ellipse2D.Float();
	Point a = new Point();
	NumberLine<Double> radius = new NumberLine<Double>("Radius", 0d) {
		@Override
		public void valueChanges(Double value) {
			setRadius(value);
		}
	};

	public CKreis(double x, double y, double radius) {
		setRadius(radius);
		setX(x);
		setY(y);
		setName("Kreis");
		getGreifItems().add(new KreisGriff(1, 0));
		getGreifItems().add(new KreisGriff(-1, 0));
		getGreifItems().add(new KreisGriff(0, -1));
		getGreifItems().add(new KreisGriff(0, 1));
		addLine(this.radius);
		addLine(new NumberLine<Double>("Durchmesser", 0d) {
			@Override
			public void valueChanges(Double value) {
				setDurchmesser(value);
			}

			@Override
			public Double getValue() {
				return getDurchmesser();
			}
		});
	}

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		a.setLocation(getX(), getY());
		r.setFrame(getX() - getRadius(), getY() - getRadius(), getRadius() * 2,
				getRadius() * 2);
	}

	@Override
	public Ellipse2D getShape() {
		return r;
	}

	@Override
	public void destroy() {
		a = null;
		r = null;
	}

	@Override
	public String getXMLName() {
		return XMLCObject.circle;
	}

	public double getRadius() {
		return radius.getValue();
	}

	public void setRadius(double radius) {
		this.radius.setValue(radius);
	}

	public void setDurchmesser(double durchmesser) {
		setRadius(durchmesser / 2);
	}

	public double getDurchmesser() {
		return getRadius() * 2;
	}

	// Interneclasse---------------------------------------------------------------
	class KreisGriff extends GreifItem {
		int i, j;

		public KreisGriff(int i, int j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public void setLocation(double x, double y) {
			this.y = y;
			this.x = x;
			// set Radius;
			// r = wurzel(x*x + y*y)
			double x1 = Math.abs(CKreis.this.getX() - x);
			double y1 = Math.abs(CKreis.this.getY() - y);
			setRadius((int) Math.sqrt(x1 * x1 + y1 * y1));
		}

		double y, x;

		public double getY() {
			return CKreis.this.getY() + (getRadius() * j);
		}

		public double getX() {
			return CKreis.this.getX() + (getRadius() * i);
		}

		public int getID() {
			return 0;
		}

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			r.setFrame(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2,
					i, i);
			g2.setColor(CSprite.verschiebeGriffColor);
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
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return menu;
		}
	}
	static JPopupMenu menu;
	static{
		menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Zu Path");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CObject c = GUI_Layerbefehle.getMatrix().getSelectedCObject();
				if(c instanceof CKreis){
					CPath p = new CPath((CKreis) c);
					GUI_Layerbefehle.getMatrix().addCObject(p);
				}
			}
		});
		menu.add(item);
	}
	@Override
	public Class<?> getGameClass() {
		return de.game.objects.geom.CKreis.class;
	}

	@Override
	public CKreis clone1() throws CloneNotSupportedException {
		CKreis c = new CKreis(getX(), getY(), getRadius());
		c.setCSS(this.getCSS().clone1(this));
		return c;
	}
	private DPoint calc = new DPoint();
	@Override
	public double getNearestPointFang(DPoint Overwrite, double distance, double x, double y, GreifItem withOut) {
		if(verschiebeGriff.equals(withOut))return distance;
//		public static boolean PUNKTFANG_ECKE = true;
		if(GreifTool.PUNKTFANG_ECKE){
			calc.setLocation(getX()+getRadius(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()-getRadius(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()+getRadius());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()-getRadius());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
//		public static boolean PUNKTFANG_ZENTRUM = false;
		if(GreifTool.PUNKTFANG_ZENTRUM){
			calc.setLocation(getX(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
		return distance;
	}
	
}