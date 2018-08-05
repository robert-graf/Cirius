package de.game.TiledMap;
import org.newdawn.slick.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;




//import org.newdawn.slick.SpriteSheet;

/**
 * A holder for tileset information
 * 
 * @author kevin
 */
public class TileSet {
	/**name of the image*/
	String ref;
	/** The index of the tile set */
	public int index;
	/** The name of the tile set */
	public String name;
	/** The first global tile id in the set */
	public int firstGID;
	/** The local global tile id in the set */
	public int lastGID = Integer.MAX_VALUE;
	/** The width of the tiles */
	public int tileWidth;
	/** The height of the tiles */
	public int tileHeight;
	/** The image containing the tiles */
	public SpriteSheet tiles;

	/** The number of tiles across the sprite sheet */
	public int tilesAcross;
	/** The number of tiles down the sprite sheet */
	public int tilesDown;

	/** The properties for each tile */
	private HashMap<Integer, Properties> props = new HashMap<>();
	/** The padding of the tiles */
	protected int tileSpacing = 0;
	/** The margin of the tileset */
	protected int tileMargin = 0;
	
	protected short[][] Cliping;
	/**
	 * Create a tile set based on an XML definition
	 * 
	 * @param element
	 *            The XML describing the tileset
	 * @param map
	 *            The map this tileset was loaded from (gives context to paths)
	 * @param loadImage
	 *            True if the images should be loaded, false if we're running
	 *            somewhere images can't be loaded
	 * @throws SlickException
	 *             Indicates a failure to parse the tileset
	 */
	public TileSet(File f, TiledMap map) {
		firstGID = map.getTileSet(map.getTileSetCount()-1).lastGID+1;
		String source = f.getPath();
		try {
			init(source, firstGID, null);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	File f;
	public TileSet(Element element)
			throws SlickException {
		firstGID = Integer.parseInt(element.getAttribute("firstgid"));
		String source = element.getAttribute("source");
		init(source, firstGID,element);
	}
	public TileSet(File f2) {
		try {
			init(f2.getPath(), 0, null);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TileSet(File f, File imagefile, int w,int h) throws SlickException {
		this.f = f;
		name = f.getName().replace(".tsx", "");
		tileWidth = w;
		tileHeight = h;
		//TODO
		Image image = null;
		image = new Image(ref);
//			image =		ImageIO.read(new File(ref));
		Cliping = new short[image.getWidth()/tileWidth][image.getHeight()/tileHeight];
		defaultValueOfClipping();
		// ();
		setTileSetImage(image);
	}
	private void init(String source, int firstGID,Element element) throws SlickException {
		f = new File(source);
		if ((source != null) && (!source.equals(""))) {
			try {
				InputStream in = ResourceLoader.getResourceAsStream(source);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(in);
				Element docElement = doc.getDocumentElement();
				element = docElement; // (Element)
										// docElement.getElementsByTagName("tileset").item(0);
				name = element.getAttribute("name");
			} catch (Exception e) {
				Log.error(e);
				throw new SlickException(
						"Unable to load or parse sourced tileset: "
								+ source);
			}
		}
		String tileWidthString = element.getAttribute("tilewidth");
		String tileHeightString = element.getAttribute("tileheight");
		if (tileWidthString.length() == 0 || tileHeightString.length() == 0) {
			throw new SlickException(
					"TiledMap requires that the map be created with tilesets that use a "
							+ "single image.  Check the WiKi for more complete information.");
		}
		tileWidth = Integer.parseInt(tileWidthString);
		tileHeight = Integer.parseInt(tileHeightString);

		String sv = element.getAttribute("spacing");
		if ((sv != null) && (!sv.equals(""))) {
			tileSpacing = Integer.parseInt(sv);
		}

		String mv = element.getAttribute("margin");
		if ((mv != null) && (!mv.equals(""))) {
			tileMargin = Integer.parseInt(mv);
		}

		NodeList list = element.getElementsByTagName("image");
		Element imageNode = (Element) list.item(0);
		ref = imageNode.getAttribute("source");
		// TODO Unsichtbarkeit wird nicht unterstützt!!!
		// Color trans = null;
		// String t = imageNode.getAttribute("trans");
		// if ((t != null) && (t.length() > 0)) {
		// int c = Integer.parseInt(t, 16);
		//
		// trans = new Color(c);
		// }
		//TODO
			Image image = null;
			image = new Image(ref);
//				image =		ImageIO.read(new File(ref));
			Cliping = new short[image.getWidth()/tileWidth][image.getHeight()/tileHeight];
			defaultValueOfClipping();
			// ();
			setTileSetImage(image);
			
		//Clipping
			list = element.getElementsByTagName("clipping");
			if(list.getLength() != 0){
			Element dataNode = (Element) list.item(0);
			String encoding = dataNode.getAttribute("encoding");
			String compression = dataNode.getAttribute("compression");
			if (encoding.equals("base64") && compression.equals("gzip")) {
				try {
					int height = new Integer(imageNode.getAttribute("height"))/tileHeight;
					int width = new Integer(imageNode.getAttribute("width"))/tileWidth;
					
					Node cdata = dataNode.getFirstChild();
					char[] enc = cdata.getNodeValue().trim().toCharArray();
					byte[] dec = Layer.decodeBase64(enc);
					GZIPInputStream gz = new GZIPInputStream(
							new ByteArrayInputStream(dec));

					byte[] buf = new byte[height * width * 4];
					gz.read(buf);

					ByteArrayInputStream is = new ByteArrayInputStream(buf);

					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							int tileId = 0;
							tileId |= is.read();
							tileId |= is.read() << 8;
							tileId |= is.read() << 16;
							tileId |= is.read() << 24;
							if(x < Cliping.length && y < Cliping[x].length){
								Cliping[x][y] = (short) tileId;
							}
						}
					}
				} catch (IOException e) {
//					Log.error(e);
					try {
						throw new Exception("Unable to decode base 64 block");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else {
				try {
					throw new Exception("Unsupport tiled map type: " + encoding
							+ "," + compression + " (only gzip base64 supported)");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
//tile settings unused!

		NodeList pElements = element.getElementsByTagName("tile");
		for (int i = 0; i < pElements.getLength(); i++) {
			Element tileElement = (Element) pElements.item(i);

			int id = Integer.parseInt(tileElement.getAttribute("id"));
			id += firstGID;
			Properties tileProps = new Properties();

			Element propsElement = (Element) tileElement.getElementsByTagName(
					"properties").item(0);
			NodeList properties = propsElement.getElementsByTagName("property");
			for (int p = 0; p < properties.getLength(); p++) {
				Element propElement = (Element) properties.item(p);

				String name = propElement.getAttribute("name");
				String value = propElement.getAttribute("value");

				tileProps.setProperty(name, value);
			}

			props.put(new Integer(id), tileProps);
		}
	}

	

	/**
	 * Get the width of each tile in this set
	 * 
	 * @return The width of each tile in this set
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Get the height of each tile in this set
	 * 
	 * @return The height of each tile in this set
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Get the spacing between tiles in this set
	 * 
	 * @return The spacing between tiles in this set
	 */
	public int getTileSpacing() {
		return tileSpacing;
	}

	/**
	 * Get the margin around tiles in this set
	 * 
	 * @return The maring around tiles in this set
	 */
	public int getTileMargin() {
		return tileMargin;
	}

	/**
	 * Set the image to use for this sprite sheet image to use for this tileset
	 * 
	 * @param image
	 *            The image to use for this tileset
	 */
	public void setTileSetImage(org.newdawn.slick.Image image) {
		tiles = new SpriteSheet(image, tileWidth, tileHeight, tileSpacing,
				tileMargin);
		tilesAcross = tiles.getHorizontalCount();
		tilesDown = tiles.getVerticalCount();

		if (tilesAcross <= 0) {
			tilesAcross = 1;
		}
		if (tilesDown <= 0) {
			tilesDown = 1;
		}

		lastGID = (tilesAcross * tilesDown) + firstGID - 1;
	}
	public int getHorizontalCount(){
		return tiles.getHorizontalCount();
	}
	public int getVerticalCount(){
		return tiles.getVerticalCount();
	}
	public org.newdawn.slick.Image getImage(int x,int y){
		return tiles.getSubImage(x, y);
	}
	/**
	 * Get the properties for a specific tile in this tileset
	 * 
	 * @param globalID
	 *            The global ID of the tile whose properties should be retrieved
	 * @return The properties for the specified tile, or null if no properties
	 *         are defined
	 */
	public Properties getProperties(int globalID) {
		return (Properties) props.get(new Integer(globalID));
	}

	/**
	 * Get the x position of a tile on this sheet
	 * 
	 * @param id
	 *            The tileset specific ID (i.e. not the global one)
	 * @return The index of the tile on the x-axis
	 */
	public int getTileX(int id) {
		return id % tilesAcross;
	}

	/**
	 * Get the y position of a tile on this sheet
	 * 
	 * @param id
	 *            The tileset specific ID (i.e. not the global one)
	 * @return The index of the tile on the y-axis
	 */
	public int getTileY(int id) {
		return id / tilesAcross;
	}

	/**
	 * Set the limit of the tiles in this set
	 * 
	 * @param limit
	 *            The limit of the tiles in this set
	 */
	public void setLimit(int limit) {
		lastGID = limit;
	}

	/**
	 * Check if this tileset contains a particular tile
	 * 
	 * @param gid
	 *            The global id to seach for
	 * @return True if the ID is contained in this tileset
	 */
	public boolean contains(int gid) {
		return (gid >= firstGID) && (gid <= lastGID);
	}
	@Override
	public String toString() {
		return name;
	}



	public File getFile() {
		return f;
	}
	public static String fileEnding() {
		return ".xts";
	}
	public SpriteSheet getImage() {
		return tiles;
	}
	public short getTileCliping(int x, int y) {
		return Cliping[x][y];
	}
	public short getTileCliping(int id) {
		int pos = id-firstGID;
		return getTileCliping(pos%Cliping.length, pos/Cliping.length);
	}
	
	
	
	
	
	public void setTileCliping(int x, int y, short cont) {
		Cliping[x][y]=cont;
	}
	@SuppressWarnings("deprecation")
	public void write(File f2) {
		try{
		/** START */
		// DocumenBuilderFactory
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		// DocumentBuilder
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// Document
		Document xmlDoc = docBuilder.newDocument();
		// TileSet bekommen
		Element rootElement = xmlDoc.createElement("tileset");
		/** CUSTOM */
		rootElement.setAttribute("name", name);
		rootElement.setAttribute("tilewidth",getTileWidth()+"");
		rootElement.setAttribute("tileheight",getTileHeight()+"");
		if(tileSpacing != 0){
			rootElement.setAttribute("spacing",tileSpacing+"");	
		}
		if(tileMargin != 0){
		rootElement.setAttribute("margin",tileMargin+"");
		}
		Element image = xmlDoc.createElement("image");
		image.setAttribute("source", ref);
		image.setAttribute("width", getImage().getWidth()+"");
		image.setAttribute("height", getImage().getHeight()+"");
		rootElement.appendChild(image);
		Element dataE = xmlDoc.createElement("clipping");
		dataE.setAttribute("encoding", "base64");
		dataE.setAttribute("compression", "gzip");
		dataE.setAttribute("layer", "default");
		int width = Cliping.length;
		int height = Cliping[0].length;
		byte[] c = new byte[width*height*4];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if(Cliping[x][y] == -1){
					c[(x+y*width)*4] = 0;	
					c[(x+y*width)*4+1] = 0;	
					c[(x+y*width)*4+2] = 0;	
					c[(x+y*width)*4+3] = 0;
				}else{
					int id = Cliping[x][y];
					byte b1 = (byte)id;
					byte b2 = (byte)(id >> 8);
					byte b3 = (byte)(id >> 16);
					byte b4 = (byte)(id >> 24);
					c[(x+y*width)*4] = b1;	
					c[(x+y*width)*4+1] = b2;	
					c[(x+y*width)*4+2] = b3;	
					c[(x+y*width)*4+3] = b4;	
				}	
			}
		}
			GZIPOutputStream GZIPstream;
			try {
				ByteOutputStream output = new ByteOutputStream();
				GZIPstream = new GZIPOutputStream(output);
				GZIPstream.write(c);
				output.flush();
				GZIPstream.close();
				output.close();
				dataE.setTextContent(javax.xml.bind.DatatypeConverter.printBase64Binary(output.toByteArray()));
			dataE.setAttribute("compression", "gzip");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		rootElement.appendChild(dataE);
// TODO Unsichtbarkeit wird nicht unterstützt!!!
// Color trans = null;
// String t = imageNode.getAttribute("trans");
// if ((t != null) && (t.length() > 0)) {
// int c = Integer.parseInt(t, 16);
//
// trans = new Color(c);
// }
//TODO eigenschaften werden nicht gespeichert / verwendet
//		NodeList pElements = element.getElementsByTagName("tile");
//		for (int i = 0; i < pElements.getLength(); i++) {
//			Element tileElement = (Element) pElements.item(i);
//
//			int id = Integer.parseInt(tileElement.getAttribute("id"));
//			id += firstGID;
//			Properties tileProps = new Properties();
//
//			Element propsElement = (Element) tileElement.getElementsByTagName(
//					"properties").item(0);
//			NodeList properties = propsElement.getElementsByTagName("property");
//			for (int p = 0; p < properties.getLength(); p++) {
//				Element propElement = (Element) properties.item(p);
//
//				String name = propElement.getAttribute("name");
//				String value = propElement.getAttribute("value");
//
//				tileProps.setProperty(name, value);
//			}
//
//			props.put(new Integer(id), tileProps);
//		}
		/** END */
		// Document anhängen
		xmlDoc.appendChild(rootElement);
		// Set OutputFormen
		OutputFormat out = new OutputFormat(xmlDoc);
		out.setIndenting(true);
		// Declare the file
		File xmlFile = f;
		// Declari the FileOutputStream
		FileOutputStream outStream = new FileOutputStream(xmlFile);
		// XMLSerializer
		XMLSerializer serializer = new XMLSerializer(outStream, out);
		// the secified Output Format
		serializer.serialize(xmlDoc);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void defaultValueOfClipping(){
		for(int x = 0; x != Cliping.length;x++){
			for(int y = 0; y != Cliping[x].length;y++){
				Cliping[x][y] = 2560;
			}
		}
	}
}