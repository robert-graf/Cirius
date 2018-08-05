package de.game.game;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.game.settings.DisplayMode;
import de.game.settings.GameDatabase;
import de.game.settings.MapHash;


public class Main extends StateBasedGame {
	public static AppGameContainer appgc;
	private MapHash hash;

	public Main(String[] args) throws Exception {
		super(DisplayMode.getName());
		hash = new MapHash(this);
		hash.init(args);
	}
//	public Main() throws Exception {
//	super(DisplayMode.getName());
//		XMLMap map = new XMLMap(new File("res/Projekt/map/map.rpg"),1);
//		Layer l = map.createLayer("default", 0);
//		CPath p =  new CPath("M 82,0 L -40,40 R -2,142 170,60 L 82,0 ");
//		map.spawnCObject(l,new CPath("M 82,0 L -40,40 L -2,142 L 170,60 L 82,0 "));
//		map.spawnCObject(l, p);
//		addState(new AdminRoom());
//		addState(map);
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Print();
			for(String s :args){
				System.out.println(s);
			}
			appgc = new AppGameContainer(new Main(args));
			setUpGameContainer(appgc);
			
			appgc.start();
		} catch (Exception e) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
			kill();
		};
		
	}
	public static void setUpGameContainer(AppGameContainer c) throws SlickException{
		if(!(c instanceof AppGameContainer)){
		new Print();
		}
		DisplayMode.init(c);
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		getState(0).init(gc, this);
		getState(1).init(gc, this);
		enterState(1);
		// getState(1).init(gc, this);
	}

	public StateBasedGame getStateBasedGame() {
		return this;
	}

	public MapHash getMapHash() {
		return hash;
	}
	static GameDatabase database;
	public static void setDatabase(GameDatabase gd) {
		database = gd;
	}
	public static GameDatabase getDatabase(){
		return database;
	}
	public static void kill(){
		try {
			appgc.destroy();
			java.awt.Toolkit.getDefaultToolkit().beep();
			while(true){
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}