package editor.rpg.vector;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import basic.DPoint;
import editor.CObject;
import editor.GreifItem;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;

public class Vector extends CObject {
	public ScriptEngine engine;
	static int i = 0;

	public Vector(String formel) {
		setName("vektor" + i++);
		engine = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			engine.eval("function " + getName() + " (){\n" + "y = " + formel
					+ ";\n}");
			engine.put("y", 0);
		} catch (ScriptException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean b = true;
		for (float x = -100; x < 100; x += .1f) {
			engine.put("x", x);
			try {
				engine.eval(getName() + "();");
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			Number y = (Number) engine.get("y");
			if (b) {
				p.moveTo(x * 10, y.doubleValue() * -10);
				b = false;
			} else {
				p.lineTo(x * 10, y.doubleValue() * -10);
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public Class<?> getGameClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getXMLName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw1(Graphics2D g, Graphics2D g2, Matrix m,Layer l, MouseEvent ev) {
		g.draw(getShape());
	}

	@Override
	public void draw3(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub

	}

	GeneralPath p = new GeneralPath();

	@Override
	public Shape getShape() {
		return p;
	}

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object clone1() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getNearestPointFang(DPoint nextPoint, double distance, double x, double y, GreifItem item) {
		// TODO Auto-generated method stub
		return Double.MAX_VALUE;
	}

}
