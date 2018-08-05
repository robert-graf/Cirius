package editor.tileSet;

import img.Icons;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import editor.AbstractToolAdapter;
import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.PlaceQuickTiles;
import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;
import editor.tileMap.objects.TiledMap.SecretAccess;
import editor.tileSet.VerknuepfesTile.TileData;

public class QuickTile extends AbstractToolAdapter{
	
	JInternalFrame frame = new JInternalFrame("verknuepfte Tiles",true,false,false,true);
	JInternalFrame frame2 = new JInternalFrame("Einstellen von ausgewÃ¤hlten verknuepfte Tiles",true,true,false,true);
	ArrayList<VerknuepfesTile> tiles = new ArrayList<>();
	TileSetEditor tileSetEditor;
	TileSet tileSet;
	JButton pinselArt = new JButton();
	int selctedTile = 0;
	int selctedTile2 = 0;
	JToggleButton addTileToVerknuepfteTiles;
	JDialog preview;
	JSlider slider = new JSlider(2,18,4);
	public QuickTile(final TileSetEditor tileSetEditor, final TileSet tileSet) {
		this.tileSetEditor = tileSetEditor;
		this.tileSet = tileSet;
		frame.setLayout(new BorderLayout());
		JPanel evapanel = new JPanel();
		evapanel.setMaximumSize(new Dimension(30, 30));
		evapanel.setPreferredSize(new Dimension(30, 30));
		evapanel.setLayout(new BoxLayout(evapanel, BoxLayout.X_AXIS));
		
		JButton b = new JButton("+");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tiles.add(new VerknuepfesTile(tileSet));
			}
		});
		evapanel.add(b);
		b = new JButton("-");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tiles.remove(selctedTile);
				selctedTile-=1;
				if(selctedTile == -1)selctedTile = 0;
			}
		});
		evapanel.add(b);
		b = new JButton("*");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame2.setVisible(true);
				tileSetEditor.panel.add(frame2);
			}
		});
		evapanel.add(b);
		b = new JButton("%");
		b.addActionListener(new ActionListener() {
			TiledMap map;
			PlaceQuickTiles place = new PlaceQuickTiles();
			JLabel l = new JLabel();
			Random r = new Random();
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(preview ==null){
					preview = new JDialog((Frame) tileSetEditor.getTopLevelAncestor());
					preview.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment()
							.getMaximumWindowBounds());
					preview.setLayout(new BorderLayout());
					slider.setMajorTickSpacing(4);
					slider.setMinorTickSpacing(1);
					slider.setPaintTicks(true);
					slider.setPaintLabels(true);
					preview.add(slider, BorderLayout.NORTH);
					try {
						SecretAccess[] acc = new SecretAccess[1];
						map = new TiledMap(null,acc);
						acc[0].changeSize(100, 60);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					preview.add(l);
					new Thread(){
						int x = 0; int y = 0;
						int value = slider.getValue();
						@Override
						public void run() {
							while(true){
								place.setCurrendVerknüfptesTile(tiles.get(selctedTile));
								x+=1;
								if(x == map.getWidth()){
									x = 0;
									y +=1;
									 if(y == map.getHeight()){
										 y = 0;
										 x = 0;
										 try {
											 	map.buffer();
												l.setIcon(new ImageIcon(map.images[0]));
												while(value == slider.getValue()){
													Thread.sleep(200);
												}
												value = slider.getValue();
										 } catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
									 }
								}
								if(r.nextInt(slider.getValue())!=0){
									place.setTile(map.layers.get(0), x, y,true, true);
								}else{
									map.layers.get(0).data[x][y][0] = -1;
									map.layers.get(0).data[x][y][1] = -1;
									map.layers.get(0).data[x][y][2] = -1;
									place.setTile(map.layers.get(0), x, y,false, true);
								}
								
								
							}
							
						}
					}.start();
				}
				map.tileSets.clear();
				map.tileSets.add(tileSet);
				place.setCurrendVerknüfptesTile(tiles.get(selctedTile));
				
				
//				for(int x = 0; x!= map.getWidth(); x+=1){
//					for(int y = 0; y != map.getHeight(); x+=1){
//						map.setTileId(0,0,0, tiles.get(selctedTile).getTileData(0).getTileID());
//					}
//				}
//				map.tileSets.clear();
//				map.tileSets.add(tileSet);
//				place.setCurrendVerknuefptesTile(tiles.get(selctedTile));
//				for(int x = 0; x!= map.getWidth(); x+=1){
//					for(int y = 0; y != map.getHeight(); y+=1){
//						if(r.nextInt(10)!=0){
//							place.setTile(map.layers.get(0), x, y,true, true);
//						}else{
//							map.layers.get(0).setTileID(x, y, 0);
//						}
//					}
//				}
////				for(int x = 0; x!= map.getWidth(); x+=1){
////					for(int y = 0; y != map.getHeight(); y+=1){
////						if(r.nextBoolean()){
////							place.setTile(map.layers.get(0), x, y,false, false);
////						}
////					}
////				}
//				map.buffer();
				l.setIcon(new ImageIcon(map.images[0]));
				preview.setVisible(true);
			}
		});
		evapanel.add(b);
		frame.add(evapanel,BorderLayout.NORTH);
		frame.add(new VerknuepfeTilesSelection());
		frame2.setLayout(new BorderLayout());
		JPanel evaPanel2 = new JPanel();
		evaPanel2.setLayout(new BoxLayout(evaPanel2,BoxLayout.X_AXIS));
		addTileToVerknuepfteTiles = new JToggleButton("+");
		evaPanel2.add(addTileToVerknuepfteTiles);
		b = new JButton("-");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tiles.get(selctedTile).remove(selctedTile2);
			}
		});
		evaPanel2.add(b);
		frame2.add(evaPanel2,BorderLayout.NORTH);
		frame2.add(new TileVerknuepfer());
	}
	public QuickTile(TileSetEditor tileSetEditor2, TileSet tileSet2,
			ArrayList<VerknuepfesTile> tiles) {
		this(tileSetEditor2,tileSet2);
		this.tiles = tiles;
	}
	Color c = new Color(255,255,0,100);
	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		if(addTileToVerknuepfteTiles.isSelected()){
			if(tiles.size() == 0){
				tiles.add(new VerknuepfesTile(tileSet));	
			};
			if(selctedTile == -1)return;
			g1.setColor(c);
			for(TileData d : tiles.get(selctedTile).tileData){
				g1.fillRect(d.x*tileSet.tileWidth, d.y*tileSet.tileHeight, tileSet.tileWidth, tileSet.tileHeight);
			}
		}
	}
	public void pack(DefaultEditorPanel panel,int x, int y,int width,int height,int width2 ,int height2) {
		frame.setBounds(x, y, width, height);
		frame.setVisible(true);
		panel.add(frame);
		frame2.setBounds(x+width-width2, y+height, width2, height2);
		frame2.setVisible(true);
		panel.add(frame2);
	}
	@Override
	public void mousePressed(Matrix m, MouseEvent ev) {
		if(addTileToVerknuepfteTiles.isSelected()){
		int x; int y;
		x = (int) m.getX(ev)/tileSet.getImage().getTileWight();
		if(x >=tileSet.getImage().getHorizontalCount()){
			x = tileSet.getImage().getHorizontalCount()-1;
		}
		y = (int)m.getY(ev)/tileSet.getImage().getTileHeight();
		if(y >=tileSet.getImage().getVerticalCount()){
			y = tileSet.getImage().getVerticalCount()-1;
		}
		if(!tiles.get(selctedTile).contains(x,y)){
			tiles.get(selctedTile).addTileData(x, y);
		}
		}
	}
	@Override
	public void mouseDragged(Matrix m, MouseEvent ev) {
		mousePressed(m, ev);
	}
	
	
	@SuppressWarnings("serial")
	private class VerknuepfeTilesSelection extends JPanel implements MouseListener, MouseMotionListener{
		int tw = tileSet.getTileWidth()+1;
		int th = tileSet.getTileHeight()+1;
		public VerknuepfeTilesSelection() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		@Override
		public void paint(Graphics g) {
			if(tiles.size() == 0)return;
			int x = 0,y =0;
			VerknuepfesTile img;
			for(int i = 0; i != tiles.size(); i++){
				img = tiles.get(i);
				g.drawImage(img.getResemblingTile(), x, y, null);
				x+=tw;
				if(x > getWidth()-tileSet.getTileWidth()){
					x = 0;
					y += th;
				}
			}
			Rectangle r = getSelectedRectangel();
			g.setColor(Color.BLACK);
			g.drawRect(r.x, r.y, r.width,r.height);
			g.setColor(Color.WHITE);
			g.drawRect(r.x+1, r.y+1,r.width-2, r.height-2);
			g.setColor(Color.BLACK);
			g.drawRect(r.x+2, r.y+2,r.width-4, r.height-4);
		}
		Rectangle r = new Rectangle();
		public Rectangle getSelectedRectangel() {
			int rowcont = getWidth()/tw;
			int y = th* (selctedTile/rowcont);
			int x = tw * (selctedTile%rowcont);
			r.setBounds(x, y, tileSet.getTileWidth(), tileSet.getTileHeight());
			return r;
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			mousePressed(e);
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			int rowcont = getWidth()/tw;
			int id = e.getX()/tw+e.getY()/th*rowcont;
			if(tiles.size() > id){
				selctedTile = id;
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	@SuppressWarnings("serial")
	private class TileVerknuepfer extends JPanel implements MouseListener, MouseMotionListener{
		int tw = tileSet.getTileWidth()+1;
		int th = tileSet.getTileHeight()+1;
		int xTranslate = 140;
		public Image tiletyp;
		public TileVerknuepfer() {
			addMouseListener(this);
			addMouseMotionListener(this);
			tiletyp = Icons.getImage("TileTyp", 0).getImage();
		}
		@Override
		public void paint(Graphics g1) {
			Graphics2D g = (Graphics2D) g1;
			g.drawImage(tiletyp, 0, 0, null);
			
			if(tiles.size() == 0)return;
			if(selctedTile == -1)return;
			VerknuepfesTile img = tiles.get(selctedTile);
			if(img.tileData.size() <= selctedTile2){
				selctedTile2 = 0;
			}
			if(img.size() ==0)return;
			g.setComposite(AlphaComposite.SrcAtop.derive(0.6f)); 
			for(int i = 0; i != img.size();i+=1){
					Rectangle r = img.getTileData(i).getSelectedTypRectangel();
					g.drawImage(img.getImage(i), r.x+3, r.y+3,18,18, null);
				
			}
			g.setComposite(AlphaComposite.SrcOver.derive(1f)); 
			
			TileData d = img.getTileData(selctedTile2);
			
			Rectangle r = d.getSelectedTypRectangel();
			g.setColor(Color.BLACK);
			g.drawRect(r.x, r.y, r.width,r.height);
			g.setColor(Color.WHITE);
			g.drawRect(r.x+1, r.y+1,r.width-2, r.height-2);
			g.setColor(Color.BLACK);
			g.drawRect(r.x+2, r.y+2,r.width-4, r.height-4);
			g.drawString(d.getTypName(), 5, 200);
			//Tile Drawing
			g.translate(xTranslate, 0);
			int x = 0,y =0;
			
			for(int i = 0; i != img.size(); i++){
				g.drawImage(img.getImage(i), x, y, null);
				x+=tw;
				if(x > getWidth()-xTranslate-tileSet.getTileWidth()){
					x = 0;
					y += th;
				}
			}
			r = getSelectedRectangel();
			g.setColor(Color.BLACK);
			g.drawRect(r.x, r.y, r.width,r.height);
			g.setColor(Color.WHITE);
			g.drawRect(r.x+1, r.y+1,r.width-2, r.height-2);
			g.setColor(Color.BLACK);
			g.drawRect(r.x+2, r.y+2,r.width-4, r.height-4);
		}
		Rectangle r = new Rectangle();
		public Rectangle getSelectedRectangel() {
			int rowcont = (getWidth()-xTranslate)/tw;
			int y = th* (selctedTile2/rowcont);
			int x = tw * (selctedTile2%rowcont);
			r.setBounds(x, y, tileSet.getTileWidth(), tileSet.getTileHeight());
			return r;
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			mousePressed(e);
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		Point p = new Point();
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getX()<xTranslate){
				
					p.setLocation(e.getX()/20, e.getY()/20);
					tiles.get(selctedTile).getTileData(selctedTile2).setTypByPoint(p);
				return;
			}
			int rowcont = (getWidth()-xTranslate)/tw;
			int id = (e.getX()-xTranslate)/tw+e.getY()/th*rowcont;
			if(tiles.get(selctedTile).size() > id){
				selctedTile2 = id;
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	public ArrayList<VerknuepfesTile> getQuickTileList() {
		return tiles;
	}
		
}
