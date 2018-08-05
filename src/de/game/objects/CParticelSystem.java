package de.game.objects;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import game.database.XMLCObject;

public class CParticelSystem extends CImageObject {
	ParticleSystem system;
	File f ;
	 int width, height;
	public CParticelSystem(int x, int y, File f, int width, int height) {
		this.f = f;
		this.width = width;
		this.height = height;
		setX(x);
		setY(y);
	}

	@Override
	public void init() {
		try {
			system = ParticleIO.loadConfiguredSystem(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getXMLName() {
		return XMLCObject.particel;
	}

	@Override
	public void drawRenderer(Graphics g, int x, int y) {
		system.render(x, y);
	}

	@Override
	public void updateRenderer(int delta, int index) {
		system.update(delta);
	}
	Rectangle r = new Rectangle(0,0,0,0);
	@Override
	public Shape getSingelShape() {
		r.setBounds(0, 0, width, height);
		return r;
	}

}
