package editor.cad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import basic.DDrawer;
import basic.DPoint;
import database.XMLCObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.rpg.CSprite;
import editor.tabelle.ListLine;
import editor.tabelle.NumberLine;
import editor.tabelle.ObjectContainerForStringTyping;
import editor.tabelle.OneLine;
import editor.tools.GreifTool;
import editor.tools.PopupShowOnHover;

public class CPath extends CShape {
	static final Color line = new Color(150, 150, 150, 230);
	ArrayList<PrivatePoint> polygon = new ArrayList<>();
	ArrayList<PolygonPoint> ppunkte = new ArrayList<>();
	ArrayList<VerschiebeFragmentGriff> fragments = new ArrayList<>();
	NumberLine<Integer> selectedPoint;
	JMenuItem currendExpensionMode;
	boolean currendExpensionModeSyncronise = false;
	ListLine<ObjectContainerForStringTyping<JMenuItem,Boolean>> expentionMode;
	static ArrayList<ObjectContainerForStringTyping<JMenuItem,Boolean>> possibleExpensionModes = new ArrayList<>();

	OneLine<Boolean> evenOdd = new OneLine<Boolean>("WindingRule", true) {
		@Override
		public void valueChanges(Boolean value) {
			setEvenOdd(value);
		}
	};
	OneLine<Boolean> close = new OneLine<Boolean>("Geschlossen", true) {
		@Override
		public void valueChanges(Boolean value) {
			setClose(value);
		}
	};
	public static JPopupMenu menuPoint = new JPopupMenu();
	public static JMenuItem menuitemEntfernen = new JMenuItem("Entfernen");
	public static JMenuItem menuitemTangenten = new JMenuItem("Tangenten");
	public static JMenuItem menuitemTangentenV = new JMenuItem("Tangenten Vorher");
	public static JMenuItem menuitemTangentenN = new JMenuItem("Tangenten Nachher");
	public static JMenuItem menuitemTangentenEntfernen = new JMenuItem("Tangenten Entfernen");

	public static JPopupMenu menuTangente = new JPopupMenu();
	public static JMenuItem menuitemTangentenEntfernenTangente = new JMenuItem("Tangenten Entfernen");
	public static JMenuItem menuitemSyncron = new JMenuItem("Syncron zu Punkt");
	public static JMenuItem menuitemDeSyncron = new JMenuItem("Nicht Syncron");
	public static JMenuItem menuitemGemeinsameTangenten = new JMenuItem("Gemeinsame Tangenten (einfach)");
	public static JMenuItem menuitemQuadratisch = new JMenuItem("Gemeinsame Tangenten (quadratisch)");
	public static JMenuItem menuitemGetennt = new JMenuItem("Getrennte Tangenten");
	public static JMenuItem menuitemZuLine = new JMenuItem("Zu Linie");

	public static JPopupMenu menuLineMitte = new JPopupMenu();
	public static JMenuItem menuitemTangentenMitte = new JMenuItem("zu Kurve");
	public static JMenuItem menuitemLinieMitte = new JMenuItem("zu Linie");
	public static JMenuItem menuitemGemeinsameLineMitte = new JMenuItem("Gemeinsame Tangenten (einfach)");
	public static JMenuItem menuitemQuadratischMitte = new JMenuItem("Gemeinsame Tangenten (quadratisch)");
	public static JMenuItem menuitemEllypse_3P = new JMenuItem("3 Punkte Ellypse");
	public static JMenuItem menuitemBogen_3P = new JMenuItem("3 Punkte Bogen");
	public static JMenuItem menuitemEllypse = new JMenuItem("Freie Ellypse");
	public static JMenuItem menuitemHorizontal = new JMenuItem("Erzwinge Horizontal");
	public static JMenuItem menuitemVertical = new JMenuItem("Erzwinge Vertical");
	public static JMenuItem menuitemNichtOrthogonal = new JMenuItem("Nicht Orthogonal");
	public static JMenuItem menuitemSprung = new JMenuItem("keine Linie / brechen");
	public static JMenuItem menuitemSchließen = new JMenuItem("keine Linie / brechen und schließen");
	// public static JMenuItem menuitemLöscheBruchstück = new JMenuItem("Lösche
	// Bruchstück");
	public static CPath currendPath;
	public static Tangente currendTangetne;
	public static PolygonPoint currendPolygonPoint;
	public static AddPoint currendAddPoint;

	static {
		/** menuPoint */
		// menuitemEntfernen "Entfernen");
		menuPoint.add(menuitemEntfernen);
		menuitemEntfernen.addMouseListener(PopupShowOnHover.That);
		menuitemEntfernen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPath.removePoint(currendPath.selectedPoint.getValue());
				menuPoint.setVisible(false);
			}
		});
		// menuitemTangenten "Tangenten");
		menuPoint.add(menuitemTangenten);
		menuitemTangenten.addMouseListener(PopupShowOnHover.That);
		menuitemTangenten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toTangente();
				currendPolygonPoint.getNextPoint().toTangente();
				menuPoint.setVisible(false);
			}
		});
		// menuitemTangentenV "Tangenten Vorher");
		menuPoint.add(menuitemTangentenV);
		menuitemTangentenV.addMouseListener(PopupShowOnHover.That);
		menuitemTangentenV.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toTangente();
				menuPoint.setVisible(false);
			}
		});
		// menuitemTangentenN "Tangenten Nachher");
		menuPoint.add(menuitemTangentenN);
		menuitemTangentenN.addMouseListener(PopupShowOnHover.That);
		menuitemTangentenN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.getNextPoint().toTangente();
				menuPoint.setVisible(false);
			}
		});
		// menuitemTangentenEntfernen "Tangenten Entfernen");
		menuPoint.add(menuitemTangentenEntfernen);
		menuitemTangentenEntfernen.addMouseListener(PopupShowOnHover.That);
		menuitemTangentenEntfernen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.getNextPoint().toLinie();
				currendPolygonPoint.toLinie();
				menuPoint.setVisible(false);
			}
		});

		/** menuTangente */
		// menuitemTangentenEntfernenTangente = new JMenuItem("Tangenten
		// Entfernen");
		menuTangente.add(menuitemTangentenEntfernenTangente);
		menuitemTangentenEntfernenTangente.addMouseListener(PopupShowOnHover.That);
		menuitemTangentenEntfernenTangente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currendTangetne.nachher) {
					currendTangetne.point.getNextPoint().toLinie();
				} else {
					currendTangetne.point.toLinie();
				}
				menuTangente.setVisible(false);
			}
		});
		// menuitemSyncron = new JMenuItem("Syncron zu Punkt");
		menuTangente.add(menuitemSyncron);
		menuitemSyncron.addMouseListener(PopupShowOnHover.That);
		menuitemSyncron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendTangetne.point.syncroniced = true;
				currendTangetne.setLocation(currendTangetne.getLocation());
				menuTangente.setVisible(false);
			}
		});
		// menuitemDeSyncron = new JMenuItem("Syncron zu Punkt");
		menuTangente.add(menuitemDeSyncron);
		menuitemDeSyncron.addMouseListener(PopupShowOnHover.That);
		menuitemDeSyncron.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendTangetne.point.syncroniced = false;
				menuTangente.setVisible(false);
			}
		});
		// menuitemGemeinsameLine
		menuTangente.add(menuitemGemeinsameTangenten);
		menuitemGemeinsameTangenten.addMouseListener(PopupShowOnHover.That);
		menuitemGemeinsameTangenten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currendTangetne.nachher) {
					currendTangetne.point.toCombindendCurve();
				} else {
					currendTangetne.point.getNextPoint().toCombindendCurve();
				}
				menuTangente.setVisible(false);
			}
		});
		// menuitemQuadratisch
		menuTangente.add(menuitemQuadratisch);
		menuitemQuadratisch.addMouseListener(PopupShowOnHover.That);
		menuitemQuadratisch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currendTangetne.nachher) {
					currendTangetne.point.toCombindendQuad();
				} else {
					currendTangetne.point.getNextPoint().toCombindendQuad();
				}
				menuTangente.setVisible(false);
			}
		});
		// menuitemGetennt
		menuTangente.add(menuitemGetennt);
		menuitemGetennt.addMouseListener(PopupShowOnHover.That);
		menuitemGetennt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currendTangetne.nachher) {
					currendTangetne.point.toTangente();
				} else {
					currendTangetne.point.getNextPoint().toTangente();
				}
				GreifTool.item = currendTangetne;
				menuTangente.setVisible(false);
			}
		});
		// menuitemZuLine = new JMenuItem("Zu Linie");
		menuTangente.add(menuitemZuLine);
		menuitemZuLine.addMouseListener(PopupShowOnHover.That);
		menuitemZuLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currendTangetne.nachher) {
					currendTangetne.point.toLinie();
				} else {
					currendTangetne.point.getNextPoint().toLinie();
				}
				menuTangente.setVisible(false);
			}
		});

		/** menuLineMitte */
		// public static JPopupMenu menuLineMitte = new JPopupMenu();

		// menuitemLinieMitte = new JMenuItem("zu Linie");
		menuLineMitte.add(menuitemLinieMitte);
		menuitemLinieMitte.addMouseListener(PopupShowOnHover.That);
		menuitemLinieMitte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toLinie();
				menuLineMitte.setVisible(false);
			}

		
		});
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>("Linie",menuitemLinieMitte,false));

		// menuitemTangentenMitte = new JMenuItem("zu Kurve");
		menuLineMitte.add(menuitemTangentenMitte);
		menuitemTangentenMitte.addMouseListener(PopupShowOnHover.That);
		menuitemTangentenMitte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toTangente();
				menuLineMitte.setVisible(false);
			}
			
		});
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>("Kurve (syncrone Tangenten) ",menuitemTangentenMitte,true));
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>("Kurve (mit Knick)",menuitemTangentenMitte,false));

		// menuitemGemeinsameLineMitte gemeinsam tangente einfach
		menuLineMitte.add(menuitemGemeinsameLineMitte);
		menuitemGemeinsameLineMitte.addMouseListener(PopupShowOnHover.That);
		menuitemGemeinsameLineMitte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toCombindendCurve();
				menuLineMitte.setVisible(false);
			}
			
		});
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>("smooth Verbunden (einfach)",menuitemGemeinsameLineMitte,true));
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>("smooth Verbunden (quadratisch)",menuitemQuadratischMitte,true));
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>(menuitemGemeinsameLineMitte.getText(),menuitemGemeinsameLineMitte,false));

		// menuitemQuadratischMitte = new JMenuItem("Quadratisch");
		menuLineMitte.add(menuitemQuadratischMitte);
		menuitemQuadratischMitte.addMouseListener(PopupShowOnHover.That);
		menuitemQuadratischMitte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toCombindendQuad();
				menuLineMitte.setVisible(false);
			}
			@Override
			public String toString() {
				return "Spline (quadatisch)";
			}
		});
		possibleExpensionModes.add(new ObjectContainerForStringTyping<JMenuItem,Boolean>(menuitemQuadratischMitte.getText(),menuitemQuadratischMitte,false));

		// menuitemEllypse_3P
		menuLineMitte.add(menuitemEllypse_3P);
		menuitemEllypse_3P.addMouseListener(PopupShowOnHover.That);
		menuitemEllypse_3P.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toEllypse3Punkte();
				menuLineMitte.setVisible(false);
			}
		});
		// menuitemEllypse_3P
		menuLineMitte.add(menuitemBogen_3P);
		menuitemBogen_3P.addMouseListener(PopupShowOnHover.That);
		menuitemBogen_3P.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toKurve3Punkte();
				menuLineMitte.setVisible(false);
			}
		});

		// menuitemEllypse
		menuLineMitte.add(menuitemEllypse);
		menuitemEllypse.addMouseListener(PopupShowOnHover.That);
		menuitemEllypse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toEllypse();
				menuLineMitte.setVisible(false);
			}
		});
		// menuitemHorizontal = new JMenuItem("Erzwinge Horizontal");
		menuLineMitte.add(menuitemHorizontal);
		menuitemHorizontal.addMouseListener(PopupShowOnHover.That);
		menuitemHorizontal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toForceHorizontal();
				menuLineMitte.setVisible(false);
			}
		});
		// menuitemVertical = new JMenuItem("Erzwinge Vertical");
		menuLineMitte.add(menuitemVertical);
		menuitemVertical.addMouseListener(PopupShowOnHover.That);
		menuitemVertical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toForceVertical();
				menuLineMitte.setVisible(false);
			}
		});

		// menuitemNichtOrthogonal = new JMenuItem("Nicht Orthogonal");
		menuLineMitte.add(menuitemNichtOrthogonal);
		menuitemNichtOrthogonal.addMouseListener(PopupShowOnHover.That);
		menuitemNichtOrthogonal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toForceNone();
				menuLineMitte.setVisible(false);
			}
		});
		// menuitemSprung = new JMenuItem("keine Linie");
		menuLineMitte.add(menuitemSprung);
		menuitemSprung.addMouseListener(PopupShowOnHover.That);
		menuitemSprung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toSkip();
				menuLineMitte.setVisible(false);
			}
		});

		// menuitemSchließen = new JMenuItem("Schließen mit selben Bruchstück");
		menuLineMitte.add(menuitemSchließen);
		menuitemSchließen.addMouseListener(PopupShowOnHover.That);
		menuitemSchließen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currendPolygonPoint.toCloseAndJump();
				menuLineMitte.setVisible(false);
			}
		});
		// menuitemLöscheBruchstück = new JMenuItem("Lösche Bruchstück");
		// TODO

	}

	public CPath(DPoint alt, DPoint p, boolean close, Matrix m) {
		init(alt.x, alt.y);
		addPoint(0, 0, 0);
		setName("Polylinie", false);
		addPoint(p.x - getX(), p.y - getY());
		setClose(close);
		init();
		griffe.add(verschiebeGriff);
//		if (copyTEst) {
//			copyTEst = false;
//			derClone = new CPath(p1, p2, close, m);
//			m.addCObject(derClone);
//		}
	}

	public CPath(int x, int y, String polygonList, boolean close) {
		init(x, y);
		String[] s = null;
		if (polygonList.contains(";")) {
			s = polygonList.replace(",", ";").split(";");
		} else {
			s = polygonList.replace(",", " ").split(" ");
		}
		for (int i = 0; i < s.length; i += 2) {
			int x1 = 0;
			int y1 = 0;
			try {
				x1 = Integer.parseInt(s[i]);
				y1 = Integer.parseInt(s[i + 1]);
			} catch (Exception e) {
				// y1 == null --> 0
			}
			addPoint(x1, y1);
		}
		setClose(close);
		init();
	}

