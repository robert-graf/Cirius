package database.game;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class Ersteller {
	public Ersteller(File f) {
		File[] file = f.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});

		if (file[0].listFiles(data).length == 0) {
			try {
				new File(file[0].getPath() + "/game.database").createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	FileFilter data = new FileFilter() {
		public boolean accept(File f) {
			return f.getName().equals("game.database");
		}
	};
}
