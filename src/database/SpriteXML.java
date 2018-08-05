package database;

import editor.rpg.Sprite;
import gui.SpriteEinstellen;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import toolbars.DialogSpriteEditor;

/**
*/

public class SpriteXML extends XML implements Cloneable {

	// constructor
	public ArrayList<SpriteXML.Animation> AnimationList = new ArrayList<SpriteXML.Animation>();
	File f;
	SpriteEinstellen spe;

	public SpriteXML(File f, SpriteEinstellen spe) {
		this.spe = spe;
		read(f);
	}

	public SpriteXML(File f) {
		if (f.getName().endsWith(".png")) {
			addAnimation();
			Image i;
			try {
				i = ImageIO.read(f);
				AnimationList.get(0).get(0).setImagePfad(f.getPath());
				AnimationList.get(0).get(0).setWidht(i.getWidth(null));
				AnimationList.get(0).get(0).setHeight(i.getHeight(null));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			read(f);
		}
	}

	public SpriteXML(Element f) {
		read(f);
	}

	@Override
	public void read(Element rootElement) {
		NodeList nlist = rootElement.getElementsByTagName("Animation");
		// Animationsloop
		for (int n = 0; n != nlist.getLength(); n++) {

			Element ani = (Element) nlist.item(n);
			Animation animation = new Animation();
			AnimationList.add(animation);
			// Sceneloop
			NodeList scen = ani.getElementsByTagName("Scene");
			for (int m = 0; m != scen.getLength(); m++) {
				Element oneScene = (Element) scen.item(m);
				animation.add(new Scene(oneScene.getAttribute("image"), Integer
						.parseInt(oneScene.getAttribute("x")), Integer
						.parseInt(oneScene.getAttribute("y")), Integer
						.parseInt(oneScene.getAttribute("width")), Integer
						.parseInt(oneScene.getAttribute("height")), Integer
						.parseInt(oneScene.getAttribute("time"))));
			}
		}
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
		for (SpriteXML.Animation ani : AnimationList) {
			Element XMLanimation = xmlDoc.createElement("Animation");
			for (Scene scene : ani) {
				Element XMLscene = xmlDoc.createElement("Scene");
				XMLscene.setAttribute("image", scene.getImagePfad());
				XMLscene.setAttribute("x", "" + scene.getX());
				XMLscene.setAttribute("y", "" + scene.getY());
				XMLscene.setAttribute("width", "" + scene.getWidht());
				XMLscene.setAttribute("height", "" + scene.getHeight());
				XMLscene.setAttribute("time", "" + scene.getTime());

				XMLanimation.appendChild(XMLscene);
			}
			rootElement.appendChild(XMLanimation);
		}
	}

	public void save() {
		try {
			write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getXMLName() {
		return getXMLNameS();
	}

	public static String getXMLNameS() {
		return "Sprite";
	}

	@Override
	public void fileDoesNotExist() {
		addAnimation();
	}

	public Scene addScene(int Animation, int Scene) {
		Scene a = sucheReihe(Animation, Scene);
		AnimationList.get(Animation).add(
				Scene,
				new Scene(a.getImagePfad(), a.getX(), a.getY(), a.getWidht(), a
						.getHeight(), a.getTime()));
		return sucheReihe(Animation, Scene);
	}

	public Scene addScene(int Animation) {
		Scene a = sucheReihe(Animation, 0);
		AnimationList.get(Animation).add(
				new Scene(a.getImagePfad(), a.getX(), a.getY(), a.getWidht(), a
						.getHeight(), a.getTime()));
		return sucheReiheLast(Animation);
	}

	// Neu Animation mit Vorlage von Vorher
	public Animation addAnimation() {
		if (AnimationList.size() == 0) {
			AnimationList.add(new Animation());
			AnimationList.get(AnimationList.size() - 1).add(
					new Scene("", 0, 0, 10, 10, 1000));
		} else {
			Scene s = getSceneByToolBar();
			Animation a = new Animation();
			AnimationList.add(
					1 + (int) DialogSpriteEditor.Animationindex.getValue(), a);
			a.add(new Scene(s.ImagePfad, s.x, s.y, s.widht, s.height, s.time));
		}
		return AnimationList.get(AnimationList.size() - 1);
	}

	public Animation getAnimation(int i) {
		return AnimationList.get(i);
	}

	public Scene sucheReiheLast(int Animation) {
		return AnimationList.get(Animation).get(
				AnimationList.get(Animation).size() - 1);
	}

	public Scene getSceneByToolBar() {
		try {
			return AnimationList.get(
					(Integer) DialogSpriteEditor.Animationindex.getValue())
					.get((Integer) DialogSpriteEditor.Scenenindex.getValue());
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
			return null;
		}
	}

	public Scene sucheReihe(int Animation, int Scene) {
		return AnimationList.get(Animation).get(Scene);
	}

	public Point getAnimationAndSceneByRow(int row) {
		int animation = 0;
		for (Animation a : AnimationList) {
			if (row - a.size() < 0) {
				break;
			}
			row -= a.size();
			animation++;
		}
		return new Point(animation, row);
	}

	public Scene sucheReihe(int row) {
		int animation = 0;
		for (Animation a : AnimationList) {
			if (row - a.size() < 0) {
				break;
			}
			row -= a.size();
			animation++;
		}
		return AnimationList.get(animation).get(row);
	}

	public Sprite getSprite() {
		Sprite sprite = new Sprite();
		for (Animation an : AnimationList) {
			sprite.addAnimation();
			for (database.SpriteXML.Scene s : an) {

				sprite.addScene(new File(s.getImagePfad()), s.getX(), s.getY(),
						s.getWidht(), s.getHeight(), s.getTime());
			}
		}
		return sprite;
	}

	@Override
	public SpriteXML clone() throws CloneNotSupportedException {
		return (SpriteXML) super.clone();
	}

	// Sprite Save Klasse
	@SuppressWarnings("serial")
	public class Animation extends ArrayList<Scene> {
	}// ende

	public class Scene {
		private int time;
		private String ImagePfad;
		private int x, y, widht, height;

		public Scene(String Pfad, int x, int y, int widht, int height, int time) {
			this.setTime(time);
			this.ImagePfad = Pfad;
			this.x = x;
			this.y = y;
			this.widht = widht;
			this.height = height;
			if (spe != null) {
				try {
					spe.imageHash.put(ImagePfad,
							ImageIO.read(new File(ImagePfad)));
				} catch (IOException e) {
				}
			}
		}

		public int getTime() {
			return time;
		}

		public String getImagePfad() {
			return ImagePfad;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidht() {
			return widht;
		}

		public int getHeight() {
			return height;
		}

		public void setTime(int time) {
			this.time = time;
		}

		public void setImagePfad(String imagePfad) {
			ImagePfad = imagePfad;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setWidht(int widht) {
			this.widht = widht;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}
}
