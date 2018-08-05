package de.game.objects;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import jdk.nashorn.internal.runtime.ScriptFunction;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;



//import sun.org.mozilla.javascript.internal.Context;
//import sun.org.mozilla.javascript.internal.Function;
//import sun.org.mozilla.javascript.internal.Scriptable;
//import sun.org.mozilla.javascript.internal.ScriptableObject;
import de.game.game.Main;

public abstract class CObject {
	private float x;
	private float y;
	private float velocity = 0;
	private double angel = 0;
	private static float ScrollX = 0f;
	private static float ScrollY = 0f;
	private static float VerschiebungX = 0;
	private static float VerschiebungY = 0;
	public WalkPath walkPath;
	protected String name;
	public final void draw(Graphics g) {
		draw((int) getX(), (int) getY(), g);
	}
	public final void draw(Graphics g, ScriptEngine e) {
		// draw((int)getX() + getVerschiebungX(), (int)getY() +
		// getVerschiebungY(),g);
		if(renderFunction == null){
			draw(g);
		}else{
			
			try {
				e.put("$$$_currendRenderObject", this);
				e.put("$$$_currendGraphicObject", g);
				e.eval("$$$_render();");
			} catch (ScriptException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


		}
	};
	@Deprecated
	public void draw(){
		draw((Integer)drawObjekt[0],(Integer) drawObjekt[1] ,(Graphics) drawObjekt[2]);
	}
	public abstract void draw(int x, int y, Graphics g);

	protected abstract void update(int delta,int index);

	public abstract void init();

	public void initWalkPath() {
		walkPath = new WalkPath(this);
	};

	public static void updateScroll(int delta) {
		VerschiebungX += ScrollX * delta;
		VerschiebungY += ScrollY * delta;
	}

	public void updateMain(GameContainer container, StateBasedGame game, Input input, int delta, ArrayList<Layer> list,int index,boolean isSolidLayer, ScriptEngine e) {
		if (walkPath != null && !walkPath.isStandingStill()) {
			walkPath.update(delta, getVelocity());
			if(isSolidLayer && redoMove(list)){
				walkPath.update(-delta, getVelocity());
			}
		} else if (getVelocity() != 0) {
			int faktor = 1;
			double angel = 1;
			if (getAngel() > Math.PI) {
				faktor = -1;
				angel = getAngel() + Math.PI * 1.5;
			} else {
				angel = getAngel() + Math.PI * 0.5;
			}
			
			x -= getVelocity() * delta * Math.cos(angel) * faktor;
			y += getVelocity() * delta * Math.sin(angel) * faktor;
			if(isSolidLayer && redoMove(list)){
				x += getVelocity() * delta * Math.cos(angel) * faktor;
				y -= getVelocity() * delta * Math.sin(angel) * faktor;
			}
		}

		if(updateFunctions.size() != 0){
			try {
				e.put("$$$_currendRenderObject", this);
				e.eval("$$$_update();");
			} catch (ScriptException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		update(delta,index);
	};
	private boolean redoMove(ArrayList<Layer> list) {
		
		for (Layer l : list) {
			if (l.isSolid()) {
				for (CObject c : l) {
					if (c.getName() == null || c.getName().equals(getName())) {
						continue;
					}
					if (c.touch(this)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	String sricpt = "";

	public void setScript(String s) {
		s = s.trim();
		sricpt = s;
	}

	public String getScript() {
		return sricpt;
	}

	public void setName(String name) {
		this.name = name.replace(".", "_");
	}

	public String getName() {
		return name;
	};
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setLocalation(int x, int y) {
		setX(x);
		setY(y);
	}

	public Point getLocalation() {
		return new Point((int) getX(), (int) getY());
	}

	// get horizontal velocity
	public float getVelocity() {
		return velocity;
	}

	// set horizontal velocity
	public void setVelocity(float vx) {
		this.velocity = vx;
	}

	public void setAngleByKey(Input input, float velocity, int up, int right,
			int down, int left) {
		this.setVelocity(velocity);
		if (input.isKeyDown(down)) {
			if (input.isKeyDown(left)) {
				setAngelR(315);
			} else if (input.isKeyDown(right)) {
				setAngelR(45);
			} else {
				setAngelR(0);
			}
		} else if (input.isKeyDown(right)) {
			if (input.isKeyDown(up)) {
				setAngelR(135);
			} else {
				setAngelR(90);
			}
		} else if (input.isKeyDown(up)) {
			if (input.isKeyDown(left)) {
				setAngelR(225);
			} else {
				setAngelR(180);
			}
		} else if (input.isKeyDown(left)) {
			setAngelR(270);
		} else {
			setVelocity(0);
		}
	}

	public void setAngleByKey(Input input, float velocity) {
		setAngleByKey(input, velocity, Input.KEY_W, Input.KEY_D, Input.KEY_S,
				Input.KEY_A);
	}

	public int getAngelR() {
		return (int) Math.toDegrees(angel);
	}

	public int getDirection(int teiler) {
		return (int) (angel / (Math.PI * 2) * teiler);
	}

	public double getAngel() {
		return angel;
	}

	public void setAngelR(int angle) {
		setAngel(Math.toRadians(angle+90));
	}

	// -0 bis 2*PI;
	public void setAngel(double angel) {
		this.angel = angel;
	}

	public static float getScrollX() {
		return ScrollX;
	}

	public static float getScrollY() {
		return ScrollY;
	}

	public static void setScrollX(float scrollX) {
		ScrollX = scrollX;
	}

	public static void setScrollY(float scrollY) {
		ScrollY = scrollY;
	}

	public static int getVerschiebungX() {
		return (int) VerschiebungX;
	}

	public static int getVerschiebungY() {
		return (int) VerschiebungY;
	}

	public static void setVerschiebungX(float verschiebungX) {
		VerschiebungX = verschiebungX;
	}

	public static void setVerschiebungY(float verschiebungY) {
		VerschiebungY = verschiebungY;
	}

	@Deprecated
	public void setName(String name2, boolean b) {
		setName(name2);
	}

	public abstract String getXMLName();

	public abstract Shape getShape();

	public boolean intersects(CObject c) {
		return getShape().intersects(c.getShape());
	}

	public boolean contains(CObject c) {
		return getShape().contains(c.getShape());
	}

	public boolean touch(CObject c) {
		if(this instanceof CTileMap){
			return ((CTileMap) this).isTileSolid(c.getShape(),c.getAngel());
		}
		if(c instanceof CTileMap){
			return ((CTileMap) c).isTileSolid(this.getShape(),c.getAngel());
		}
		//TODO Prüfen
		Shape s1 = getShape(),s2 = c.getShape();
		if(s1 == null || s2 == null){
			return false;
		}
		float absX = s1.getX() - s2.getX();
		float absY = s1.getY() - s2.getY();
		double abs = absX*absX+absY*absY;
		if(abs > (s1.getBoundingCircleRadius()+s2.getBoundingCircleRadius())*(s1.getBoundingCircleRadius()+s2.getBoundingCircleRadius())){
			return false;
		}
		int fps = Main.appgc.getFPS();
		if(fps == 0)fps=1;
		int jumpPerFrame = (int)(1000/fps*velocity)*2;
		if(getWidth() < jumpPerFrame || getHeight()< jumpPerFrame){
			return contains(c) || intersects(c);
		}
		return intersects(c);
	}
	public int getWidth() {
		return (int)getShape().getWidth();
	}
	public int getHeight() {
		return (int)getShape().getHeight();
	}
	static Rectangle r = new Rectangle(0, 0, 100, 100);
	public Rectangle getVisibleRectangle(){
		r.setX(-getVerschiebungX());
		r.setY(-getVerschiebungY());
		r.setWidth(Main.appgc.getWidth());
		r.setHeight(Main.appgc.getHeight());
		return r;
	}
	//**Script*/
//	static final Context context = Context.enter();
//	static final ScriptableObject scope = context.initStandardObjects();
//	static final Scriptable scriptable = context.newObject(scope);
	public ArrayList<ScriptFunction > updateFunctions = new ArrayList<>();
	Object[] updateObjekt = new Object[6];
	Object[] drawObjekt = new Object[4];
//	Functiontion renderFunction;
	public Object renderFunction;
	public void addUpdateFunktion(ScriptFunction  o){//sun.org.mozilla.javascript.internal.Function
		updateFunctions.add(o);
	}
	public void removeUpdateFunktion(ScriptFunction  o){//sun.org.mozilla.javascript.internal.Function
		updateFunctions.remove(o);
	}
	public void clearUpdateFunktion(){
		updateFunctions.clear();
	}
	public void setDrawFunktion(Object o){//sun.org.mozilla.javascript.internal.Function
		renderFunction = o;
	}
}
