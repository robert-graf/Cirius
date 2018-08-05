package de.game.settings;


import java.io.File;

import de.game.game.Main;
import de.game.maps.*;

public class MapHash {
	Main m;

	public MapHash(Main main) {
		m = main;
	}

	public void init(String file[]) throws Exception {
		m.addState(new AdminRoom());
		int i = 1;
		if (file == null || file.length == 0) {
//			m.addState(new XMLMap(new File("res/Projekt/map/name.rpg"), i++));
//			m.addState(new XMLMap(new File("res/Projekt/map/map.rpg"), i++));
//			m.addState(new XMLMap(new File("res/Projekt/map/LeoVSBlue.rpg"),
//					i++));
		} else {
			for (String s : file) {
				if (s.endsWith("database")) {
					Main.setDatabase(new GameDatabase(new File(s)));
				} else {
					m.addState(new XMLMap(new File(s), i++));
				}
			}
		}
	}
}
