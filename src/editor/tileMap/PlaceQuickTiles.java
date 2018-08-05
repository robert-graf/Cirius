package editor.tileMap;


import editor.tileSet.VerknuepfesTile;

public class PlaceQuickTiles {
	public static final int EDGE_TOP = 0;
	public static final int EDGE_LEFT = 1;
	public static final int EDGE_RIGHT = 2;
	public static final int EDGE_BOT = 3;
	VerknuepfesTile vt;
	int setID=0;

	public PlaceQuickTiles() {
	}

	public PlaceQuickTiles(VerknuepfesTile vt, int setID) {
		this.vt = vt;
		this.setID = setID;
	}

	static byte[][] data = new byte[52][9];
	boolean[][] edgesH;
	boolean[][] edgesV;
	static {
		/**
		 * 0 	1 	2	 3	 4 	5	 6 	7	 8	 9	 10	 11	12 13 
		 * XXX ?#? ?#? ?XX XX? ?#? XX? XXX ?XX ### #XX XX# XXX XXX
		 * XXX #XX XX# #XX XX# XXX XX# XXX #XX #X# XXX XXX XXX XXX
		 * XXX ?XX XX? ?#? ?#? XXX XX? ?#? ?XX ### XXX XXX XX# #XX
		 * 
		 * 9 --> 0 ? --> 9
		 */
		setData(data.length-1, "???" + "???" + "???");
		setData(0 , "XXX" + "X?X" + "XXX");
//		setData(1 , "?#?" + "#?X" + "?XX");Ecke ohne schägen
//		setData(2 , "?#?" + "X?#" + "XX?");
//		setData(3 , "?XX" + "#?X" + "?#?");
//		setData(4 , "XX?" + "X?#" + "?#?");
		setData(1 , "?##" + "#?X" + "#XX");
		setData(2 , "?#?" + "X?#" + "XX#");
		setData(3 , "#XX" + "#?X" + "?##");
		setData(4 , "XX#" + "X?#" + "##?");
		setData(5, "?#?" + "#?X" + "?XX");//Ecke 45°	
		setData(6, "?#?" + "X?#" + "XX?");//Ecke 45°
		setData(7, "?XX" + "#?X" + "?#?");//Ecke 45°
		setData(8, "XX?" + "X?#" + "?#?");//Ecke 45°
		setData(10, "?#?" + "X?X" + "XXX");
		setData(11, "XX?" + "X?#" + "XX?");
		setData(12, "XXX" + "X?X" + "?#?");
		setData(13, "?XX" + "#?X" + "?XX");
		setData(9 , "?#?" + "#?#" + "?#?");
		setData(47, "#XX" + "X?X" + "XXX");
		setData(48, "XX#" + "X?X" + "XXX");
		setData(49, "XXX" + "X?X" + "XX#");
		setData(50, "XXX" + "X?X" + "#XX");
		
//		setData(14, "XXX" + "X?X" + "XXX");  //"Schräg Links Oben innen");	       
//		setData(15, "XXX" + "X?X" + "XXX");  //"Schräg Links Oben außen");	       
//		setData(16, "XXX" + "X?X" + "XXX");  //"Schräg Rechts Oben innen");	   
//		setData(17, "XXX" + "X?X" + "XXX");  //"Schräg Rechts Oben außen");	   
//		setData(18, "XXX" + "X?X" + "XXX");  //"Schräg Rechts Unten innen");	   
//		setData(19, "XXX" + "X?X" + "XXX");  //"Schräg Rechts Unten außen");	   
//		setData(20, "XXX" + "X?X" + "XXX");  //"Schräg Links Unten innen");	   
//		setData(21, "XXX" + "X?X" + "XXX");  //"Schräg Links Unten außen");       
		setData(14, "?X?" + "#?#" + "?X?");  //"Vertikal");                       
		setData(15, "?#?" + "X?X" + "?#?");  //"Horizontal");                     
		setData(16, "?X?" + "#?#" + "?#?");  //"Endstück nach Oben");             
		setData(17, "?#?" + "#?X" + "?#?");  //"Endstück nach Rechts");           
		setData(18, "?#?" + "#?#" + "?X?");  //"Endstück nach Unten");            
		setData(19, "?#?" + "X?#" + "?#?");  //"Endstück nach Links");            
		setData(20, "XXX" + "X?X" + "#X#");  //"Rand mit Streifen nach Unten");   
		setData(21, "#XX" + "X?X" + "#XX");  //"Rand mit Streifen nach Links");   
		setData(22, "#X#" + "X?X" + "XXX");  //"Rand mit Streifen nach Oben");    
		setData(23, "XX#" + "X?X" + "XX#");  //"Rand mit Streifen nach Rechts");  
		setData(24, "#X#" + "X?X" + "#X#");  //"Kreuzung von Streifen");
		setData(25, "#X#" + "X?X" + "#XX");  //"Kreuzung von Streifen(L/O) und Fläche");
		setData(26, "#X#" + "X?X" + "XX#");  //"Kreuzung von Streifen(R/O) und Fläche");
		setData(27, "XX#" + "X?X" + "#X#");  //"Kreuzung von Streifen(R/U) und Fläche");
		setData(28, "#XX" + "X?X" + "#X#");  //"Kreuzung von Streifen(L/U) und Fläche");
		setData(29, "#X#" + "X?X" + "?#?");  //"Horizontal mit Abzweigung nach Oben");
		setData(30, "?#?" + "X?X" + "#X#");  //"Horizontal mit Abzweigung nach Unten");
		setData(31, "?X#" + "#?X" + "?X#");  //"Vertikal mit Abzweigung nach Rechts");
		setData(32, "#X?" + "X?#" + "#X?");  //"Vertikal mit Abzweigung nach Links");
		setData(33, "?X?" + "X?#" + "?#?");  //"Streifen Ecke von Links nach Oben");
		setData(34, "?X?" + "#?X" + "?#?");  //"Streifen Ecke von Rechts nach Oben");
		setData(35, "?#?" + "X?#" + "?X?");  //"Streifen Ecke von Links nach Unten");
		setData(36, "?#?" + "#?X" + "?X?");  //"Streifen Ecke von Rechts nach Unten");
		setData(37, "?X#" + "#?X" + "?XX");  //"Ecke(R/U) mit Streifen nach Oben");
		setData(38, "?#?" + "X?X" + "XX#");  //"Ecke(L/U) mit Streifen nach Rechts");
		setData(39, "XX?" + "X?#" + "#X?");  //"Ecke(L/O) mit Streifen nach Unten");
		setData(40, "#XX" + "X?X" + "?#?");  //"Ecke(R/O) mit Streifen nach Links");
		setData(41, "?X?" + "X?#" + "XX?");  //"Ecke(L/U) mit Streifen nach Oben");
		setData(42, "XX#" + "X?X" + "?#?");  //"Ecke(L/O) mit Streifen nach Rechts");
		setData(43, "?XX" + "#?X" + "?X#");  //"Ecke(R/O) mit Streifen nach Unten");
		setData(44, "?#?" + "X?X" + "#XX");  //"Ecke(R/U) mit Streifen nach Links");
		setData(45, "#XX" + "X?X" + "XX#");  //"Schrägerübergang");
		setData(46, "XX#" + "X?X" + "#XX");   //"Schrägerübergang");
		
		

	}

