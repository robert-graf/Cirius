package de.game.objects.geom;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.FastTrig;


public class CPath extends CShape {
	/**
	 * // M = moveto NOT USED // L = lineto NOT USED // H = horizontal lineto
	 * NOT USED // V = vertical lineto NOT USED // C = curveto NOT USED // S =
	 * smooth curveto NOT USED // Q = quadratic Bézier curve NOT USED // T =
	 * smooth quadratic Bézier curveto NOT USED // A = elliptical Arc NOT USED
	 * // Z = closepath NOT USED Workarounds: // R = 3 Punkte Bogen
	 **/
	Path path;
	boolean requestUpdate = true;
	static boolean isUndoing = false;
	//TODO Transform und weiterzeichen
//	Boolean evenOdd =true;
	ArrayList<PrivatePoint> points = new ArrayList<PrivatePoint>();
	static final Color line = new Color(150, 150, 150, 230);
	static final Color ausgewählt = new Color(255, 255, 0);

	public CPath(CKreis kreis) {
		float[] i = new float[7];
		i[0] = kreis.getX() + kreis.getRadius();
		i[1] = kreis.getY();
		points.add(new PrivatePoint('M', i));
		i = new float[7];
		i[0] = (float) (kreis.getX() + kreis.getRadius() / Math.sqrt(2));
		i[1] = (float) (kreis.getY() - kreis.getRadius() / Math.sqrt(2));
		i[2] = kreis.getX();
		i[3] = kreis.getY() - kreis.getRadius();
		points.add(new PrivatePoint('R', i));
		i = new float[7];
		i[0] = (float) (kreis.getX() - kreis.getRadius() / Math.sqrt(2));
		i[1] = (float) (kreis.getY() + kreis.getRadius() / Math.sqrt(2));
		i[2] = kreis.getX() + kreis.getRadius();
		i[3] = kreis.getY();
		points.add(new PrivatePoint('R', i));
		init();
		// points.add(new PrivatePoint('Z', new float[7]));
	}

	public CPath(String code) {
		StringTokenizer token = new StringTokenizer(code.trim(),
				" \n\r\t,abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVEXYZ",
				true);
		char c = '?';
		float[] i = new float[7];
		int n = 0;
		while (token.hasMoreElements()) {
			String st = token.nextToken();
			if (st.equals(" ") || st.equals("\r") || st.equals("\t")
					|| st.equals(",")) {
				continue;
			}
			if (st.contains(".")) {
				i[n] = Float.parseFloat(st);
				n++;
			} else if (st.contains("0") || st.contains("1") || st.contains("2")
					|| st.contains("3") || st.contains("4") || st.contains("5")
					|| st.contains("6") || st.contains("7") || st.contains("8")
					|| st.contains("9")) {
				i[n] = Integer.parseInt(st);
				n++;
			} else {
				if (c != '?') {
					points.add(new PrivatePoint(c, i));
					i = new float[7];
				}
				c = st.charAt(0);
				n = 0;
			}
			
		}
		points.add(new PrivatePoint(c, i));
		init();
	}

