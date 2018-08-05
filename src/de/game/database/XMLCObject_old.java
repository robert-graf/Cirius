package de.game.database;

import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;

import de.game.objects.*;
import de.game.objects.geom.*;

public class XMLCObject_old {
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
			_style = "style" //

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
		}
		return object;
	}

	/**
	 * CObject Verteiler
	 * 
	 * @param parents
	 * @param  
	 */
	static public CObject loadCObject(Element CElement,
			ArrayList<CObject> parents) {
		CObject cobject = null;
		int x = 0, y = 0;
		try {
			if (CElement.hasAttribute(_x)) {
				x = Integer.parseInt(CElement.getAttribute(_x));
			}
			if (CElement.hasAttribute(_y)) {
				y = Integer.parseInt(CElement.getAttribute(_y));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		/** Shape SVG */
		if (shapes.contains(CElement.getNodeName())) {
			cobject = loadShapeSVG(CElement, x, y);
		}
		/** Shape */
		else if (CElement.getNodeName().equals("Shape")) {
			cobject = loadShape(CElement, x, y);
		}
		/** Sprite */
		else if (CElement.getNodeName().equals("Sprite")) {
			cobject = loadSprite(CElement, x, y);
		}
		/** Tiledmap */
		else if (CElement.getNodeName().equals("Tiledmap")) {
			cobject = loadTileMap(CElement, x, y);
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
			if (!(s.isEmpty() || s.equals("1"))) {
				try {
					int i = Integer.parseInt(s);
					int i2 = Integer.parseInt(CElement.getAttribute(abstandX));
					((CImageObject) cobject).setRepeatX(i);
					((CImageObject) cobject).setAbstandX(i2);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
			// Y
			s = CElement.getAttribute(repeatY);
			if (!(s.isEmpty() || s.equals("1"))) {
				try {
					int i = Integer.parseInt(s);
					int i2 = Integer.parseInt(CElement.getAttribute(abstandY));
					((CImageObject) cobject).setRepeatY(i);
					((CImageObject) cobject).setAbstandY(i2);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		String name = CElement.getAttribute("id");
		if (name == null || name.isEmpty()) { // TODO REMOVE
			name = CElement.getAttribute("name");
		}
		if (name != null && !name.isEmpty()) {
			cobject.setName(name);
		}
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
		//TODO SVG
//		shape.getCSS().setAttributeByElement(cElement);
		return shape;
	}

	/** Shape TODO REMOVE */
	private static CObject loadShape(Element cElement, int x, int y) {
		System.err.println("Old File. It will be updated.");
		CShape shape = null;
		String typ = cElement.getAttribute("Typ");
		// Kreis
		if (typ.equals("Kreis")) {
			int radius = Integer.parseInt(cElement.getAttribute("r1"));
			shape = new CKreis(x, y, radius);
		}
		// Oval
		else if (typ.equals("Oval")) {
			int r1 = Integer.parseInt(cElement.getAttribute("r1"));
			int r2 = Integer.parseInt(cElement.getAttribute("r2"));
			shape = new COval(x, y, r1, r2);
		} else if (typ.equals(rect) || typ.equals("Rect")) {
			int width = Integer.parseInt(cElement.getAttribute("weite"));
			int height = Integer.parseInt(cElement.getAttribute("höhe"));
			shape = new CRect(x, y, width, height);
		}
		else {
			System.out.println(typ + " kann nicht geladen werden als Shape");
		}
		return shape;
	}

	/** Sprite */
	static private CSprite loadSprite(Element CElement, int x, int y) {
		int index = 0;
		String source = CElement.getAttribute("source");
		// bleib erhalten
		String name = CElement.getAttribute("name");
		try {
			index = Integer.parseInt(CElement.getAttribute("index"));
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

	/** Tiledmap */
	static private CTileMap loadTileMap(Element CElement, int x, int y) {
		String source = CElement.getAttribute("source");
		String until = CElement.getAttribute("to");
		String from = CElement.getAttribute("from");
		CTileMap map;
		map = new CTileMap(x, y, source, from, until);
		return map;
	}

	/**
	 * CRef
	 * 
	 * @param parents
	 */
	static private CRef loadCRef(Element CElement, int x, int y,
			ArrayList<CObject> parents) {
		String source = CElement.getAttribute("source");
		String until = CElement.getAttribute("to");
		String from = CElement.getAttribute("from");
		CRef map = new CRef(x, y, source, from, until, parents);
		return map;
	}

	/**
	 * save
	 * 
	 */
//	public static Element saveCObject(Element layerElement, Document xmlDoc,
//			CObject c,boolean truetyp) {
//		boolean saveXandY = true;
//		Element rootObject = xmlDoc.createElement(c.getXMLName());
//		if (c.getName() != null && !c.getName().isEmpty()) {
//			rootObject.setAttribute("id", c.getName());
//		}
//		if (c instanceof CShape) {
//			saveXandY = false;
//			saveShape(rootObject, xmlDoc, (CShape) c, truetyp);
//		} else if (c instanceof CSprite) {
//			saveSprite(rootObject, xmlDoc, (CSprite) c);
//		} else if (c instanceof CTileMap) {
//			saveTileMap(rootObject, xmlDoc, (CTileMap) c);
//		} else if (c instanceof CRef) {
//			saveCRef(rootObject, xmlDoc, (CRef) c);
//		} else {
//			Toolkit.getDefaultToolkit().beep();
//			System.out
//					.println(c.getClass().getName() + " ist nicht schreibbar");
//		}
//		if (c instanceof CImageObject) {
//			saveImageObject(rootObject, xmlDoc, (CImageObject) c);
//		}
//		if (saveXandY) {
//			rootObject.setAttribute(_x, c.getX() + "");
//			rootObject.setAttribute(_y, c.getY() + "");
//		}
//		layerElement.appendChild(rootObject);
//		return rootObject;
//	}
//
//	private static void saveImageObject(Element rootObject, Document xmlDoc,
//			CImageObject c) {
//		if (c.getRepeatX() != 1) {
//			rootObject.setAttribute(repeatX, c.getRepeatX() + "");
//			rootObject.setAttribute(abstandX, c.getAbstandX() + "");
//		}
//		if (c.getRepeatY() != 1) {
//			rootObject.setAttribute(repeatY, c.getRepeatY() + "");
//			rootObject.setAttribute(abstandY, c.getAbstandY() + "");
//		}
//	}
//
//	/** SVG verteiler */
//	/** Shape verteiler */
//	private static void saveShape(Element rootObject, Document xmlDoc, CShape c,boolean truetyp) {
//		// Kreis
//		if (c instanceof CKreis) {
//			CKreis kreis = (CKreis) c;
//			rootObject.setAttribute(_cx, kreis.getX() + "");
//			rootObject.setAttribute(_cy, kreis.getY() + "");
//			rootObject.setAttribute(_r, kreis.getRadius() + "");
//		}
//		// Oval
//		else if (c instanceof COval) {
//			COval shape = (COval) c;
//			rootObject.setAttribute(_cx, shape.getX() + "");
//			rootObject.setAttribute(_cy, shape.getY() + "");
//			rootObject.setAttribute(_rx, shape.getRadius1() + "");
//			rootObject.setAttribute(_ry, shape.getRadius2() + "");
//		}
//		// Rect
//		else if (c instanceof CRect) {
//			CRect shape = (CRect) c;
//			rootObject.setAttribute(_x, shape.getX() + "");
//			rootObject.setAttribute(_y, shape.getY() + "");
//			rootObject.setAttribute(_width, shape.getWidth() + "");
//			rootObject.setAttribute(_height, shape.getHeight() + "");
//		}
//		// Poly1
//		else if (c instanceof CPoly) {
//			String typ = c.getXMLName();
//			CPoly shape = (CPoly) c;
//			if (typ.equals(line)) {
//				shape.setSelectedPoint(0);
//				rootObject.setAttribute("x1", shape.getCurrendPointXKordinate()
//						+ "");
//				rootObject.setAttribute("y1", shape.getCurrendPointYKordinate()
//						+ "");
//				shape.setSelectedPoint(1);
//				rootObject.setAttribute("x2", shape.getCurrendPointXKordinate()
//						+ "");
//				rootObject.setAttribute("y2", shape.getCurrendPointYKordinate()
//						+ "");
//			} else {
//				rootObject.setAttribute("points", shape.getPointListString());
//			}
//			;
//			if (shape.isEvenodd()) {
//				rootObject.setAttribute("fill-rule", "evenodd");
//			}
//		}
//		// Path
//		else if (c instanceof CPath) {
//			CPath shape = (CPath) c;
//			if(truetyp){
//				rootObject.setAttribute("d", shape.getSVGText());
//			}else{
//				rootObject.setAttribute("d", shape.getInternelText());
//			}
//			if (shape.isEvenodd()) {
//				rootObject.setAttribute("fill-rule", "evenodd");
//			}
//		}
//		// TODO in Allgemein
//		c.getCSS().save(rootObject, xmlDoc);
//	}
//
//	private static void saveSprite(Element rootElement, Document xmlDoc,
//			CSprite c) {
//		if (c.getAnimation() != 0) {
//			rootElement.setAttribute("index", "" + c.getAnimation());
//		}
//		if (c.getName() != null && !c.getName().isEmpty()) {
//			rootElement.setAttribute("name", "" + c.getName());
//		} else if (c.getFile() == null) {
//			rootElement.setAttribute("name", "unnamed");
//		} else {
//			rootElement.setAttribute("name", "" + c.getFile().getName());
//		}
//		if (c.getFile() == null) {
//			c.getSpriteXML().write(rootElement, xmlDoc);
//		} else {
//			rootElement.setAttribute("source", "" + c.getFile());
//		}
//	}
//
//	private static void saveTileMap(Element rootElement, Document xmlDoc,
//			CTileMap c) {
//		rootElement.setAttribute("source", c.getFile().getPath());
//		rootElement.setAttribute("to", c.getMap().getLayerName(c.getEnd()));
//		rootElement.setAttribute("from", c.getMap().getLayerName(c.getStart()));
//	}
//
//	private static void saveCRef(Element rootElement, Document xmlDoc, CRef c) {
//		rootElement.setAttribute("source", c.getFile().getPath());
//		rootElement.setAttribute("to", c.getLayerName(c.getEnd()));
//		rootElement.setAttribute("from", c.getLayerName(c.getStart()));
//	}
}
