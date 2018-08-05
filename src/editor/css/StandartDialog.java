package editor.css;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

	abstract class StandartDialog {
		public abstract void updateAtt();
		public abstract void secendItem();
	GridBagLayout gridbag =  CSSEditor.THAT.gridbag;
	ArrayList<StandartDialog> dialoglist = CSSEditor.THAT.dialoglist;
	GridBagConstraints c = CSSEditor.THAT.c;
		JPanel panel;
		String key;

		public StandartDialog(JPanel p, String key) {
			this.panel = p;
			this.key = key;
			p.setLayout(gridbag);
			dialoglist.add(this);
		}
		private JCheckBox b;
		public void init(){
			b = new JCheckBox("");
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.repaint();
					secendItem();
					if(!isEditable()){
						CSSEditor.THAT.cssClasse.remove(key);
					}
				}
			});
			addLast(b);
			secendItem();
		}
		public Component add(Component comp) {
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 2.0;
			c.gridwidth = 1;
			gridbag.setConstraints(comp, c);
			return panel.add(comp);
		}
		public Component addLast(Component comp) {
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.weightx = 0.5;
			gridbag.setConstraints(comp, c);
			return panel.add(comp);
		}
		public boolean isEditable(){
			return b.isSelected();
		}
		protected void setEdit(boolean bo) {
			b.setSelected(bo);
			secendItem();
			panel.repaint();
//			if(b.isSelected() != bo){
//				b.doClick();
//			}
		}
	}
