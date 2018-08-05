package toolbars;

import static toolbars.Toolbar.createMenuItem;

import java.awt.event.InputEvent;

import javax.swing.KeyStroke;

import editor.cad.CKreisTool;
import editor.cad.COvalTool;
import editor.cad.CPathTool;
import editor.cad.CPolyTool;
import editor.cad.CRectTool;
import editor.cad.SelectByFrame;
import editor.css.CSSEditor;
import editor.tools.Pan;
import editor.tools.setMainTool;

public class DialogCAD {
	public static String name = "CAD";

	public static Pan pan = new Pan();

	public static void init() {
		pan.init(null);
		// TODO
		// AbstractTool delete = new SelectByFrame() {
		// GreifItem i;
		//
		// @Override
		// public void mousePressed(Matrix m, MouseEvent ev) {
		// if (GreifTool.item != null)
		// i = GreifTool.item;
		// super.mousePressed(m, ev);
		// }
		//
		// public void mouseReleased(Matrix m, java.awt.event.MouseEvent ev) {
		// super.mouseReleased(m, ev);
		// if (ev.getClickCount() == 2) {
		// GreifTool.item = i;
		// remove();
		// }
		// };
		// };
		createMenuItem("Auswählen", "select_continuous_area", 16, name,
				new setMainTool(new SelectByFrame()), null);
		createMenuItem("Pan", "transform_move", 16, name, new setMainTool(pan),
				KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Rechteck", "shape_square", 16, name, new setMainTool(
				new CRectTool()), KeyStroke.getKeyStroke('7',
				InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Kreis", "draw_ellipse", 16, name, new setMainTool(
				new CKreisTool()), KeyStroke.getKeyStroke('8',
				InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Oval", "draw_ellipse", 0, name, new setMainTool(
				new COvalTool()), KeyStroke.getKeyStroke('9',
				InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Polygon", "draw_polygon", 16, name, new setMainTool(
				new CPolyTool()), KeyStroke.getKeyStroke('0',
				InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Path", "draw_wave", 16, name, new setMainTool(
				new CPathTool()), KeyStroke.getKeyStroke('ß',
				InputEvent.CTRL_DOWN_MASK));
//		 createMenuItem("Shape to Fractal", "database_add", 16, name, new ActionListener() {
//			
//			@Override
//			public void actionPerformed(java.awt.event.ActionEvent arg0) {
//				Matrix m = ((GUIMapEditStart) AAufbau.f.CurrendPanel).panel.m;
//				m.addCObject(new Fractal(m.getSelectedList()));
//			}
//		});
		createMenuItem("Stylsheet", "page_white_paint", 16, name,CSSEditor.THAT);
		// createMenuItem("Löschen", "shape_square_delete", 16, name,new
		// setMainTool(delete));
	}
	
}
