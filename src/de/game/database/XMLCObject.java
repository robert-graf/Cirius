package de.game.database;

import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;

import de.game.objects.CFont;
import de.game.objects.CImageObject;
import de.game.objects.CObject;
import de.game.objects.CParticelSystem;
import de.game.objects.CRef;
import de.game.objects.CSprite;
import de.game.objects.CTileMap;
import de.game.objects.geom.CKreis;
import de.game.objects.geom.COval;
import de.game.objects.geom.CPath;
import de.game.objects.geom.CPoly;
import de.game.objects.geom.CRect;
import de.game.objects.geom.CShape;


public class XMLCObject {
	//truetyp == true wenn an SVG sich gehalten wird
	
	/** constand namespace */
	public final static String //
			repeatX = "repeatX",//
			repeatY = "repeatY",//
			abstandX = "abstandX",//
			abstandY = "abstandY",//
			// Formen
			rect = "rect",//
			circle = "circle",//
			ellipse = "ellipse",//
			line = "line",//
			polyline = "polyline",//
			polygon = "polygon",//
			path = "path",//
			// attribute
			_width = "width", //
			_height = "height", //
			_x = "x", //
			_y = "y", //
			_r = "r", // Radius Kreis
			_rx = "rx", //
			_ry = "ry", //
			_cx = "cx", //
			_cy = "cy", //
			_style = "style", //
			_source = "source",
			//Others
			 particel = "Particel"//
			// style="fill:rgb(0,0,255);stroke-width:1;stroke:rgb(0,0,0)"/
			// rx (rundung x)
			// ry (rundung y)
			;
	static final ArrayList<String> shapes = new ArrayList<>();
	static {
		shapes.add(rect); //
		shapes.add(circle); //
		shapes.add(ellipse); //
		shapes.add(line); //
		shapes.add(polyline); //
		shapes.add(polygon); //
		shapes.add(path); //
	} //

	static public CObject loadCObjectByFile(File f, int x, int y) {
		CObject object = null;
		if (f.getName().toLowerCase().endsWith(".das")
				|| ".png".endsWith(f.getName().substring(
						f.getName().lastIndexOf(".")))) {
			object = new CSprite(0, 0, f, f.getName(), 0);
		} else if (".tmx".endsWith(f.getName().substring(
				f.getName().lastIndexOf(".")))) {
			object = new CTileMap(0, 0, f, f.getName());
		} else if (".rpg".endsWith(f.getName().substring(
				f.getName().lastIndexOf(".")))) {
			object = new CRef(x, y, f.getPath());
		}else if (".pesx".endsWith(f.getName().substring(
				f.getName().lastIndexOf(".")))) {
			object = new CParticelSystem(x, y,f,100,100);
		}
		return object;
	}

