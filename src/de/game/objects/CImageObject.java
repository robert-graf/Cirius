package de.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public abstract class CImageObject extends CObject{
	private int repeatX = 1;
	private int repeatY = 1;
	private int abstandX ;
	private int abstandY ;
	Render renderer = new DefaultRenderer();
	public CImageObject() {
	}
	@Override
	public final void draw(int xl, int yl, Graphics g) {
		renderer.draw(xl, yl, g);
	}
	@Override
	protected final void update(int delta,int index) {
		updateRenderer(delta,index);
	}
	public abstract void drawRenderer(Graphics g, int x, int y);

	public abstract void updateRenderer(int delta, int index);

	public abstract Shape getSingelShape();

	@Override
	public final Shape getShape() {
		return getSingelShape();
	}
	public int getRepeatX() {
		return repeatX;
	}
	public void setRepeatX(int repeatX) {
		if(!(renderer instanceof MultieRender)){
			renderer = new MultieRender();
		}
		this.repeatX = repeatX;
	}
	public int getRepeatY() {
		return repeatY;
	}
	public void setRepeatY(int repeatY) {
		if(!(renderer instanceof MultieRender)){
			renderer = new MultieRender();
		}
		this.repeatY = repeatY;
	}
	public int getAbstandX() {
		return abstandX;
	}
	public void setAbstandX(int abstandX) {
		this.abstandX = abstandX;
	}
	public int getAbstandY() {
		return abstandY;
	}
	public void setAbstandY(int abstandY) {
		this.abstandY = abstandY;
	}
	private abstract class Render{
		public abstract void draw(int xl, int yl, Graphics g);
	}
	private class DefaultRenderer extends Render{
		public void draw(int xl, int yl, Graphics g) {
			drawRenderer(g,xl,yl);
		}
	}
	private class MultieRender extends Render{
		@Override
		public final void draw(int xl, int yl, Graphics g) {
			if(abstandX < getWidth()/2){
				abstandX = getWidth();
			}
			if(abstandY < getHeight()/2){
				abstandY = getHeight();
			}
			int x = (int)xl;
			int y = (int)yl;
			int finalContX = repeatX;
			int finalContY = repeatY;
			if(repeatX == 0){
			Rectangle r = getVisibleRectangle();
			x = ((int)getX())%abstandX+((int)r.getX())/abstandX*abstandX-abstandX*2;
			finalContX = (int) (r.getWidth()/abstandX)+4;
			}
			if(repeatY == 0){
				Rectangle r = getVisibleRectangle();
				y = ((int)getY())%abstandY+((int)r.getY())/abstandY*abstandY-abstandY*2;
				finalContY = (int) (r.getHeight()/abstandY)+4;
			}
			for(int n = 0; n != finalContX;n++){
				for(int i = 0; i!= finalContY; i++){
					drawRenderer(g,x+n*abstandX,y+i*abstandY);
				}
			}
		}
	}
}
