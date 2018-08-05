package de.game.objects;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.MorphShape;
import org.newdawn.slick.geom.Rectangle;
import org.w3c.dom.Element;

import game.database.SpriteXML;
import game.database.XMLRoot;

public class CSprite extends CImageObject {
	// TODO Klipping
	public BigAnimation a;
	public ArrayList<MorphShape> wall;
	public int index = 0;
	public ArrayList<BigAnimation> Animationen = new ArrayList<BigAnimation>();
	
	private String Name = null;

	// Constructor XXX
	Element load;

	public CSprite(Element load) {
		this.load = load;

	}

	public CSprite(int x, int y, Element cElement, String name, int index) {
		this(cElement);
		setX(x);
		setY(y);
		setName(name);
		this.index = index;
	}

	File f;

	public CSprite(int x, int y, File f, String name, int index) {
		this(x, y, new XMLRoot(f).getRoot(), name, index);
		this.f = f;
	}

	@Override
	public void drawRenderer(Graphics g, int x, int y) {
		a.draw(x, y);
	}

	@Override
	public void updateRenderer(int delta,int index) {
		a.update(delta);
	}

	Rectangle r;

	public Rectangle getSingelShape() {
		// TODO
		if (r == null) {
			r = new Rectangle(getX(), getY(), getWidth(), getHeight());
		}
		r.setBounds(getX(), getY(), getWidth(), getHeight());
		return r;
	}

	public void addAnimation(Image[] image, int[] time) {
		// TODO
		BigAnimation a = new BigAnimation(image, time);
		if (this.a == null) {
			this.a = a;
		}
		Animationen.add(a);
	}

	public void addAnimation(Image[] image, int[] time, int WallIndex) {
		BigAnimation a = new BigAnimation(image, time);
		if (this.a == null) {
			this.a = a;
		}
		Animationen.add(a);
	}


	public int getAnimation() {
		return index;
	}

	public void setAnimation(int index) {
		try {
			this.index = index;
			a = Animationen.get(index);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(null, e);
		}
	}
	// get sprite width
	public int getWidth() {
		return a.getWidth();
	}

	// get sprite height
	public int getHeight() {
		return a.getHeight();
	}

//	// get sprite/image
//	 public org.newdawn.slick.Image getImage() {
//	 return a.getCurrentFrame();
//	 }
	public void setTyp(String Name) {
		this.Name = Name;
	}

	public String getTyp() {
		return Name;
	}

	public void init() {
		try {
			SpriteXML.init(load, this);
			setAnimation(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(a.getFrame());
	}

	public static class BigAnimation {
		final static int Bruch = 256;
		ArrayList<ArrayList<Animation>> ani = new ArrayList<ArrayList<Animation>>();
		int maxWidth = 0, maxHeight = 0;

		public BigAnimation(Image[] image, int[] time) {
			for (Image i : image) {
				if (maxWidth < i.getWidth()) {
					maxWidth = i.getWidth();
				}
				if (maxHeight < i.getHeight()) {
					maxHeight = i.getHeight();
				}
			}
			for (int x = 0; x < maxWidth; x += Bruch) {
				ArrayList<Animation> eineLinie = new ArrayList<Animation>();
				ani.add(eineLinie);
				for (int y = 0; y < maxHeight; y += Bruch) {
					Image[] ima = new Image[image.length];
					for (int i = 0; i < image.length; i++) {
						int width = image[i].getWidth() - x;
						if (width > Bruch) {
							width = Bruch;
						}
						int height = image[i].getHeight() - y;
						if (height > Bruch) {
							height = Bruch;
						}
						ima[i] = image[i].getSubImage(x, y, width, height);
					}
					eineLinie.add(new Animation(ima, time, false));
				}
			}
		}

		public void draw(float x1, float y1) {
			for (int x = 0; x < ani.size(); x++) {
				ArrayList<Animation> eineLinie = ani.get(x);
				for (int y = 0; y < eineLinie.size(); y++) {
					eineLinie.get(y).draw(x1 + x * Bruch, y1 + y * Bruch);
				}
			}
		}

		public void update(long timePassed) {
			for (int x = 0; x < ani.size(); x++) {
				ArrayList<Animation> eineLinie = ani.get(x);
				for (int y = 0; y < eineLinie.size(); y++) {
					eineLinie.get(y).update(timePassed);
				}
			}
		};

		public int getHeight() {
			return maxHeight;
		};

		public int getWidth() {
			return maxWidth;
		};
		// public void getCurrentFrame(){};
	}

	public File getFile() {
		return f;
	}

	@Override
	public String getXMLName() {
		return "Sprite";
	}
}