	/**
	 * CObject Verteiler
	 * 
	 * @param parents
	 */
	static public CObject loadCObject(Element CElement,
			ArrayList<CObject> parents) {
		CObject cobject = null;
		int x = 0, y = 0;
		try {
			if (CElement.hasAttribute(_x)) {
				x = Integer.parseInt(CElement.getAttribute(_x));
				CElement.removeAttribute(_x);
			}
			if (CElement.hasAttribute(_y)) {
				y = Integer.parseInt(CElement.getAttribute(_y));
				CElement.removeAttribute(_y);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		/** Shape SVG */
		if (shapes.contains(CElement.getNodeName())) {
			cobject = loadShapeSVG(CElement, x, y);
		}
		/** Sprite */
		else if (CElement.getNodeName().equals("Sprite")) {
			cobject = loadSprite(CElement, x, y);
		}
		/** Font */
		else if (CElement.getNodeName().equals("Font")) {
			cobject = loadFont(CElement, x, y);
		}
		/** Tiledmap */
		else if (CElement.getNodeName().equals("Tiledmap")) {
			cobject = loadTileMap(CElement, x, y);
		}/** Particel System */
		else if (CElement.getNodeName().equals(particel)) {
			cobject = loadParticelSystem(CElement, x, y);
		}/** CRef */
		else if (CElement.getNodeName().equals(CRef.ref)) {
			cobject = loadCRef(CElement, x, y, parents);
		} else if (CElement.getNodeName().equals("Scene")) {
		} else if (CElement.getNodeName().equals("Animation")) {
		}// TODO add her CObjects
		else {
			Toolkit.getDefaultToolkit().beep();
			System.out.println(CElement.getNodeName()
					+ " kann nicht über MapXML geladen werden");
			System.out.println(CElement);
			return null;
		}
		/** CImageObject */
		if (cobject instanceof CImageObject) {
			String s;
			// X
			s = CElement.getAttribute(repeatX);
			CElement.removeAttribute(repeatX);
			if (!(s.isEmpty() || s.equals("1"))) {
				try {
					int i = Integer.parseInt(s);
					int i2 = Integer.parseInt(CElement.getAttribute(abstandX));
					CElement.removeAttribute(abstandX);
					((CImageObject) cobject).setRepeatX(i);
					((CImageObject) cobject).setAbstandX(i2);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
			// Y
			s = CElement.getAttribute(repeatY);
			CElement.removeAttribute(repeatY);
			if (!(s.isEmpty() || s.equals("1"))) {
				try {
					int i = Integer.parseInt(s);
					int i2 = Integer.parseInt(CElement.getAttribute(abstandY));
					CElement.removeAttribute(abstandY);
					((CImageObject) cobject).setRepeatY(i);
					((CImageObject) cobject).setAbstandY(i2);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		String name = CElement.getAttribute("id");
		CElement.removeAttribute("id");
		if (name == null || name.isEmpty()) { // TODO REMOVE
			name = CElement.getAttribute("name");
			CElement.removeAttribute("name");
		}
		if (name != null && !name.isEmpty()) {
			cobject.setName(name);
		}
		// cobject.setX(x);
		// cobject.setY(y);
//		String script = CElement.getTextContent();
//		if(CElement.hasAttribute("class")){
//			StringTokenizer token = new StringTokenizer(CElement.getAttribute("class")," \n\t");
//			while(token.hasMoreTokens()){
//				String s = token.nextToken();
//				CSSClasse cl = css.get(s);
//				if(cl == null){
//					cl = new CSSClasse(s);
//				}
//				cobject.getCSS().addCSSClasses(cl);
//			}
//			CElement.removeAttribute("class");
//		}
//		cobject.getCSS().setAttributeByElement(CElement);
		//FIXME Entfernen. Alter Scriptstandart
//		if (script != null && !script.isEmpty()) {
//			System.out.println(name);
//			global.createScript(name, script);
//		}
		return cobject;
	}

	/** Shape SVG TODO */
	private static CObject loadShapeSVG(Element cElement, int x, int y) {
		CShape shape = null;
		String typ = cElement.getNodeName();
		// Kreis
		if (typ.equals(circle)) {
			try {
				if (cElement.hasAttribute(_cx)) {
					x = Integer.parseInt(cElement.getAttribute(_cx));
				}
				if (cElement.hasAttribute(_cy)) {
					y = Integer.parseInt(cElement.getAttribute(_cy));
				}
				cElement.removeAttribute(_cx);
				cElement.removeAttribute(_cy);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int radius = Integer.parseInt(cElement.getAttribute(_r));
			cElement.removeAttribute(_r);
			shape = new CKreis(x, y, radius);
		}
		// Oval
		else if (typ.equals(ellipse)) {
			try {
				if (cElement.hasAttribute(_cx)) {
					x = Integer.parseInt(cElement.getAttribute(_cx));
				}
				if (cElement.hasAttribute(_cy)) {
					y = Integer.parseInt(cElement.getAttribute(_cy));
				}
				cElement.removeAttribute(_cx);
				cElement.removeAttribute(_cy);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			int r1 = Integer.parseInt(cElement.getAttribute(_rx));
			int r2 = Integer.parseInt(cElement.getAttribute(_ry));
			shape = new COval(x, y, r1, r2);
			cElement.removeAttribute(_rx);
			cElement.removeAttribute(_ry);
		}
		// Rect
		else if (typ.equals(rect)) {
			int width = Integer.parseInt(cElement.getAttribute(_width));
			int height = Integer.parseInt(cElement.getAttribute(_height));
			shape = new CRect(x, y, width, height);
			cElement.removeAttribute(_width);
			cElement.removeAttribute(_height);
		}
		// line
		else if (typ.equals(line)) {
			int x1 = Integer.parseInt(cElement.getAttribute("x1"));
			int y1 = Integer.parseInt(cElement.getAttribute("y1"));
			int x2 = Integer.parseInt(cElement.getAttribute("x2"));
			int y2 = Integer.parseInt(cElement.getAttribute("y2"));
			shape = new CPoly(x1, y1, x2, y2);
			cElement.removeAttribute("x1");
			cElement.removeAttribute("y1");
			cElement.removeAttribute("x2");
			cElement.removeAttribute("y2");
			cElement.removeAttribute("fill-rule");
		}
		// Poly1
		else if (typ.equals(polygon) || typ.equals(polyline)) {
			shape = new CPoly(cElement.getAttribute("points"),
					typ.equals(polygon));

//			((CPoly) shape).setEvenOdd(cElement.getAttribute("fill-rule")
//					.equals("evenodd"));
			cElement.removeAttribute("points");
			cElement.removeAttribute("fill-rule");

		}
		// Path
		else if (typ.equals(path)) {
			shape = new CPath(cElement.getAttribute("d"));
//			((CPath) shape).setEvenOdd(cElement.getAttribute("fill-rule")
//					.equals("evenodd"));
			cElement.removeAttribute("d");
			cElement.removeAttribute("fill-rule");

		} else {
			System.out.println(typ + " kann nicht geladen werden als Shape");
		}
		// TODO Text
		cElement.removeAttribute("x");
		cElement.removeAttribute("y");
		cElement.removeAttribute("id");
		return shape;
	}

	
	/** Sprite */
	static private CSprite loadSprite(Element CElement, int x, int y) {
		int index = 0;
		String source = CElement.getAttribute(_source);
		CElement.removeAttribute(_source);
		// bleib erhalten
		String name = CElement.getAttribute("name");
		CElement.removeAttribute("name");
		try {
			index = Integer.parseInt(CElement.getAttribute("index"));
			CElement.removeAttribute("index");
		} catch (NumberFormatException e) {
		}

		if (source.isEmpty()) {
			return new CSprite(x, y, CElement, name, index);
		} else {
			File f = new File(source);
			if (name.isEmpty()) {
				name = f.getName();
			}
			return new CSprite(x, y, f, name, index);
		}
	}
	/** Font */
	static private CFont loadFont(Element CElement, int x, int y) {
		
		String source = CElement.getAttribute(_source);
		CElement.removeAttribute(_source);
		
		String name = CElement.getAttribute("name");
		CElement.removeAttribute("name");
		
		String content = CElement.getAttribute("content");
		CElement.removeAttribute("content");
		
		int width = new Integer(CElement.getAttribute(_width));
		CElement.removeAttribute(_width);
		int maxCharCont = new Integer(CElement.getAttribute("maxCharCont"));
		CElement.removeAttribute("maxCharCont");
		boolean relativ = CElement.getAttribute("relativ").equals("1");
		CElement.removeAttribute("relativ");
		return new CFont(x, y, new File(source), content, width,name,maxCharCont,relativ);

	}
	/** Tiledmap */
	static private CTileMap loadTileMap(Element CElement, int x, int y) {
		String source = CElement.getAttribute(_source);
		String until = CElement.getAttribute("to");
		String from = CElement.getAttribute("from");
		CElement.removeAttribute(_source);
		CElement.removeAttribute("to");
		CElement.removeAttribute("from");
		CTileMap map;
		map = new CTileMap(x, y, source, from, until);
		return map;
	}
	static private CParticelSystem loadParticelSystem(Element CElement, int x, int y){
		String source = CElement.getAttribute(_source);
		CElement.removeAttribute(_source);
		int width = 100;
		int height = 100;
		try{
			width = Integer.parseInt(CElement.getAttribute(_width));
			height = Integer.parseInt(CElement.getAttribute(_height));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		CParticelSystem system = new CParticelSystem(x, y, new File(source),width,height);
		return system;
	}
	/**
	 * CRef
	 * 
	 * @param parents
	 */
	static private CRef loadCRef(Element CElement, int x, int y,
			ArrayList<CObject> parents) {
		String source = CElement.getAttribute(_source);
		String until = CElement.getAttribute("to");
		String from = CElement.getAttribute("from");
		CElement.removeAttribute(_source);
		CElement.removeAttribute("to");
		CElement.removeAttribute("from");
		CRef map = new CRef(x, y, source, from, until, parents);
		return map;
	}
}
