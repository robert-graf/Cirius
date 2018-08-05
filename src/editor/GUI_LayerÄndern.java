package editor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class GUI_LayerÄndern extends JPanel {
	GUI_LayerÄndern() {
		JButton button;
		button = new JButton(new ImageIcon(this.getClass().getResource(
				"/img/plus.png")));
		button.addActionListener(GUI_Layerbefehle.ActionAddLayer());
		add(button);
		button = new JButton(new ImageIcon(this.getClass().getResource(
				"/img/up.png")));
		button.addActionListener(GUI_Layerbefehle.ActionOneUp());
		add(button);
		button = new JButton(new ImageIcon(this.getClass().getResource(
				"/img/gpunkt.png")));
		button.addActionListener(GUI_Layerbefehle.ActionSetLayer());
		add(button);
		button = new JButton(new ImageIcon(this.getClass().getResource(
				"/img/down.png")));
		button.setName("down");
		button.addActionListener(GUI_Layerbefehle.ActionOneDown());
		add(button);
		button = new JButton(new ImageIcon(this.getClass().getResource(
				"/img/x.png")));
		button.addActionListener(GUI_Layerbefehle.ActionRemoveLayer());
		add(button);
	}
}