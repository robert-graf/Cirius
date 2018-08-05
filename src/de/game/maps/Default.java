package de.game.maps;

import javax.swing.JOptionPane;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import de.game.game.Main;
import de.game.game.Print;
import de.game.objects.CObject;

public class Default {
	public static void update(final GameContainer gc, final StateBasedGame sbg,
			int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
		if (gc.getInput().isKeyPressed(Input.KEY_TAB) && !gc.isFullscreen()) {
			new Thread() {
				public void run() {

					int i = Integer.parseInt(""
							+ JOptionPane
									.showInputDialog(Print.frame, "Wohin?"));
					if(sbg.getStateCount() <= i){
						System.out.println("No existing State ID");
						return;
					}
					sbg.enterState(i);
				};
			}.start();
		}
		if (gc.getInput().isKeyDown(Input.KEY_F11)) {
			if (gc.isFullscreen()) {
				Main.appgc.setDisplayMode(Main.appgc.getWidth(), Main.appgc.getHeight(), false);
			} else {
				Main.appgc.setDisplayMode(Main.appgc.getWidth(), Main.appgc.getHeight(), true);
			}
		}
		if (gc.getInput().isKeyPressed(Input.KEY_F12)) {
			gc.setShowFPS(!gc.isShowingFPS());
		}
		if (gc.getInput().isKeyPressed(Input.KEY_1)) {
			sbg.getState(1).init(gc, sbg);
			sbg.enterState(1);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_2)) {
			sbg.getState(2).init(gc, sbg);
			sbg.enterState(2);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_3)) {
			sbg.getState(3).init(gc, sbg);
			sbg.enterState(3);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_4)) {
			sbg.enterState(4);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_5)) {
			sbg.enterState(5);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_6)) {
			sbg.enterState(6);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_7)) {
			sbg.enterState(7);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_8)) {
			sbg.enterState(8);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_9)) {
			sbg.enterState(9);
		}
		if (gc.getInput().isKeyPressed(Input.KEY_0)) {
			sbg.enterState(0);
		}
		if (gc.getInput().isKeyDown(Input.KEY_UP)) {
			CObject.setScrollY(-1f);
		} else if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
			CObject.setScrollY(1f);
		} else {
			CObject.setScrollY(0f);
		}
		if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
			CObject.setScrollX(1f);
		} else if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
			CObject.setScrollX(-1f);
		} else {
			CObject.setScrollX(0);
		}
	}

}
