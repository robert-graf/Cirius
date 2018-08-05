package editor.tileSet;

import img.Icons;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editor.AbstractToolAdapter;
import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.SpriteSheet;
import editor.tileMap.objects.TileSet;

public class TileSetter extends AbstractToolAdapter implements ActionListener{
	int x = 0;
	int y = 0;
	JInternalFrame frame = new JInternalFrame("Wände",true,false,false,true);
	TileSetEditor tileSetEditor;
	TileSet tileSet;
	JToggleButton isPinsel;
	ArrayList<JButton> buttons = new ArrayList<>();
	JSpinner costSpinner;
	int pinseltyp = 0;
	JCheckBox showClipping = new JCheckBox("",true);
	JButton pinselArt = new JButton();
	public TileSetter(TileSetEditor tileSetEditor, TileSet tileSet) {
		this.tileSetEditor = tileSetEditor;
		this.tileSet = tileSet;
		frame.setLayout(new BorderLayout());
		JPanel cliping = new JPanel();
		cliping.setMaximumSize(new Dimension(100, 100));
		cliping.setPreferredSize(new Dimension(100, 100));
		cliping.setLayout(new GridLayout(3, 3));
		for(int i = 0; i != 6; i++){
			JButton b = new JButton();
			buttons.add(b);
			b.addActionListener(this);
		}
		buttons.get(5).setText("R");
		int i = 0;
		//row 1
		cliping.add(new JLabel());
		cliping.add(buttons.get(i++));
		cliping.add(new JLabel());
		//row 2
		cliping.add(buttons.get(i++));
		cliping.add(buttons.get(i++));
		cliping.add(buttons.get(i++));
		//row 3
		cliping.add(new JLabel());
		cliping.add(buttons.get(i++));
		cliping.add(buttons.get(i++));
		frame.add(cliping,BorderLayout.NORTH);
		JPanel mid = new JPanel();
		mid.setLayout(new GridLayout(3,2));
		mid.add(new JLabel("Kost"));
		costSpinner = new JSpinner(new SpinnerNumberModel(1,0, Short.MAX_VALUE/256, 1));
		costSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				actionPerformed(new ActionEvent(costSpinner, 0, ""));
			}
		});
		mid.add(costSpinner);
		isPinsel = new JToggleButton("Pinsel",Icons.getImage("paintbrush", 16));
		mid.add(isPinsel);
		pinselArt.setText("<html>Alles<br>kopieren</html>");
		pinselArt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pinseltyp+=1;
				if(pinseltyp == 3){
					pinseltyp = 0;
				}
				if(pinseltyp == 1){
					pinselArt.setText("<html>Clipping <br>kopieren</html>");
				}else if(pinseltyp == 0){
					pinselArt.setText("<html>Alles<br>kopieren</html>");
				}else{
					pinselArt.setText("<html>Kosten<br>kopieren</html>");
				}
			}
		});
		mid.add(pinselArt);
		mid.add(showClipping);
		mid.add(new JLabel("show Clipping"));
		frame.add(mid);
	}
	AlphaComposite alphaFill = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
	AlphaComposite alphaFillreset = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		update();
		SpriteSheet s = tileSet.getImage();
		for(int x = 0; x != s.getHorizontalCount(); x+=1){
			for(int y = 0; y != s.getVerticalCount(); y+=1){
				g1.drawImage(s.getSubImage(x, y),x*s.getTileWight(), y*s.getTileHeight(), null);
			}
		}
		
		if(showClipping.isSelected()){
			int[] xAr = new int[3],yAr = new int[3];
			int halfWidth = s.getTileHeight()/2; int halfHeight = s.getTileHeight()/2;
			g1.setComposite(alphaFill);
			for(int x = 0; x != s.getHorizontalCount(); x+=1){
				for(int y = 0; y != s.getVerticalCount(); y+=1){
					int i = tileSet.getTileCliping(x, y);
					if(i == 85){
						g1.setColor(Color.WHITE);
						int x1 = x*s.getTileWight();
						int y1 = y*s.getTileHeight();
						g1.fillRect(x1, y1, s.getTileWight()-1, s.getTileHeight()-1);
						g1.drawRect(x1, y1, s.getTileWight()-1, s.getTileHeight()-1);
						g1.drawRect(x1, y1, s.getTileWight()-1, s.getTileHeight()-1);
					}
					else{
						int div=4;
						int numb = 0;
						//up
						numb = i % div;						
						i /= div;
						/**
						 * 1 enter and leav
						 * 2 Wall
						 * 3 enter
						 * 4 leav
						 */
						if(numb != 0){
							xAr[0] = x*s.getTileWight();	
							xAr[1] = xAr[0]+halfWidth;
							xAr[2] = xAr[0]+s.getTileWight();
							yAr[0] = y*s.getTileHeight();
							yAr[1] = yAr[0]+halfHeight;
							yAr[2] = yAr[0];
							if(numb == 1){
								g1.setColor(Color.WHITE);
							}
							if(numb == 2){
								g1.setColor(Color.RED);
							}
							if(numb == 3){
								g1.setColor(Color.GRAY);
							}
							g1.fillPolygon(xAr, yAr, 3);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
						}
						//down
						numb = i % div;						
						i /= div;
						if(numb != 0){
							xAr[0] = x*s.getTileWight();	
							xAr[1] = xAr[0]+halfWidth;
							xAr[2] = xAr[0]+s.getTileWight();
							yAr[0] = (y+1)*s.getTileHeight();
							yAr[1] = yAr[0]-halfHeight;
							yAr[2] = yAr[0];
							if(numb == 1){
								g1.setColor(Color.WHITE);
							}
							if(numb == 2){
								g1.setColor(Color.RED);
							}
							if(numb == 3){
								g1.setColor(Color.GRAY);
							}
							g1.fillPolygon(xAr, yAr, 3);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
						}
						//right
						numb = i % div;						
						i /= div;
						if(numb != 0){
							xAr[0] = (x+1)*s.getTileWight();	
							xAr[1] = xAr[0]-halfWidth;
							xAr[2] = xAr[0];
							yAr[0] = y*s.getTileHeight();
							yAr[1] = yAr[0]+halfHeight;
							yAr[2] = yAr[0] + s.getTileHeight();
							if(numb == 1){
								g1.setColor(Color.WHITE);
							}
							if(numb == 2){
								g1.setColor(Color.RED);
							}
							if(numb == 3){
								g1.setColor(Color.GRAY);
							}
							g1.fillPolygon(xAr, yAr, 3);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
						}
						//left
						numb = i % div;						
						i /= div;
						if(numb != 0){
							xAr[0] = x*s.getTileWight();	
							xAr[1] = xAr[0]+halfWidth;
							xAr[2] = xAr[0];
							yAr[0] = y*s.getTileHeight();
							yAr[1] = yAr[0]+halfHeight;
							yAr[2] = yAr[0] + s.getTileHeight();
							if(numb == 1){
								g1.setColor(Color.WHITE);
							}
							if(numb == 2){
								g1.setColor(Color.RED);
							}
							if(numb == 3){
								g1.setColor(Color.GRAY);
							}
							g1.fillPolygon(xAr, yAr, 3);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
							g1.drawLine(xAr[0],yAr[0],xAr[2],yAr[2]);
						}
					}
				}
			}
		}
		g1.setComposite(alphaFillreset);
		g1.setColor(Color.YELLOW);
		g1.drawRect(x*s.getTileWight(), y*s.getTileHeight(), s.getTileWight()-1, s.getTileHeight()-1);
	}
	public void pack(DefaultEditorPanel panel,int x, int y,int width,int height) {
		frame.setBounds(x, y, width, height);
		frame.setVisible(true);
		panel.add(frame);
	}
	int up=-1,down=-1,right=-1,left=-1,cost = -1;
	public void update(){
		if(x < 0)x = 0;
		if(y < 0)y = 0;
		int i = tileSet.getTileCliping(x,y);
		if(isPinsel.isSelected()){
			if(pinseltyp == 0){
				i = up+down*4+right*16+left*64+cost*256;
			}else if(pinseltyp == 1){
				i = ((int)i/256*256)+up+down*4+right*16+left*64;
			}else if(pinseltyp == 2){
				i = i%256+cost*256;
			}
			tileSet.setTileCliping(x, y,(short)i);
		}
		buttons.get(2).setText(i+"");
		int div=4;
		int numb = 0;
		//↑←→↓↔↕↨►   ●◙■□
		/**
		 * 1 enter and leav
		 * 2 Wall
		 * 3 enter
		 * 4 leav
		 */
		//up
		numb = i % div;
		i /= div;
		if(numb != up){
			up = numb;
			 if(up==0)buttons.get(0).setText("↕");
		else if(up==1)buttons.get(0).setText("●");
		else if(up==2)buttons.get(0).setText("↓");
		else if(up==3)buttons.get(0).setText("↑");
		}
		//down
		numb = i % div;
		i /= div;
		if(numb != down){
			down = numb;
			 if(down==0)buttons.get(4).setText("↕");
		else if(down==1)buttons.get(4).setText("●");
		else if(down==2)buttons.get(4).setText("↑");
		else if(down==3)buttons.get(4).setText("↓");
		}
		//right
		numb = i % div;
		i /= div;//↑←→↓↔↕↨►   ●◙■□
		
		if(numb != right){
			right = numb;
			 if(right==0)buttons.get(3).setText("↔");
		else if(right==1)buttons.get(3).setText("●");
		else if(right==2)buttons.get(3).setText("←");
		else if(right==3)buttons.get(3).setText("→");
		}
		//left
		numb = i % div;
		i /= div;
		if(numb != left){
			left = numb;
			 if(left==0)buttons.get(1).setText("↔");
		else if(left==1)buttons.get(1).setText("●");
		else if(left==2)buttons.get(1).setText("→");
		else if(left==3)buttons.get(1).setText("←");
		}
		if(cost != i){
			cost = i;
			costSpinner.setValue(cost);
		}
		
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		int up = this.up;
		int down = this.down;
		int right = this.right;
		int left = this.left;
		int cost = this.cost;
		int i = -1;
		
		if(e != null){
		if(e.getSource() instanceof JSpinner){
			cost = (int)((JSpinner) e.getSource()).getValue();
		}else{
			i = buttons.indexOf(e.getSource());
			if(i == 0){
				up++;
				if(up == 4)up = 0;
			}
			if(i == 1){
				left++;
				if(left == 4)left = 0;
			}
			if(i == 3){
				right++;
				if(right == 4)right = 0;
			}
			if(i == 4){
				down++;
				if(down == 4)down = 0;
			}
			if(i == 2){
				if(up == 3)up=-1;
				up = up+=1;
				down = up;
				right = up;
				left = up;
			}
			}
		}
		int info = up+down*4+right*16+left*64+cost*256;
//		if(i == 2){
//			if(info == 85){
//				info = 0;
//			}else{
//				info = 85;
//			}
//		}
		if(i == 5){
			info = cost*128;
		}
		tileSet.setTileCliping(x, y,(short)info);
	}
	@Override
	public void mouseClicked(Matrix m, MouseEvent ev) {
		mouseDragged(m, ev);
	}
	@Override
	public void mouseDragged(Matrix m, MouseEvent ev) {
		x = (int)m.getX(ev)/tileSet.getImage().getTileWight();
		if(x >=tileSet.getImage().getHorizontalCount()){
			x = tileSet.getImage().getHorizontalCount()-1;
		}
		y =(int) m.getY(ev)/tileSet.getImage().getTileHeight();
		if(y >=tileSet.getImage().getVerticalCount()){
			y = tileSet.getImage().getVerticalCount()-1;
		}
	}
	@Override
	public void keyReleased(Matrix m, KeyEvent ev) {
		int i = ev.getKeyCode();
		if(i == KeyEvent.VK_ESCAPE){
			isPinsel.setSelected(false);
		}
		else if(i == KeyEvent.VK_DELETE || i == KeyEvent.VK_BACK_SPACE){
			tileSet.setTileCliping(x, y, (short) 0);
		}else if(i == KeyEvent.VK_SPACE){
			tileSet.setTileCliping(x, y, (short)85);
		}else if(i == KeyEvent.VK_P){
			isPinsel.setSelected(true);
		}
	}
}
