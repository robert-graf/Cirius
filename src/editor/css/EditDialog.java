package editor.css;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class EditDialog <T extends SpecialEditObject> extends StandartDialog implements ActionListener {
		String key;
		JButton butten = new JButton();
		ImageIcon image = new ImageIcon(new BufferedImage(150, 20, BufferedImage.TYPE_INT_RGB));
		public EditDialog(JPanel panel, String key, String text) {
			super(panel,key);
			this.key = key;
			add(new JLabel(text));
			butten.addActionListener(this);
			add(butten);
			init();
		}
		public void secendItem() {
			butten.setVisible(isEditable());
			if(isEditable()&& !CSSEditor.THAT.cssClasse.containsKeyAsObject(key)){
				actionPerformed(null);
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			SpecialEditObject n = CSSEditor.THAT.cssClasse.getValueAsObject(key);
			if(n == null){
					n = CSSEditor.THAT.cssClasse.creatValueAsObject(key, null);
					CSSEditor.THAT.cssClasse.setAttributeObject(key, n);
			}
			n.edit();
			butten.setIcon(n.getImage(image));
		}
	

		@Override
		public void updateAtt() {
			try{
			SpecialEditObject n = CSSEditor.THAT.cssClasse.getValueAsObject(key);
			if (n == null) {
				butten.setIcon(null);
				setEdit(false);
				return;
			}
			setEdit(true);
			butten.setIcon(n.getImage(image));
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}