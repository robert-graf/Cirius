package editor.rpg;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;

import org.w3c.dom.Element;

import database.SpriteXML;
import editor.DefaultEditorPanel.Matrix;
import editor.tabelle.NumberLine;
import interfaces.Destroyer;
import interfaces.UndoMethode;

public class CSprite extends CImageObject {

	private SpriteXML sp;
	private Sprite sprite;

	// null wenn Anonym
	// protected File file;

	// XML erstellen
	public CSprite(double x, double y, Element oneSprite, String name, int index) {
		this(x, y, new SpriteXML(oneSprite), name, index);
	}

	// Über einzelne Datei
	public CSprite(double x, double y, File f, String name, int index) {
		this(x, y, new SpriteXML(f), name, index);
		if (".png"
				.endsWith(f.getName().substring(f.getName().lastIndexOf(".")))) {
			return;
		}
		setFile(f);
	}

	NumberLine<Integer> animationLine;

	// Classe SpriteXML
	public CSprite(double x, double y, SpriteXML sp, String name, int index) {
		setName(name);
		this.sp = sp;
		sprite = sp.getSprite();
		init(x, y);
		sprite.setX(x);
		sprite.setY(y);
		setAnimation(index);
		animationLine = new NumberLine<Integer>("Animatin (Init)", 0, 0,
				sp.AnimationList.size() - 1, 1) {
			@Override
			public Integer getValue() {
				return getAnimation();
			}

			@Override
			public void valueChanges(Integer value) {
				setAnimation(value);
			}
		};
		addLine(animationLine);
	}

	@Override
	public void drawRenderer(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y) {
		g.drawImage(sprite.getImage(), x, y, null);
	}

	Rectangle2D.Double r = new Rectangle2D.Double();

	public Rectangle2D.Double getSingelShape() {
		r.setFrame(getX(), getY(), sprite.getWidth(), sprite.getHeight());
		return r;
	}

	@Override
	public void updateRenderer(Matrix m, int timePassed, int conter) {
		sprite.update(timePassed);
	};

	public void setAnimation(int i) {
		setAnimation(i, true);
	}

	public void setAnimation(Integer i, Boolean redo) {
		if (getMatrix() != null && redo) {
			getMatrix().addUndo(
					new UndoMethode(sprite.getAnimation(), i, "setAnimation",
							this, "Neue Startanimation"));
		}
		sprite.setAnimation(i);
	}

	public SpriteXML getSpriteXML() {
		return sp;
	}

	public Class<?> getGameClass() {
		return de.game.objects.CSprite.class;
	}

	public int getAnimation() {
		return sprite.getAnimation();
	}

	public int getAnimationCont() {
		return sprite.Animationen.size();
	}

	@Override
	public String getXMLName() {
		return "Sprite";
	}

	@Override
	public void destroy() {
		super.destroyMain();
		Destroyer.destroy(sp);
		sp = null;
		Destroyer.destroy(sprite);
		sprite = null;
	}

	@Override
	public CSprite clone2() throws CloneNotSupportedException {
		CSprite c = new CSprite(getX(), getY(), sp.clone(), getName(),
				getAnimation());
		return c;
	}

	@Override
	public void reload() {
		int i = getAnimation();
		sp = new SpriteXML(sp.getFile());
		sprite = sp.getSprite();
		animationLine.setMaximum(sp.AnimationList.size() - 1);
		setAnimation(i);
	}

}// ende class
