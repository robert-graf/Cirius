package toolbars;

import static toolbars.Toolbar.createMenuItem;
import editor.GUI_Layer;
import editor.rpg.nexus.NexusPanel;
import editor.tabelle.ObjectTableGUI;
import gui.AAufbau;
import gui.DefaultTree;
import interfaces.ExtendsDefaultEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class DialogRPG {
	static String name = "Fenster";
	static ExtendsDefaultEditor c;
	// REMOVE add Event and Paceholder
	static AbstractButton addButton, addEvent, Objectmanager, Layermanager,
			 Nexus;
	static ActionListener addButtonAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			addToMap();
		}
	};
	static ActionListener EventAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(AAufbau.f, "Dies wurde entfernt!");
		}
	};

	public static void init() {
		addButton = createMenuItem("Hinzufügen", "user_add", 16, name,
				addButtonAction, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
						InputEvent.CTRL_DOWN_MASK));
		addEvent = createMenuItem("Event", "ruby_add", 16, name, EventAction,
				KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));
		Objectmanager = createMenuItem("Objectmanager", "text_list_bullets",
				16, name, ObjectTableGUI.THAT,
				KeyStroke.getKeyStroke('1', InputEvent.CTRL_DOWN_MASK));
		Layermanager = createMenuItem("Layermanager", "layers_map", 16, name,
				GUI_Layer.getAction(),
				KeyStroke.getKeyStroke('2', InputEvent.CTRL_DOWN_MASK));
//		Script = createMenuItem("Script", "script_code", 16, name,
//				ScriptPanel.THAT,
//				KeyStroke.getKeyStroke(KeyEvent.VK_LESS,
//						InputEvent.CTRL_DOWN_MASK));
		Nexus = createMenuItem("Nexus", "connect", 16, name, NexusPanel.THAT,
				KeyStroke.getKeyStroke('3', InputEvent.CTRL_DOWN_MASK));
	}

	public static void update() {
		c = (ExtendsDefaultEditor) AAufbau.f.CurrendPanel;
		DefaultTree.Erlaubt = erlaubt;
	}

	static ArrayList<String> erlaubt = new ArrayList<>();
	static {
		erlaubt.add(".png");
		erlaubt.add(".das");
		erlaubt.add(".tmx");
	}

	public static void addToMap() {
		DefaultTree.getDefaultTree(erlaubt, null);
	}
}
