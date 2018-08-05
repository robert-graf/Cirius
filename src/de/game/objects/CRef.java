package de.game.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import game.database.MapXML;


public class CRef extends CImageObject {
	File file;
	String bis,von;
	int start,ende;
	CRefMap map;
	ArrayList<CObject> parents;
	public static HashMap<File, CRefMap> hmRefMap = new HashMap<>();
	//TODO Nexus zugang für Script
	//TODO Allgemeinerzugang für Script
	
	public CRef(int x, int y, String source, String von, String bis,
			ArrayList<CObject> parents) {
		setFile(new File(source));
		setX(x);
		setY(y);
		this.bis = bis;
		this.von = von;
		setName(getFile().getName());

		if (parents != null) {
			for (CObject c : parents) {
				if (c instanceof CRef) {
					if (getFile().equals(((CRef) c).getFile())) {
						start = Integer.MAX_VALUE;
						return;
					}
				}
			}
		}
	}
	public CRef(int x, int y, String source) {
		this(x, y, source, "$All$", "", null);
	}

	@Override
	public void drawRenderer(Graphics g, int x, int y) {
		if (start > ende)
			return;
		for (int i = ende; i != start - 1; i--) {
			map.drawLayer(g, i, x, y);
		}
	}
	@Override
	public void updateRenderer(int delta,int index) {
		map.update(delta,index);
	}
	Rectangle r = new Rectangle(0, 0, 0, 0);
	@Override
	public Shape getSingelShape() {
		return r;
	}

	@Override
	public void init() {
		if (hmRefMap.containsKey(getFile())) {
			map = hmRefMap.get(getFile());
		} else {
			map = new CRefMap(getFile());
			hmRefMap.put(getFile(), map);
		}
		if (von.endsWith("$All$")) {
			ende = map.size() - 1;
			start = 0;
		} else {
			if (bis.isEmpty()) {
				ende = map.size() - 1;
			} else {
				for (Layer l : map.getList()) {
					if (l.getName().equals(bis)) {
						ende = map.indexOf(l);
					}
					if (l.getName().equals(von)) {
						start = map.indexOf(l);
					}
				}
			}
		}
	}
	static public String ref = "Ref";
	@Override
	public String getXMLName() {
		return ref;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}

	@SuppressWarnings("unchecked")
	private class CRefMap{
		ArrayList<Layer> map = new ArrayList<>();
		int index = -1;
		public CRefMap(File f) {
			ArrayList<CObject> par;
			if (parents == null) {
				par = new ArrayList<>();
			} else {
				par = ((ArrayList<CObject>) parents.clone());
			}
			par.add(CRef.this);
			//FIXME Funktionen
			new MapXML(map,par, f);
			for (Layer l : map) {
				for (CObject c : l) {
					c.init();
//					c.initScript(engine);
				}
			}
		}
		public int indexOf(Layer l) {
			return map.indexOf(l);
		}
		public ArrayList<Layer> getList() {
			return map;
		}
		public void update(int delta, int index) {
			if(this.index != index){
				for (Layer l : map) {
					for (CObject c : l) {
						c.update(delta,index);
					}
				}
			}
			this.index = index;
		}
		void drawLayer(Graphics g, int i, int x, int y) {
			for (int n = map.get(i).size() - 1; n != -1; n--) {
				CObject c = map.get(i).get(n);
				c.draw(x+(int)c.getX(),y+(int)c.getY(),g);
			}
		}
		public int size() {
			return map.size();
		}
	}

	public int getEnd() {
		return ende;
	}
	public int getStart(){
		return start;
	}
	public String getLayerName(int end) {
		return map.getList().get(end).getName();
	}
}
