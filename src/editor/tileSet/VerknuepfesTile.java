package editor.tileSet;

import img.Icons;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;


import editor.tileMap.objects.TileSet;

public class VerknuepfesTile {
	static HashMap<Integer, Rectangle> typToRect = new HashMap<>();
	static HashMap<Point,Integer> RectToTyp = new HashMap<>();
	static HashMap<Integer,String> TypToName = new HashMap<>();
	static HashMap<Integer,Integer> replacerList = new HashMap<>();
	static {
		setTypToRect(1,1,0,"Zentrum");
		setTypToRect(0,0,1,"Außenecke Oben Links");
		setTypToRect(2,0,2,"Außenecke Oben Rechts");
		setTypToRect(0,2,3,"Außenecke Unten Links");
		setTypToRect(2,2,4,"Außenecke Unten Rechts");
		setTypToRect(5,0,5,"Schräge außen");
		setTypToRect(6,0,6,"Schräge außen");
		setTypToRect(5,3,7,"Schräge außen");
		setTypToRect(6,3,8,"Schräge außen");
		setTypToRect(1,0,10,"Rand Oben");
		setTypToRect(2,1,11,"Rand Rechts");
		setTypToRect(1,2,12,"Rand Unten");
		setTypToRect(0,1,13,"Rand Links");
		setTypToRect(3,2,9,"Einzelstück");
		setTypToRect(3,0,47,"Innenecke von Oben Links");
		setTypToRect(4,0,48,"Innenecke von Oben Rechts");
		setTypToRect(4,1,49,"Innenecke von Unten Rechts");
		setTypToRect(3,1,50,"Innenecke von Unten Links");
//		setTypToRect(5,1,14,"Schräg Links Oben innen");	
//		setTypToRect(5,0,15,"Schräg Links Oben außen");	
//		setTypToRect(6,1,16,"Schräg Rechts Oben innen");	
//		setTypToRect(6,0,17,"Schräg Rechts Oben außen");	
//		setTypToRect(6,2,18,"Schräg Rechts Unten innen");	
//		setTypToRect(6,3,19,"Schräg Rechts Unten außen");	
//		setTypToRect(5,2,20,"Schräg Links Unten innen");	
//		setTypToRect(5,3,21,"Schräg Links Unten außen");
		setTypToRect(0,4,14,"Vertikal");
		setTypToRect(2,3,15,"Horizontal");
		setTypToRect(0,5,16,"Endstück nach Oben");
		setTypToRect(1,3,17,"Endstück nach Rechts");
		setTypToRect(4,3,18,"Endstück nach Unten");
		setTypToRect(2,4,19,"Endstück nach Links");
		setTypToRect(0,3,20,"Rand mit Streifen nach Unten");
		setTypToRect(3,3,21,"Rand mit Streifen nach Links");
		setTypToRect(4,4,22,"Rand mit Streifen nach Oben");
		setTypToRect(1,4,23,"Rand mit Streifen nach Rechts");
		setTypToRect(0,6,24,"Kreuzung von Streifen");
		setTypToRect(1,5,25,"Kreuzung von Streifen(L/O) und Fläche");
		setTypToRect(2,5,26,"Kreuzung von Streifen(R/O) und Fläche");
		setTypToRect(2,6,27,"Kreuzung von Streifen(R/U) und Fläche");
		setTypToRect(1,6,28,"Kreuzung von Streifen(L/U) und Fläche");
		setTypToRect(3,5,29,"Horizontal mit Abzweigung nach Oben");
		setTypToRect(4,5,30,"Horizontal mit Abzweigung nach Unten");
		setTypToRect(3,6,31,"Vertikal mit Abzweigung nach Rechts");
		setTypToRect(4,6,32,"Vertikal mit Abzweigung nach Links");
		setTypToRect(5,5,33,"Streifen Ecke von Links nach Oben");
		setTypToRect(6,5,34,"Streifen Ecke von Rechts nach Oben");
		setTypToRect(5,6,35,"Streifen Ecke von Links nach Unten");
		setTypToRect(6,6,36,"Streifen Ecke von Rechts nach Unten");
		setTypToRect(1,7,37,"Ecke(R/U) mit Streifen nach Oben");
		setTypToRect(2,7,38,"Ecke(L/U) mit Streifen nach Rechts");
		setTypToRect(2,8,39,"Ecke(L/O) mit Streifen nach Unten");
		setTypToRect(1,8,40,"Ecke(R/O) mit Streifen nach Links");
		setTypToRect(5,7,41,"Ecke(L/U) mit Streifen nach Oben");
		setTypToRect(5,8,42,"Ecke(L/O) mit Streifen nach Rechts");
		setTypToRect(4,8,43,"Ecke(R/O) mit Streifen nach Unten");
		setTypToRect(4,7,44,"Ecke(R/U) mit Streifen nach Links");
		setTypToRect(0,7,45,"Schrägerübergang");
		setTypToRect(0,8,46,"Schrägerübergang");
		setTypToRect(5,1,-1,"Schräge innen");
		setTypToRect(6,1,-2,"Schräge innen");
		setTypToRect(5,2,-4,"Schräge innen");
		setTypToRect(6,2,-3,"Schräge innen");
		
		replacerList.put( 1,0);//"Außenecke Oben Links");
		replacerList.put( 2,0);//"Außenecke Oben Rechts");
		replacerList.put( 3,0);//"Außenecke Unten Links");
		replacerList.put( 4,0);//"Außenecke Unten Rechts");
		replacerList.put( 5,1);//"Schräge außen");
		replacerList.put( 6,2);//"Schräge außen");
		replacerList.put( 7,3);//"Schräge außen");
		replacerList.put( 8,4);//"Schräge außen");
		replacerList.put(10,0);//"Rand Oben");
		replacerList.put(11,0);//"Rand Rechts");
		replacerList.put(12,0);//"Rand Unten");
		replacerList.put(13,0);//"Rand Links");
		replacerList.put( 9,0); //"Einzelstück");
		replacerList.put(47,0);//"Innenecke von Oben Links");
		replacerList.put(48,0);//"Innenecke von Oben Rechts");
		replacerList.put(49,0);//"Innenecke von Unten Rechts");
		replacerList.put(50,0);//"Innenecke von Unten Links");
		replacerList.put(14,15);//"Vertikal");
		replacerList.put(15,0);//"Horizontal");
		replacerList.put(16,14);//"Endstück nach Oben");
		replacerList.put(17,15);//"Endstück nach Rechts");
		replacerList.put(18,14);//"Endstück nach Unten");
		replacerList.put(19,15);//"Endstück nach Links");
		replacerList.put(20,12);//"Rand mit Streifen nach Unten");
		replacerList.put(21,13);//"Rand mit Streifen nach Links");
		replacerList.put(22,10);//"Rand mit Streifen nach Oben");
		replacerList.put(23,11);//"Rand mit Streifen nach Rechts");
		replacerList.put(24,15);//"Kreuzung von Streifen");
		replacerList.put(25,1);//"Kreuzung von Streifen(L/O) und Fläche");
		replacerList.put(26,2);//"Kreuzung von Streifen(R/O) und Fläche");
		replacerList.put(27,4);//"Kreuzung von Streifen(R/U) und Fläche");
		replacerList.put(28,3);//"Kreuzung von Streifen(L/U) und Fläche");
		replacerList.put(29,15);//"Horizontal mit Abzweigung nach Oben");
		replacerList.put(30,15);//"Horizontal mit Abzweigung nach Unten");
		replacerList.put(31,14);//"Vertikal mit Abzweigung nach Rechts");
		replacerList.put(32,14);//"Vertikal mit Abzweigung nach Links");
		replacerList.put(33,14);//"Streifen Ecke von Links nach Oben");
		replacerList.put(34,14);//"Streifen Ecke von Rechts nach Oben");
		replacerList.put(35,14);//"Streifen Ecke von Links nach Unten");
		replacerList.put(36,14);//"Streifen Ecke von Rechts nach Unten");
		replacerList.put(37,1);//"Ecke(R/U) mit Streifen nach Oben");
		replacerList.put(38,2);//"Ecke(L/U) mit Streifen nach Rechts");
		replacerList.put(39,4);//"Ecke(L/O) mit Streifen nach Unten");
		replacerList.put(40,3);//"Ecke(R/O) mit Streifen nach Links");
		replacerList.put(41,2);//"Ecke(L/U) mit Streifen nach Oben");
		replacerList.put(42,4);//"Ecke(L/O) mit Streifen nach Rechts");
		replacerList.put(43,3);//"Ecke(R/O) mit Streifen nach Unten");
		replacerList.put(44,1);//"Ecke(R/U) mit Streifen nach Links");
		replacerList.put(45,3);//"Schrägerübergang");
		replacerList.put(46,4);//"Schrägerübergang");
		replacerList.put(-1,47);//"Schräge innen");
		replacerList.put(-2,48);//"Schräge innen");
		replacerList.put(-4,50);//"Schräge innen");
		replacerList.put(-3,49);//"Schräge innen");
		
		
	}
	private static void setTypToRect(int x,int y,int id,String s){
		typToRect.put(id, new Rectangle(20*x-1,20*y-1,22,22));//Zentrum
		RectToTyp.put(new Point(x,y),id);
		TypToName.put(id, s);
	}
	TileSet tileSet;
	static final Random r = new Random();
	ArrayList<TileData> tileData = new ArrayList<>();
	public VerknuepfesTile(TileSet tileSet) {
		this.tileSet = tileSet;
	}
	public void addTileData(int x, int y, int typ) {
		if(x < 0)return;
		if(y < 0)return;
		TileData t = new TileData(x, y,typ);
		tileData.add(t);
		resemblingTile = t;
	}

