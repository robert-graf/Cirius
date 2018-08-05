package de.game.objects;

import java.util.ArrayList;

import org.newdawn.slick.geom.Path;

public class WalkPath {
	private ArrayList<Path> p = new ArrayList<>();
	private CObject object;
	private int destinationX, destinationY;
	private Path currentPath;
	private int indexOfCurrentPath = 0;
	// aktuelle Strecke
	private float x1, y1, x2, y2;
	private float GegangeneStrecke = 0;

	public WalkPath(CObject o) {
		object = o;
		destinationX = (int) object.getX();
		destinationY = (int) object.getY();
	}

	public void update(int delta, float Velocity) {
		if (isStandingStill()) {
			return;
		}
		if (currentPath == null) {
			currentPath = p.get(0);
		}
		GegangeneStrecke += delta * Velocity;
		// c = wurzel aus (a²+b²)
		float StreckenLänge = (float) Math
				.sqrt(hoch2(x2 - x1) + hoch2(y2 - y1));

		if (StreckenLänge < GegangeneStrecke) {
			x1 = currentPath.getPoint(indexOfCurrentPath)[0];
			y1 = currentPath.getPoint(indexOfCurrentPath)[1];

			if (currentPath.getPointCount() <= indexOfCurrentPath + 1) {
				if (p.size() == 1) {
					p.remove(0);
					currentPath = null;
					indexOfCurrentPath = 0;
				} else {
					currentPath = p.get(1);
					p.remove(0);
					x2 = currentPath.getPoint(0)[0];
					y2 = currentPath.getPoint(0)[1];
					indexOfCurrentPath = 0;
				}
			} else {
				x2 = currentPath.getPoint(indexOfCurrentPath + 1)[0];
				y2 = currentPath.getPoint(indexOfCurrentPath + 1)[1];
				indexOfCurrentPath++;
			}
			GegangeneStrecke -= StreckenLänge;
			update(0, Velocity);
			return;
		}

		double γ = Math.atan((x2 - x1) / (y2 - y1));// tan^-1((x2-x1)/(y2-y1));
		double faktor = 1;
		if (y2 < y1) {
			faktor = -1;
			object.setAngel(γ + Math.PI * 1.5);
		} else {
			object.setAngel(γ + Math.PI * 0.5);
		}

		object.setX((float) (x1 + GegangeneStrecke * Math.sin(γ) * faktor));
		object.setY((float) (y1 + GegangeneStrecke * Math.cos(γ) * faktor));

	};

	private float hoch2(float x) {
		return x * x;
	}

	public int getX() {
		return (int) object.getX();
	};

	public int getY() {
		return (int) object.getY();
	};

	public void lineTo(int px1, int py1) {
		Path path_neuer = new Path(destinationX, destinationY);
		path_neuer.lineTo(px1, py1);
		destinationX = px1;
		destinationY = py1;
		p.add(path_neuer);
	}

	public void curveTo(int px1, int py1, int px2, int py2, int px3, int py3,
			int Linien) {
		Path path_new = new Path(destinationX, destinationY);
		path_new.curveTo(px1, py1, px2, py2, px3, py3, Linien);
		destinationX = px1;
		destinationY = py1;
		p.add(path_new);
	}

	public void curveTo(int px1, int py1, int px2, int py2, int px3, int py3) {
		curveTo(px1, py1, px2, py2, px3, py3, 25);
	}

	public boolean isStandingStill() {
		if (p.size() == 0) {
			return true;
		}
		return false;
	}
}
