package de.game.maps;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class AdminRoom extends BasicGameState{
	int id = 0;
	
	static int idCont = 1;

	public AdminRoom(){id = 0;}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
			g.drawString("" +
					"Escap für Programm beenden\n" +
					"Tab für Teleport \n" +
					"F11 für Vollbild (Statisch!)\n" +
					"F12 für FPS zeigen " , gc.getWidth()/2-50, gc.getHeight()/2-40);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Default.update(gc, sbg, delta);
	}

	@Override
	public int getID() {
		return id;
	}

}
