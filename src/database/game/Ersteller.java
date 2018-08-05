package database.game;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class Ersteller implements FileFilter {
	public Ersteller(File f) {
		if(!f.exists()){
			f.mkdirs();
		}
		File[] file = f.listFiles(this);
		if(file.length == 0){}else
		if (file[0].listFiles(this::acceptData).length == 0) {
			try {
				new File(file[0].getPath() + "/game.database").createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public boolean acceptData(File f) {
		return f.getName().equals("game.database");
	}
	
	public boolean accept(File f) {
		return f.isDirectory();
	}
}
