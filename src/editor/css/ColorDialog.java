package editor.css;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import basic.CSSClasse;

class ColorDialog extends StandartDialog implements ActionListener {
		Color c;
		String key, text;
		JButton button;
		String s = "";
		@SuppressWarnings("serial")
		public ColorDialog(JPanel panel, String key, String text) {
			super(panel,key);
			
			this.key = key;
			this.text = text;
			add(new JLabel(text));
			button = new JButton() {
				public void paintComponent(Graphics g) {
					//super.paintComponent(g);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					g.setColor(c);
					if(c == null)g.setColor(Color.white);
					g.fill3DRect(0, 0, getSize().width, getSize().height, true);
					g.setColor(Color.BLACK);
					if(s!=null){
					Rectangle2D r = g.getFontMetrics().getStringBounds(s, g);
					int w = (int) (r.getWidth()/2);
					int h = (int) (r.getHeight()/2);
					g.drawString(s,  getSize().width/2-w, getSize().height/2+h-1);
					}
				}
			};
			button.addActionListener(this);
			add(button);
			init();
		}
		@Override
		public void secendItem() {
			button.setVisible(isEditable());
			setColor(CSSEditor.THAT.cssClasse.getValueAsColor(key));
			if(isEditable() && !CSSEditor.THAT.cssClasse.containsKeyAsColor(key)){
				actionPerformed(null);
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			setColor(JColorChooser.showDialog(CSSEditor.THAT.dialog, text, c));
			CSSEditor.THAT.cssClasse.setAttributeColor(key, c);
		}

		@Override
		public void updateAtt() {
			setEdit(CSSEditor.THAT.cssClasse.containsKeyAsColor(key));
		}
		public void setColor(Color c){
			this.c = c;
			s = CSSClasse.getColorName(c);
			if(c == null){
				s = "none";
			}
		}
	}
