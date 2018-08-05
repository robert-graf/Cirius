package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPopupMenu;

import basic.DDrawer;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.cad.CPath.Tangente;
import editor.tools.GreifTool;

public class GriffOval {

	private final CPath.Tangente tangente;
	private final CPath.Tangente tangente2;
	private final CPath path;

	private final OneGriff a = new OneGriff(1);
	private final OneGriff b = new OneGriff(2);
	private final OneGriff c = new OneGriff(3);
	private final OneGriff d = new OneGriff(4);
	private final SweepFlagGriff sweep = new SweepFlagGriff();
	private double xAxisRotationR;
	private double sinValueOfRotation;
	private double cosValueOfRotation;

	public GriffOval(CPath.Tangente t, CPath p, Tangente tangente2) {
		this.tangente = t;
		this.path = p;
		this.tangente2 = tangente2;
		path.getGreifItems().add(sweep);
		path.getGreifItems().add(a);
		path.getGreifItems().add(b);
		path.getGreifItems().add(c);
		path.getGreifItems().add(d);

	}

	public double getCenterX() {
		final double x0 = tangente.point.getX();
		// Determine target "to" position
		final double xto = tangente2.point.getX();
		// Compute the half distance between the current and the final point
		return x0 - (x0 - xto) / 2.0;
	}

	public double getCenterY() {
		final double y0 = tangente.point.getY();
		// Determine target "to" position
		final double yto = tangente2.point.getY();
		// Compute the half distance between the current and the final point
		return y0 - (y0 - yto) / 2.0;
	}

	public void updateRotation() {
		final double x0 = tangente.point.getX();
		final double y0 = tangente.point.getY();

		// Determine target "to" position
		final double xto = tangente2.point.getX();
		final double yto = tangente2.point.getY();
		// Compute the half distance between the current and the final point
		final double dx2 = (x0 - xto) / 2.0;
		final double dy2 = (y0 - yto) / 2.0;
		// Crate Rotation from Tangente
		final double tx = tangente2.point.tangeteVor.x - dx2;
		final double ty = tangente2.point.tangeteVor.y - dy2;
		// Convert angle from degrees to radians
		xAxisRotationR = Math.atan(ty / tx);
		sinValueOfRotation = Math.sin(xAxisRotationR);
		cosValueOfRotation = Math.cos(xAxisRotationR);
	}

	public double getRotation() {
		return xAxisRotationR;
	}

	static final Color ColorGriff = new Color(50, 125, 255);

	private class OneGriff extends GreifItem {
		public OneGriff(int id) {
			this.id = id;
		}

		int id = -1;

		@Override
		public int getID() {
			return id;
		}

		@Override
		public double getX() {
			return (int) (getCenterX() + getXOffset());
		}

		public double getXOffset() {
			if (id == 1) {
				return -tangente.y * sinValueOfRotation;
			}
			if (id == 2) {
				return tangente.y * sinValueOfRotation;
			}

			if (id == 3) {
				return tangente.x * cosValueOfRotation;
			}
			if (id == 4) {
				return -tangente.x * cosValueOfRotation;
			}

			return 10;

		}

		@Override
		public double getY() {
			return (int) (getCenterY() + getYOffset());
		}

		public double getYOffset() {
			if (id == 1) {
				return tangente.y * cosValueOfRotation;
			}
			if (id == 2) {
				return -tangente.y * cosValueOfRotation;
			}
			if (id == 3) {
				return tangente.x * sinValueOfRotation;
			}
			if (id == 4) {
				return -tangente.x * sinValueOfRotation;
			}

			return 10;

		}

		@Override
		public void setLocation(double x, double y) {
			if (id == 1 || id == 2) {
				tangente.y = Math.abs((x - getCenterX()) * sinValueOfRotation)
						+ Math.abs((y - getCenterY()) * cosValueOfRotation);
				return;
			}
			tangente.x = Math.abs((x - getCenterX()) * cosValueOfRotation + (y - getCenterY()) * sinValueOfRotation);

		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		@Override
		public void draw(Graphics2D g1, Graphics2D g2, Matrix m) {
			updateRotation();
			g1.setColor(Color.WHITE);
			int i = 9;
			r.setFrame(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2, i, i);
			g2.setColor(ColorGriff);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		@Override
		public boolean remove() {
			tangente.point.toLinie();
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return null;
		}

	}

	private class SweepFlagGriff extends GreifItem {

		@Override
		public int getID() {
			return -1337;
		}

		@Override
		public double getX() {
			return (int) (getCenterX());
		}

		@Override
		public double getY() {
			return (int) (getCenterY());
		}

		@Override
		public void setLocation(double x, double y) {
			tangente.point.LargeArcFlag = !tangente.point.LargeArcFlag;
			tangente2.x = -tangente2.x + (getCenterX()-tangente2.point.getX()) * 2;
			tangente2.y = -tangente2.y + (getCenterY()-tangente2.point.getY()) * 2;
			GreifTool.item = null;
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		@Override
		public void draw(Graphics2D g1, Graphics2D g2, Matrix m) {
			updateRotation();
			r.setFrame(getX(), getY(), 10, 10);
			g1.setColor(Color.WHITE);
			int i = 12;
			r.setFrame(m.getRealX(getX()) - i / 2, m.getRealY(getY()) - i / 2, i, i);
			g2.setColor(ColorGriff);
			DDrawer.fillOval(r.x, r.y, r.width, r.height,g2);
			g2.setColor(Color.WHITE);
			DDrawer.drawOval(r.x, r.y, r.width, r.height,g2);
			g2.draw(r);
		}

		@Override
		public boolean remove() {
			tangente.point.toLinie();
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return null;
		}

	}

	public void remove() {
		path.getGreifItems().remove(a);
		path.getGreifItems().remove(b);
		path.getGreifItems().remove(c);
		path.getGreifItems().remove(d);
		path.getGreifItems().remove(sweep);
	}

}
