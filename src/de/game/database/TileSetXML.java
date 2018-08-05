package de.game.database;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TileSetXML extends XML {
	/**
	 * @author rubberOo 
	 *E: 		Diese Klasse lädt TileSet ein (read()) oder erstellt
	 * 			(construktor) eine neu Datei im tsx/XML-Fromat. 
	 *V: 		Das Objekt
	 *         enthält alle getter und setter die Benötigt werden. Auf ein
	 *         Tile(Kachel) wird über das interne Objekt zugegriffen. Zusätzlich
	 *         wird hier noch gezeigt wie zusatz infos (cost,level) eingebaut
	 *         wird. Dies möchte ich für eine Wegfindung nutzen. cost ist der
	 *         aufwand diesen Feld zudurchqueren 1 = weg 2 = graßland 4 =
	 *         wald/gebüsch ... 100 = Flüsse 1000 = Feuer... level ist ob es
	 *         blockiert 1 = überfahrbar 10 = übergehbar 20 = überspringbar 50 =
	 *         überschwimmbar 100 = überfliegbar 200 = Only Goast 
	 * A: 		Schreibt .tsx Datein im XML-Format
	 *         
	 */
	/**
	 * So solte das Ergebnis aussehen: <image source="XXXFILEXXX" trans="xxx"
	 * width="xxx" height="xxx"/> <tile id="xx"> <properties> <property
	 * name="cost" value="XXXZAHLXXX"/> <property name="level"
	 * value="XXXZAHLXXX"/> <property name="freiwählbar" value="freiwählbar"/>
	 * ... </properties> ... </tile> </tileset> ... Einlesen in die tmx-Datei
	 * <tileset firstgid="0" source="/name.tsx"/>
	 */
	private int tilewidth, tileheight;
	public HashMap<Point, TileXML> hashmap = new HashMap<Point, TileXML>();
	private File imageFile;// = new File("/xxx/bild.png");
	private Image i;
	private String name, trans = null, spacing = null, margin = null;

	public TileSetXML(File imageFile, String name, int tilewidth,
			int tileheight, String trans, String spacing, String margin) {
		this(imageFile, name, tilewidth, tileheight);
		this.trans = trans;
		this.spacing = spacing;
		this.margin = margin;
	}

	public TileSetXML(File imageFile, String name, int tilewidth,
			int tileheight) {
		this.imageFile = imageFile;
		this.name = name;
		this.tilewidth = tilewidth;
		this.tileheight = tileheight;
		try {
			i = ImageIO.read(imageFile);
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
		}
	}

	public TileSetXML(File f) {
		read(f);
	}

	public TileXML addTile(int x, int y) {
		TileXML t = new TileXML(x, y, i.getWidth(null) / tilewidth);
		hashmap.put(new Point(x, y), t);
		return t;
	}

	public Element getElements(Element root, Document xmlDoc, int firstgid,
			File destiny) {
		root.setAttribute("firstgid", firstgid + "");
		root.setAttribute("name", name);
		root.setAttribute("tilewidth", tilewidth + "");
		root.setAttribute("tileheight", tileheight + "");
		Element image = xmlDoc.createElement("image");
		image.setAttribute("source",
				imageFile.getPath().replace(destiny.getParent(), ""));
		if (trans != null) {
			/* "004080" */
			/* Integer.toHexString(Color.BLUE.getRGB()) */
			image.setAttribute("trans", trans);
		}
		if (spacing != null) {
			image.setAttribute("spacing", spacing);
		}
		if (margin != null) {
			image.setAttribute("margin", margin);
		}
		image.setAttribute("width", i.getWidth(null) + "");
		image.setAttribute("height", i.getHeight(null) + "");
		root.appendChild(image);
		for (Point p : hashmap.keySet()) {
			hashmap.get(p).getElements(root, xmlDoc, firstgid);
		}

		return root;
	}//

	public class TileXML {
		public int cost;
		public int level;
		int id;

		public TileXML(int x, int y, int TilesInX) {
			this(((y * TilesInX) + x));
		}

		public TileXML(int x, int y, int TilesInX, int cost, int level) {
			this(((y * TilesInX) + x));
			this.cost = cost;
			this.level = level;
		}

		public TileXML(int id) {
			this.id = id;
		}

		HashMap<String, String> propertys = new HashMap<String, String>();

		public void getElements(Element tielset, Document xmlDoc, int firstgid) {
			// System.out.println(y+" + " + x + " = "+ id);
			Element tile = xmlDoc.createElement("tile");
			tile.setAttribute("id", id + firstgid + "");
			Element properties = xmlDoc.createElement("properties");

			Element property = xmlDoc.createElement("property");
			property.setAttribute("name", "cost");
			property.setAttribute("value", cost + "");
			properties.appendChild(property);

			Element property2 = xmlDoc.createElement("property");
			property2.setAttribute("name", "level");
			property2.setAttribute("value", level + "");
			properties.appendChild(property2);

			for (String s : propertys.keySet()) {
				Element property3 = xmlDoc.createElement("property");
				property3.setAttribute("name", s);
				property3.setAttribute("value", propertys.get(s));
				properties.appendChild(property3);
			}
			tile.appendChild(properties);
			tielset.appendChild(tile);
		}

		public int getCost() {
			return cost;
		}

		public int getLevel() {
			return level;
		}

		public int getId() {
			return id;
		}

		public String getProperty(String name) {
			return propertys.get(name);
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public void setProperty(String name, String value) {
			propertys.put(name, value);
		}
	}

	public int getTilewidth() {
		return tilewidth;
	}

	public int getTileheight() {
		return tileheight;
	}

	public File getImageFile() {
		return imageFile;
	}

	public Image getImage() {
		return i;
	}

	public String getName() {
		return name;
	}

	public String getTrans() {
		return trans;
	}

	public String getSpacing() {
		return spacing;
	}

	public String getMargin() {
		return margin;
	}

	public TileXML getTile(int x, int y) {
		return hashmap.get(new Point(x, y));
	}

	public void setTilewidth(int tilewidth) {
		this.tilewidth = tilewidth;
	}

	public void setTileheight(int tileheight) {
		this.tileheight = tileheight;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
		try {
			i = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public void setSpacing(String spacing) {
		this.spacing = spacing;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public File getZiel() {
		return getFile();
	}

	public void setZiel(File ziel) {
		setFile(ziel);
	}

	@Override
	public void read(Element rootElement) {
		int firstgid = 0;
		try {
			// Bei externem Tileset immer 0;
			firstgid = Integer.parseInt(rootElement.getAttribute("firstgid"));
		} catch (NumberFormatException e) {
		}
		// Tielset name
		String name = rootElement.getAttribute("name");
		int tileWidth = Integer.parseInt(rootElement.getAttribute("tilewidth"));
		int tileHeight = Integer.parseInt(rootElement
				.getAttribute("tileheight"));
		// FIXME Ungenutze Eigenschaften. Kann zu Rechenfehler bei id führen,
		// wird aber nur druchgeleitet.
		String spacing = rootElement.getAttribute("spacing"); // Abstand in
																// Tilesets
		if (spacing == "") {
			spacing = null;
		}
		String margin = rootElement.getAttribute("margin"); // Rand
		if (margin == "") {
			margin = null;
		}
		// Bild für Attribute
		Element imageNode = (Element) rootElement.getElementsByTagName("image")
				.item(0);
		// Für berechnung
		int width = Integer.parseInt(imageNode.getAttribute("width"));
		// new File(...)
		String fileRef = imageNode.getAttribute("source");
		// Transparenz in Hexal
		String trans = imageNode.getAttribute("trans");
		this.imageFile = new File(getFile().getParent() + "\\"+ fileRef);
		this.name = name;
		this.tilewidth = tileWidth;
		this.tileheight = tileHeight;
		try {
			i = ImageIO.read(imageFile);
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			e.printStackTrace();
		}
		this.trans = trans;
		this.spacing = spacing;
		this.margin = margin;
		NodeList tileNodes = rootElement.getElementsByTagName("tile");
		for (int i = 0; i < tileNodes.getLength(); i++) {
			// 1 Tile
			Element tileElement = (Element) tileNodes.item(i);

			int id = Integer.parseInt(tileElement.getAttribute("id"));
			id -= firstgid;
			int x = id % (width / tileWidth);
			int y = id / (width / tileWidth);
			TileXML tile = addTile(x, y);
			Element propsElement = (Element) tileElement.getElementsByTagName(
					"properties").item(0);
			NodeList properties = propsElement.getElementsByTagName("property");
			for (int p = 0; p < properties.getLength(); p++) {
				Element propElement = (Element) properties.item(p);

				String name1 = propElement.getAttribute("name");
				String value = propElement.getAttribute("value");
				if (name1.equals("cost")) {
					tile.setCost(Integer.parseInt(value));
				} else if (name1.equals("level")) {
					tile.setLevel(Integer.parseInt(value));
				} else {
					tile.setProperty(name1, value);
				}
			}

		}
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
		// Wenn Kind von TileMap 0 austauschen
		getElements(rootElement, xmlDoc, 0, getZiel());
	}

	@Override
	public String getXMLName() {
		return "tileset";
	}

	@Override
	public void fileDoesNotExist() {
		//FIXME
		JOptionPane.showMessageDialog(null,"File does not exist!");
	};
}
