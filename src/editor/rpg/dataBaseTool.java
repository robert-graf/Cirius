package editor.rpg;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import database.game.GameDatabase;
import editor.AbstractToolAdapter;
import editor.DefaultEditorPanel.Matrix;

public class dataBaseTool extends AbstractToolAdapter {
	long time = 0;
	File f;

	public dataBaseTool(File file) {
		try {
			while (!new File(file.getParent() + "/game.database").exists()) {
				file = file.getParentFile();
			}
			f = new File(file.getParent() + "/game.database");
		} catch (Exception e) {
			try {
				System.err.println(e + "");
				f = new File("res/game.database");
				f.createNewFile();
				Toolkit.getDefaultToolkit().beep();
				System.err.println(f + "wurder erstellt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		refresh();
	}

	@Override
	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
		refresh();
	}

	private void refresh() {
		if (time != f.lastModified()) {
			db = new GameDatabase(f);
			time = f.lastModified();
		}
	}

	GameDatabase db;

	public GameDatabase getDatabase() {
		return db;
	}
}