	private static void setData(int i, String s) {
		for (int n = 0; n != s.length(); n += 1) {
			char c = (char) s.getBytes()[n];
			if (c == '#') {
				data[i][n] = 0;
			} else if (c == 'X'||c == 'x') {
				data[i][n] = 1;
			} else if (c == '?') {
				data[i][n] = -1;
			}

		}
	}

	boolean[] check = new boolean[9];

	public void setTile(Layer layer, int x, int y, boolean add,boolean recursion) {
		if(x < 0 || y < 0||x > layer.width||y > layer.height || vt == null){
			return;
		}
		if(edgesH == null || layer.width > edgesH.length || layer.height > edgesH[0].length){
		updateEdges(layer);
		}
		//Prüfen ob verknüpft Tiels angrenzen
		int poss = 0;
		int id = layer.getTileID(x-1, y-1, true);
		if(((x!= 0 && edgesH[x-1][y])||(y!=0 &&edgesV[x][y-1])))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x  , y-1, true);
		if(edgesH[x][y])check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x+1, y-1, true);
		if(((x!=edgesH.length-1 && edgesH[x+1][y])||(y!=0  &&edgesV[x+1][y-1])))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x-1, y  , true);
		if((edgesV[x][y]))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		check[poss] = false;
		poss+=1;
		id = layer.getTileID(x+1, y  , true);
		if((edgesV[x+1][y]))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x-1, y+1, true);
		if((x!= 0 && y!=edgesH[0].length-1 && edgesH[x-1][y+1]||(y!=edgesV[0].length-1 &&edgesV[x][y+1])))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x  , y+1, true);
		if(edgesH[x][y+1])check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		id = layer.getTileID(x+1, y+1, true);
		if(((x!=edgesH.length-1 && edgesH[x+1][y+1])||(y!=edgesV[0].length-1 &&edgesV[x+1][y+1])))check[poss]=false;else
		check[poss] = vt.containsTile(id);
		poss+=1;
		
		//Typ auswäheln
		for (int n = 0; n != data.length; n += 1) {
			byte[] set = data[n];
			boolean fit = true;
			for (int i = 0; i != check.length; i += 1) {
				if (set[i] == -1) {
				} else if (set[i] == 0) {
					if(check[i])fit = false;
				} else if (set[i] == 1) {
					if(!check[i])fit = false;
				}
				if(!fit)break;
			}
			if(fit){
				
				//Typ gefunden
				if(add|| vt.containsTile(layer.getTileID(x, y, true))){
					//innenecken ausnahmen für schräge tiles
					if(n == 47){
						// 47 wenn 5 zu -1
//						Oben
						if(!vt.containsTile(layer.getTileID(x, y-2, true)) && vt.containsTile(layer.getTileID(x+1, y-2, true))){
							n = -1;
						}else//Links
						 if(!vt.containsTile(layer.getTileID(x-2, y, true)) && vt.containsTile(layer.getTileID(x-2, y+1, true))){
							n= -1;
						}
					}else if(n == 48){
//						// 48 wenn 6 zu -1
						//Oben
						if(!vt.containsTile(layer.getTileID(x, y-2, true)) && vt.containsTile(layer.getTileID(x-1, y-2, true))){
								n = -2;
						}else//Rechts
						if(!vt.containsTile(layer.getTileID(x+2, y, true)) && vt.containsTile(layer.getTileID(x+2, y+1, true))){
							n = -2;
						}
					}else if(n == 49){
						// 49 wenn 7 zu -1
						//Unten
						if(!vt.containsTile(layer.getTileID(x, y+2, true)) && vt.containsTile(layer.getTileID(x+1, y+2, true))){
							n = -3;
						}//Rechts
						else if(!vt.containsTile(layer.getTileID(x+2, y, true)) && vt.containsTile(layer.getTileID(x+2, y-1, true))){
							n = -3;
						}
					}else if(n == 50){
						// 50 wenn 8 zu -1
						//Unten
						if(!vt.containsTile(layer.getTileID(x, y+2, true)) && vt.containsTile(layer.getTileID(x-1, y+2, true))){
							n = -4;
						}//Links
						else if(!vt.containsTile(layer.getTileID(x-2, y, true)) && vt.containsTile(layer.getTileID(x-2, y-1, true))){
							n = -4;
						}
					}
					layer.setTileID(x, y, getTileID(n),setID);
				}
				
				//umschließene Setzen
				if(recursion){
//					System.out.println("recursion");
					setTile(layer, x-1, y-1, false,false);
					setTile(layer, x  , y-1, false,false);
					setTile(layer, x+1, y-1, false,false);
					setTile(layer, x-1, y  , false,false);
					setTile(layer, x+1, y  , false,false);
					setTile(layer, x-1, y+1, false,false);
					setTile(layer, x  , y+1, false,false);
					setTile(layer, x+1, y+1, false,false);
				}
				
				return;
			}
		}
