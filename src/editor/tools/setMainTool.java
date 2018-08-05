package editor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import editor.AbstractTool;
import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;

public class setMainTool implements ActionListener {
	private AbstractTool tool;

	public setMainTool(AbstractTool tool) {
		this.tool = tool;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Matrix m = GUI_Layerbefehle.getMatrix();
		m.setMainTool(tool);
		tool.init(m);
	}
}
