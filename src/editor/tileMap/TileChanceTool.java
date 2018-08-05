package editor.tileMap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;
import editor.tileMap.objects.TiledMap.SecretAccess;
import interfaces.UndoObject;

public class TileChanceTool implements AbstractTool{
	private TiledMap tile;
	private SecretAccess access;
	private TileSetPanel tileSet;
		public TileChanceTool(TiledMap tile, SecretAccess access, TileSetPanel tileSet2) {
		this.tile = tile;
		this.access = access;
		this.tileSet = tileSet2;
	}
	private static int n = 0;
	public final static int STATUS_NO_DRAW = n++;
	public final static int STATUS_FREE_DRAW = n++;
	public final static int STATUS_SQUERE_DRAW = n++;
	public final static int STATUS_RING_SQUERE_DRAW = n++;
	public final static int STATUS_DROP_DRAW = n++;
	public final static int STATUS_ERASE = n++;
	public final static int STATUS_AUTO_TILE = n++;
	public final static int STATUS_AUTO_TILE_SQUERE = n++;
	public final static int STATUS_AUTO_TILE_ERASE = n++;
	public final static int STATUS_AUTO_TILE_EDGES = n++;
	
	static int status = STATUS_FREE_DRAW;
	PlaceQuickTiles placeQuickTiles = new PlaceQuickTiles();
	
	@SuppressWarnings("serial")
	Point startPoint = new Point(){
		public void setLocation(int x, int y) {
			if(x < 0)x=0;
			if(y < 0)y=0;
			int w = tile.getWidth()*tile.getTileWidth();
			if(x > w)x=w;
			int h = tile.getHeight()*tile.getTileHeight();
			if(y > h)y=h;	
			super.setLocation(x, y);
		};
	};
	@SuppressWarnings("serial")
	Point currendPoint = new Point(){
		public void setLocation(int x, int y) {
			if(x < 0)x=0;
			if(y < 0)y=0;
			int w = tile.getWidth()*tile.getTileWidth();
			if(x > w)x=w;
			int h = tile.getHeight()*tile.getTileHeight();
			if(y > h)y=h;	
			super.setLocation(x, y);
		};
	};
	int[][][] oldData;
	Layer layer;
	//FREE_DRAW
	//STATUS_SQUERE_DRAW
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(Matrix m, KeyEvent ev) {}

	@Override
	public void keyReleased(Matrix m, KeyEvent ev) {
		
	}

	@Override
	public void keyPressed(Matrix m, KeyEvent ev) {}

	@Override
	public void mouseReleased(Matrix m, MouseEvent ev) {
			currendPoint.setLocation(m.getX(ev), m.getY(ev));
			changeLayer();
			showIfLayerChanges(m);
			createUNDO(m);
		
	}

	int button = 0;
	boolean shiftdown = false;
	@Override
	public void mousePressed(Matrix m, MouseEvent ev) {
		button = ev.getButton();
		shiftdown = ev.isShiftDown();
		startPoint.setLocation(m.getX(ev),m.getY(ev));
		currendPoint.setLocation(m.getX(ev),m.getY(ev));
		showIfLayerChanges(m);

	}

