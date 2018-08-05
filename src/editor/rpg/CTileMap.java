package editor.rpg;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import basic.DPoint;
import editor.DefaultEditorPanel.Matrix;
import editor.rpg.TiledMapEditor.Teilbar;
import editor.tabelle.OneLine;
import editor.tileMap.objects.TiledMap;

public class CTileMap extends CImageObject implements Teilbar {
	static HashMap<String, TiledMap> hmTileMap = new HashMap<String, TiledMap>();

	// init
	String bis, von;
	// Runtime
	TiledMap map;
	int ende, start;
	Polygon poly;

	public CTileMap(double x, double y, String source, String von, String bis) {
		setFile(source);
		init(x, y);
		this.bis = bis;
		this.von = von;
		init();
		TiledMapManager.THAT.update();
	}

	public CTileMap(double x, double y, File f, String name) {
		setFile(f);
		init(x, y);
		this.von = "$All$";
		setName(name);
		init();
		TiledMapManager.THAT.update();
	}

	OneLine<Integer> LayerContLine = new OneLine<Integer>("LayerCont", 0) {
		@Override
		public void valueChanges(Integer value) {
		}
	};

	@Override
	public void init(double x, double y) {
		super.init(x, y);
		addLine(LayerContLine);
	}

	@Override
	public void drawRenderer(Graphics2D g1, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y) {
		if (start > ende)
			return;
		for (int i = start; i != ende + 1; i++) {
			drawLayer(g1, g2, m, ev, i, x, y);
		}
	}

	public void drawLayer(Graphics2D g1, Graphics2D g2, Matrix m,
			MouseEvent ev, int index, int x, int y) {
		map.draw(g1, x, y, index, m.getVisibleRectangle(), 0);
	}

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.CTileMap.class;
	}

	public String getXMLName() {
		return "Tiledmap";
	}

	@Override
	public void destroy() {
	}

	public void setFile(String source) {
		setFile(new File(source));
	}

	public String getSource() {
		return getFile().getPath();
	}

	public void init() {
		if (hmTileMap.containsKey(getSource())) {
			map = hmTileMap.get(getSource());
			LayerContLine.setValue(map.getLayerCount());
		} else {
			try {
				map = new TiledMap(new File(getSource()));
				LayerContLine.setValue(map.getLayerCount());
				hmTileMap.put(getSource(), map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (von.endsWith("$All$")) {
			ende = map.getLayerCount() - 1;
			start = 0;
		} else {
			ende = map.getLayerIndex(bis);
			if (bis.isEmpty()) {
				ende = map.getLayerCount() - 1;
			}
			start = map.getLayerIndex(von);
		}
	}

	public TiledMap getMap() {
		return map;
	}

	@Override
	public CTileMap clone2() throws CloneNotSupportedException {
		CTileMap tile = new CTileMap(getX(), getY(), getSource(),
				map.getLayerName(getStart()), map.getLayerName(getEnd()));
		return tile;
	}

	@Override
	public void updateRenderer(Matrix m, int timePassed, int conter) {
		if (poly == null || poly.getBounds().x != getX()
				|| poly.getBounds().y != getY()) {
			if (poly == null) {
				poly = new Polygon();
			} else {
				poly.reset();
			}
			int x1 = (int) getX();
			int y1 = (int) getY();
			// Ober links außen
			poly.addPoint(x1, y1);
			// Ober rechs außen
			poly.addPoint(x1 + map.getWidth() * map.getTileWidth(), y1);
			// Unten rechs außen
			poly.addPoint(x1 + map.getWidth() * map.getTileWidth(), y1
					+ map.getHeight() * map.getTileHeight());
			// Unten links außen
			poly.addPoint(x1,
					y1 + map.getHeight() * map.getTileHeight());
			// Oben links außen (verschluss)
			poly.addPoint(x1, y1);
			// Oben links innen
			poly.addPoint(x1 + map.getTileWidth(),
					y1 + map.getTileHeight());
			// Oben rechs innen
			poly.addPoint(
					x1 + map.getWidth() * map.getTileWidth()
							- map.getTileWidth(), y1 + map.getTileHeight());
			// Unten recht innen
			poly.addPoint(
					x1 + map.getWidth() * map.getTileWidth()
							- map.getTileWidth(), y1 + map.getHeight()
							* map.getTileHeight() - map.getTileHeight());
			// Unten links innen
			poly.addPoint(x1 + map.getTileWidth(), y1 + map.getHeight()
					* map.getTileHeight() - map.getTileHeight());
			// Oben links innen (Verschluss)
			poly.addPoint(x1 + map.getTileWidth(),
					y1 + map.getTileHeight());
		}
	}

	public Polygon getSingelShape() {
		if (poly == null) {
			update(null, 0, 0);
		}
		return poly;
	}

	@Override
	public int getLayerCount() {
		return map.getLayerCount();
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
		return map.getLayerName(n);
	}

	@Override
	public int getEnd() {
		return ende;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void reload() {
		try {
			map = new TiledMap(new File(getSource()));
			LayerContLine.setValue(map.getLayerCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void getNextRasterPoint(DPoint calc2, double x, double y) {
		if(!poly.getBounds().contains(x,y))return;
		int h = map.getTileHeight();
		double hcont = (x - getX()) / h;
		if( (x - getX()) % h > h/2)hcont+=1;
		
		int w = map.getTileHeight();
		double wcont = (y - getY()) / w;
		if( (y - getY()) % w > w/2)wcont+=1;
		
		
		calc2.setLocation(getX()+hcont*h, getY()+wcont*w);
	}
}