//	static boolean copyTEst = true;
//	CPath derClone;

	public CPath(String list, Matrix m) {
		setMatrix(m);
		SVGtoData(list);
		double x = polygon.get(0).getX();
		double y = polygon.get(0).getY();
		for (PrivatePoint p : polygon) {
			p.x1 -= x;
			p.y1 -= y;
		}
		init(x, y);

		init();
	}

	public CPath(int x1, int y1, int x2, int y2, Matrix m) {
		this(new DPoint(x1, y1), new DPoint(x2, y2), true, m);
	}

	public CPath(CRect c) {
		double i = c.getX();
		double j = c.getY();
		addPoint(i, j);
		i = c.getX() + c.getWidth();
		j = c.getY();
		addPoint(i, j);
		i = c.getX() + c.getWidth();
		j = c.getY() + c.getHeight();
		addPoint(i, j);
		i = c.getX();
		j = c.getY() + c.getHeight();
		addPoint(i, j);
		init(c.getX(), c.getY());
		setClose(true);
		init();
	}

	public CPath(CKreis kreis) {

		double x = kreis.getX() + kreis.getRadius();
		double y = kreis.getY();
		addPoint(x, y);
		float x1 = (float) (kreis.getX() + kreis.getRadius() / Math.sqrt(2));
		float y1 = (float) (kreis.getY() - kreis.getRadius() / Math.sqrt(2));
		double x2 = kreis.getX();
		double y2 = kreis.getY() - kreis.getRadius();
		PolygonPoint p = addPoint(x2, y2);
		p.toKurve3Punkte();
		p.tangeteVor.setLocation(x1, y1);

		x1 = (float) (kreis.getX() - kreis.getRadius() / Math.sqrt(2));
		y1 = (float) (kreis.getY() + kreis.getRadius() / Math.sqrt(2));
		x2 = kreis.getX() + kreis.getRadius();
		y2 = kreis.getY();
		p = addPoint(x2, y2);
		p.toKurve3Punkte();
		p.tangeteVor.setLocation(x1, y1);
		init(x, y);
		setClose(true);
		init();
	}

	private void init() {

		selectedPoint = new NumberLine<Integer>("Aktueller Punkt", 0, 0, 1, 1) {
			@Override
			public void valueChanges(Integer value) {
				setSelectedPoint(value);
			}
		};
		addLine(selectedPoint);
		addLine(new OneLine<Double>("X", null) {
			@Override
			public Double getValue() {
				return getCurrendPointX();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointX(value);
			}
		});
		addLine(new OneLine<Double>("Y", null) {
			@Override
			public Double getValue() {
				return getCurrendPointY();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointY(value);
			}
		});
		addLine(new OneLine<Double>("X Koordinate", null) {
			@Override
			public Double getValue() {
				return getCurrendPointXKordinate();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointXKordinate(value);
			}
		});
		addLine(new OneLine<Double>("Y Koordinate", null) {
			@Override
			public Double getValue() {
				return getCurrendPointYKordinate();
			}

			@Override
			public void valueChanges(Double value) {
				setCurrendPointYKordinate(value);
			}
		});
		expentionMode = new ListLine<ObjectContainerForStringTyping<JMenuItem,Boolean>>("Expensionmodus", possibleExpensionModes.get(0), possibleExpensionModes) {
			ObjectContainerForStringTyping<JMenuItem,Boolean> value;
			
			@Override
			public void valueChanges(ObjectContainerForStringTyping<JMenuItem,Boolean> value) {
				currendExpensionMode = value.getObject();
				currendExpensionModeSyncronise = value.getObject2();
				this.value = value;
			}
			@Override
			public ObjectContainerForStringTyping<JMenuItem, Boolean> getValue() {
				if(value == null){
					return possibleExpensionModes.get(0); 
				}
				return value;
			}
		};
		addLine(expentionMode);

		addLine(evenOdd);
		addLine(close);

	}

	public PolygonPoint addPoint(double x, double y) {
		return addPoint(x, y, polygon.size());
	}

	public PolygonPoint addPoint(double x, double y, int id) {
		PrivatePoint point = new PrivatePoint(Matrix.rastern(x), Matrix.rastern(y));
		polygon.add(id, point);
		PolygonPoint p = new PolygonPoint(id);
		ppunkte.add(id, p);
		getGreifItems().add(p);
		
		int cont = 0;
		for (PolygonPoint pp : ppunkte) {
			pp.id = cont++;
		}
		currendPolygonPoint = p;
		if(currendExpensionMode != null){
			p.syncroniced = currendExpensionModeSyncronise;
			currendExpensionMode.doClick();
		}
		return p;
	}

	public void removePoint() {
		removePoint(polygon.size() - 1);
		setSelectedPoint(0);
	}

	public void removePoint(int id) {
		polygon.remove(id);
		PolygonPoint p = ppunkte.get(id);
		getGreifItems().remove(p);
		getGreifItems().remove(p.tangeteNach);
		getGreifItems().remove(p.tangeteVor);
		getGreifItems().remove(p.addpoint);
		ppunkte.remove(id);
		int cont = 0;
		for (PolygonPoint pp : ppunkte) {
			pp.id = cont++;
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getXMLName() {
		if (polygon.size() == 2) {
			return XMLCObject.line;
		} else if (isClose()) {
			return XMLCObject.path;
		} else {
			return XMLCObject.path;
		}
	}

	GeneralPath path = new GeneralPath();
	private boolean isInit = false;
	private boolean oneShape = false;
	int fragmentCont = 0;

	@Override
	public void update(Matrix m, int timePassed, int conter) {
		selectedPoint.setMaximum(polygon.size() - 1);
		path.reset();
		if (isEvenodd()) {
			path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
		} else {
			path.setWindingRule(GeneralPath.WIND_NON_ZERO);
		}
		PolygonPoint vor = ppunkte.get(ppunkte.size() - 1);
		ppunkte.get(0).skip = false;
		isInit = false;
		int i = -1;
		fragmentCont = 0;
		for (PolygonPoint p : ppunkte) {

			if (i == -1) {
				if (p.skip) {
					i = p.id;
				} else {

					vor = p;
					continue;
				}
			}
			singleLineDraw(path, p, vor);
			vor = p;
		}
		oneShape = i == -1;
		if (i == -1) {
			path.moveTo(ppunkte.get(ppunkte.size() - 1).getX(), ppunkte.get(ppunkte.size() - 1).getY());
		}

		for (PolygonPoint p : ppunkte) {
			if (i == p.id) {
				vor = p;
				break;
			}
			singleLineDraw(path, p, vor);
			vor = p;
		}
		if (isClose()) {
			// PolygonPoint target = ppunkte.get(0);
			//// for(PolygonPoint p : ppunkte){
			//// if(p.id == 0)continue;
			//// if(p.skip){
			//// target = p;
			//// break;
			//// }
			//// }
			//// if(target == null)target = ppunkte.get(0);
			// target.skip = false;
			//
			// singleLineDraw(path,target,ppunkte.get(ppunkte.size()-1));
			//// path.lineTo(target.getX(), target.getY());

			path.closePath();
			// target.skip = true;
		}
		updateFragmentCont();
//		if (derClone != null) {
//			derClone.polygon.clear();
//			derClone.ppunkte.clear();
//			derClone.getGreifItems().clear();
//			derClone.SVGtoData(getSVGText());
//			derClone.verschiebeGriff.setLocation(0, 0);
//
//		}
	}

	boolean debugg = true;

	private void singleLineDraw(GeneralPath path, PolygonPoint p, PolygonPoint vor) {
		if (p.skip) {
			fragmentCont += 1;
			if (p.close && isInit) {
				path.closePath();
			}
			path.moveTo(p.getX(), p.getY());
			isInit = true;
		} else if (p.linie) {
			path.lineTo(p.getX(), p.getY());
		} else if (p.verbunden) {
			if (p.quad) {
				path.quadTo(p.tangeteVor.getX(), p.tangeteVor.getY(), p.getX(), p.getY());
			} else {
				path.curveTo(vor.tangeteNach.getX(), vor.tangeteNach.getY(), p.tangeteVor.getX(), p.tangeteVor.getY(),
						p.getX(), p.getY());
			}
		} else if (p.ellypse) {

			if (p.ellypse_3Points) {

				if (p.bogen) {
					add3PunkteBogen(vor, p);
				} else {
					addArc3PTo(vor, p);
				}

			} else {
				addArcTo(vor, p);
			}

		} else {
			path.curveTo(vor.tangeteNach.getX(), vor.tangeteNach.getY(), p.tangeteVor.getX(), p.tangeteVor.getY(),
					p.getX(), p.getY());

		}
	}

	private void addArcTo(PolygonPoint vor, PolygonPoint target) {
		final double x0 = vor.getX();
		final double y0 = vor.getY();

		// Determine target "to" position
		final double xto = target.getX();
		final double yto = target.getY();
		// Compute the half distance between the current and the final point
		final double dx2 = (x0 - xto) / 2.0;
		final double dy2 = (y0 - yto) / 2.0;
		// Crate Rotation from Tangente
		final double tx = target.tangeteVor.x - dx2;
		final double ty = target.tangeteVor.y - dy2;
		// Compute the half distance between the current and the final point
		final double tanVorX = vor.tangeteNach.x;
		final double tanVorY = vor.tangeteNach.y;

		/***/
		final int radiusX = (int) Math.abs(tanVorX);
		/***/
		final int radiusY = (int) Math.abs(tanVorY);

		// von links nach rechts? // Tangenten punkt über Gerade der beiden
		// punkte

		/***/
		boolean localSweepFlag = x0 < xto;
		if (yto + ((y0 - yto) / (x0 - xto)) * (target.tangeteVor.x) < target.tangeteVor.getY())
			localSweepFlag = !localSweepFlag;

		/***/
		// final boolean localLargeArcFlag = tx * tx + ty * ty > (radiusX *
		// radiusX + radiusY * radiusY);
		final boolean localLargeArcFlag = vor.LargeArcFlag;

		// Convert angle from degrees to radians
		/***/
		final double xAxisRotationR = Math.atan(ty / tx);
		addArcTo(x0, y0, xto, yto, dx2, dy2, radiusX, radiusY, localSweepFlag, localLargeArcFlag, xAxisRotationR);
	}

	private void addArc3PTo(PolygonPoint vor, PolygonPoint target) {
		final double x0 = vor.getX();
		final double y0 = vor.getY();

		// Determine target "to" position
		final double xto = target.getX();
		final double yto = target.getY();
		// Compute the half distance between the current and the final point
		final double dx2 = (x0 - xto) / 2.0;
		final double dy2 = (y0 - yto) / 2.0;
		// Crate Rotation from Tangente
		final double tx = target.tangeteVor.x - dx2;
		final double ty = target.tangeteVor.y - dy2;
		// Compute the half distance between the current and the final point

		// von links nach rechts? // Tangenten punkt über Gerade der beiden
		// punkte
		/***/
		boolean localSweepFlag = x0 < xto;
		if (yto + ((y0 - yto) / (x0 - xto)) * (target.tangeteVor.x) < target.tangeteVor.getY())
			localSweepFlag = !localSweepFlag;

		/***/
		final boolean localLargeArcFlag = false;// tx*tx+ty*ty >
												// (radiusX*radiusX +
												// radiusY*radiusY);

		// Convert angle from degrees to radians
		// c*sin(al) = a
		/***/
		final double xAxisRotationR = Math.atan(ty / tx);
		/***/
		double radiusX = (Math.sqrt(tx * tx + ty * ty));
		/***/
		double radiusY = (Math.sqrt(dx2 * dx2 + dy2 * dy2) * Math.sin(Math.atan(dy2 / dx2) - xAxisRotationR));

		final double cosAngle = Math.cos(xAxisRotationR);
		final double sinAngle = Math.sin(xAxisRotationR);
		// double altern =
		// (Math.sqrt(dx2*dx2+dy2*dy2)*Math.cos(Math.atan(dy2/dx2)-xAxisRotationR));
		// if(radiusX< altern){
		// radiusX = altern;
		// }

		//
		// Step 1 : Compute (x1, y1)
		//
		final double x1 = (cosAngle * dx2 + sinAngle * dy2);
		final double y1 = (-sinAngle * dx2 + cosAngle * dy2);
		// Ensure radii are large enough
		final double rx = Math.abs(radiusX);
		final double ry = Math.abs(radiusY);
		// check that radii are large enough
		final double radiiCheck = (x1 * x1) / (rx * rx) + (y1 * y1) / (ry * ry);
		if (radiiCheck > 1.0) {
			radiusY = Math.sqrt(radiiCheck) * ry;
			if (rx == rx && ry == ry) {
				/* not NANs */} else {
				path.lineTo((float) xto, (float) yto);
				return;
			}
		}

		vor.tangeteNach.x = radiusX;
		vor.tangeteNach.y = radiusY;
		addArcTo(x0, y0, xto, yto, dx2, dy2, radiusX, radiusY, localSweepFlag, localLargeArcFlag, xAxisRotationR);
	}

	// Quelle:
	// http://grepcode.com/file/repo1.maven.org/maven2/net.java.openjfx.backport/openjfx-78-backport/1.8.0-ea-b96.1/javafx/scene/shape/ArcTo.java#ArcTo.0xAxisRotation;
	private void addArcTo(final double x0, final double y0, final double xto, final double yto, final double dx2,
			final double dy2, final double radiusX, final double radiusY, final boolean localSweepFlag,
			final boolean localLargeArcFlag, final double xAxisRotationR) {

		final double cosAngle = Math.cos(xAxisRotationR);
		final double sinAngle = Math.sin(xAxisRotationR);
		//
		// Step 1 : Compute (x1, y1)
		//
		final double x1 = (cosAngle * dx2 + sinAngle * dy2);
		final double y1 = (-sinAngle * dx2 + cosAngle * dy2);
		// Ensure radii are large enough
		double rx = Math.abs(radiusX);
		double ry = Math.abs(radiusY);
		double Prx = rx * rx;
		double Pry = ry * ry;
		final double Px1 = x1 * x1;
		final double Py1 = y1 * y1;
		// check that radii are large enough
		final double radiiCheck = Px1 / Prx + Py1 / Pry;
		if (radiiCheck > 1.0) {
			rx = Math.sqrt(radiiCheck) * rx;
			ry = Math.sqrt(radiiCheck) * ry;
			if (rx == rx && ry == ry) {
				/* not NANs */} else {
				path.lineTo((float) xto, (float) yto);
				return;
			}
			Prx = rx * rx;
			Pry = ry * ry;
		}

		//
		// Step 2 : Compute (cx1, cy1)
		//
		double sign = ((localLargeArcFlag == localSweepFlag) ? -1.0 : 1.0);
		double sq = ((Prx * Pry) - (Prx * Py1) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1));
		sq = (sq < 0.0) ? 0.0 : sq;
		final double coef = (sign * Math.sqrt(sq));
		final double cx1 = coef * ((rx * y1) / ry);
		final double cy1 = coef * -((ry * x1) / rx);

		//
		// Step 3 : Compute (cx, cy) from (cx1, cy1)
		//
		final double sx2 = (x0 + xto) / 2.0;
		final double sy2 = (y0 + yto) / 2.0;
		final double cx = sx2 + (cosAngle * cx1 - sinAngle * cy1);
		final double cy = sy2 + (sinAngle * cx1 + cosAngle * cy1);

		//
		// Step 4 : Compute the angleStart (angle1) and the angleExtent (dangle)
		//
		final double ux = (x1 - cx1) / rx;
		final double uy = (y1 - cy1) / ry;
		final double vx = (-x1 - cx1) / rx;
		final double vy = (-y1 - cy1) / ry;
		// Compute the angle start
		double n = Math.sqrt((ux * ux) + (uy * uy));
		double p = ux; // (1 * ux) + (0 * uy)
		sign = ((uy < 0.0) ? -1.0 : 1.0);
		double angleStart = Math.toDegrees(sign * Math.acos(p / n));

		// Compute the angle extent
		n = Math.sqrt((ux * ux + uy * uy) * (vx * vx + vy * vy));
		p = ux * vx + uy * vy;
		sign = ((ux * vy - uy * vx < 0.0) ? -1.0 : 1.0);
		double angleExtent = Math.toDegrees(sign * Math.acos(p / n));
		if (!localSweepFlag && (angleExtent > 0)) {
			angleExtent -= 360.0;
		} else if (localSweepFlag && (angleExtent < 0)) {
			angleExtent += 360.0;
		}
		angleExtent = angleExtent % 360;
		angleStart = angleStart % 360;

		//
		// We can now build the resulting Arc2D
		//
		Arc2D.Double arc = new Arc2D.Double();
		arc.x = -rx;
		arc.y = -ry;
		arc.width = rx * 2.0;
		arc.height = ry * 2.0;
		arc.start = -angleStart;
		arc.extent = -angleExtent;
		AffineTransform rtt = new AffineTransform();
		rtt.rotate(xAxisRotationR);
		// rtt.translate(target.getX(),target.getY());

		PathIterator pi = arc.getPathIterator(rtt);
		AffineTransform move = new AffineTransform();
		move.translate(-cx, -cy);
		path.transform(move);
		path.append(pi, true);
		move.translate(2 * cx, 2 * cy);
		path.transform(move);

	}

	public void add3PunkteBogen(PolygonPoint vor, PolygonPoint target) {
		// Punkt A
		final double x0 = vor.getX();
		final double y0 = vor.getY();

		// Punkt C
		final double xto = target.getX();
		final double yto = target.getY();

		// Punkt B
		final double xB = target.tangeteVor.getX();
		final double yB = target.tangeteVor.getY();

		double MittelPunktAB_X = (x0 + xB) / 2;
		double MittelPunktAB_Y = (y0 + yB) / 2;
		double SteigungAB = -(xB - x0) / (yB - y0);
		/**
		 * y = SteigungAB(x-MittelPunktAB_X)+MittelPunktAB_Y
		 */
		double MittelPunktAC_X = (x0 + xto) / 2;
		double MittelPunktAC_Y = (y0 + yto) / 2;
		double SteigungAC = -(xto - x0) / (yto - y0);

		/**
		 * y = SteigungAC(x-MittelPunktAC_X)+MittelPunktAC_Y
		 * 
		 * SteigungAB*x-SteigungAC*x =
		 * MittelPunktAC_Y-MittelPunktAB_Y+SteigungAB *
		 * MittelPunktAB_X-SteigungAC*MittelPunktAC_X
		 */
		/**
		 * Gleiche Rechnug nur mit Vecoren statt geraden. Kein
		 * Genauigkeitsgewinn // double r_vec_x = (y0 - yB); // double r_vec_y =
		 * (xB - x0); // double r_vec_x2 = (y0 - yto); // double r_vec_y2 = (xto
		 * - x0); // double x1 = MittelPunktAB_X; // double x3 =
		 * MittelPunktAB_Y; // double x2 = r_vec_x; // double x4 = r_vec_y; //
		 * double y1 = MittelPunktAC_X; // double y3 = MittelPunktAC_Y; //
		 * double y2 = r_vec_x2; // double y4 = r_vec_y2; // double lamda =
		 * ((x4/x2)*(x1-y1) + y3-x3) / ((x4/x2)*y2 - y4); // double x = y1 +
		 * lamda * y2; // double y = y3 + lamda * y4;
		 * 
		 */

		double x = (MittelPunktAC_Y - MittelPunktAB_Y + SteigungAB * MittelPunktAB_X - SteigungAC * MittelPunktAC_X)
				/ (SteigungAB - SteigungAC);
		double y = SteigungAB * (x - MittelPunktAB_X) + MittelPunktAB_Y;

		double pytX = x - x0;
		double pytY = y - y0;
		double radius = Math.sqrt(pytX * pytX + pytY * pytY);

		double winkel1 = Math.toDegrees(Math.acos((x0 - x) / radius));
		if (y < y0) {
			winkel1 = 360 - winkel1;
		}
		double winkel2 = Math.toDegrees(Math.acos((xto - x) / radius));
		if (y < yto) {
			winkel2 = 360 - winkel2;
		}
		double winkel3 = Math.toDegrees(Math.acos((xB - x) / radius));
		if (y < yB) {
			winkel3 = 360 - winkel3;
		}
		// System.out.println("Radius: " + radius + "\tMitte: " + x + "," +
		// y+"\t Winkel:" + winkel1 + "\t" + winkel2);
		if (Double.isNaN(winkel1) || Double.isNaN(winkel2) || Double.isInfinite(radius)) {
			path.lineTo(xto, yto);
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
		/**
		 * Mittelpunkt: Schnittpunkt der Senkrechen Seiten halbierenden A-B = MA
		 * B-C = MB M Radius: pythagoras aus Punkt X und Mittelpunkt.
		 */
		Arc2D.Double arc = new Arc2D.Double();
		arc.x = x - radius;
		arc.y = y - radius;
		arc.width = 2 * radius;
		arc.height = 2 * radius;
		arc.start = winkel1;

		double winkel = winkel2 - winkel1;
		if (gegenUrzeiger && winkel < 0) {
			winkel += 360;
		}
		if (!gegenUrzeiger && winkel > 0) {
			winkel -= 360;
		}
		arc.extent = winkel;
		AffineTransform rtt = new AffineTransform();
		PathIterator pi = arc.getPathIterator(rtt);
		AffineTransform move = new AffineTransform();
		// move.translate(-cx,-cy);
		path.transform(move);
		path.append(pi, true);
		// move.translate(2*cx,2*cy);
		path.transform(move);

	}

	private void updateFragmentCont() {
		if (fragmentCont == fragments.size()) {
			return;
		}
		if (fragmentCont == 0 || fragmentCont == 1) {
			for (VerschiebeFragmentGriff griff : fragments) {
				getGreifItems().remove(griff);
			}
			fragments.clear();
			return;
		}
		if (fragmentCont > fragments.size()) {
			while (true) {
				VerschiebeFragmentGriff griff = new VerschiebeFragmentGriff(fragments.size() + 1);
				getGreifItems().add(griff);
				fragments.add(griff);
				if (fragments.size() >= fragmentCont)
					return;
			}
		}

		// if(fragmentCont < fragments.size()){
		while (true) {
			VerschiebeFragmentGriff griff = fragments.get(fragments.size() - 1);
			getGreifItems().remove(griff);
			fragments.remove(griff);
			if (fragments.size() <= fragmentCont)
				return;
		}

	}

	@Override
	public Shape getShape() {
		return path;
	}

	public int getSelectedPoint() {
		int i = selectedPoint.getValue();
		if (i == -1 || i == polygon.size()) {
			return 0;
		}
		return selectedPoint.getValue();
	}

	public void setSelectedPoint(int selectedPoint) {
		this.selectedPoint.setValue(selectedPoint);
	}

	public double getCurrendPointX() {
		return polygon.get(getSelectedPoint()).getX();
	}

	public double getCurrendPointY() {
		return polygon.get(getSelectedPoint()).getY();
	}

	public void setCurrendPointX(double value) {
		polygon.get(getSelectedPoint()).setX(value);
	}

	public void setCurrendPointY(double y) {
		polygon.get(getSelectedPoint()).setY(y);
	}

	public double getCurrendPointXKordinate() {
		return polygon.get(getSelectedPoint()).getX() + getX();
	}

	public double getCurrendPointYKordinate() {
		return polygon.get(getSelectedPoint()).getY() + getY();
	}

	public void setCurrendPointXKordinate(double x) {
		polygon.get(getSelectedPoint()).setX(x - getX());
	}

	public void setCurrendPointYKordinate(double y) {
		polygon.get(getSelectedPoint()).setY(y - getY());
	}

	private DPoint calc = new DPoint();
	public boolean fixTangenteToLocation;

	@Override
	public double getNearestPointFang(DPoint Overwrite, double distance, double x, double y, GreifItem withOut) {
		// public static boolean PUNKTFANG_ECKE = true;
		int Xid = -1;
		if (withOut instanceof PolygonPoint) {
			Xid = withOut.getID();
		}
		if (GreifTool.PUNKTFANG_ECKE) {
			int i = 0;
			for (PrivatePoint p : polygon) {
				i += 1;
				if (Xid == i - 1) {
					continue;
				}
				calc.setLocation(p.getX() + getX(), p.getY() + getY());
				distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance, x, y, calc);
			}
		}
		// public static boolean PUNKTFANG_MITTE = true;
		if (GreifTool.PUNKTFANG_MITTE) {
			for (int id = 0; id != polygon.size(); id += 1) {
				calc.setLocation(CPath.this.getX()
						+ (polygon.get(id).getX() + polygon.get(id == 0 ? polygon.size() - 1 : id - 1).getX()) / 2,
						CPath.this.getY()
								+ (polygon.get(id).getY() + polygon.get(id == 0 ? polygon.size() - 1 : id - 1).getY())
										/ 2);
				distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance, x, y, calc);
			}

		}
		// public static boolean PUNKTFANG_ZENTRUM = false;
		if (GreifTool.PUNKTFANG_ZENTRUM) {
			int mx = 0, my = 0;
			for (PrivatePoint p : polygon) {
				mx += p.getX();
				my += p.getY();
			}
			mx /= polygon.size();
			my /= polygon.size();
			calc.setLocation(mx, my);
			distance = GreifTool.getDistanceAndOverWirte(Overwrite, distance, x, y, calc);
		}
		return distance;
	}

	/** Interne Classen */
	public class PolygonPoint extends GreifItem {
		int id;
		Tangente tangeteNach = new Tangente(this);
		Tangente tangeteVor = new Tangente(this);
		AddPoint addpoint = new AddPoint(this);
		boolean skip = false;
		boolean close = false;
		boolean linie = true;
		boolean verbunden = false;
		boolean quad = false;
		boolean ellypse = false;
		boolean ellypse_3Points = false;
		boolean bogen = false;
		boolean syncroniced = false;
		boolean LargeArcFlag = true;

		// 0 = kein Zwang | 2 = Horizontal und | 1 = Vertical
		int OrthogonalZwang = 0;

		public PolygonPoint(int id) {
			this.id = id;
			getGreifItems().add(addpoint);
			getGreifItems().add(tangeteNach);
			getGreifItems().add(tangeteVor);
			tangeteVor.other = tangeteNach;
			tangeteNach.other = tangeteVor;
			tangeteNach.setToDefaultPossision(true);
			tangeteVor.setToDefaultPossision(false);

		}

		public void toForceVertical() {
			OrthogonalZwang = 1;
		}

		public void toForceHorizontal() {
			OrthogonalZwang = 2;
		}

		public void toForceNone() {
			OrthogonalZwang = 0;
		}

		public void toLinie() {
			checkTangenten();
			skip = false;
			linie = true;
			verbunden = false;
			quad = false;
			ellypse = false;
			ellypse_3Points = false;
			// syncroniced = false;
			bogen = false;
			OrthogonalZwang = 0;
		}

		public void toTangente() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = false;
			quad = false;
			ellypse = false;
			ellypse_3Points = false;
			bogen = false;
			OrthogonalZwang = 0;
		}

		public void toCombindendCurve() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = true;
			quad = false;
			ellypse = false;
			ellypse_3Points = false;
			bogen = false;
			OrthogonalZwang = 0;
			tangeteVor.updateSyncronicedAndVerbunden();
		}

		public void toCombindendQuad() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = true;
			quad = true;
			ellypse = false;
			ellypse_3Points = false;
			bogen = false;
			OrthogonalZwang = 0;
			tangeteVor.updateSyncronicedAndVerbunden();
		}

		public void toEllypse() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = false;
			quad = false;
			ellypse = true;
			checkTangenteForEllypse();
			ellypse_3Points = false;
			bogen = false;
			OrthogonalZwang = 0;
			PolygonPoint p = getPriviosPoint();
			p.tangeteNach.x = 200;
			p.tangeteNach.y = 100;
		}

		public void toEllypse3Punkte() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = false;
			quad = false;
			ellypse = true;
			checkTangenteForEllypse();
			ellypse_3Points = true;
			bogen = false;
			OrthogonalZwang = 0;
		}

		public void toKurve3Punkte() {
			checkTangenten();
			skip = false;
			linie = false;
			verbunden = false;
			quad = false;
			ellypse = true;
			checkTangenteForEllypse();
			ellypse_3Points = true;
			bogen = true;
			OrthogonalZwang = 0;
		}

		public void toSkip() {
			skip = true;
			close = false;
			linie = true;
			verbunden = false;
			quad = false;
			ellypse = false;
			ellypse_3Points = false;
			bogen = false;
			OrthogonalZwang = 0;
			setClose(!oneShape);
		}

		public void toCloseAndJump() {
			toSkip();
			close = true;
		}

		private void checkTangenten() {
			final PolygonPoint p = getPriviosPoint();
			if (p.tangeteNach.griffoval != null) {
				p.tangeteNach.griffoval.remove();
				p.tangeteNach.griffoval = null;
			}

			if (linie) {
				getPriviosPoint().tangeteNach.setToDefaultPossision(true);
				tangeteVor.setToDefaultPossision(false);
			}
		}

		private void checkTangenteForEllypse() {
			tangeteVor.x += 10;
			tangeteVor.y += 10;
		}

		public PolygonPoint getPriviosPoint() {
			if (getID() == 0) {
				return ppunkte.get(ppunkte.size() - 1);
			}
			return ppunkte.get(getID() - 1);
		}

		public int getID() {
			return id;
		}

		public PolygonPoint getNextPoint() {
			if (getID() >= ppunkte.size() - 1) {
				return ppunkte.get(0);
			}
			return ppunkte.get(getID() + 1);
		}

		public double getX() {
			if (OrthogonalZwang == 1) {
				PolygonPoint p = getPriviosPoint();
				double x = polygon.get(p.id).getX();
				polygon.get(id).setX(x);
				return x + CPath.this.getX();
			}
			return polygon.get(id).getX() + CPath.this.getX();
		}

		public double getY() {
			if (OrthogonalZwang == 2) {
				PolygonPoint p = getPriviosPoint();
				double y = polygon.get(p.id).getY();
				polygon.get(id).setY(y);
				return y + CPath.this.getY();
			}
			return polygon.get(id).getY() + CPath.this.getY();
		}

		public void setLocation(double x, double y) {
			try {
				// X
				polygon.get(id).setX(Matrix.rastern(x - CPath.this.getX()));
				// Y
				polygon.get(id).setY(Matrix.rastern(y - CPath.this.getY()));
				setSelectedPoint(id);
				if(fixTangenteToLocation)return;
				tangeteVor.updateSyncronicedAndVerbunden();
			} catch (Exception e) {
				GreifTool.item = null;
				System.err.println("entfernt CPoly error");

			}
		}

		Color ausgewählt = new Color(255, 255, 0);

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			r.setBounds((int)m.getRealX(getX()) - i / 2, (int)m.getRealY(getY()) - i / 2, i, i);
			if (getID() != getSelectedPoint()) {
				g2.setColor(CSprite.verschiebeGriffColor);
			} else {
				g2.setColor(ausgewählt);
			}
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		@Override
		public boolean remove() {
			removePoint(getID());
			GreifTool.item = null;
			return false;
		}

		@Override
		public JPopupMenu showContext() {

			currendPath = getObject();
			currendPolygonPoint = this;
			setSelectedPoint(id);
			menuitemEntfernen.setVisible(true);
			menuitemTangenten.setVisible(linie || getNextPoint().linie);
			menuitemTangentenV.setVisible(linie);
			menuitemTangentenN.setVisible(getNextPoint().linie);
			menuitemTangentenEntfernen.setVisible(!linie || !getPriviosPoint().linie);
			return menuPoint;
		}

	}

	class Tangente extends GreifItem {
		double x;
		double y;
		PolygonPoint point;
		Tangente other;
		boolean nachher = false;
		GriffOval griffoval = null;

		public Tangente(PolygonPoint point) {
			this.point = point;
		}

		private void setToDefaultPossision(boolean vorher) {
			
			this.nachher = vorher;
			if(fixTangenteToLocation)return;
			PolygonPoint vorPoint = null;
			if (vorher) {
				if (ppunkte.size() == 0) {
					x = -5;
					y = -5;
					return;
				}
				if (ppunkte.size() <= getID() + 1) {
					vorPoint = ppunkte.get(0);
				} else {
					vorPoint = ppunkte.get(getID() + 1);
				}
				x = (vorPoint.getX() - point.getX()) / 4;
				y = (vorPoint.getY() - point.getY()) / 4;
				if (point.syncroniced) {
					other.x = -x;
					other.y = -y;
				}
				;

			} else {
				if (point.syncroniced)
					return;
				if (ppunkte.size() == 0) {
					x = 5;
					y = 5;
					return;
				}
				if (ppunkte.size() == 1) {
					x = 5;
					y = 5;
					return;
				} else if (getID() == 0) {
					vorPoint = ppunkte.get(ppunkte.size() - 1);
				} else {
					vorPoint = ppunkte.get(getID() - 1);
				}
				x = (vorPoint.getX() - point.getX()) / 4;
				y = (vorPoint.getY() - point.getY()) / 4;

			}

		}

		public int getID() {
			return point.id;
		}

		public double getX() {
			if (getID() == polygon.size()) {
				getGreifItems().remove(this);
				return 0;
			}
			return (x + polygon.get(getID()).getX() + CPath.this.getX());
		}

		public double getY() {
			if (getID() == polygon.size()) {
				getGreifItems().remove(this);
				return 0;
			}
			return (y + polygon.get(getID()).getY() + CPath.this.getY());
		}
//		public void setLocation(int xM, int yM) {
//			setLocation((double) xM, (double) yM);
//		}

		public void setLocation(double x, double y) {
			if (getID() == polygon.size()) {
				getGreifItems().remove(this);
			}
			try {
				// if (point.verbunden && !nachher) {
				// PolygonPoint vor = point.getPriviosPoint();
				// // vor.tangeteNach.setLocation(x, y);
				// vor.tangeteNach.x = x + getX() - vor.getX();
				// vor.tangeteNach.y = y + getY() - vor.getY();
				// }

				;
				// if(point.verbunden && !nachher && point.syncroniced &&
				// GreifTool.item != this ){
				// return;
				// };
				if (nachher) {
					final PolygonPoint next = point.getNextPoint();
					if (next.ellypse) {
						GreifTool.item = null;
						if (griffoval == null) {
							griffoval = new GriffOval(this, CPath.this, next.tangeteVor);
						} else {
							griffoval.remove();
							griffoval = null;
						}
						return;
					} else if (griffoval != null) {
						griffoval.remove();
						griffoval = null;
					}

				}

				this.x = x - (polygon.get(getID()).getX() + CPath.this.getX());
				// Y
				this.y = y - (polygon.get(getID()).getY() + CPath.this.getY());

				setSelectedPoint(getID());
				// if (point.syncroniced && point.id != 0) {
				// updateSyncronicedTangenten();
				// }
				updateSyncronicedAndVerbunden();

			} catch (Exception e) {
				GreifTool.item = null;
				System.err.println("Tangete wirft fehler: " + getID());
				e.printStackTrace();

			}
		}

		private void updateVerbundenTangenten() {
			if (nachher) {
				PolygonPoint nextPoint = point.getNextPoint();
				nextPoint.tangeteVor.x = getX() - nextPoint.getX();
				nextPoint.tangeteVor.y = getY() - nextPoint.getY();
			} else {
				PolygonPoint vor = point.getPriviosPoint();
				vor.tangeteNach.x = getX() - vor.getX();
				vor.tangeteNach.y = getY() - vor.getY();
			}

		}

		private void updateSyncronicedTangenten() {
			other.x = -this.x;
			other.y = -this.y;
		}

		// Dise Methode verändert alle andern Tangenten nach der Position der
		// Aktuellen.
		public void updateSyncronicedAndVerbunden() {
			
			int zirkelbezug = 0;
			Tangente superTangente = this;
			// eine "Vorher"tangente vorberteiten und auswählen, wenn von einer
			// "Nachher"Tangente aufgrufen wurde
			if (nachher) {
				if (point.syncroniced) {
					updateSyncronicedTangenten();
					superTangente = superTangente.point.tangeteVor;
				} else if (point.verbunden) {
					updateVerbundenTangenten();
					superTangente = superTangente.point.getNextPoint().tangeteVor;
				} else
					return;
			}
			// in Zeiger Richtung
			if (superTangente.point.syncroniced) {
				superTangente.updateSyncronicedTangenten();
				PolygonPoint vorher = superTangente.point;
				PolygonPoint aktuellePunkt = superTangente.point.getNextPoint();
				while (true) {
					if (aktuellePunkt.verbunden) {
						vorher.tangeteNach.updateVerbundenTangenten();
						if (aktuellePunkt.syncroniced) {
							aktuellePunkt.tangeteVor.updateSyncronicedTangenten();
							vorher = aktuellePunkt;
							aktuellePunkt = aktuellePunkt.getNextPoint();
							if (!aktuellePunkt.equals(superTangente.point))
								continue;
							else
								zirkelbezug += 1;
						}
					}
					break;
				}
			}
			// gegen Zeiger Richtung
			if (superTangente.point.verbunden) {
				superTangente.updateVerbundenTangenten();
				PolygonPoint aktuellePunkt = superTangente.point.getPriviosPoint();
				while (true) {
					if (aktuellePunkt.syncroniced) {

						aktuellePunkt.tangeteNach.updateSyncronicedTangenten();
						;
						if (aktuellePunkt.verbunden) {
							aktuellePunkt.tangeteVor.updateVerbundenTangenten();
							aktuellePunkt = aktuellePunkt.getPriviosPoint();
							if (!aktuellePunkt.equals(superTangente.point))
								continue;
							else
								zirkelbezug += 1;
						}
					}
					break;
				}
			}

			if (zirkelbezug == 2) {
				superTangente.point.syncroniced = false;
			}
		}
		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			if (point.verbunden && !nachher) {
				g.setColor(line);
				// g.drawLine(getX(), getY(), point.getX(), point.getY());
				
				PolygonPoint vor = point.getPriviosPoint();
				DDrawer.drawLine(getX(), getY(),vor.getX(), vor.getY(),g);
				
			}

			if (!nachher && point.linie) {
				r.height = 0;
				r.width = 0;
				return;
			}
			g.setColor(Color.WHITE);
			final int i = 10;
			r.setBounds((int)m.getRealX(getX()) - i / 2, (int)m.getRealY(getY()) - i / 2, i, i);
			g.setColor(line);
			if (nachher) {
				final PolygonPoint next = point.getNextPoint();
				if (next.verbunden || next.linie || next.ellypse_3Points) {
//					g.drawRect(r.x, r.y, r.height, r.height);
					r.height = 0;
					r.width = 0;
					return;
				}
				if (next.ellypse) {
					r.setBounds((int)m.getRealX(point.getX()) + 15, (int)m.getRealY(point.getY()) + 15, i, i);

					g2.setColor(Color.BLUE);
					g2.fillOval(r.x, r.y, i, i);
					g2.setColor(Color.WHITE);
					g2.drawOval(r.x, r.y, i, i);
					g2.draw(r);
					return;
				}

			}
			if (point.ellypse && !point.bogen) {
				final PolygonPoint vor = point.getPriviosPoint();

				final double x0 = vor.getX();
				final double y0 = vor.getY();

				// Determine target "to" position
				final double xto = point.getX();
				final double yto = point.getY();
				// Compute the half distance between the current and the final
				// point
				final double dx2 = (x0 - xto) / 2.0;
				final double dy2 = (y0 - yto) / 2.0;
				double x = vor.getX() - dx2;
				double y = vor.getY() - dy2;
				DDrawer.drawLine(getX(),getY(),x,y,g);
				if (vor.tangeteNach.griffoval != null) {
					;
					double rx = Math.abs(vor.tangeteNach.x);
					double ry = Math.abs(vor.tangeteNach.y);

					final double tx = getX() - vor.getX() + dx2;
					final double ty = getY() - vor.getY() + dy2;
					final double xAxisRotationR = Math.atan(ty / tx);
					final double cosAngle = Math.cos(xAxisRotationR);
					final double sinAngle = Math.sin(xAxisRotationR);
					g.translate(x, y);
					g.rotate(xAxisRotationR);
					g.drawRect((int) (-rx), (int) (-ry), (int) (2 * rx), (int) (2 * ry));
					g.rotate(-xAxisRotationR);
					g.translate(-x, -y);
					//
					// Step 1 : Compute (x1, y1)
					//
					final double x1 = (cosAngle * dx2 + sinAngle * dy2);
					final double y1 = (-sinAngle * dx2 + cosAngle * dy2);
					double Prx = rx * rx;
					double Pry = ry * ry;
					final double Px1 = x1 * x1;
					final double Py1 = y1 * y1;
					// check that radii are large enough
					final double radiiCheck = Px1 / Prx + Py1 / Pry;
					if (radiiCheck > 1.0) {
						rx = Math.sqrt(radiiCheck) * rx;
						ry = Math.sqrt(radiiCheck) * ry;
						if (rx == rx && ry == ry) {
							/* not NANs */} else {
							return;
						}
					} else {
						// von links nach rechts? // Tangenten punkt über Gerade
						// der
						// beiden punkte
						/***/
						boolean localSweepFlag = x0 < xto;
						if (yto + ((y0 - yto) / (x0 - xto)) * (point.tangeteVor.x) < point.tangeteVor.getY())
							localSweepFlag = !localSweepFlag;

						/***/
						final boolean localLargeArcFlag = vor.LargeArcFlag;
						//
						// Step 2 : Compute (cx1, cy1)
						//
						double sign = ((localLargeArcFlag == localSweepFlag) ? -1.0 : 1.0);
						double sq = ((Prx * Pry) - (Prx * Py1) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1));
						sq = (sq < 0.0) ? 0.0 : sq;
						final double coef = (sign * Math.sqrt(sq));
						final double cx1 = coef * ((rx * y1) / ry);
						final double cy1 = coef * -((ry * x1) / rx);

						//
						// Step 3 : Compute (cx, cy) from (cx1, cy1)
						//
						final double sx2 = (x0 + xto) / 2.0;
						final double sy2 = (y0 + yto) / 2.0;
						x = (int) (sx2 + (cosAngle * cx1 - sinAngle * cy1));
						y = (int) (sy2 + (sinAngle * cx1 + cosAngle * cy1));
					}

					g.translate(x, y);
					g.rotate(xAxisRotationR);
					g.drawRect((int) (-rx), (int) (-ry), (int) (2 * rx), (int) (2 * ry));
					g.drawOval((int) (-rx), (int) (-ry), (int) (2 * rx), (int) (2 * ry));
					g.rotate(-xAxisRotationR);
					g.translate(-x, -y);
				}
			} else {
				DDrawer.drawLine(getX(), getY(),point.getX(), point.getY(),g);
			}
			if (getID() != getSelectedPoint()) {
				g2.setColor(Color.CYAN);
			} else {
				g2.setColor(Color.CYAN);
			}
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}

		@Override
		public boolean remove() {
			removePoint(getID());
			GreifTool.item = null;
			return false;
		}

		@Override
		public JPopupMenu showContext() {
			currendTangetne = this;
			menuitemSyncron.setVisible(!point.syncroniced);
			menuitemDeSyncron.setVisible(point.syncroniced);
			if (nachher) {
				PolygonPoint nextPoint = point.getNextPoint();
				menuitemGemeinsameTangenten.setVisible(!nextPoint.linie && !nextPoint.verbunden);
				menuitemQuadratisch.setVisible(!nextPoint.quad);
				menuitemGetennt.setVisible(nextPoint.verbunden);
				menuitemZuLine.setVisible(!nextPoint.linie);
			} else {
				menuitemGemeinsameTangenten.setVisible(!point.linie && !point.verbunden);
				menuitemQuadratisch.setVisible(!point.quad);
				menuitemGetennt.setVisible(point.verbunden);
				menuitemZuLine.setVisible(!point.linie);

			}
			return menuTangente;
		}

	}
	protected static final AddPointTool addPointTool = new AddPointTool();
	
	class AddPoint extends GreifItem {
		PolygonPoint polygonPoint;
		final Color ColorGriff = new Color(0, 255, 0);

		public AddPoint(PolygonPoint polygonPoint) {
			this.polygonPoint = polygonPoint;
		}

		public int getID() {
			return polygonPoint.getID();
		}

		public double getX() {
			return CPath.this.getX()
					+ (polygon.get(getID()).getX() + polygon.get(getID() == 0 ? polygon.size() - 1 : getID() - 1).getX()) / 2;
		}

		public double getY() {
			return CPath.this.getY()
					+ (polygon.get(getID()).getY() + polygon.get(getID() == 0 ? polygon.size() - 1 : getID() - 1).getY()) / 2;
		}

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			if (getID() == polygon.size()) {
				getGreifItems().remove(this);
				return;
			}
			g2.setColor(Color.WHITE);
			int i = 8;
			r.setBounds((int)m.getRealX(getX()) - i / 2, (int) m.getRealY(getY()) - i / 2, i, i);
			g2.setColor(ColorGriff);
			g2.fillOval(r.x, r.y, i, i);
			g2.setColor(Color.BLACK);
			g2.drawOval(r.x, r.y, i, i);
			g2.setColor(Color.WHITE);

			g2.draw(r);
		}

		Rectangle r = new Rectangle();

		@Override
		public Rectangle getBounds() {
			return r;
		}
		public void setLocation(double x, double y) {
			PolygonPoint p = addPoint(getX() - CPath.this.getX(), getY() - CPath.this.getY(), getID());
			GreifTool.item = p;
			addPointTool.setID(getID(), p);
			addPointTool.setCPath(CPath.this);
			getMatrix().setMainTool(addPointTool);
		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {

			currendAddPoint = this;
			currendPolygonPoint = polygonPoint;
			menuitemTangentenMitte.setVisible(currendPolygonPoint.linie || currendPolygonPoint.verbunden);
			menuitemLinieMitte.setVisible(!currendPolygonPoint.linie || currendPolygonPoint.skip);
			menuitemGemeinsameLineMitte.setVisible(currendPolygonPoint.quad || currendPolygonPoint.linie);
			menuitemQuadratischMitte.setVisible(!currendPolygonPoint.quad);
			menuitemEllypse.setVisible(!currendPolygonPoint.ellypse);
			menuitemEllypse_3P.setVisible(!currendPolygonPoint.ellypse_3Points);
			menuitemBogen_3P.setVisible(!currendPolygonPoint.bogen);
			menuitemHorizontal.setVisible(currendPolygonPoint.OrthogonalZwang != 2);
			menuitemVertical.setVisible(currendPolygonPoint.OrthogonalZwang != 1);
			menuitemNichtOrthogonal.setVisible(currendPolygonPoint.OrthogonalZwang != 0);
			menuitemSprung.setVisible(!currendPolygonPoint.skip);
			menuitemSchließen.setVisible(!currendPolygonPoint.skip || !currendPolygonPoint.close);
			return menuLineMitte;
		}
	}

	public class PrivatePoint {
		private double x1;
		private double y1;

		public double getX() {
			return x1;
		}

		public double getY() {
			return y1;
		}

		public void setX(double x) {
			this.x1 = x;
		}

		public void setY(double y) {
			this.y1 = y;
		}

		public PrivatePoint(double x, double y) {
			this.x1 = x;
			this.y1 = y;
		}

		@Override
		public String toString() {
			return x1 + "\t" + y1 + "\t";
		}
	}

	public CPath getObject() {
		return this;
	}

	public class VerschiebeFragmentGriff extends GreifItem {
		Rectangle r = new Rectangle();

		public VerschiebeFragmentGriff(int fragmentID) {
			this.fragmentID = fragmentID;
		}

		Color griffcolor = transformGriffColor.darker().darker();

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			update();
			if (!aktiv) {
				r.height = 0;
				r.width = 0;
				return;
			}
			g.setColor(Color.WHITE);
			int i = 10;
			// if(i == 0){i = 1;}
			r.setBounds((int)m.getRealX(getX()) - i / 2,(int) m.getRealY(getY()) - i / 2, i, i);
			g2.setColor(griffcolor);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		@Override
		public Rectangle getBounds() {
			return r;
		}

		public int getID() {
			return -fragmentID;
		}

		double centerX = 0;
		double centerY = 0;
		int fragmentID = 0;

		boolean aktiv = true;
		int start = -1, ende = -1;
		int lastSkip = -1;

		public void update() {
			int fragmentNR = 0;
			start = -1;
			ende = -1;
			lastSkip = -1;
			for (PolygonPoint p : ppunkte) {
				if (p.skip || p.id == 0) {
					fragmentNR += 1;
					if (start != -1) {
						ende = p.id;
						break;
					} else if (fragmentNR == fragmentID) {
						start = p.id;
					}

				}

			}
			if (start == 0) {
				for (PolygonPoint p : ppunkte) {
					if (p.skip || p.id == 0) {
						lastSkip = p.id;
					}
				}
			}
			if (ende == -1 || start == -1) {
				aktiv = false;
				return;
			}
			centerX = 0;
			centerY = 0;
			for (int i = start; i < ende; i += 1) {
				PolygonPoint p = ppunkte.get(i);
				centerY += p.getY();
				centerX += p.getX();
			}
			if (start == 0) {
				for (int i = lastSkip; i < ppunkte.size(); i += 1) {
					PolygonPoint p = ppunkte.get(i);
					centerY += p.getY();
					centerX += p.getX();
				}
				// PolygonPoint p = ppunkte.get(0);
				//// centerY -= p.getY();
				//// centerX -= p.getX();
				// p = ppunkte.get(ppunkte.size()-1);
				// centerY += p.getY();
				// centerX += p.getX();
				centerY /= (ende - start) + lastSkip - 1;
				centerX /= (ende - start) + lastSkip - 1;
				// centerX += CPath2.this.getX();
				// centerY += CPath2.this.getY();

			} else {
				centerY /= ende - start;
				centerX /= ende - start;
			}

			aktiv = true;
			int verschieben = 0;
			if (GreifTool.item == null || !GreifTool.item.equals(this)) {
				verschieben = 0;
				for (int n = 0; n != getGreifItems().size(); n++) {
					GreifItem i = getGreifItems().get(n);
					try {
						if (!(i instanceof VerschiebeFragmentGriff) && !(i instanceof VerschiebeGriff)) {

							if (centerX - 10 + verschieben < i.getX() && i.getX() < centerX + 10 + verschieben
									&& centerY - 10 < i.getY() && i.getY() < centerY + 10) {
								verschieben += 12;
								n = 0;
							}
						}
					} catch (NullPointerException ex) {
						System.out.println(i);
						ex.printStackTrace();
					}
				}
			}
			centerX += verschieben;
		}

		public double getX() {
			return centerX;
		}

		public double getY() {
			return centerY;
		}

		@Override
		public void setLocation(double x, double y) {
			for (int i = start; i < ende; i += 1) {
				PolygonPoint p = ppunkte.get(i);
				p.setLocation(p.getX() - centerX + x, p.getY() - centerY + y);
			}
			if (start == 0) {
				for (int i = lastSkip; i < ppunkte.size(); i += 1) {
					PolygonPoint p = ppunkte.get(i);
					p.setLocation(p.getX() - centerX + x, p.getY() - centerY + y);
				}
			}
			centerX = x;
			centerY = y;

		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return getMenu();
		}
	}// Ende class VerschiebeGriff

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.geom.CPoly.class;
	}

	@Override
	public CPath clone1() throws CloneNotSupportedException {
		CPath c = new CPath(getSVGText(), getMatrix());
		c.setCSS(this.getCSS().clone1(this));
		return c;
	}

	public boolean isClose() {
		return close.getValue();
	}

	public void setClose(boolean close) {
		this.close.setValue(close);
	}

	public boolean isEvenodd() {
		return evenOdd.getValue();
	}

	public void setEvenOdd(boolean evenOdd) {
		this.evenOdd.setValue(evenOdd);
	}

	public void resetCurrendTangenten() {
		if (ppunkte.size() == 0)
			return;
		if (getSelectedPoint() == 0) {
			ppunkte.get(ppunkte.size() - 1).tangeteVor.setToDefaultPossision(false);
			ppunkte.get(ppunkte.size() - 1).tangeteNach.setToDefaultPossision(true);
		} else {
			ppunkte.get(getSelectedPoint() - 1).tangeteVor.setToDefaultPossision(false);
			ppunkte.get(getSelectedPoint() - 1).tangeteNach.setToDefaultPossision(true);
		}
		ppunkte.get(getSelectedPoint()).tangeteVor.setToDefaultPossision(false);
		ppunkte.get(getSelectedPoint()).tangeteNach.setToDefaultPossision(true);
	}

	public String getSVGText() {

		if (ppunkte.size() == 0)
			return "M 100 100 L 200 100 ";
		isInit = false;
		PolygonPoint vor = ppunkte.get(ppunkte.size() - 1);
		int i = -1;
		StringBuilder s = new StringBuilder();
		for (PolygonPoint p : ppunkte) {
			if (i == -1) {
				if (p.skip) {
					i = p.id;
				} else {
					vor = p;
					continue;
				}
			}
			singleLineSVGText(p, vor, s);
			vor = p;
		}
		oneShape = i == -1;
		PolygonPoint pl = ppunkte.get(0);
		boolean replacedWithMove = false;
		if (oneShape && !pl.skip) {
			s.append("M ").append(pl.getX()).append(" ").append(pl.getY()).append(" ");
			replacedWithMove = true;
		}
		for (PolygonPoint p : ppunkte) {
			if (i == p.id) {
				break;
			}
			if (replacedWithMove) {
				replacedWithMove = false;
			} else {
				singleLineSVGText(p, vor, s);
			}
			vor = p;
		}
		if (isClose()) {
			s.append("Z");
		}
		return s.toString();
	}

	/**
	 * // Z = closepath
	 */
	private void singleLineSVGText(PolygonPoint p, PolygonPoint vor, StringBuilder b) {
		if (p.skip) {
			if (p.close && isInit) {
				b.append("Z ");
			}
			// M = moveto
			b.append("M ").append(p.getX()).append(" ").append(p.getY()).append(" ");
			isInit = true;
		} else if (p.linie) {
			// L = lineto
			if (p.OrthogonalZwang == 0) {
				b.append("L ").append(p.getX()).append(" ").append(p.getY());
			}
			// V = vertical lineto
			else if (p.OrthogonalZwang == 1) {
				b.append("V ").append(p.getY());
			}
			// H = horizontal lineto
			else if (p.OrthogonalZwang == 2) {
				b.append("H ").append(p.getX());
			}
		} else if (p.quad) {
			// TODO SMOOTH Q

			if (vor.quad && vor.syncroniced) {
				// T = smooth quadratic Bézier curveto
				b.append("T ").append(p.getX()).append(" ").append(p.getY());
			} else {
				// Q = quadratic Bézier curve
				b.append("Q ").append(p.tangeteVor.getX()).append(" ").append(p.tangeteVor.getY()).append(" ")
						.append(p.getX()).append(" ").append(p.getY());
			}

		} else if (p.ellypse) {
			if (p.bogen) {
				// see: creat3PunkteBogen()
				final double x0 = vor.getX();
				final double y0 = vor.getY();

				// Determine target "to" position
				final double xto = p.getX();
				final double yto = p.getY();

				final double x2 = p.tangeteVor.getX();
				final double y2 = p.tangeteVor.getY();

				double MittelPunktAB_X = (x0 + x2) / 2;
				double MittelPunktAB_Y = (y0 + y2) / 2;
				double SteigungAB = -(x2 - x0) / (y2 - y0);
				/**
				 * y = SteigungAB(x-MittelPunktAB_X)+MittelPunktAB_Y
				 */
				double MittelPunktAC_X = (x0 + xto) / 2;
				double MittelPunktAC_Y = (y0 + yto) / 2;
				double SteigungAC = -(xto - x0) / (yto - y0);
				/**
				 * y = SteigungAC(x-MittelPunktAC_X)+MittelPunktAC_Y
				 * 
				 * SteigungAB*x-SteigungAC*x =
				 * MittelPunktAC_Y-MittelPunktAB_Y+SteigungAB
				 * *MittelPunktAB_X-SteigungAC*MittelPunktAC_X
				 */

				double x = (MittelPunktAC_Y - MittelPunktAB_Y + SteigungAB * MittelPunktAB_X
						- SteigungAC * MittelPunktAC_X) / (SteigungAB - SteigungAC);
				double y = SteigungAB * (x - MittelPunktAB_X) + MittelPunktAB_Y;
				double pytX = x - x0;
				double pytY = y - y0;
				double radius = Math.sqrt(pytX * pytX + pytY * pytY);
				double winkel1 = Math.toDegrees(Math.acos((x0 - x) / radius));
				if (y < y0) {
					winkel1 = 360 - winkel1;
				}
				// System.out.println(winkel1);
				double winkel2 = Math.toDegrees(Math.acos((xto - x) / radius));
				if (y < yto) {
					winkel2 = 360 - winkel2;
				}
				double winkel3 = Math.toDegrees(Math.acos((x2 - x) / radius));
				if (y < y2) {
					winkel3 = 360 - winkel3;
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
				double winkel = winkel2 - winkel1;
				if (gegenUrzeiger && winkel < 0) {
					winkel += 360;
				}
				if (!gegenUrzeiger && winkel > 0) {
					winkel -= 360;
				}
				// System.out.println(winkel);
				// A Radus
				b.append("A ")
						// der Radius der x-Achse der Ellipse
						// der Radius der y-Achse der Ellipse
						.append(Math.abs((float) radius)).append(" ").append(Math.abs((float) radius)).append(" ")
						// die Rotation der x-Achse der Ellipse in Grad
						// (0 bedeutet keine Rotation)
						/** .append(winkel3).append(" ") */
						.append("0").append(" ")
						// das large-arc-flag:
						// 0 für den kurzen Weg um die Ellipse
						// 1 für den langen Weg um die Ellipse
						.append((Math.abs(winkel) > 180) ? "1" : "0").append(" ")
						// das sweep-flag:
						// 0 für Zeichnung entgegen den Uhrzeigersinn
						// 1 für Zeichnung mit dem Uhrzeigersinn
						.append((!gegenUrzeiger) ? "1" : "0").append(" ")
						// die Koordinaten des Endpunktes.
						.append(p.getX()).append(" ").append(p.getY());

			} else {
				double x0 = vor.getX();
				double y0 = vor.getY();
				// Determine target "to" position
				final double xto = p.getX();
				final double yto = p.getY();
				// Compute the half distance between the current and the final
				// point
				final double dx2 = (x0 - xto) / 2.0;
				final double dy2 = (y0 - yto) / 2.0;
				// Crate Rotation from Tangente
				final double tx = p.tangeteVor.x - dx2;
				final double ty = p.tangeteVor.y - dy2;
				/***/
				boolean localSweepFlag = x0 < xto;
				if (yto + ((y0 - yto) / (x0 - xto)) * (p.tangeteVor.x) < p.tangeteVor.getY())
					localSweepFlag = !localSweepFlag;

				// Convert angle from degrees to radians
				/***/
				final double xAxisRotationR = Math.atan(ty / tx);

				// A = elliptical Arc
				b.append("A ")

						// der Radius der x-Achse der Ellipse
						// der Radius der y-Achse der Ellipse
						.append(Math.abs((float) vor.tangeteNach.x)).append(" ")
						.append(Math.abs((float) vor.tangeteNach.y)).append(" ")
						// die Rotation der x-Achse der Ellipse in Grad
						// (0 bedeutet keine Rotation)
						.append(normalizeDegrees((float) Math.toDegrees(xAxisRotationR), tx < 0)).append(" ")
						// das large-arc-flag:
						// 0 für den kurzen Weg um die Ellipse
						// 1 für den langen Weg um die Ellipse
						.append((vor.LargeArcFlag) ? "1" : "0").append(" ")
						// das sweep-flag:
						// 0 für Zeichnung entgegen den Uhrzeigersinn
						// 1 für Zeichnung mit dem Uhrzeigersinn
						.append((localSweepFlag) ? "1" : "0").append(" ")
						// die Koordinaten des Endpunktes.
						.append(p.getX()).append(" ").append(p.getY());
			}

		} else {
			if (vor.syncroniced) {
				// S = smooth curveto
				b.append("S ").append(p.tangeteVor.getX()).append(" ").append(p.tangeteVor.getY()).append(" ")
						.append(p.getX()).append(" ").append(p.getY());
			} else {
				// C = curveto
				b.append("C ").append(vor.tangeteNach.getX()).append(" ").append(vor.tangeteNach.getY()).append(" ")
						.append(p.tangeteVor.getX()).append(" ").append(p.tangeteVor.getY()).append(" ")
						.append(p.getX()).append(" ").append(p.getY());
			}
		}
		b.append(" ");
	}

	private double normalizeDegrees(double degrees, boolean b) {
		if (b) {
			degrees = 180 - degrees;
		} else if (degrees > 0) {
			degrees = 360 - degrees;
		} else {
			degrees = -degrees;
		}

		return degrees;
	}

	private void SVGtoData(String list) {
//		System.out.println(list);
		StringTokenizer token = new StringTokenizer(list.trim(),
				" \n\r\t,abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVEXYZ", true);
		float[] i = new float[7];
		char thisChar = '?';
		char nextChar = '?';
		boolean closed = false;
		while (true) {

			for (int n = 0; n != i.length; n += 1) {
				i[n] = 0;
			}
			int n = 0;
			String nextToken;
			if (token.hasMoreTokens()) {
				nextToken = token.nextToken();
				while (nextToken.matches("[0-9]+") || nextToken.contains(".") || nextToken.contains("-")
						|| nextToken.matches("\\s")) {

					if (nextToken.equals(" ") || nextToken.equals("\r") || nextToken.equals("\n")
							|| nextToken.equals("\t") || nextToken.equals(",")) {
						nextToken = token.nextToken();
						continue;
					}
					i[n] = Float.parseFloat(nextToken);
					n += 1;
					if (token.hasMoreElements()) {
						nextToken = token.nextToken();
					} else {
						nextToken = "?killcommand";
						break;
					}
				}
			} else {
				nextToken = "?killcommand";
			}
			nextChar = nextToken.charAt(0);
			// neue Sachen erstellen
			if (thisChar == '?') {

			} else if (thisChar == 'M') {
				if (polygon.size() == 0) {
					addPoint((int) i[0], (int) i[1]);

				} else {
					if (closed) {
						addPoint((int) i[0], (int) i[1]).toCloseAndJump();
						closed = false;
					} else {
						addPoint((int) i[0], (int) i[1]).toSkip();
					}
				}
			} else if (thisChar == 'L') {
				addPoint((int) i[0], (int) i[1]).toLinie();
			} else if (thisChar == 'V') {
				addPoint((int) i[0], (int) i[0]).OrthogonalZwang = 1;
			} else if (thisChar == 'H') {
				addPoint((int) i[0], (int) i[0]).OrthogonalZwang = 2;
			} else if (thisChar == 'Q') {
				PolygonPoint p = addPoint((int) i[2], (int) i[3]);
				p.toCombindendQuad();
				p.tangeteVor.setLocation(i[0], i[1]);
			} else if (thisChar == 'T') {
				PolygonPoint p = addPoint((int) i[0], (int) i[1]);
				PolygonPoint vor = p.getPriviosPoint();
				p.toCombindendQuad();
				vor.syncroniced = true;
				vor.tangeteVor.updateSyncronicedAndVerbunden();
			} else if (thisChar == 'S') {
				PolygonPoint p = addPoint((int) i[2], (int) i[3]);
				p.toTangente();
				PolygonPoint vor = p.getPriviosPoint();
				vor.syncroniced = true;
				p.tangeteVor.setLocation(i[0], i[1]);
				vor.tangeteVor.updateSyncronicedAndVerbunden();
				if(vor.tangeteNach.getX() == i[0] && vor.tangeteNach.getY() == i[1]){
					p.toCombindendCurve();
				}
			} else if (thisChar == 'C') {
				PolygonPoint p = addPoint((int) i[4], (int) i[5]);
				p.toTangente();
				p.syncroniced = false;
				p.tangeteVor.setLocation(i[2], i[3]);
				p.getPriviosPoint().tangeteNach.setLocation(i[0], i[1]);
				if(i[2] == i[0] && i[3] == i[1]){
					p.toCombindendCurve();
				}
			} else if (thisChar == 'Z' || thisChar == 'z') {
				closed = true;
			} else if (thisChar == 'A') {
				// der Radius der x-Achse der Ellipse
				final double radiusX = i[0];
				// // der Radius der y-Achse der Ellipse
				final double radiusY = i[1];
				// // die Rotation der x-Achse der Ellipse in Grad
				final double xAxisRotationR = Math.toRadians(i[2]);
				// // das large-arc-flag:
				// // 0 für den kurzen Weg um die Ellipse
				// // 1 für den langen Weg um die Ellipse
				final boolean localLargeArcFlag = (int) i[3] == 1 ? true : false;
				// .append((Math.abs(winkel2) > 180) ? "1" : "0").append(" ")
				// // das sweep-flag:
				// // 0 für Zeichnung entgegen den Uhrzeigersinn
				// // 1 für Zeichnung mit dem Uhrzeigersinn
				final boolean localSweepFlag = (int) i[4] == 1 ? true : false;
				// // die Koordinaten des Endpunktes.
				final int x = (int) i[5];
				final int y = (int) i[6];
				// Kreis
				if (radiusX == radiusY) {
					PolygonPoint target = addPoint(x, y);
					target.toKurve3Punkte();
					PolygonPoint vor = target.getPriviosPoint();

					// Punkt A
					final double xA = target.getX();
					final double yA = target.getY();
					// Punkt B
					final double xB = vor.getX();
					final double yB = vor.getY();
					double MittelPunktAB_X = (xA + xB) / 2;
					double MittelPunktAB_Y = (yA + yB) / 2;
					double AB = (xA - xB) * (xA - xB) + (yA - yB) * (yA - yB);
					double s = Math.sqrt(radiusX * radiusX - (AB / 4));
					double vec_x = -(yB - yA) / Math.sqrt(AB);
					double vec_y = -(xA - xB) / Math.sqrt(AB);
					final double xM;
					final double yM;
					int invertUrzeigerSinn = -1;
					if (localSweepFlag) {
						invertUrzeigerSinn = 1;
					}
					int invertÜber180 = -1;
					if (localLargeArcFlag) {
						invertÜber180 = 1;
					}
					xM = MittelPunktAB_X + vec_x * (s) * invertUrzeigerSinn * invertÜber180;
					yM = MittelPunktAB_Y + vec_y * (s) * invertUrzeigerSinn * invertÜber180;

					// if (xAxisRotationR != 0) {
					// double xC = xM + Math.cos(xAxisRotationR) *
					// (radiusX)*invertUrzeigerSinn;
					// double yC = yM + Math.sin(xAxisRotationR) *
					// (radiusX)*invertUrzeigerSinn;
					// target.tangeteVor.setLocation(xC, yC);
					// } else {
					double xC = xM + vec_x * (radiusX) * invertUrzeigerSinn;
					double yC = yM + vec_y * (radiusX) * invertUrzeigerSinn;
					target.tangeteVor.setLocation(xC, yC);
					// }

				} else {
					// Oval
					PolygonPoint target = addPoint(x, y);
					target.toEllypse();
					PolygonPoint vor = target.getPriviosPoint();
					vor.tangeteNach.x = radiusX;
					vor.tangeteNach.y = radiusY;
					vor.LargeArcFlag = localLargeArcFlag;
					final double x0 = vor.getX();
					final double y0 = vor.getY();

					// Determine target "to" position
					final double xto = target.getX();
					final double yto = target.getY();
					// Compute the half distance between the current and the
					// final point
					final double dx2 = (x0 - xto) / 2.0;
					final double dy2 = (y0 - yto) / 2.0;

					// if (localSweepFlag) {
					target.tangeteVor.x = (dx2 + 300 * Math.cos(xAxisRotationR));
					target.tangeteVor.y = (dy2 - 300 * Math.sin(xAxisRotationR));
					// } else {
					// target.tangeteVor.x = (dx2 + 300 *
					// Math.cos(xAxisRotationR));
					// target.tangeteVor.y = (dy2 - 300 *
					// Math.sin(xAxisRotationR));
					// }
				}

				// FIXME klein Buchstaben
			}
			thisChar = nextChar;
			if (nextToken.equals("?killcommand"))
				break;
		}
		setClose(closed);

	}
}