	@Override
	public void mouseExited(Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(Matrix m, MouseEvent ev) {
		showIfLayerChanges(m);
	}

	@Override
	public void mouseClicked(Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(Matrix m, MouseEvent ev) {
		
		currendPoint.setLocation(m.getX(ev), m.getY(ev));
		changeLayer();
	}

	@Override
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Matrix m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lost(Matrix m, MouseEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		boolean h[][] = placeQuickTiles.edgesH;
		boolean v[][] = placeQuickTiles.edgesV;
		if(h ==null)return;
		if(status<STATUS_AUTO_TILE)return;
		int tw = tile.getTileHeight();
		int th = tile.getTileWidth();
		for(int x = 0; x!= h.length;x++){
			for(int y = 0; y!= h[0].length;y++){
				g1.translate(0, 1);
				g1.setColor(Color.BLACK);
				if(h[x][y])g1.drawLine(tw*x+1, th*y, tw*(x+1)-1, th*y);
				g1.translate(0, -2);
				if(h[x][y])g1.drawLine(tw*x+1, th*y, tw*(x+1)-1, th*y);
				g1.translate(0, 1);
				g1.setColor(Color.white);
				if(h[x][y])g1.drawLine(tw*x, th*y, tw*(x+1), th*y);
				
			}	
		}
		for(int x = 0; x!= v.length;x++){
			for(int y = 0; y!=v[0].length;y++){
				g1.translate(1, 0);
				g1.setColor(Color.BLACK);
				if(v[x][y])g1.drawLine(tw*x, th*y+1, tw*x, th*(y+1)-1);	
				g1.translate(-2, 0);
				if(v[x][y])g1.drawLine(tw*x, th*y+1, tw*x, th*(y+1)-1);	
				g1.translate(1, 0);
				g1.setColor(Color.white);
				if(v[x][y])g1.drawLine(tw*x, th*y, tw*x, th*(y+1));
			}	
		}
//		Layer l = access.getLayers().get(m.getSelectedLayerIndex());
//		l.setTileID(x, y, tile)
	}
	
	
	private void changeLayer(){
		if(button == 3)return;
		if(button == 2)return;
		updateOldLayerSize();
		int tw = tile.getTileWidth();
		int th = tile.getTileHeight();
		int startX = startPoint.x/tw;
		int startY = startPoint.y/th;
		int currendX = currendPoint.x/tw;
		int currendY = currendPoint.y/th;	
		int setID = (int)tileSet.tilesChooser.getSelectedIndex();
		
		if(status == STATUS_FREE_DRAW){
			int i = tileSet.getRelativeTileID(currendX-startX,currendY-startY);
			layer.setTileID(currendX, currendY, i, setID);
		}else if(status == STATUS_ERASE){
			layer.setTileID(currendX, currendY, 0, setID);
		}else if(status == STATUS_DROP_DRAW){
			int width = tileSet.getSelectedTileContWidth();
			int height = tileSet.getSelectedTileContHeight();
			
			for(int x = 0; x != layer.width; x+=1){
				for(int y = 0; y != layer.height; y+=1){
					int i = oldData[x][y][2]; 
					layer.setTileID(x, y, i, oldData[x][y][0]);
				}
			}
			for(int x = 0; x != width; x+=1){
				for(int y = 0; y != height; y+=1){
					int i = tileSet.getRelativeTileID(x,y);
					layer.setTileID(currendX+x, currendY+y, i, setID);
				}
			}
		}else if(status == STATUS_SQUERE_DRAW){
			int width = Math.abs(currendX-startX);
			int height = Math.abs(currendY-startY);
			int invertX = 1,invertY = 1;
			if(currendX < startX){
				invertX = -1;
				startX -= 1;
			}
			if(currendY < startY){
				invertY = -1;
				startY -= 1;
			}
			for(int x = 0; x != layer.width; x+=1){
				for(int y = 0; y != layer.height; y+=1){
					int i = oldData[x][y][2]; 
					layer.setTileID(x, y, i, oldData[x][y][0]);
				}
			}
			for(int x = 0; x != width; x+=1){
				for(int y = 0; y != height; y+=1){
					int xid = x*invertX;
					if(invertX == -1)xid-=1;
					int yid = y*invertY;
					if(invertY == -1)yid-=1;
					int i = tileSet.getRelativeTileID(xid,yid);
					
					layer.setTileID(startX+x*invertX, startY+y*invertY, i, setID);
				}
			}
			
		
		
		}else if(status == STATUS_RING_SQUERE_DRAW){
			int width = Math.abs(currendX-startX);
			int height = Math.abs(currendY-startY);
			if(currendX < startX){
				startX -= width;
			}
			if(currendY < startY){
				startY -= height;
			}
			for(int x = 0; x != layer.width; x+=1){
				for(int y = 0; y != layer.height; y+=1){
					int i = oldData[x][y][2]; 
					layer.setTileID(x, y, i, oldData[x][y][0]);
				}
			}
			for(int x = 0; x != width; x+=1){
				for(int y = 0; y != height; y+=1){
					
					int i = tileSet.getRelativeRingSquerTileID(x,y,width,height);
					if(i == -1)continue;
					layer.setTileID(startX+x, startY+y, i, setID);
				}
			}
			
		
		
		}else if(status == STATUS_AUTO_TILE){
			TileSet set = (TileSet)tileSet.tilesChooser.getSelectedItem();
			if(set.qt.isEmpty())return;
			placeQuickTiles.setID = setID;
			placeQuickTiles.vt = set.qt.get(tileSet.selectedQuickTile);
			placeQuickTiles.setTile(layer, currendX, currendY, true, true);

		}else if(status == STATUS_AUTO_TILE_SQUERE){
			TileSet set = (TileSet)tileSet.tilesChooser.getSelectedItem();
			if(set.qt.isEmpty())return;
			int width = Math.abs(currendX-startX);
			int height = Math.abs(currendY-startY);
			int invertX = 1,invertY = 1;
			if(currendX < startX){
				invertX = -1;
				startX -= 1;
			}
			if(currendY < startY){
				invertY = -1;
				startY -= 1;
			}
			for(int x = 0; x != layer.width; x+=1){
				for(int y = 0; y != layer.height; y+=1){
					int i = oldData[x][y][2]; 
					layer.setTileID(x, y, i, oldData[x][y][0]);
				}
			}
			
			placeQuickTiles.setID = setID;
			placeQuickTiles.vt = set.qt.get(tileSet.selectedQuickTile);
			
			for(int x = 0; x != width; x+=1){
				for(int y = 0; y != height; y+=1){
					placeQuickTiles.setTile(layer, startX+x*invertX, startY+y*invertY, true, true);
				}
			}
			

		}else if(status == STATUS_AUTO_TILE_ERASE){
			TileSet set = (TileSet)tileSet.tilesChooser.getSelectedItem();
			if(set.qt.isEmpty())return;
			placeQuickTiles.setID = setID;
			placeQuickTiles.vt = set.qt.get(tileSet.selectedQuickTile);
			layer.setTileID(currendX, currendY, 0, setID);
			placeQuickTiles.setTile(layer, currendX, currendY, false, true);

		}else if(status == STATUS_AUTO_TILE_EDGES){
			placeQuickTiles.updateEdges(layer);
			int left = currendPoint.x%tw;
			int top = currendPoint.y%th;
			int changeEdge = -1;
			if(top<5 && left < 5||top<5 && Math.abs(left-tw) < 5||Math.abs(top-th)<5 && left < 5||Math.abs(top-th)<5 && Math.abs(left-tw) < 5){
				
			}else if(top*2<th){
				if(left*2<tw){
					if(top<left){
						changeEdge = PlaceQuickTiles.EDGE_TOP;
					}else{
						changeEdge = PlaceQuickTiles.EDGE_LEFT;
					}
				}else{
					if(top<Math.abs(left-tw)){
						changeEdge = PlaceQuickTiles.EDGE_TOP;
					}else{
						changeEdge = PlaceQuickTiles.EDGE_RIGHT;
					}
				}
			}else{
				if(left*2<tw){
					if(Math.abs(top-th)<left){
						changeEdge = PlaceQuickTiles.EDGE_BOT;
					}else{
						changeEdge = PlaceQuickTiles.EDGE_LEFT;
					}
				}else{
					if(Math.abs(top-th)<Math.abs(left-tw)){
						changeEdge = PlaceQuickTiles.EDGE_BOT;
					}else{
						changeEdge = PlaceQuickTiles.EDGE_RIGHT;
					}
				}
			}
			placeQuickTiles.setEdge(layer, currendX, currendY,changeEdge,!shiftdown);

//			TODO TileSet set = (TileSet)tileSet.tilesChooser.getSelectedItem();
//			PlaceQuickTiles place = new PlaceQuickTiles(set.qt.get(tileSet.selectedQuickTile), setID);
//			place.setTile(layer, currendX, currendY, true, true);

		}
		tile.buffer();
	}
	
	
	private void updateOldLayerSize() {
		if(oldData.length != layer.width || oldData[0].length != layer.height){
			oldData = layer.data;
		}
	}

	private void createUNDO(Matrix m){
		if(showIfLayerChanges(m)){
			return;
		}
		m.addUndo(new UndoChanges(oldData, layer));
		oldData = layer.cloneData();
	}
	public boolean showIfLayerChanges(Matrix m){
//		if(access.getLayers().size() == 0){
//			return false;
//		}
		if(m.getSelectedCObjectIndex() != -1 && layer == null || access.getLayers().get(m.getSelectedLayerIndex()) != layer){
			if(m.getSelectedLayerIndex() == -1){
				if(m.getLayerSize() == 0){
					m.addLayer(new editor.Layer("default_Layer", m));
				}
			m.setSelectedLayer(0);
			}
			layer = access.getLayers().get(m.getSelectedLayerIndex());
			oldData = layer.cloneData();
			return true;
		}
		
		return false;
	}
	private class UndoChanges extends UndoObject{
		int[][][] olddata_;
		int[][][] newdata;
		Layer oldLayer;
		public UndoChanges(int[][][] olddata,Layer oldLayer) {
			super(-1, olddata, olddata, oldLayer);
			this.oldLayer = oldLayer;
			this.olddata_ = oldData;
		}
		@Override
		public void doRedo() {
			oldLayer.resetData(newdata);
			TileChanceTool.this.oldData = oldLayer.cloneData();
			tile.buffer();
		}

		@Override
		public void doUndo() {
			newdata = oldLayer.cloneData();
			oldLayer.resetData(olddata_);
			TileChanceTool.this.oldData = oldLayer.cloneData();
			tile.buffer();
		}

		@Override
		public String getName() {
			return "tiles changes";
		}
		
	}
}
