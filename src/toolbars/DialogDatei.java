package toolbars;

import static toolbars.Toolbar.createMenuItem;
import database.SVG_XML;
import editor.rpg.GUIMapEditStart;
import gui.AAufbau;
import interfaces.DestroyAble;
import interfaces.Save;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class DialogDatei {
	public static AbstractButton save, saveAll, saveNcloseAll,export,svgMode, close;
	public static int answ = -1;
	public static String name = "Datei";

	public static void init() {
		save = createMenuItem("Sicheren", "disk", 16, name, saveAction,
				KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		saveAll = createMenuItem(
				"Alles Sicheren",
				"disk",
				0,
				name,
				saveAllAction,
				KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK
						| InputEvent.SHIFT_DOWN_MASK));
		saveNcloseAll = createMenuItem(
				"Alles Schließen und Sicheren",
				"disk_all",
				0,
				name,
				saveNcloseAllAction,
				KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK
						| InputEvent.SHIFT_DOWN_MASK));
		export = createMenuItem("Export", "card_export", 16, name, exportAction);
		svgMode = Toolbar.createCheckBoxMenuItem("Schließen", "cancel", 16, name, closeAction,
				false);
		close = createMenuItem("Schließen", "cancel", 16, name, closeAction,
				KeyStroke.getKeyStroke('W', InputEvent.CTRL_DOWN_MASK));
	}

	public static final ActionListener saveAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComponent c = AAufbau.f.CurrendPanel;
			if (c instanceof Save) {
				((Save) c).save();
			} else {
				JOptionPane.showMessageDialog(AAufbau.f, ""
						+ "<html>Diese Datei untersützt kein Abspeichern!<br>"
						+ "z. B. Bilder oder es ist keine Datei offen</html>");
			}
		}
	};
	public static final ActionListener saveAllAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for (JComponent c : AAufbau.f.OpenFileList.values()) {
				if (c instanceof Save) {
					((Save) c).save();
				}
			}
		}
	};
	public static final ActionListener saveNcloseAllAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			for (JComponent c : AAufbau.f.OpenFileList.values()) {
				if (c instanceof Save) {
					((Save) c).save();
				}
				c.removeAll();
				AAufbau.f.OpenFile.remove(c);
				if (c instanceof DestroyAble) {
					((DestroyAble) c).destroy();
				}
			}
			AAufbau.f.OpenFileList.clear();
		}
	};
	public static final ActionListener exportAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComponent c = AAufbau.f.CurrendPanel;
			if(c instanceof GUIMapEditStart){
				File f = new File( ((GUIMapEditStart) c).getFile().getParent() +"/" + ((GUIMapEditStart) c).getFile().getName().replace(".rpg", "") + ".svg");
				new SVG_XML((GUIMapEditStart) c).write(f);
				try {
					Desktop.getDesktop().open(f);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(AAufbau.f, "Dies kann nicht Exportiert werden.");
			}
			AAufbau.f.OpenFileList.clear();
		}
	};
	public static final ActionListener closeAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			JComponent c = AAufbau.f.CurrendPanel;
			if (c == null) {
				answ = 3;
				return;
			}
			String s = AAufbau.f.CurrendPanel.getName();
			if (c instanceof Save) {
				answ = JOptionPane.showConfirmDialog(AAufbau.f,
						"Möcheten sie \"" + s
								+ "\" vorher sichern bevor sie es Schiließen?");
				if (answ == 0) {
					((Save) c).save();
				} else if (answ == 2) {
					return;
				}
			} else {
				answ = 0;
			}
			;
			if (c instanceof DestroyAble) {
				((DestroyAble) c).destroy();
			}
			c.removeAll();
			AAufbau.f.OpenFile.remove(c);
			AAufbau.f.OpenFileList.remove(AAufbau.f.getFile(c));
		}
	};
}
