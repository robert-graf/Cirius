package editor.tileMap;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JToggleButton;

import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.TiledMap;

public class TileRenderer implements AbstractTool {
	TiledMap map;
	boolean transparenz = true;
	int showOne = -1;
	// Image walker,ziel,car,flieg;
	public TileRenderer(TiledMap map, Matrix m) {
		init(m);
		// walker = new
		// ImageIcon(this.getClass().getResource("/img/mover/Walker.png")).getImage();
		// ziel = new
		// ImageIcon(this.getClass().getResource("/img/mover/Ziel.png")).getImage();
		// car = new
		// ImageIcon(this.getClass().getResource("/img/mover/Auto.png")).getImage();
		// flieg = new
		// ImageIcon(this.getClass().getResource("/img/mover/drache.png")).getImage();
		this.map = map;
	}

	@Override
	public void destroy() {
		map = null;
	}

	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
	};

	public void keyPressed(Matrix m, KeyEvent ev) {
	};

	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
	};

	public void mousePressed(Matrix m, MouseEvent ev) {
	};

	public void mouseExited(Matrix m, MouseEvent ev) {
	};

	public void mouseEntered(Matrix m, MouseEvent ev) {
	};

	public void mouseClicked(Matrix m, MouseEvent ev) {
	};

	//
	// mouseMotion
	public void mouseMoved(Matrix m, MouseEvent ev) {
	};

	public void mouseDragged(Matrix m, MouseEvent ev) {
	};

	//
	// mouseWheel
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	};

	// custom
	JToggleButton showWall = new JToggleButton("Zeig Wände für X");

	public void init(Matrix m) {
		m.getPanel().setLayout(null);
		m.setTransX(-200);
		showWall.setBounds(0, 0, 150, 30);
		m.getPanel().add(showWall);
	};

	public void lost(Matrix m, MouseEvent ev) {
	};

	AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
	AlphaComposite alphaSolid = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
	
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		
		if (map.layers.size() != m.getLayerSize()) {
			updateList(m);
		}
		g1.drawRect(-1,-1, map.getWidth()*map.getTileWidth()+1, map.getHeight()*map.getTileHeight()+1);
		for (int LayerCont = 0; LayerCont != map.layers.size(); LayerCont++) {
			if (!m.getLayer(LayerCont).isVisible()) {
				continue;
			}
			if(transparenz && showOne == -1){
				g1.setComposite(alpha);
			}else{
				g1.setComposite(alphaSolid);
			}
			if (showOne != -1 && LayerCont != showOne ) {
				continue;
			}
			if(m.getSelectedLayerIndex() == LayerCont){
				g1.setComposite(alphaSolid);
			}
			map.draw(g1, 0, 0, LayerCont);
//			for (int x = 0; x != map.getWidth(); x++) {
//				for (int y = 0; y != map.getHeight(); y++) {
//					Image i = map.getTileImage(x, y, LayerCont);
//					int x1 = (x) * (map.getTileWidth());
//					int y1 = (y) * (map.getTileHeight());
//					
//					
//					if(i != null){
//						g1.drawImage(i, x1, y1, null);
//					}
//					
//				}
//			}
			g1.setComposite(alphaSolid);
		}
		// g1.drawImage(walker,0,0,null);
		// g1.drawImage(ziel,16,0,null);
		// g1.drawImage(car,32,0,null);
		// g1.drawImage(flieg,48,0,null);
	}

	private void updateList(Matrix m) {
		int index = m.getSelectedLayerIndex();
		while(m.getLayerSize() != 0){
			m.removeLayer(0);
		}
		for (Layer layer : map.layers) {
			m.addLayer(new TileLayer(layer, m));
		}
		if(index == -1){
			index = 0;
		}
		m.setSelectedLayer(index);
	
		if(tlp != null){
			tlp.update();
		}
	}
	TileLayerPanel tlp;
	public void setLayerPanle(TileLayerPanel tileLayerPanel) {
		tlp = tileLayerPanel;
	}

}