package editor.css;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NumberDialog extends StandartDialog implements ChangeListener {
		String key;
		JSpinner spinner;

		public NumberDialog(JPanel panel, String key, String text, Number f,
				Comparable<?> minimum, Comparable<?> maximum, Number stepSize,String tooltip) {
			super(panel,key);
			this.key = key;
			SpinnerNumberModel numberModel = new SpinnerNumberModel(f, minimum,
					maximum, stepSize);
			JLabel l = new JLabel(text);
			ToolTipManager.sharedInstance().setDismissDelay(24000);
//			ToolTipManager.sharedInstance().setInitialDelay(2000);
			ToolTipManager.sharedInstance().registerComponent(l);
			l.setToolTipText(tooltip);
			add(l);
			spinner = new JSpinner();
			spinner.setModel(numberModel);
			spinner.addChangeListener(this);
			add(spinner);
			init();
		}
		public void secendItem() {
			spinner.setVisible(isEditable());
			if(isEditable()){
				stateChanged(null);
			}
		}
		@Override
		public void stateChanged(ChangeEvent e) {
			CSSEditor.THAT.cssClasse.setAttributeNumber(key, (Number) spinner.getValue());
		}

		@Override
		public void updateAtt() {
			Number n = CSSEditor.THAT.cssClasse.getValueAsNumber(key);
			if (n == null) {
				setEdit(false);
				return;
			}
			setEdit(true);
			spinner.setValue(n);
		}
	}