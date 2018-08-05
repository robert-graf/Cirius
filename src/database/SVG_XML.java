package database;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;
import editor.rpg.GUIMapEditStart;

/**
 * import static database.XMLCObject.*;
 */

public class SVG_XML extends XML {
	private Matrix matrix;
	private ArrayList<Layer> l;
	
	public SVG_XML(GUIMapEditStart map) {
		this((ArrayList<Layer>) map.getMatrix().getLayerList());
		this.matrix = map.getMatrix();
		l = (ArrayList<Layer>) matrix.getLayerList();
	}

	public SVG_XML(ArrayList<Layer> l) {
		this.l = l;
	}
	
	@Override
	public void write(Element rootElement, Document xmlDoc) {
//		xmlDoc.setPrefix("DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"");
		rootElement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		rootElement.setAttribute("version", "1.1");
		ArrayList<Layer> layers = l;
		/** Layer */
		for (Layer layer : layers) {
			Element OneLayer = xmlDoc.createElement("g");
			OneLayer.setAttribute("id", "" + layer.getName());
//			OneLayer.setAttribute("faktor", "" + layer.getFaktor());
//			OneLayer.setAttribute("visible", layer.isVisible() ? "1" : "0");
//			OneLayer.setAttribute("solid", layer.isSolid() ? "1" : "0");
			layer.getCSS().save(OneLayer, xmlDoc);
			/** Object */
			for (CObject object : layer) {
				XMLCObject.saveCObject(OneLayer, xmlDoc, object,true);
			}
			rootElement.appendChild(OneLayer);
		}
	}

	@Override
	public String getXMLName() {
		return "svg";
	}

	@Override
	public void fileDoesNotExist() {
		// TODO
	}

	@Override
	public void read(Element rootElement) {
		// TODO Auto-generated method stub
		
	}
}
