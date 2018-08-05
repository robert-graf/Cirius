package basic;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;


public class DDrawer {
	static GeneralPath lineDrawer = new GeneralPath();
	static Ellipse2D.Double oval = new Ellipse2D.Double();
	static Rectangle2D.Double rect = new Rectangle2D.Double();
	static long accuracy = 1000;
	public static double round(double round){
		round = (double)(Math.round(round * accuracy))/accuracy;
		return round;
	}
	public static double round(double round,long accuracy){
		round = (double)(Math.round(round * accuracy))/accuracy;
		return round;
	}

	public static void drawLine(double x, double y, double x2, double y2, Graphics2D g) {
		lineDrawer.reset();
		lineDrawer.moveTo(x,y);
		lineDrawer.lineTo(x2,y2);
		g.draw(lineDrawer);
	}
	public static void drawOval(double x, double y, double width, double height, Graphics2D g) {
		oval.setFrame(x, y, width, height);
		g.draw(oval);
	}
	public static void fillOval(double x, double y, double width, double height, Graphics2D g) {
		oval.setFrame(x, y, width, height);
		g.fill(oval);
	}
	public static void drawRect(double x, double y, double width, double height, Graphics2D g) {
		rect.setFrame(x, y, width, height);
		g.draw(rect);
	}
	public static void fillRect(double x, double y, double width, double height, Graphics2D g) {
		rect.setFrame(x, y, width, height);
		g.fill(rect);
	}
	
	
	
	
	
}
