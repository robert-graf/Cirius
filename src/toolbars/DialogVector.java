package toolbars;

import static toolbars.Toolbar.createMenuItem;
import editor.rpg.vector.Vector;
import gui.AAufbau;
import interfaces.ExtendsDefaultEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class DialogVector {
	static final String name = "Vector";
	static ActionListener neueFormel = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			((ExtendsDefaultEditor) AAufbau.f.CurrendPanel).getMatrix()
					.addCObject(
							new Vector(JOptionPane
									.showInputDialog("Formel eingeben")));
		}
	};

	public static void init() {
		createMenuItem("Neu Formel", "check_box_empty", 0, name, neueFormel,
				KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
	}

}
