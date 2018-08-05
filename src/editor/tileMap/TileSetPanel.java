package editor.tileMap;

import img.Icons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.Transient;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;
import editor.tileSet.QuickTile;
import editor.tileSet.VerknuepfesTile;

@SuppressWarnings("serial")
public class TileSetPanel extends JInternalFrame implements MouseListener, MouseMotionListener {
	TiledMap map;
	public JComboBox<TileSet> tilesChooser;
	TileChoosPanel tcpane;
	int verändereAnsicht = -1;
	int  selectedQuickTile= 0;
	JPanel buttons = new JPanel();
	public TileSetPanel(TiledMap map) {
		super("TileSet",true,true,false,true);
		this.map = map;
		setLayout(new BorderLayout());
		tilesChooser = new JComboBox<>();
		tilesChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(tcpane != null){			
					tcpane.revalidate();
					tcpane.repaint();
				}
			}
		});
		for(int i = 0; i != map.getTileSetCount();i+=1){
			tilesChooser.addItem(map.getTileSet(i));
		}
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.setPreferredSize(new Dimension(28,10));
		final ArrayList<JToggleButton> buttonlist = new ArrayList<>();
		ActionListener listner = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(JToggleButton b : buttonlist){
					b.setSelected(false);
				}
				((JToggleButton) e.getSource()).setSelected(true);
				TileChanceTool.status = buttonlist.indexOf(e.getSource());
			}
		};
		int n = 0;
		JToggleButton b = new JToggleButton(Icons.getImage("draw_not", 0),TileChanceTool.status==n++);
		b.setToolTipText("no draw");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("pencil", 16),TileChanceTool.status==n++);
		b.setToolTipText("free draw");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("shape_square", 16),TileChanceTool.status==n++);
		b.setToolTipText("rect draw");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("shape_square", 16),TileChanceTool.status==n++);
		b.setToolTipText("ring-rect draw");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("draw_clone", 16),TileChanceTool.status==n++);
		b.setToolTipText("drop draw");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("draw_eraser", 16),TileChanceTool.status==n++);
		b.setToolTipText("eraser");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		verändereAnsicht = n;
		b = new JToggleButton(Icons.getImage("pencil", 16),TileChanceTool.status==n++);
		b.setToolTipText("autoTiles");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("shape_square", 16),TileChanceTool.status==n++);
		b.setToolTipText("autoTiles");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("draw_eraser", 16),TileChanceTool.status==n++);
		b.setToolTipText("autoTiles");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		b = new JToggleButton(Icons.getImage("cut_red", 16),TileChanceTool.status==n++);
		b.setToolTipText("autoTilesEdges");
		buttonlist.add(b);
		b.addActionListener(listner);
		buttons.add(b);
		add(buttons, BorderLayout.WEST);
		add(tilesChooser,BorderLayout.NORTH);
		tcpane = new TileChoosPanel();
		tcpane.addMouseListener(this);
		tcpane.addMouseMotionListener(this);
		add(new JScrollPane(tcpane));
		
	}
	Rectangle r3 = new Rectangle();
	int divi;
	private class TileChoosPanel extends JPanel{
		Dimension d = new Dimension();
		@Override
		@Transient
		public Dimension getPreferredSize() {
			TileSet set = (TileSet)tilesChooser.getSelectedItem();
			if(set!=null){
				d.setSize((set.getTileWidth()+1)*set.getHorizontalCount(),(set.getTileHeight()+1)* set.getVerticalCount());
			}
			return d;
		}
		@Override
		public void paint(Graphics g) {
			TileSet set = (TileSet)tilesChooser.getSelectedItem();
			if(set == null){
				g.drawString("Drag and Drop a TileSet", 20, 20);
				return;
			}else if(verändereAnsicht > TileChanceTool.status){
			for(int x = 0; x != set.getHorizontalCount(); x +=1){
				for(int y = 0; y != set.getVerticalCount(); y +=1){
					g.drawImage(set.getImage(x, y), 
							(set.getTileWidth())*x, 
							(set.getTileHeight())* y, null);
				}
			}
			
//			if(x2 < set.getTileWidth() && x2 > 0){
//				x2 = set.getTileWidth();
//			}else if(x2 > -set.getTileWidth() && x2 < 0){
//				x2 = -set.getTileWidth();
//			}
//			if(y2 < set.getTileHeight() && y2 > 0){
//				y2 = set.getTileHeight();
//			}else if(y2 > -set.getTileHeight() && y2 < 0){
//				y2 = -set.getTileHeight();
//			}
			Rectangle r = getSelectedRectangel(set);
			g.setColor(Color.BLACK);
			g.drawRect(r.x, r.y, r.width,r.height);
			g.setColor(Color.WHITE);
			g.drawRect(r.x+1, r.y+1,r.width-2, r.height-2);
			g.setColor(Color.BLACK);
			g.drawRect(r.x+2, r.y+2,r.width-4, r.height-4);
			}else{
				Rectangle r = r3;
				int index = 0;
				divi = getWidth()/set.getTileWidth();
				for(VerknuepfesTile q : set.qt){
					g.drawImage(q.getResemblingTile(),set.getTileWidth()*(index%divi),set.getTileHeight()*(index/divi),null);
					
					index++;
				}
				r.x = set.getTileWidth()*(selectedQuickTile%divi);
				r.y = set.getTileHeight()*(selectedQuickTile/divi);
				r.height = set.getTileHeight();
				r.width = set.getTileWidth();
				g.setColor(Color.BLACK);
				g.drawRect(r.x, r.y, r.width,r.height);
				g.setColor(Color.WHITE);
				g.drawRect(r.x+1, r.y+1,r.width-2, r.height-2);
				g.setColor(Color.BLACK);
				g.drawRect(r.x+2, r.y+2,r.width-4, r.height-4);
				
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(verändereAnsicht > TileChanceTool.status){
		endPoint.setLocation(e.getX(),e.getY());
		}else{
			TileSet set = (TileSet)tilesChooser.getSelectedItem();
				selectedQuickTile = (e.getX()/set.getTileWidth()+e.getY()/set.getTileHeight()*(divi));
				if(selectedQuickTile >= set.qt.size()){
					selectedQuickTile = set.qt.size()-1;
				}
		}
	}
		

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	Point startPoint = new Point(0,0);
	Point endPoint = new Point(2,2);
	@Override
	public void mousePressed(MouseEvent e) {
		if(verändereAnsicht > TileChanceTool.status){
		startPoint.setLocation(e.getX(),e.getY());
		endPoint.setLocation(e.getX(),e.getY());
		}else{
			mouseDragged(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(verändereAnsicht > TileChanceTool.status){
		endPoint.setLocation(e.getX(),e.getY());
		}
	}

	public int getRelativeTileID(int x, int y) {
		TileSet set = (TileSet)tilesChooser.getSelectedItem();
		Rectangle r = getSelectedGrid(set);
		x %= r.width;
		y %= r.height;
		if(x < 0){
			x += r.width;
		}
		if(y < 0){
			y += r.height;
		}
		
		return set.firstGID+r.x+x+((r.y+y)*set.getHorizontalCount());
		
	}
	
	public int getRelativeRingSquerTileID(int x, int y,int width, int height) {
		TileSet set = (TileSet)tilesChooser.getSelectedItem();
		Rectangle r = getSelectedGrid(set);
		//Oben links
		if(x == 0 && y == 0)
		return getRelativeTileID(0,0);
		//Unten links
		if(x == 0 && y == height-1)
		return getRelativeTileID(0,r.height-1);
		//Oben rechts
		if(x == width-1 && y == 0)
		return getRelativeTileID(r.width-1,0);		
		//Unten recht
		if((x == width-1 && y == height-1))
		return getRelativeTileID(r.width-1,r.height-1);
		//Oben 
		if(y == 0 && r.width > 2){
		x = 1+(x-1)%(r.width-2);
		return getRelativeTileID(x,0);
		}
		//Unten 
		if(y == height-1 && r.width > 2){
		x = 1+(x-1)%(r.width-2);
		return getRelativeTileID(x,r.height-1);
		}
		//link 
		if(x == 0 && r.height > 2){
		y = 1+(y-1)%(r.height-2);
		return getRelativeTileID(0,y);
		}
		//recht 
		if(x == width-1 && r.height > 2){
		y = 1+(y-1)%(r.height-2);
		return getRelativeTileID(r.width-1,y);
		}
		//Zentrum 
		if(r.height > 2 && r.width > 2){
		x = 1+(x-1)%(r.width-2);
		y = 1+(y-1)%(r.height-2);
		return getRelativeTileID(x,y);
		}

		return -1;
	}
	
	private Rectangle r = new Rectangle();
	private Rectangle r2 = new Rectangle();
	//Gibt Rechteck in tiels (nicht in pxl) zurück das gerade Akiv ist.
	public Rectangle getSelectedGrid(TileSet set){
		r2 = getSelectedRectangel(set);
		int w = set.getTileWidth();
		int h = set.getTileHeight();
		
		r2.setBounds(r2.x/w,r2.y/h,r2.width/w,r2.height/h);
		return r2;
	}
	public Rectangle getSelectedRectangel(TileSet set){
		Point a = startPoint;
		Point b = endPoint;
		int x = a.x < b.x ? a.x : b.x;
		int y = a.y < b.y ? a.y	: b.y;
		int x2 = a.x > b.x ? a.x : b.x;
		int y2 = a.y > b.y ? a.y : b.y;
		
		x -= x%set.getTileWidth();
		y -= y%set.getTileHeight();
		int width =Math.abs(x -x2);
		int height= Math.abs(y-y2);
		width -= width%set.getTileWidth();
		height -= height%set.getTileHeight();
		width += set.getTileWidth();
		height += set.getTileHeight();
		if(x + width > set.getHorizontalCount()*set.getTileWidth()){
			width = set.getHorizontalCount()*set.getTileWidth()-x;
		}
		if(y + height > set.getVerticalCount()*set.getTileHeight()){
			height = set.getVerticalCount()*set.getTileHeight()-y;
		}
		r.setBounds(x, y, width, height);
		return r;
	}

	public void addTileSet(File f) {
		int i = map.containsTileSet(f);
		if(i != -1){
			tilesChooser.setSelectedIndex(i);
		}else{
			TileSet set = new TileSet(f, map);
			map.addtileSet(set);
			tilesChooser.addItem(set);
			tilesChooser.setSelectedIndex(tilesChooser.getItemCount()-1);
		}
	}

	public int getSelectedTileContWidth() {
		return getSelectedGrid((TileSet)tilesChooser.getSelectedItem()).width;
	}
	public int getSelectedTileContHeight() {
		return getSelectedGrid((TileSet)tilesChooser.getSelectedItem()).height;
	}
}
