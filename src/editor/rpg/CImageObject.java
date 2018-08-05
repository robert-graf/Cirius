package editor.rpg;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;

import basic.DDrawer;
import basic.DPoint;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;
import editor.GreifItem;
import editor.Layer;
import editor.tabelle.NumberLine;
import editor.tabelle.OneLine;
import editor.tools.GreifTool;
import interfaces.FileHolder;
import interfaces.HasPanel;

public abstract class CImageObject extends CObject implements HasPanel,FileHolder {
	static JPanel p = new JPanel();
	static JButton reloud = new JButton("Neu Laden");
	public static JButton teilenButton = new JButton("Editor");
	static {
		teilenButton.addActionListener(TiledMapManager.THAT);
		reloud.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CObject c = GUI_Layerbefehle.getMatrix().getSelectedCObject();
				if (c instanceof CImageObject) {
					((CImageObject) c).reload();
				}
			}
		});
		p.add(reloud);
	}
	private NumberLine<Integer> repeatX = new NumberLine<Integer>(
			"Widerholen X", 1, 0, Integer.MAX_VALUE, 1) {
		@Override
		public void valueChanges(Integer value) {
			setRepeatX(value);
		}
	};
	private NumberLine<Integer> repeatY = new NumberLine<Integer>(
			"Widerholen Y", 1, 0, Integer.MAX_VALUE, 1) {
		@Override
		public void valueChanges(Integer value) {
			setRepeatY(value);
		}
	};
	private NumberLine<Double> abstandX = new NumberLine<Double>("Abstand Y",
			1d, 1d, Integer.MAX_VALUE, 1d) {
		@Override
		public void valueChanges(Double value) {
			setAbstandX(value);
		}
	};
	private NumberLine<Double> abstandY = new NumberLine<Double>("Abstand Y",
			1d, 1d, Integer.MAX_VALUE, 1d) {
		@Override
		public void valueChanges(Double value) {
			setAbstandY(value);
		}
	};
	private OneLine<String> fileName = new OneLine<String>("Dateiname",
			"Anonym") {
		@Override
		public void valueChanges(String value) {

		}
	};
	private File file;

	// TODO Klipping
	public CImageObject() {
		addLine(repeatX);
		addLine(repeatY);
		addLine(fileName);
		fileName.setEditable(false);
	}

	@Override
	public void draw1(Graphics2D g, Graphics2D g2, Matrix m,Layer layer, MouseEvent ev) {
		getCSS().setParent(layer.getCSS());
		if(getCSS().doFill()){
		double x = getX();
		double y = getY();
		int finalContX = getRepeatX();
		int finalContY = getRepeatY();
		getCSS().setFillDrawSet(g);
		if (getRepeatX() == 0) {
			Rectangle r = m.getVisibleRectangle();
			double abstandX = this.abstandX.getValue();
			x = getX() % abstandX + r.x / abstandX * abstandX - abstandX;
			finalContX = (int) (r.getWidth() / abstandX) + 3;
		}
		if (getRepeatY() == 0) {
			Rectangle r = m.getVisibleRectangle();
			double abstandY = this.abstandY.getValue();
			y = getY() % abstandY + r.y / abstandY * abstandY - abstandY;
			finalContY = (int) (r.getHeight() / abstandY) + 3;
		}
		for (int n = 0; n != finalContX; n++) {
			for (int i = 0; i != finalContY; i++) {
				drawRenderer(g, g2, m, ev, (int)(x + n * getAbstandX()), (int)(y + i* getAbstandY()));
			}
		}
		getCSS().restore(g);
		if(getCSS().doDrawLine()){
			getCSS().setLineDrawSet(g);
			for (int n = 0; n != finalContX; n++) {
				for (int i = 0; i != finalContY; i++) {
					DDrawer.drawRect(getSingelShape().getBounds().x + n * getAbstandX(),  getSingelShape().getBounds().y + i* getAbstandY(), getWidth(), getHeight(),g);
				}
			}
			getCSS().restore(g);
		}
		
		}
	}

	@Override
	public void draw3(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {

	}

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		updateRenderer(m, timePassed, conter);
	}

	@Override
	public Shape getShape() {
		// TODO Klipping
		return getSingelShape();
	}

	public int getRepeatX() {
		return repeatX.getValue();
	}

	public void setRepeatX(int repeatX) {
		if (repeatX == 1) {
			removeLine(abstandX);
		} else {
			if (indexOfLine(abstandX) == -1) {
				addLine(indexOfLine(this.repeatX) + 1, abstandX);
			}
			if (getAbstandX() < getWidth() / 2) {
				setAbstandX(getWidth());
			}
		}
		this.repeatX.setValue(repeatX);
	}

	public int getRepeatY() {
		return repeatY.getValue();
	}

	public void setRepeatY(int repeatY) {
		if (repeatY == 1) {
			removeLine(abstandY);
		} else {
			if (indexOfLine(abstandY) == -1) {
				addLine(indexOfLine(this.repeatY) + 1, abstandY);
			}
			if (getAbstandY() < getHeight() / 2) {
				setAbstandY(getHeight());
			}
		}
		this.repeatY.setValue(repeatY);
	}

	public double getAbstandX() {
		return abstandX.getValue();
	}

	public void setAbstandX(double d) {
		if (d < getWidth() / 2) {
			d = getWidth();
		}
		this.abstandX.setValue(d);
	}

	public double getAbstandY() {
		return abstandY.getValue();
	}

	public void setAbstandY(double abstandY) {
		if (abstandY < getHeight() / 2) {
			abstandY = getHeight();
		}
		this.abstandY.setValue(abstandY);
	}

	@Override
	public void destroy() {
	}

	@Override
	public Object clone1() throws CloneNotSupportedException {
		CImageObject o = clone2();
		o.setAbstandX(getAbstandX());
		o.setAbstandY(getAbstandY());
		o.setRepeatX(getRepeatX());
		o.setRepeatY(getRepeatY());
		return o;
	}

	public final File getFile() {
		return file;
	}

	public final void setFile(File file) {
		if (file == null) {
			fileName.setValue("Anonym");
		} else {
			fileName.setValue(file.getName());
		}
		this.file = file;
	}
	private DPoint calc = new DPoint();
	@Override
	public double getNearestPointFang(DPoint Overwrite, double distance, double x, double y,
			GreifItem withOut) {
//		public static boolean PUNKTFANG_ECKE = true;
		if(verschiebeGriff.equals(withOut))return distance;
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
//		public static boolean PUNKTFANG_TILEMAP = false;
		if(this instanceof CTileMap && GreifTool.PUNKTFANG_TILEMAP){
			((CTileMap)this).getNextRasterPoint(calc,x,y);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance,x,y,calc);
		}
		return distance;
	}

	protected abstract CImageObject clone2() throws CloneNotSupportedException;

	public abstract void drawRenderer(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y);

	public abstract void updateRenderer(Matrix m, int timePassed, int conter);

	public abstract Shape getSingelShape();

	public abstract void reload();
	
	@Override
	public JPanel getPanel() {
		if (this instanceof CSprite) {
			p.remove(teilenButton);
		} else {
			p.add(teilenButton);
		}
		return p;
	}
}
