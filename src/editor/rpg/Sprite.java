package editor.rpg;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Sprite implements Cloneable {
	public Animation a;
	public int index = 0;
	public ArrayList<Animation> Animationen = new ArrayList<Animation>();
	private double x;
	private double y;
	private double vx = 0;
	private double vy = 0;
	public static float ScrollX = 0f;
	public static float ScrollY = 0f;

	public Sprite() {
	}

	public void draw(Graphics g) {
		a.draw(g, (int)x, (int)y);
	}

	public void addAnimation() {
		Animation a = new Animation();
		if (this.a == null) {
			this.a = a;
		}
		Animationen.add(a);
	}

	public void addAnimation(Animation a) {
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
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showConfirmDialog(null, e);
		}
	}

	public void update(long timePassed) {
		x -= vx * timePassed;
		y -= vy * timePassed;
		a.update(timePassed);
	}

	// get x position
	public double getX() {
		return x;
	}

	// get y position
	public double getY() {
		return  y;
	}

	// get x position
	public double getXg() {
		return x;
	}

	// get y position
	public double getYg() {
		return y;
	}

	// set x position
	public void setX(double x) {
		this.x = x;
	}

	// set y position
	public void setY(double y) {
		this.y = y;
	}

	// get sprite width
	public int getWidth() {
		return a.getCurrentFrame().getWidth();
	}

	// get sprite height
	public int getHeight() {
		return a.getCurrentFrame().getHeight();
	}

	// get horizontal velocity
	public double getVelocityX() {
		return vx;
	}

	// get vertical velocity
	public double getVelocityY() {
		return vy;
	}

	// set horizontal velocity
	public void setVelocityX(float vx) {
		this.vx = vx;
	}

	// set vertical velocity
	public void setVelocityY(float vy) {
		this.vy = vy;
	}

	// get sprite/image
	public java.awt.Image getImage() {
		return a.getCurrentFrame();
	}

	public Sprite clone() {
		try {
			Sprite sprite = (Sprite) super.clone();
			// TODO
			// sprite.a = a.clone();
			return sprite;
		} catch (CloneNotSupportedException e) {
			JOptionPane.showMessageDialog(null,
					"Konnte kein Objekt erstellen (Klon fehler bei Spirte)");
			e.printStackTrace();
		}
		return null;
	}

	Rectangle r;

	public Rectangle getBounds() {
		if (r == null) {
			r = new Rectangle();
		}
		r.setFrame(x, y, getWidth(), getHeight());
		return r;
	}

	@SuppressWarnings("serial")
	class Animation extends ArrayList<Scene> {
		int i = 0;
		int time = -100;

		public BufferedImage getCurrentFrame() {
			return this.get(i).getImage();
		}

		public void draw(Graphics g, float x, float y) {
			g.drawImage(getCurrentFrame(), (int) x, (int) y, null);
		}

		public void update(long timePassed) {
			time += timePassed;
			if (time > 0) {
				time = -get(i).getTime();
				i++;
				if (i == size()) {
					i = 0;
				}
			}
		}
	}

	class Scene {
		private int x, y, width, height, time;
		private BufferedImage bimage;

		public Scene(int x, int y, int width, int height, int time, File f) {
			setX(x);
			setY(y);
			setWidth(width);
			setHeight(height);
			setTime(time);
			try {
				bimage = ImageIO.read(f).getSubimage(getX(), getY(),
						getWidth(), getHeight());
			} catch (IOException e) {
				System.out.println(f.getPath());
				e.printStackTrace();
			}
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public BufferedImage getImage() {
			return bimage;
		}

		public void setX(int x) {
			this.x = x;
		}

		public void setY(int y) {
			this.y = y;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}

	public void addScene(File file, int x, int y, int widht, int height,
			int time) {
		Animationen.get(Animationen.size() - 1).add(
				new Scene(x, y, widht, height, time, file));
	}
}