	public CPath(CRect c) {
		float[] i = new float[7];
		i[0] = c.getX();
		i[1] = c.getY();
		points.add(new PrivatePoint('M', i));
		i = new float[7];
		i[0] = c.getX() + c.getWidth();
		i[1] = c.getY();
		points.add(new PrivatePoint('L', i));
		i = new float[7];
		i[0] = c.getX() + c.getWidth();
		i[1] = c.getY() + c.getHeight();
		points.add(new PrivatePoint('L', i));
		i = new float[7];
		i[0] = c.getX();
		i[1] = c.getY() + c.getHeight();
		points.add(new PrivatePoint('L', i));
		points.add(new PrivatePoint('Z', new float[7]));
		init();
	}
	public void init(){
//Center
//		int mx = 0,my = 0;
//		for(PrivatePoint p : points){
//			mx+= p.getX();
//			my+= p.getY();
//		}
//		mx /= points.size();
//		my /= points.size();
//		lineX.setValue(mx);
//		lineY.setValue(my);
	}
	@Override
	public void setX(float x) {
		if(x == getX()){
			return;
		}
		requestUpdate = true;
		float verschiebung = getX() - x;
		for(PrivatePoint p : points){
			p.transformX(verschiebung);
		}
		super.setX(x);
	}
	@Override
	public void setY(float y) {
		if(y == getY()){
			return;
		}
		requestUpdate = true;
		float verschiebung = getY() - y;
		for(PrivatePoint p : points){
			p.transformY(verschiebung);
		}
		super.setY(y);
	}
	@Override
	public String getXMLName() {
		return "path";
	}
	@Override
	public void update(int timePassed, int conter) {
		if(!requestUpdate){
			return;
		}
		init();
		path = null;
//		if (isEvenodd()) {
////			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
//		} else {
////			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
//		}
		PrivatePoint prev = null;

		for (PrivatePoint p : points) {
			p.previews = prev;
			prev = p;
			// System.out.println(p);
			if (p.getC() == 'M' || p.getC() == 'm') {
				//TODO MOVETO
				if(path == null){
					path = new Path(p.getInt(0), p.getInt(1));
				}else{
					path.startHole(p.getInt(0), p.getInt(1));
				}
				path.lineTo(p.getInt(0), p.getInt(1));
			} else if (p.getC() == 'L' || p.getC() == 'l' || p.getC() == 'V'
					|| p.getC() == 'v' || p.getC() == 'H' || p.getC() == 'h') {
				path.lineTo(p.getX(), p.getY());
			} else if (p.getC() == 'C' || p.getC() == 'c') {
				path.curveTo(p.getInt(0), p.getInt(1), p.getInt(2),
						p.getInt(3), p.getInt(4), p.getInt(5));
			} else if (p.getC() == 'S' || p.getC() == 's') {
				PrivatePoint v = p.previews;
				float x1 = p.getInt(0);
				float y1 = p.getInt(1);
				if (v.getC() == 'C' || v.getC() == 'c') {
					x1 = v.getInt(4) + (v.getInt(4) - v.getInt(2));
					y1 = v.getInt(5) + (v.getInt(5) - v.getInt(3));
				}
				path.curveTo(x1, y1, p.getInt(0), p.getInt(1), p.getInt(2),
						p.getInt(3));
			} else if (p.getC() == 'Q' || p.getC() == 'q') {
				//TODO quad to richtig?
				path.curveTo(p.getInt(0), p.getInt(1), p.getInt(2), p.getInt(3),p.getInt(2), p.getInt(3));
			} else if (p.getC() == 'T' || p.getC() == 't') {
				PrivatePoint v = p.previews;
				float x1 = p.getInt(0);
				float y1 = p.getInt(1);
				if (v.getC() == 'Q' || v.getC() == 'q') {
					x1 = v.getInt(2) + (v.getInt(2) - v.getInt(0));
					y1 = v.getInt(3) + (v.getInt(3) - v.getInt(1));
				}
				//TODO quad to richtig?
				path.curveTo(x1, y1, p.getInt(0), p.getInt(1),p.getInt(0), p.getInt(1));
			} else if (p.getC() == 'A' || p.getC() == 'a') {
				// arcTo(path, rx, ry, theta, largeArcFlag, sweepFlag, x, y);
				arcTo(path, p.i[0], p.i[1], p.i[2], p.i[3] == 1, p.i[4] == 1,
						p.getInt(5), p.getInt(6), p);
			} else if (p.getC() == 'R' || p.getC() == 'r') {
				creat3PunkteBogen(path, p);
			} else if (p.getC() == 'Z' || p.getC() == 'z') {
				path.close();
			} else {
				try {
					throw new Exception("Unbekannter Buchstaben für CPath: "
							+ p.getC());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestUpdate = false;
			
		}
	}

	@Override
	public Shape getShape() {
		return path;
	}

//	public boolean isEvenodd() {
//		return evenOdd.getValue();
//	}
//
//	public void setEvenOdd(boolean evenOdd) {
//		requestUpdate = true;
//		this.evenOdd.setValue(evenOdd);
//	}

	/** Interne Klassen */
	// M = moveto
	// L = lineto
	// H = horizontal lineto
	// V = vertical lineto
	// C = curveto
	// S = smooth curveto
	// Q = quadratic Bézier curve
	// T = smooth quadratic Bézier curveto
	// A = elliptical Arc
	// Z = closepath
	private class PrivatePoint {
		public float getX() {
			if (Character.isUpperCase(c1)) {
				if (c1 == 'V') {
					return previews.getX();
				} else if (c1 == 'A') {
					return i[5];
				} else if (c1 == 'C') {
					return i[4];
				} else if (c1 == 'Q' || c1 == 'S' || c1 == 'R') {
					return i[2];
				} else if (c1 == 'Z') {
					try {
						throw new Exception("getRequest bei Z");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return i[0];
				} else {// MLHT
					return i[0];
				}
			} else {
				if (c1 == 'v') {
					return previews.getX();
				} else if (c1 == 'a') {
					return i[5] + previews.getX();
				} else if (c1 == 'c') {
					return i[4] + previews.getX();
				} else if (c1 == 'q' || c1 == 's' || c1 == 'r') {
					return i[2] + previews.getX();
				} else if (c1 == 'z') {
					try {
						throw new Exception("getRequest bei z");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return i[0] + previews.getX();
				} else {// mlht
					return i[0] + previews.getX();
				}
			}
		}

		public void transformX(float verschiebung) {
			if (c1 == 'A' || c1 == 'a') {
				i[5]-=verschiebung;
				return;
			}
			i[0]-=verschiebung;
			i[2]-=verschiebung;
			i[4]-=verschiebung;
		}
		public void transformY(float verschiebung) {
			if (c1 == 'A' || c1 == 'a') {
				i[6]-=verschiebung;
				return;
			}
			i[1]-=verschiebung;
			i[3]-=verschiebung;
			i[5]-=verschiebung;
		}
		public float getY() {
			if (Character.isUpperCase(c1)) {
				if (c1 == 'H') {
					return previews.getY();
				} else if (c1 == 'A') {
					return i[6];
				} else if (c1 == 'C') {
					return i[5];
				} else if (c1 == 'Q' || c1 == 'S' || c1 == 'R') {
					return i[3];
				} else if (c1 == 'Z') {
					try {
						throw new Exception("getRequest bei Z");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return i[1];
				} else {// MLVT
					return i[1];
				}
			} else {
				if (c1 == 'h') {
					return previews.getY();
				} else if (c1 == 'a') {
					return i[6] + previews.getY();
				} else if (c1 == 'c') {
					return i[5] + previews.getY();
				} else if (c1 == 'q' || c1 == 's' || c1 == 'r') {
					return i[3] + previews.getY();
				} else if (c1 == 'z') {
					try {
						throw new Exception("getRequest bei z");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return i[1] + previews.getY();
				} else {// mlvt
					return i[1] + previews.getY();
				}
			}
		}

		public char getC() {
			return c1;
		}

		public void setX(float x) {
			requestUpdate = true;
			if (Character.isUpperCase(c1)) {
				if (c1 == 'V') {
				} else if (c1 == 'A') {
					i[5] = x;
				} else if (c1 == 'C') {
					i[4] = x;
				} else if (c1 == 'Q' || c1 == 'S' || c1 == 'R') {
					i[2] = x;
				} else {// MLHT
					i[0] = x;
				}
			} else {
				if (c1 == 'v') {
				} else if (c1 == 'a') {
					i[5] = x - previews.getX();
				} else if (c1 == 'c') {
					i[4] = x - previews.getX();
				} else if (c1 == 'q' || c1 == 's' || c1 == 'r') {
					i[2] = x - previews.getX();
				} else {// mlht
					i[0] = x - previews.getX();
				}
			}
		}

		public void setY(float y) {
			requestUpdate = true;
			if (Character.isUpperCase(c1)) {
				if (c1 == 'H') {
				} else if (c1 == 'A') {
					i[6] = y;
				} else if (c1 == 'C') {
					i[5] = y;
				} else if (c1 == 'Q' || c1 == 'S' || c1 == 'R') {
					i[3] = y;
				} else {// MLVT
					i[1] = y;
				}
			} else {
				if (c1 == 'h') {
				} else if (c1 == 'a') {
					i[6] = y - previews.getY();
				} else if (c1 == 'c') {
					i[5] = y - previews.getY();
				} else if (c1 == 'q' || c1 == 's' || c1 == 'r') {
					i[3] = y - previews.getY();
				} else {// mlht
					i[1] = y - previews.getY();
				}
			}
		}

		public void setInt(int n, float v) {
			requestUpdate = true;
			if (Character.isUpperCase(c1)) {
				this.i[n] = v;
			} else {
				if (n % 2 == 0) {
					this.i[n] = v - previews.getX();
				} else {
					this.i[n] = v - previews.getY();
				}
			}
		}

		public float getInt(int n) {
			if (Character.isUpperCase(c1) || c1 == 'z') {
				return this.i[n];
			} else {
				if (n % 2 == 0) {
					return this.i[n] + previews.getX();
				} else {
					return this.i[n] + previews.getY();
				}
			}
		}

		public void setC(char c) {
			requestUpdate = true;
			// TODO Transformaction
			this.c1 = c;
		}

		private char c1;
		private float[] i;
		PrivatePoint previews;
		double radius;
		public boolean gegenUrzeiger;
		public boolean kleiner180;

		public PrivatePoint(char c, float[] i) {
			this.i = i;
			this.c1 = c;
			if (c == 'v' || c == 'V') {
				i[1] = i[0];
			}
		}

		@Override
		public String toString() {
			return c1 + "\t" + i[0] + "\t" + i[1] + "\t" + i[2] + "\t" + i[3]
					+ "\t" + i[4] + "\t" + i[5] + "\t" + i[6];
		}

		public String getText(boolean b) {
			String s = c1 + " ";
			if(b && (c1 == 'R'||c1 == 'r')){
				s =  (Character.isUpperCase(c1)?"A":"a")+ radius + ","+ radius+" " + 0 + " " + (kleiner180?1:0)+ " "+ (gegenUrzeiger?1:0)+ " "+i[2]+","+i[3];
				return s;
			}
			// Z 									  0
			if (c1 == 'Z' || c1 == 'z') {
				return s;
			}
			// H = horizontal lineto                  1
			// V = vertical lineto                    1
			if(i[0]%1f == 0){
				s+=(int)i[0];
			}else{
				s+=i[0];
			}			
			if(c1 == 'H' || c1 == 'h' || c1 == 'V'|| c1 == 'v'){
				return s+" ";
			}
			// M = moveto							  2
			// L = lineto                             2
			// S = smooth curveto                     2
			if(i[1]%1f == 0){
				s+= ","+(int)i[1]+" ";				
			}else{
				s+=(int)i[0];
			}
			if (c1 == 'M' || c1 == 'm' || c1 == 'L' || c1 == 'l' || c1 == 'S' || c1 == 's') {
				return s;
			}
			// T = smooth quadratic Bézier curveto    4
			// Q = quadratic Bézier curve             4
			// R = 3 Punkte Kreis                     4
			if(i[2]%1f == 0){
				s+= (int)i[2]+ ",";
			}else{
				s+= i[2]+ ",";
			}	
			if(i[3]%1f == 0){
				s+=(int)i[3]+" ";
			}else{
				s+=i[3]+" ";
			}
			if (c1 == 'Q' || c1 == 'q' || c1 == 'R' || c1 == 'R'|| c1 == 'T' || c1 == 't') {
				return s;
			}
			// C = curveto                            6
			if(i[4]%1f == 0){
				s+= (int)i[4]+ ",";
			}else{
				s+= i[4]+ ",";
			}	
			if(i[5]%1f == 0){
				s+=(int)i[5]+" ";
			}else{
				s+=i[5]+" ";
			}
			if(c1 == 'C' || c1 == 'c'){
				return s;
			}
			if(i[6]%1f == 0){
				s+=(int)i[6]+" ";
			}else{
				s+=i[6]+" ";
			}
			if (c1 == 'A' || c1 == 'a') {
				return s;
			}
			try {
				throw new Exception("\""+s+"\" ist unbekannt zum Speichern.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return s;
		}
	}
	static int segmentCount = 50;
	public static void creat3PunkteBogen(Path path, PrivatePoint p) {
		//TODO Fast Triangolator nutzen
		double x1, x2, x3, y1, y2, y3;
		x1 = p.previews.getX();
		y1 = p.previews.getY();
		x2 = p.getInt(0);
		y2 = p.getInt(1);
		x3 = p.getInt(2);
		y3 = p.getInt(3);
		double MittelPunktAB_X = (x1 + x2) / 2;
		double MittelPunktAB_Y = (y1 + y2) / 2;
		double SteigungAB = -(x2 - x1) / (y2 - y1);
		/**
		 * y = SteigungAB(x-MittelPunktAB_X)+MittelPunktAB_Y
		 * */
		double MittelPunktAC_X = (x1 + x3) / 2;
		double MittelPunktAC_Y = (y1 + y3) / 2;
		double SteigungAC = -(x3 - x1) / (y3 - y1);
		/**
		 * y = SteigungAC(x-MittelPunktAC_X)+MittelPunktAC_Y
		 * 
		 * SteigungAB*x-SteigungAC*x =
		 * MittelPunktAC_Y-MittelPunktAB_Y+SteigungAB
		 * *MittelPunktAB_X-SteigungAC*MittelPunktAC_X
		 * */
		double x = (MittelPunktAC_Y - MittelPunktAB_Y + SteigungAB
				* MittelPunktAB_X - SteigungAC * MittelPunktAC_X)
				/ (SteigungAB - SteigungAC);
		double y = SteigungAB * (x - MittelPunktAB_X) + MittelPunktAB_Y;
		double pytX = x - x1;
		double pytY = y - y1;
		double radius = (Math.sqrt(pytX * pytX + pytY * pytY));
		double winkel1 = Math.toDegrees(Math.acos((x1 - x) / radius));
		if (y < y1) {
			winkel1 = 360 - winkel1;
		}
		// System.out.println(winkel1);
		double winkel2 = Math.toDegrees(Math.acos((x3 - x) / radius));
		if (y < y3) {
			winkel2 = 360 - winkel2;
		}
		double winkel3 = Math.toDegrees(Math.acos((x2 - x) / radius));
		if (y < y2) {
			winkel3 = 360 - winkel3;
		}
		// System.out.println("Radius: " + radius + "\tMitte: " + x + "," +
		// y+"\t Winkel:" + winkel1 + "\t" + winkel2);
		if (Double.isNaN(winkel1) || Double.isNaN(winkel2)
				|| Double.isInfinite(radius)) {
			System.err.println("NAN CPath "
					+ new Exception().getLocalizedMessage());
			path.lineTo((float)x3,(float) y3);
			return;
		}
		boolean gegenUrzeiger = false;
		if (radius < 5000) {
			// Über 3 Punkt gehen
			if (winkel1 <= winkel2) {
				if (winkel1 < winkel3 && winkel3 < winkel2) {
					gegenUrzeiger = true;
				}
			} else {
				if (winkel1 < winkel3 || winkel3 < winkel2) {
					gegenUrzeiger = true;
				}
			}
		} else {
			// Kurzer weg
			if (winkel1 < winkel2) {
				if ((winkel2 - winkel1) < 180) {
					gegenUrzeiger = true;
				}
			} else {
				if ((winkel2 + 360 - winkel1) < 180) {
					gegenUrzeiger = true;
				}
			}
		}
		// System.out.println(winkel2);
		/**
		 * Mittelpunkt: Schnittpunkt der Senkrechen Seiten halbierenden A-B = MA
		 * B-C = MB M Radius: pythagoras aus Punkt X und Mittelpunkt.
		 */
		if(gegenUrzeiger){
			if(winkel1>winkel2){
				winkel2+=360;
			}
		}else{
			if(winkel1<winkel2){
				winkel2-=360;
			}
		}
		int step = 360 / (segmentCount);
		boolean stop = false;
		double angle = winkel1;
		while(!stop){
			float currX = (float) (x + (FastTrig.cos(Math.toRadians(angle)) * radius));
			float currY = (float) (y - (FastTrig.sin(Math.toRadians(angle)) * radius));
			if(gegenUrzeiger){
				angle+=step;
				if(angle > winkel2){
					stop = true;
				}
			}else{
				angle-=step;
				if(angle < winkel2){
					stop = true;
				}
			}
			path.lineTo(currX, currY);
		}
		path.lineTo((float)x3, (float)y3);
//		float fak = 10f;
//		p.setInt(0,(float) (p.getInt(0)+Math.random()*fak-Math.random()*fak));
//		p.setInt(1,(float) (p.getInt(1)+Math.random()*fak-Math.random()*fak));
//		p.setInt(2,(float) (p.getInt(2)+Math.random()*fak-Math.random()*fak));
//		p.setInt(3,(float) (p.getInt(3)+Math.random()*fak-Math.random()*fak));
//		p.previews.setX((float) (p.previews.getX()+Math.random()*fak-Math.random()*fak));
//		p.previews.setY((float) (p.previews.getY()+Math.random()*fak-Math.random()*fak));
		p.radius = radius;
		p.gegenUrzeiger = gegenUrzeiger;
		p.kleiner180 = Math.abs(winkel1-winkel2) < 180;	
	}
	public static final void arcTo(Path path, double rx, double ry,
			double theta, boolean largeArcFlag, boolean sweepFlag, double x,
			double y, PrivatePoint pp) {
			path.lineTo((float)x, (float)y);
			//TODO
			return;
	}

	public static void isFalse(double err, String s, int step) {
		if (Double.isInfinite(err) || Double.isNaN(err)) {
			System.err.println(s + " ist " + err + ". Bei Step " + step + ".");
		}
	}
	public String getSVGText() {
		String s = "";
		for (PrivatePoint p : points) {
			s +=p.getText(true);
		}
		return s;
	}

	public String getInternelText() {
		String s = "";
		for (PrivatePoint p : points) {
			s +=p.getText(false);
		}
		return s;
	}

	@Override
	public String getShapeName() {
		return "Path";
	}

	@Override
	public void setLocalistaon(int x, int y) {
		setX(x);
		setY(y);
	}
	@Deprecated
	public void setEvenOdd(boolean equals) {
		// TODO Auto-generated method stub
		
	}
}
