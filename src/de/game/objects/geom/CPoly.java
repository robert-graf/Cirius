package de.game.objects.geom;

import org.newdawn.slick.geom.Polygon;

public class CPoly extends CShape {
	Polygon p;
	public CPoly(String list, boolean close) {
		PolygonByList(list);
		p.setClosed(close);
		init();
	}
	public CPoly(int x1, int y1, int x2, int y2) {
		setX(x1);
		setY(y1);
		addPoint(x2-x1, y2-y1);
	}
	//	public CPoly(int x, int y, String list) {
//		p = new Polygon();
//		PolygonByList(list);
//		p.setLocation(x, y);
//		p.closed();
//	}
	@Override
	public Polygon getShape() {
		return p;
	}

	@Override
	public String getShapeName() {
		return "Poly1";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	private void PolygonByList(String list) {

		String[] s = list.split(" ");
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
		int[] x = new int[s.length];
		int[] y = new int[s.length];

		for (int i = 0; i < s.length; i++) {
			String[] point = s[i].split(",");
			x[i] = Integer.parseInt(point[0]);
			y[i] = Integer.parseInt(point[1]);
			if (x[i] < minX) {
				minX = x[i];
			}
			if (y[i] < minY) {
				minY = y[i];
			}
		}
		setX(minX);
		setY(minY);
		for (int i = 0; i < x.length; i++) {
			addPoint(x[i] - minX, y[i] - minY);
		}
		p.setLocation(minX, minY);
	}

	private void addPoint(float x, float y) {
		p.addPoint(x, y);
	}

	public String getPointListString() {
		String s = "";
		for(int n = 0; n!= p.getPointCount();n++){
			s += ((int)p.getPoint(n)[0]) +","+ ((int)p.getPoint(n)[1]) + ";";
		}
		return s;
	}

	@Override
	public void setLocalistaon(int x, int y) {
		//TODO
		p.setLocation(x, y);
	}
	@Deprecated
	public void setEvenOdd(boolean rule) {
			System.out.println("Windingrule does not worke");
	}


}
