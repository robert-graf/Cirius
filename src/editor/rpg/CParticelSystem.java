package editor.rpg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import basic.DDrawer;
import database.XMLCObject;
import editor.DefaultEditorPanel.Matrix;

public class CParticelSystem extends CImageObject{
	BufferedImage image;
	Rectangle r = new Rectangle();
	Rectangle bounds = new Rectangle();
	public CParticelSystem(double x, double y,File f) {
		setFile(f);
		reload();
		init(x, y);
	}
	@Override
	public Class<?> getGameClass() {
		return null;
	}

	@Override
	protected CImageObject clone2() throws CloneNotSupportedException {
		return new CParticelSystem(getX(),getY(),getFile());
	}
	public static final Color color = new Color(0,0,0,100);
	@Override
	public void drawRenderer(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y ) {
		if(image == null){
			g.drawString("Please create a preview of the particle system.",(int) x,(int) y);
		}else{
			Color c = g.getColor();
			g.setColor(color);
			DDrawer.fillRect(r.x + x, r.y +y, r.width, r.height,g);
			g.drawImage(image,(int)x+r.x,(int)y+r.y,null);
			g.setColor(c);
		}
		
	}

	@Override
	public void updateRenderer(Matrix m, int timePassed, int conter) {
		
	}

	@Override
	public Shape getSingelShape() {
		bounds.setBounds(r);
		bounds.x += getX();
		bounds.y += getY();
		return bounds;
	}

	@Override
	public void reload() {
		String s =getFile().getAbsolutePath().replace(getFile().getName(), "!"+getFile().getName());
		File imagefile = new File(s.replace(".pesx", ".png"));
		try {
			image = ImageIO.read(imagefile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		s = s.replace(".pesx",".previewSeting");
		if(new File(s).exists()){
			try {
				Scanner scanner = new Scanner(new File(s));
				String rec[] = scanner.nextLine().split(";");
				scanner.close();
				r.x = (Integer.parseInt(rec[0]));
				r.y = (Integer.parseInt(rec[1]));
				r.width = (Integer.parseInt(rec[2]));
				r.height =(Integer.parseInt(rec[3]));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getXMLName() {
		return XMLCObject.particel;
	}
	public BufferedImage getImage() {
		return image;
	}
	
}
