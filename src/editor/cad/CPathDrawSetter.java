package editor.cad;

import img.Icons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import editor.tabelle.OneLine;

@SuppressWarnings("serial")
public class CPathDrawSetter extends JPanel {
	public LinkedList<ConectedToggelButten> list = new LinkedList<>();

	public CPathDrawSetter() {
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//		add(new ConectedToggelButten(PUNKTFANG, Icons.getImage("chess_horse",16)));
		update();
	}

	public void update() {
		for (ConectedToggelButten c : list) {
			c.update();
		}
	}

	/****/
	// ------------------------------------------------
//	public static OneLine<Boolean> PUNKTFANG = new OneLine<Boolean>(
//			"Punktfang", true) {
//
//		@Override
//		public void setValue(Boolean value) {
//			XXX = value;
//		}
//
//		public Boolean getValue() {
//			return XXX;
//		}
//
//		@Override
//		public void valueChanges(Boolean value) {
//			// TODO Auto-generated method stub
//
//		};
//	};
	/**
	 M = moveto  
	 L = lineto  
	 H = horizontal lineto
	 V = vertical lineto 
	 C = curveto  
	 S = smooth curveto 
	 Q = quadratic Bézier curve 
	 T = smooth quadratic Bézier curveto  
	 A = elliptical Arc 
	 Z = closepath  Workarounds: 
	 R = 3 Punkte Bogen
	 */

	/****/
	class ConectedToggelButten extends JToggleButton {
		private OneLine<Boolean> b;

		public ConectedToggelButten(OneLine<Boolean> bo, ImageIcon i) {
			super(i);
			setToolTipText(bo.getKey());
			this.b = bo;
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					b.setValue(!b.getValue());
				}
			});
			list.add(this);
		}

		public void update() {
			setSelected(b.getValue());
		}
	}

	public LinkedList<ConectedToggelButten> getList() {
		return list;
	}

}
