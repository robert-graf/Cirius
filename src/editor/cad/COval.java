package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPopupMenu;

import basic.DPoint;
import database.XMLCObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.rpg.CSprite;
import editor.tabelle.NumberLine;
import editor.tools.GreifTool;

public class COval extends CShape {

	public NumberLine<Double> radius1, radius2;
	Ellipse2D r = new Ellipse2D.Float();
	

	public COval(double x, double y, double radius1, double radius2) {
		setX(x);
		setY(y);
		setName("Oval", false);
		getGreifItems().add(new KreisGriff(1, 1));
		getGreifItems().add(new KreisGriff(1, 0));
		getGreifItems().add(new KreisGriff(-1, 0));
		getGreifItems().add(new KreisGriff(0, 1));
		getGreifItems().add(new KreisGriff(0, -1));
		this.radius1 = new NumberLine<Double>("Radius —", radius1) {
			@Override
			public void valueChanges(Double value) {
				setRadius1(value);
			}
		};
		this.radius2 = new NumberLine<Double>("Radius |", radius2) {
			@Override
			public void valueChanges(Double value) {
				setRadius2(value);
			}
		};
		
		setRadius1(radius1);
		setRadius2(radius2);

		addLine(this.radius1);
		addLine(this.radius2);
		
	}

	
	@Override
	public void update(Matrix m, int timePassed, int conter) {
		r.setFrame(getX() - getRadius1(), getY() - getRadius2(),
				getRadius1() * 2, getRadius2() * 2);
	}

	@Override
	public Ellipse2D getShape() {
		return r;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getXMLName() {
		return XMLCObject.ellipse;
	}

	public double getRadius1() {
		return radius1.getValue();
	}

	public double getRadius2() {
		return radius2.getValue();
	}
	
	public void setRadius1(double radius1) {
		if (radius1 == 0) {
			radius1 = 1d;
		}
		this.radius1.setValue(Math.abs(radius1));
	}
	

	public void setRadius2(double radius2) {
		if (radius2 == 0) {
			radius2 = 1d;
		}
		this.radius2.setValue(Math.abs(radius2));
	}
	Color verschiebeGriffColor2 = CSprite.verschiebeGriffColor.brighter();
	// Interneclassen-----------------------------------
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
			// set radien
			// r = wurzel(x*x + y*y)
			if (i != 0) {
				setRadius1(COval.this.getX() - x);
			}
			if (j != 0) {
				setRadius2(COval.this.getY() - y);
			}
		}

		double y, x;

		public double getY() {
			return COval.this.getY() + (getRadius2() * j);
		}

		public double getX() {
			return COval.this.getX() + (getRadius1() * i);
		}

		public int getID() {
			return 0;
		}

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			r.setFrame(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2,
					i, i);
			g2.setColor(verschiebeGriffColor2);
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
			return null;
		}
	}

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.geom.COval.class;
	}

	@Override
	public COval clone1() throws CloneNotSupportedException {
		COval c = new COval(getX(), getY(), getRadius1(), getRadius2());
		c.setCSS(this.getCSS().clone1(this));
		return c;
	}
	private DPoint calc = new DPoint();
	
	@Override
	public double getNearestPointFang(DPoint Overwrite, double distance, double x, double y,
			GreifItem withOut) {
		if(verschiebeGriff.equals(withOut))return distance;
//		public static boolean PUNKTFANG_ECKE = true;
		if(GreifTool.PUNKTFANG_ECKE){
			calc.setLocation(getX()+getRadius1(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()-getRadius1(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()+getRadius2());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()-getRadius2());
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