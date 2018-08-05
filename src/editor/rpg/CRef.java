package editor.rpg;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import database.MapXML;
import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;
import editor.rpg.TiledMapEditor.Teilbar;
import editor.tabelle.OneLine;
import interfaces.FileHolder;
import interfaces.HasPanel;

public class CRef extends CImageObject implements HasPanel, Teilbar, FileHolder {
	// TODO Funktionen
	public static HashMap<File, CRefMap> hmRefMap = new HashMap<>();
	OneLine<Integer> LayerContLine = new OneLine<Integer>("LayerCont", 0) {
		@Override
		public void valueChanges(Integer value) {
		}
	};

	private ArrayList<CObject> parents;
	// init
	private String bis, von;
	// Runtime
	private CRefMap map;
	int ende, start;
	Polygon poly;

	public CRef(double x, double y, String source, String von, String bis,
			ArrayList<CObject> parents) {
		setFile(new File(source));
		init(x, y);
		this.bis = bis;
		this.von = von;
		setName(getFile().getName());

		if (parents != null) {
			for (CObject c : parents) {
				if (c instanceof FileHolder) {
					if (getFile().equals(((FileHolder) c).getFile())) {
						poly = new Polygon();
						start = Integer.MAX_VALUE;
						return;
					}
				}
			}
		}
		this.parents = parents;
		addLine(LayerContLine);
		LayerContLine.setEditable(false);
		init();
		reload();
	}

	public CRef(double x, double y, String source) {
		this(x, y, source, "$All$", "", null);
	}

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.CRef.class;
	}

	@Override
	public CRef clone2() throws CloneNotSupportedException {
		String von = map.getList().get(start).getName();
		String bis = map.getList().get(ende).getName();
		CRef ref = new CRef(getX(), getY(), getFile().getPath(), von, bis,
				parents);
		return ref;
	}

	@Override
	public void drawRenderer(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y) {
		if (start > ende)
			return;
		for (int i = ende; i != start - 1; i--) {
			map.drawLayer(g, g2, m, ev, i, x, y);
		}
	}

	@Override
	public void updateRenderer(Matrix m, int timePassed, int conter) {
		map.update(m, timePassed, conter);
		// Polygon
		int width = (int)map.getBounds().getHeight();
		int height = (int) map.getBounds().getWidth();
		int tilewidth = 16;
		if (poly == null || poly.getBounds().x != getX()
				|| poly.getBounds().y != getY()) {
			if (poly == null) {
				poly = new Polygon();
			} else {
				poly.reset();
			}
			int x1 = (int) getX();
			int y1 = (int) getY();
			
			// Ober links auﬂen
			poly.addPoint(x1, y1);
			// Ober rechs auﬂen
			poly.addPoint(x1 + width, y1);
			// Unten rechs auﬂen
			poly.addPoint(x1 + width, y1 + height);
			// Unten links auﬂen
			poly.addPoint(x1, y1 + height);
			// Oben links auﬂen (verschluss)
			poly.addPoint(x1, y1);
			// Oben links innen
			poly.addPoint(x1 + tilewidth, y1 + tilewidth);
			// Oben rechs innen
			poly.addPoint(x1 + width - tilewidth, y1 + tilewidth);
			// Unten recht innen
			poly.addPoint(x1 + width - tilewidth, y1 + height
					- tilewidth);
			// Unten links innen
			poly.addPoint(x1 + tilewidth, y1 + height - tilewidth);
			// Oben links innen (Verschluss)
			poly.addPoint(x1 + tilewidth, y1 + tilewidth);
		}
	}

	@Override
	public Shape getSingelShape() {
		return poly;
	}

	static public String ref = "Ref";

	@Override
	public String getXMLName() {
		return ref;
	}

	public void reload() {
		map.reloud();
	}

	@SuppressWarnings("unchecked")
	public void init() {
		if (hmRefMap.containsKey(getFile())) {
			map = hmRefMap.get(getFile());
		} else {
			ArrayList<CObject> par;
			if (parents == null) {
				par = new ArrayList<>();
			} else {
				par = ((ArrayList<CObject>) parents.clone());
			}
			par.add(this);
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
		LayerContLine.setValue(map.size());
	}

	public int getLayerCount() {
		return map.size();
	}

	@Override
	public void setEnd(int ende) {
		this.ende = ende;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public String getLayerName(int n) {
		return map.getList().get(n).getName();
	}

	@Override
	public int getEnd() {
		return ende;
	}

	@Override
	public int getStart() {
		return start;
	}

	// Interne Classe
	@SuppressWarnings("unchecked")
	private class CRefMap {
		long lastModi = 0;
		ArrayList<Layer> map = new ArrayList<>();
		int index = -1;
		// Bounds
		private Rectangle2D.Double r = new Rectangle2D.Double();
		private double x, y, x2, y2;
		File file;

		public CRefMap(File f) {
			file = f;
			reloud();
		}

		public Rectangle2D getBounds() {
			return r;
		}

		public void reloud() {
			if (getFile().lastModified() != lastModi) {
				map.clear();
				ArrayList<CObject> par;
				if (parents == null) {
					par = new ArrayList<>();
				} else {
					par = ((ArrayList<CObject>) parents.clone());
				}
				par.add(CRef.this);
				new MapXML(map, par).read(getFile());
			}
			LayerContLine.setValue(map.size());
			// Bounds
			for (Layer l : map) {
				for (CObject c : l) {
					if (x > c.getY()) {
						x = c.getX();
					}
					if (y > c.getY()) {
						y = c.getY();
					}
					if (x2 < c.getX() + c.getWidth()) {
						x2 = c.getX() + c.getWidth();
					}
					if (y2 < c.getY() + c.getHeight()) {
						y2 = c.getY() + c.getHeight();
					}
				}
			}
			r.setFrame(x, y, x2 - x, y2 - y);
			lastModi = getFile().lastModified();
		}

		public int indexOf(Layer l) {
			return map.indexOf(l);
		}

		public ArrayList<Layer> getList() {
			return map;
		}

		public void update(Matrix m, int delta, int index) {
			if (this.index != index) {
				for (Layer l : map) {
					l.getCSS().setParent(getCSS());
					for (CObject c : l) {
						c.update(m, delta, index);
					}
				}
			}
			this.index = index;
		}

		void drawLayer(Graphics2D g, Graphics2D g2, Matrix m, MouseEvent ev,
				int i, int x, int y) {
			for (int n = map.get(i).size() - 1; n != -1; n--) {
				CObject c = map.get(i).get(n);
				// TODO Performent?
				c.setX(c.getX() + x, false);
				c.setY(c.getY() + y, false);
				c.draw1(g, g2, m,map.get(i), ev);
				c.draw3(g, g2, m, ev);
				c.setX(c.getX() - x, false);
				c.setY(c.getY() - y, false);
			}
		}

		public int size() {
			return map.size();
		}

		public File getFile() {
			return file;
		}
	}// ENDE INTERNE CLASSW
}