	public void addTileData(int x, int y) {
		addTileData(x, y, 0);
	}

	TileData resemblingTile;
	public Image getResemblingTile(){
		if(r.nextInt(100) == 0 && tileData.size()!=1&& tileData.size()!=0){
			resemblingTile = tileData.get(r.nextInt(tileData.size()-1));
		}
		if(resemblingTile == null){
			return Icons.getImage("no_image", 16).getImage();
		}
		return tileSet.getImage(resemblingTile.x,resemblingTile.y);
	}
	public TileData getTileData(int id) {
		return tileData.get(id);
	}
	@Deprecated
	public TileData getTileDataByTyp(int typ) {
		ArrayList<TileData> typdata = new ArrayList<>();
		//Random
		for(TileData d : tileData){
			if(d.typ == typ)typdata.add(d);
		}
		if(typdata.isEmpty()){
			if(typ != 0){
				return getTileDataByTyp(replacerList.get(typ));
			}else{
				return tileData.get(r.nextInt(tileData.size()));
			}
			
		}
		return typdata.get(r.nextInt(typdata.size()));
	}
	public int size() {
		return tileData.size();
	}
	public void remove(int id) {
		tileData.remove(id);
	}
	public boolean contains(int x, int y) {
		for(TileData d : tileData){
			if(d.x == x && d.y == y){
				return true;
			}
		}
		return false;
	}
	public class TileData implements Comparable<TileData>{
		int x = -1;
		int y = -1;
		int typ = 0;
		public TileData(int x,int y, int typ) {
			this.x = x;
			this.y = y;
			this.typ = typ;
		}
		public Rectangle getSelectedTypRectangel() {
			return typToRect.get(typ);
		}
		public String getTypName() {
			return TypToName.get(typ);
		}
		public void setTypByPoint(Point p) {
			try {
				typ = RectToTyp.get(p);
			} catch (Exception e) {
			
			}
		}
		@Override
		public int compareTo(TileData o) {
			return typ-o.typ;
		}
		public int getTyp() {
			return typ;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public int getTileID() {
			
			return tileSet.firstGID + tileSet.getTileID(x, y);
		}
	}
	public Image getImage(int id) {
		TileData t = tileData.get(id);
		return tileSet.getImage(t.x,t.y);
	}

	public ArrayList<TileData> getCopyList() {
		@SuppressWarnings("unchecked")
		ArrayList<TileData> d = (ArrayList<TileData>) tileData.clone();
		Collections.sort(d);
		return d;
	}
	public boolean containsTile(int id) {
		if(id == -1){
			return false;
		}
		if(tileSet.firstGID > id){
			return false;
		}
		if(tileSet.lastGID < id){
			return false;
		}
		int x = tileSet.getTileX(id-tileSet.firstGID);
		int y = tileSet.getTileY(id-tileSet.firstGID);
		for(TileData d : tileData){
			if(d.x == x && d.y == y){
				return true;
			}
		}
		return false;
	}
	public boolean isTileIdTyp(int id, int typ) {
		if(id == -1){
			return false;
		}
		if(tileSet.firstGID > id){
			return false;
		}
		if(tileSet.lastGID < id){
			return false;
		}
		int x = tileSet.getTileX(id-tileSet.firstGID);
		int y = tileSet.getTileY(id-tileSet.firstGID);
		//Random
		for(TileData d : tileData){
			if(d.typ == typ){
				if(d.x == x && d.y == y){
					return true;
				}
			}
		}
		return false;
	}

	
	



	
}
