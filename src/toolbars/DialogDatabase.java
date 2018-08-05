package toolbars;

import static toolbars.Toolbar.createMenuItem;
import gui.DefaultTree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.KeyStroke;

import database.game.DataBaseEditor;

public class DialogDatabase {
	static String name = "DataBase";
	static AbstractButton add, set, remove, rename, update, bereinigen;
	static ActionListener addAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (editor.add() == false) {
				DefaultTree.getDefaultTree(DialogRPG.erlaubt, null);
			}
		}
	};
	static ActionListener setAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			editor.editSprite();
		}
	};
	static ActionListener removeAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			editor.removeSelected();
		}
	};
	static ActionListener renameAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			editor.rename();
		}
	};
	static ActionListener updateAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			editor.update();
		}
	};
	static ActionListener bereinigenAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			editor.bereinigen();
		}
	};
	static DataBaseEditor editor;

	public static void update(DataBaseEditor currendPanel) {
		editor = currendPanel;
	}

	public static void init() {
		add = createMenuItem("Hinzufügen", "database_add", 16, name, addAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
						InputEvent.CTRL_DOWN_MASK));
		set = createMenuItem("Ändern", "database_gear", 16, name, setAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_NUMBER_SIGN,
						InputEvent.CTRL_DOWN_MASK));
		remove = createMenuItem("Entfernen", "database_delete", 16, name,
				removeAction, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
						InputEvent.CTRL_DOWN_MASK));
		rename = createMenuItem("Umbenennen", "database_edit", 16, name,
				renameAction,
				KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		update = createMenuItem("Update", "database_refresh", 16, name,
				updateAction,
				KeyStroke.getKeyStroke('U', InputEvent.CTRL_DOWN_MASK));
		bereinigen = createMenuItem("Bereinigen", "database_lightning", 16,
				name, bereinigenAction,
				KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
	}
}