//		if(add && !vt.containsTile(layer.getTileID(x, y, true))){
//			layer.setTileID(x, y, getTileID(9));
//		}
	}

	private int getTileID(int typ) {
		//TODO Gibt Tile id von Entspreche Typ wieder
		return vt.getTileDataByTyp(typ).getTileID();
	}
	public void updateEdges(Layer l ){
		if(edgesV == null || l.width != edgesV.length+1 || l.height != edgesH[0].length+1){
			boolean[][] edgesHT = new boolean[l.width+1][l.height+2];
			boolean[][] edgesVT = new boolean[l.width+2][l.height+1];
			if(edgesV != null){
				for(int x = 0; x!= edgesVT.length&&x!=edgesV.length;x++){
					for(int y = 0; y!= edgesVT[0].length&&x!=edgesV[0].length;y++){
						edgesVT[x][y] = edgesV[x][y];
					}	
				}
				for(int x = 0; x!= edgesHT.length&&x!=edgesH.length;x++){
					for(int y = 0; y!= edgesHT[0].length&&x!=edgesH[0].length;y++){
						edgesHT[x][y] = edgesH[x][y];
					}	
				}
			}
			edgesH=edgesHT;
			edgesV = edgesVT;
			
			
		}
	}
	public void setCurrendVerknüfptesTile(VerknuepfesTile t) {
		vt = t;
	}

	public void setEdge(Layer layer, int x, int y,
			int changeEdge, boolean b) {
		if(changeEdge == EDGE_TOP){
			edgesH[x][y] = b;
		}else if(changeEdge == EDGE_BOT){
			edgesH[x][y+1] = b;
		}else if(changeEdge == EDGE_LEFT){
			edgesV[x][y] = b;
		}else if(changeEdge == EDGE_RIGHT){
			edgesV[x+1][y] = b;
		}
		setTile(layer, x, y, false,true);
//		setTile(layer, x-1, y-1, false,false);
//		setTile(layer, x  , y-1, false,false);
//		setTile(layer, x+1, y-1, false,false);
//		setTile(layer, x-1, y  , false,false);
//		setTile(layer, x+1, y  , false,false);
//		setTile(layer, x-1, y+1, false,false);
//		setTile(layer, x  , y+1, false,false);
//		setTile(layer, x+1, y+1, false,false);
		

	}

}
