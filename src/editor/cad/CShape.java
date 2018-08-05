package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;

public abstract class CShape extends CObject {
	protected static final Color paintColor = new Color(255, 255, 255);
	protected static final Color paintColorSelect = new Color(0, 0, 255, 100);
	protected static final Color fillDefault = new Color(100, 100, 100, 160);
//	static final Color moveGriff = new Color(255, 0, 0);
	
	@Override
	public void draw1(Graphics2D g, Graphics2D g2, Matrix m,Layer layer, MouseEvent ev) {
		css.setParent(layer.getCSS());
		if (getCSS().doFill()) {
			css.setFillDrawSet(g);
			g.fill(getShape());
		}
		css.restore(g);
		if (css.doDrawLine()) {
			css.setLineDrawSet(g);
			g.draw(getShape());
		}
		css.restore(g);
	}

	@Override
	public void draw3(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev) {
	}

	
}
