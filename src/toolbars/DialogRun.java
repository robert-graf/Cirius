package toolbars;

import static toolbars.Toolbar.createMenuItem;
import editor.rpg.GUIMapEditStart;
import gui.AAufbau;
import img.Icons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.KeyStroke;

public class DialogRun {
	public static String name = "Run";
	static FileFilter filter = new FileFilter() {
		
		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(".database");
		}
	};

	public static void init() {
		ActionListener runAction = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File databasefile = new File("");
					File f = new File("");
					
					if (AAufbau.f.CurrendPanel instanceof GUIMapEditStart) {
						f = ((GUIMapEditStart) AAufbau.f.CurrendPanel)
								.getFile();
						// TODO Database;
						// databasefile = Event
						// .getDatabase(
						// ((GUIMapEditStart) AAufbau.f.CurrendPanel)
						// .getMatrix()).getDatabase()
						// .getFile();
					}
					File tree = f;
					while(true){
						tree = tree.getParentFile();
						if(tree == null)break;
						File[] list = tree.listFiles(filter);
						if(list.length != 0){
							databasefile = list[0];
							break;
						}
					}
					// Runtime.getRuntime().exec(new File("").getPath() +
					// "exe.bat");
					String path = null;
					if (Icons.debugg) {
						path = new File("").getPath() + "game.jar";
					} else {
						path = new File("").getPath() + "editor_lib/game.jar";

					}
					Runtime.getRuntime().exec(
							"java -jar " + " " + path + " " + f.getPath() + " "
									+ databasefile.getPath() + " ");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		ActionListener hieroAction = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Runtime.getRuntime().exec(
							"java -jar lib\\jars\\Hiero.jar -classpath slick.jar");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		createMenuItem("Run", "control_play_blue", 16, name, runAction,
				KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
		createMenuItem("Hiero", "control_play_blue", 16, name, hieroAction,
				KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
	}

}
