package de.game.objects;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import de.game.TiledMap.TiledMap;

public class CTileMap extends CImageObject {

	private static HashMap<String, TiledMap> hmTileMap = new HashMap<String, TiledMap>();
	// init
	String source;
	String bis, von;
	// Runtime
	TiledMap map;
	int ende, start;

	public CTileMap(int x, int y, String source, String von, String bis) {
		this.source = source;
		setX(x);
		setY(y);
		this.bis = bis;
		this.von = von;
	}

	public CTileMap(int x, int y, File f, String name) {
		this.source = f.getAbsolutePath();
		setX(x);
		setY(y);
		this.von = "$All$";
		setName(name);
	}
	@Override
	public void updateRenderer(int delta,int index) {
	}
	@Override
	public void drawRenderer(Graphics g, int px, int py) {
		g.setColor(Color.white);
		for (int i = start; i != ende+1; i++) {
			//TODO Render Einschränken aus das Sichtfeld.
//			map.draw(g1, px, py, LayerIndex, sichtfeld, Raster)
			map.draw(g, px, py, i, 0);
		}
	
	}

	@Override
	public void init() {
		if (hmTileMap.containsKey(source)) {
			map = hmTileMap.get(source);
		} else {
			try {
				map = new TiledMap(new File(source));
				hmTileMap.put(source, map);
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		if (von.endsWith("$All$")) {
			ende = map.getLayerCount()-1;
			start = 0;
		} else {
			ende = map.getLayerIndex(bis);
			if (bis.isEmpty()) {
				ende = map.getLayerCount() - 1;
			}
			start = map.getLayerIndex(von);
		}

	}

	public File getFile() {
		return new File(source);
	}

	@Override
	public String getXMLName() {
		return "TileMap";
	}
	Rectangle r = new Rectangle(getX(), getY(), 0,0);
	@Override
	public Shape getSingelShape() {
		//TODO
//		r.setBounds(getX(), getY(), map.getWidth()*map.getTileWidth(), map.getHeight()*map.getTileHeight());
		return r;
	}
	@Override
	public int getWidth() {
		return map.getWidth()*map.getTileWidth();
	}
	@Override
	public int getHeight() {
		return map.getHeight()*map.getTileHeight();
	}

	public TiledMap getMap() {
		return map;
	}
	public int getEnd() {
		return ende;
	}
	public int getStart() {
		return start;
	}
	public boolean isTileSolid(Shape s,double angle){
		return map.isTileSolid(s,angle,this);
	};
}
