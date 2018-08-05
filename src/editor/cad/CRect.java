package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import editor.tabelle.OneLine;
import editor.tools.GreifTool;

public class CRect extends CShape {
	CRect that = this;
	OneLine<Double> breite, höhe;
	static JPopupMenu menu;
	static{
		menu = new JPopupMenu();
		JMenuItem item = new JMenuItem("Zu Path");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CObject c = GUI_Layerbefehle.getMatrix().getSelectedCObject();
				if(c instanceof CRect){
					CPath p = new CPath((CRect) c);
					GUI_Layerbefehle.getMatrix().addCObject(p);
					
				}
			}
		});
		menu.add(item);
	}
	public CRect(double x, double y, double width1, double height1) {
		setX(x);
		setY(y);
		setName("Rechteck", false);
		breite = new OneLine<Double>("Breite", width1) {
			@Override
			public void valueChanges(Double value) {
				setWidth(value);
			}
		};
		addLine(breite);
		höhe = new OneLine<Double>("Höhe", height1) {
			@Override
			public void valueChanges(Double value) {
				setHeight(value);
			}
		};
		addLine(höhe);

		getGreifItems().add(new GreifItem() {
			@Override
			public void setLocation(double x, double y) {
				setWidth(Matrix.rastern(x) - that.getX());
				setHeight(Matrix.rastern(y) - that.getY());
			}

			public double getY() {
				return that.getY() + getHeight();
			}

			public double getX() {
				return that.getX() + getWidth();
			}

			public int getID() {
				return 0;
			}

			public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
				g.setColor(Color.WHITE);
				int i = 10;
				r.setBounds(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i
						/ 2, i, i);
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
		});
	}

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		r.setFrame(getX(), getY(), getWidth(), getHeight());
	}

	Rectangle2D.Double r = new Rectangle2D.Double();

	@Override
	public Rectangle2D.Double getShape() {
		return r;
	}

	public double getHeight() {
		return  höhe.getValue();
	}

	public void setHeight(double height) {
		if (height < 1) {
			höhe.setValue((double)Matrix.getRast());
		} else {
			höhe.setValue(height);
		}
	}

	public double getWidth() {
		return breite.getValue();
	}

	public void setWidth(double d) {
		if (d < 1) {
			breite.setValue((double)Matrix.getRast());
		} else {
			breite.setValue(d);
		}
	}

	@Override
	public void destroy() {
		r = null;
		that = null;
	}

	@Override
	public String getXMLName() {
		return XMLCObject.rect;
	}
	@Override
	public Class<?> getGameClass() {
		return de.game.objects.geom.CRect.class;
	}

	@Override
	public CRect clone1() throws CloneNotSupportedException {
		CRect c = new CRect(getX(), getY(), getWidth(), getHeight());
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
			calc.setLocation(getX(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()+getWidth(),getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()+getHeight());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()+getWidth(),getY()+getHeight());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
//		public static boolean PUNKTFANG_MITTE = true;
		if(GreifTool.PUNKTFANG_MITTE){
			calc.setLocation(getX()+getWidth()/2,getY());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()+getWidth(),getY()+getHeight()/2);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX()+getWidth()/2,getY()+getHeight());
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
			calc.setLocation(getX(),getY()+getHeight()/2);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
//		public static boolean PUNKTFANG_ZENTRUM = false;
		if(GreifTool.PUNKTFANG_ZENTRUM){
			calc.setLocation(getX()+getWidth()/2,getY()+getHeight()/2);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
		return distance;
	}
}
