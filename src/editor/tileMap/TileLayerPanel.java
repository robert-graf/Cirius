package editor.tileMap;

import img.Icons;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;

import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.TiledMap;
import editor.tileMap.objects.TiledMap.SecretAccess;

@SuppressWarnings("serial")
public class TileLayerPanel extends JInternalFrame implements ActionListener, MouseListener{
	TiledMap map;
	JPanel botPanle = new JPanel();
	JPanel mainPanle = new JPanel();
	Matrix m;
	TileRenderer render;
	public TileLayerPanel(final TiledMap map,final SecretAccess acc, final TileRenderer render,Matrix ma) {
		super("Layer",true,true,false,true);
		this.map = map;
		this.m = ma;
		this.render = render;
		setLayout(new BorderLayout());
		mainPanle.setLayout(new BoxLayout(mainPanle, BoxLayout.Y_AXIS));
		add(mainPanle);
		botPanle.setLayout(new BoxLayout(botPanle, BoxLayout.X_AXIS));
		int perSize = 24;
//		botPanle.setPreferredSize(new Dimension(perSize,perSize));
		JButton button;
		//Layer add
		button = new JButton(Icons.getImage("layer_add", 16));
		button.setToolTipText("add Layer");
		button.setPreferredSize(new Dimension(perSize,perSize));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = JOptionPane.showInputDialog(mainPanle,"Neuer Layername","_" + m.getSelectedLayer().getName());
				if(s == null)return;
				Layer l = new Layer(map, map.layers.get(m.getSelectedLayerIndex()),false);
				l.name = s;
				map.addLayer(l,m.getSelectedLayerIndex()+1);
			}
		});
		botPanle.add(button);
//Layer clone
		button = new JButton(Icons.getImage("layer_group", 16));
		button.setToolTipText("clone Layer");
		button.setPreferredSize(new Dimension(perSize,perSize));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = JOptionPane.showInputDialog(mainPanle,"Neuer Layername (Kopie)","_" + m.getSelectedLayer().getName());
				if(s == null)return;
				Layer l = new Layer(map, map.layers.get(m.getSelectedLayerIndex()),true);
				l.name = s;
				map.addLayer(l,m.getSelectedLayerIndex()+1);
			}
		});
		botPanle.add(button);
//Layer rename
		button = new JButton(Icons.getImage("layer_label", 16));
		button.setToolTipText("rename Layer");
		button.setPreferredSize(new Dimension(perSize,perSize));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Layer l = map.layers.get(m.getSelectedLayerIndex());
				String s = JOptionPane.showInputDialog(mainPanle, "Neuer Name", l.getName());
				if(s != null)
				l.setName(s);
				update();
			}
		});
		botPanle.add(button);
//resize Map
		final JSpinner xSpinner = new JSpinner(), ySpinner = new JSpinner();
		final Object[] slist = new Object[4];
		slist[0] = "Breite";
		slist[1] = xSpinner;
		slist[2] = "Höhe";
		slist[3] = ySpinner;
		button = new JButton(Icons.getImage("layer_to_image_size", 16));
		button.setToolTipText("resize Mao");
		button.setPreferredSize(new Dimension(perSize,perSize));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				xSpinner.setValue(map.getWidth());
				ySpinner.setValue(map.getHeight());
				JOptionPane.showMessageDialog(mainPanle, slist, "Größe ändern", 0, Icons.getImage("layer_to_image_size", 16));
				if(((int)xSpinner.getValue())==map.getWidth() 
						&&
					((int)ySpinner.getValue())==map.getHeight()){
					return;
				}
				if(((int)xSpinner.getValue())<map.getWidth() 
						||
					((int)ySpinner.getValue())<map.getHeight()){
					int i = JOptionPane.showConfirmDialog(mainPanle, "Daten würden verloren gehen. Forfahren?", "TileMap verkleinern",JOptionPane.YES_NO_OPTION, 0, Icons.getImage("layer_to_image_size", 16));
					if(i == 1){
						return;
					}
				}
				acc.changeSize((int) xSpinner.getValue(),(int)ySpinner.getValue());				
			}
		});
		botPanle.add(button);
//Remove Layer
		button = new JButton(Icons.getImage("layer_remove", 16));
		button.setToolTipText("remove Layer");
		button.setPreferredSize(new Dimension(perSize,perSize));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int i = JOptionPane.showConfirmDialog(mainPanle, "Layer wirklich löschen?", "Layer löschen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Icons.getImage("layer_remove", 16));
				if(i == 0){
					map.layers.remove(m.getSelectedLayerIndex());
					update();
					m.setSelectedLayer(0);
				}
			}
		});
		botPanle.add(button);
		final JToggleButton b = new JToggleButton(Icons.getImage("layers", 16));
		b.setSelected(true);
		b.setToolTipText("show normal Layer");
		b.setPreferredSize(new Dimension(perSize,perSize));
		b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				render.transparenz = b.isSelected();
			}
		});
		botPanle.add(b);
		add(botPanle, BorderLayout.SOUTH);
		update();
		render.setLayerPanle(this);
	}
	ButtonGroup group;
	public void update() {
		group = new ButtonGroup();
		mainPanle.removeAll();
		int i = 0;
		for(Layer l : map.layers){
			JRadioButton box = new JRadioButton(l.name);
			box.setSelected(m.getSelectedLayerIndex() == i);
			group.add(box);
			mainPanle.add(box);
			box.addActionListener(this);
			box.addMouseListener(this);
			i+=1;
		}
		repaint(100);
		revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		m.setLayer(((JRadioButton)e.getSource()).getText());
		render.showOne = -1;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JRadioButton b = (JRadioButton)e.getSource();
		if(!b.isSelected()){
			render.showOne = m.getIndexOfLayer(m.getLayer(b.getText()));	
		}else{
			render.showOne = -1;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		render.showOne = -1;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
