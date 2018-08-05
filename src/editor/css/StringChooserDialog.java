package editor.css;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class StringChooserDialog extends StandartDialog implements ActionListener {
		String key;
		JComboBox<String> box = new JComboBox<>();

		public StringChooserDialog(JPanel panel, String key, String text, String ... auswahl) {
			super(panel,key);
			this.key = key;
			add(new JLabel(text));
			for(String s : auswahl){
				box.addItem(s);
			}
			box.addActionListener(this);
			add(box);
			init();
		}
		public void secendItem() {
			box.setVisible(isEditable());
			if(isEditable()){
				actionPerformed(null);
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			CSSEditor.THAT.cssClasse.setAttributeString(key, (String)box.getSelectedItem());
		}

		@Override
		public void updateAtt() {
			String n = CSSEditor.THAT.cssClasse.getValueAsString(key);
			if (n == null) {
//				spinner.setValue(1);
				setEdit(false);
				return;
			}
			setEdit(true);
			box.setSelectedItem(n);
		}
	}