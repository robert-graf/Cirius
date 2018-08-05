package basic;


public class DPoint {
	public double x, y;

	public DPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public DPoint() {
		x = 0;
		y = 0;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(DPoint calc) {
		setLocation(calc.x,calc.y);
	}

}
