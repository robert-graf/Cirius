package database;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import basic.CSSClasse;
import basic.ListenTreeList;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;
import editor.rpg.GUIMapEditStart;
import interfaces.LayerListener;

/**
 * import static database.XMLCObject.*;
 */

public class MapXML extends XML {
	private Matrix matrix;
	private ArrayList<Layer> l;
	private ListenTreeList<String, CSSClasse> css;
	
	private ArrayList<CObject> parents;

	public MapXML(GUIMapEditStart map) {
		this((ArrayList<Layer>) map.getMatrix().getLayerList());
		this.matrix = map.getMatrix();
		this.css = map.CSSCList;
		l = (ArrayList<Layer>) matrix.getLayerList();
	}

	public MapXML(ArrayList<Layer> l) {
		this(l, null);
	}

	public MapXML(ArrayList<Layer> l,
			ArrayList<CObject> parents) {
		this.l = l;
		this.parents = parents;
	}
	@Override
	public void read(Element rootElement) {
		if (matrix != null) {
			try {
				matrix.setVersion(Integer.parseInt(rootElement
						.getAttribute("version")));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			matrix.removeLayer(0);
		}
		/** Script */
		NodeList scriptnode = rootElement.getElementsByTagName("Script");
		String script = "";
		if(scriptnode.getLength() != 0){
			script = (scriptnode.item(0).getTextContent());
		}
		/**Class*/
		NodeList classes = rootElement.getElementsByTagName("class");
		for (int n = 0; n != classes.getLength(); n++) {
			Element cssClass = (Element) classes.item(n);
			css.put(cssClass.getAttribute("id"), new CSSClasse(cssClass));
		}
		/**Layer*/
		NodeList layer = rootElement.getElementsByTagName("Layer");
		for (int n = 0; n != layer.getLength(); n++) {
			Element onelayer = (Element) layer.item(n);
			
			String name = onelayer.getAttribute("id");
			if(name == null){
				name = onelayer.getAttribute("name");
			}
			float faktor = Float.parseFloat(onelayer.getAttribute("faktor"));
			boolean visible = onelayer.getAttribute("visible").equals("1");
			boolean solid = onelayer.getAttribute("solid").equals("1");
			Layer layer1 = new Layer(name,faktor,visible,solid);
			onelayer.removeAttribute("id");
			onelayer.removeAttribute("name");
			onelayer.removeAttribute("faktor");
			onelayer.removeAttribute("visible");
			onelayer.removeAttribute("solid");
			if(onelayer.hasAttribute("class")){
				StringTokenizer token = new StringTokenizer(onelayer.getAttribute("class")," \n\t");
				while(token.hasMoreTokens()){
					String s = token.nextToken();
					CSSClasse cl = css.get(s);
					if(cl == null){
						cl = new CSSClasse(s);
					}
					layer1.getCSS().addCSSClasses(cl);
				}
				onelayer.removeAttribute("class");
			}
			layer1.getCSS().setAttributeByElement(onelayer); 
			l.add(n,layer1);
			NodeList nodelist = onelayer.getElementsByTagName("*");
			
			/** CObject Verteiler */
			for (int i = 0; i != nodelist.getLength(); i++) {
				Element CElement = (Element) nodelist.item(i);
				CObject c = XMLCObject.loadCObject(CElement, parents,script,css);
				if (c == null)
					continue;
				l.get(n).add(c, false);
			}
		}
		if (matrix != null) {
			matrix.setSelectedCObject(null, null);
		}
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
		if (matrix != null) {
			rootElement.setAttribute("version", matrix.getVersion() + "");
		}
		ArrayList<Layer> layers = l;
		/**Class*/
		for(String s : css.getMap().keySet()){
			Element classe = xmlDoc.createElement("class");
			classe.setAttribute("id", css.getMap().get(s).getName());
			css.getMap().get(s).save(classe);
			rootElement.appendChild(classe);
		}
		/** Layer */
		for (Layer layer : layers) {
			Element OneLayer = xmlDoc.createElement("Layer");
			OneLayer.setAttribute("id", "" + layer.getName());
			OneLayer.setAttribute("faktor", "" + layer.getFaktor());
			OneLayer.setAttribute("visible", layer.isVisible() ? "1" : "0");
			OneLayer.setAttribute("solid", layer.isSolid() ? "1" : "0");
			layer.getCSS().save(OneLayer, xmlDoc);
			/** Object */
			for (CObject object : layer) {
				XMLCObject.saveCObject(OneLayer, xmlDoc, object,false);
			}
			rootElement.appendChild(OneLayer);
		}
			
	}

	@Override
	public String getXMLName() {
		return "map";
	}

	@Override
	public void fileDoesNotExist() {
		// TODO
	}

	

	public void addListenerToLayer(LayerListener guiMapEditStart) {
		for(Layer layer : l){
			layer.addLayerListener(guiMapEditStart);
			for(CObject c : layer){
				guiMapEditStart.addToLayerEvent(layer, c);
			}
		}
	}
}
