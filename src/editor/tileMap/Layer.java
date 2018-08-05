package editor.tileMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;



/**
 * A layer of tiles on the map
 * 
 * @author kevin
 */
public class Layer {
	/** The code used to decode Base64 encoding */
	private static byte[] baseCodes = new byte[256];

	/**
	 * Static initialiser for the codes created against Base64
	 */
	static {
		for (int i = 0; i < 256; i++)
			baseCodes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			baseCodes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			baseCodes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			baseCodes[i] = (byte) (52 + i - '0');
		baseCodes['+'] = 62;
		baseCodes['/'] = 63;
		// for(byte c : baseCodes){
		// // System.out.println(Character.getName(c));
		// }
	}

	/** The map this layer belongs to */
	private final TiledMap map;
	/** The index of this layer */
	public int index;
	/** The name of this layer - read from the XML */
	public String name;
	/**
	 * The tile data representing this data, index 0 = tileset, index 1 = tile
	 * id
	 */
	public int[][][] data;
	/** The width of this layer */
	public int width;
	/** The height of this layer */
	public int height;
	public Layer(TiledMap map, int width, int height) {
		this.map = map;
		name = "unnamed";
		this.width = width;
		this.height = height;
		data = new int[width][height][3];
		for(int x = 0;width != x; x+=1){
			for(int y = 0;height != y; y+=1){
				
				data[x][y][0] = -1;
				data[x][y][1] = 0;
				data[x][y][2] = 0;
			
		}
		}
	}
	public Layer(TiledMap map, Layer l, boolean copy){
		this.map = map;
		name = "unnamed";
		width = l.width;
		height = l.height;
		if(copy){
			data = l.cloneData();
		}else{
			data = new int[width][height][3];
			for(int x = 0;width != x; x+=1){
				for(int y = 0;height != y; y+=1){
					
					data[x][y][0] = -1;
					data[x][y][1] = 0;
					data[x][y][2] = 0;
				
			}
			}
		}
	}
	/**
	 * Create a new layer based on the XML definition
	 * 
	 * @param element
	 *            The XML element describing the layer
	 * @param map
	 *            The map this layer is part of
	 * @throws SlickException
	 *             Indicates a failure to parse the XML layer
	 */	
	public Layer(TiledMap map, Element element){
		this.map = map;
		name = element.getAttribute("name");
		width = Integer.parseInt(element.getAttribute("width"));
		height = Integer.parseInt(element.getAttribute("height"));
		data = new int[width][height][3];

		// now read the layer properties
//		Element propsElement = (Element) element.getElementsByTagName(
//				"properties").item(0);
//		if (propsElement != null) {
//			NodeList properties = propsElement.getElementsByTagName("property");
//			if (properties != null) {
//				props = new Properties();
//				for (int p = 0; p < properties.getLength(); p++) {
//					Element propElement = (Element) properties.item(p);
//
//					String name = propElement.getAttribute("name");
//					String value = propElement.getAttribute("value");
//					props.setProperty(name, value);
//				}
//			}
//		}

		Element dataNode = (Element) element.getElementsByTagName("data").item(
				0);
		String encoding = dataNode.getAttribute("encoding");
		String compression = dataNode.getAttribute("compression");
		if(encoding.equals("base64") && compression.equals("none")){
			Node cdata = dataNode.getFirstChild();
			char[] c = cdata.getTextContent().toCharArray();
			byte[] dec = decodeBase64(c);
			ByteArrayInputStream is = new ByteArrayInputStream(dec);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int tileId = 0;
					tileId |= is.read();
					tileId |= is.read() << 8;
					tileId |= is.read() << 16;
					tileId |= is.read() << 24;
					if(tileId==0){
						data[x][y][0] = -1;
						data[x][y][1] = 0;
						data[x][y][2] = 0;
					}else{
					
					TileSet set = map.findTileSet(tileId);
					data[x][y][0] = (int)set.index;
					data[x][y][1] = (int)(tileId - set.firstGID);
					data[x][y][2] = (int) tileId;
					}
				}
			}
			
		}else if (encoding.equals("base64") && compression.equals("gzip")) {
			try {
				Node cdata = dataNode.getFirstChild();
				char[] enc = cdata.getNodeValue().trim().toCharArray();
				byte[] dec = decodeBase64(enc);
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

						if (tileId == 0) {
							data[x][y][0] = -1;
							data[x][y][1] = 0;
							data[x][y][2] = 0;
						} else {
							TileSet set = map.findTileSet(tileId);

							if (set != null) {
								data[x][y][0] = (int)set.index;
								data[x][y][1] = (int)(tileId - set.firstGID);
							}
							data[x][y][2] = (int)tileId;
						}
					}
				}
			} catch (IOException e) {
//				Log.error(e);
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
	
	/**
	 * Get the gloal ID of the tile at the specified location in this layer
	 * 
	 * @param x
	 *            The x coorindate of the tile
	 * @param y
	 *            The y coorindate of the tile
	 * @return The global ID of the tile
	 */
	public int getTileID(int x, int y) {
		return data[x][y][2];
	}
	public int getTileID(int x, int y, boolean doNotThrow) {
		if(x < 0 || y < 0||x >= data.length||y >= data[x].length){
			return -1;
		}
		return getTileID(x, y);
	}

	/**
	 * Set the global tile ID at a specified location
	 * 
	 * @param x
	 *            The x location to set
	 * @param y
	 *            The y location to set
	 * @param tile
	 *            The tile value to set
	 */
	public void setTileID(int x, int y, int tile,int setID) {
		if(x<0 || y <0)return;
		if(x>=width || y>=height)return;
		if (tile == 0) {
			data[x][y][0] = -1;
			data[x][y][1] = 0;
			data[x][y][2] = 0;
		} else {
			TileSet set = map.getTileSet(setID);

			data[x][y][0] =  (int)setID;
			data[x][y][1] =  (int)(tile - set.firstGID);
			data[x][y][2] =  (int)tile;
		}
	}

	/**
	 * Decode a Base64 string as encoded by TilED
	 * 
	 * @param data
	 *            The string of character to decode
	 * @return The byte array represented by character encoding
	 */
	public static byte[] decodeBase64(char[] data) {
		int temp = data.length;
		for (int ix = 0; ix < data.length; ix++) {
			if ((data[ix] > 255) || baseCodes[data[ix]] < 0) {
				--temp;
			}
		}

		int len = (temp / 4) * 3;
		if ((temp % 4) == 3)
			len += 2;
		if ((temp % 4) == 2)
			len += 1;

		byte[] out = new byte[len];

		int shift = 0;
		int accum = 0;
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = (data[ix] > 255) ? -1 : baseCodes[data[ix]];

			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}

		if (index != out.length) {
			throw new RuntimeException(
					"Data length appears to be wrong (wrote " + index
							+ " should be " + out.length + ")");
		}

		return out;
	}
	public void save(Element rootElement, Document xmlDoc,boolean zip) {
		Element lay = xmlDoc.createElement("layer");
		lay.setAttribute("name", name);
		lay.setAttribute("width", width+"");
		lay.setAttribute("height", height+"");
		rootElement.appendChild(lay);
		Element dataE = xmlDoc.createElement("data");
		dataE.setAttribute("encoding", "base64");
		
		byte[] c = new byte[width*height*4];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if(data[x][y][0] == -1){
					c[(x+y*width)*4] = 0;	
					c[(x+y*width)*4+1] = 0;	
					c[(x+y*width)*4+2] = 0;	
					c[(x+y*width)*4+3] = 0;
				}else{
					int id = (int) (data[x][y][1]+map.getTileSet(data[x][y][0]).firstGID);
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
		if(zip){
			GZIPOutputStream GZIPstream;
			try {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
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
		}else{
			dataE.setTextContent(javax.xml.bind.DatatypeConverter.printBase64Binary(c));
			dataE.setAttribute("compression", "none");
		}
		lay.appendChild(dataE);
		
//		 <layer name="Böden" width="30" height="30">
//		  <data encoding="base64" compression="gzip">
//		   H4sIAAAAAAAAC+3SPQqAMAyG4VwhXsLZyTP6dwD1sjbUQUSwCSUZ+g3vVMJDSZiIuMGm1OzU8nC9zNZd7/12d4OyKFfaglzrbmq6a+oo7Kzoyh9GRWw0v1zNLNz/Nw52d8r32VO+Ua1rSWbfN2rdl8dNwIULFy7csi48ctlVEA4AAA==
//		  </data>
//		 </layer>
	}
	public int[][][] cloneData(){
		int[][][] i = new int[width][height][3];
		for(int x = 0; x != width; x+=1){
			for(int y = 0; y != height; y+=1){
				i[x][y][0] = data[x][y][0];
				i[x][y][1] = data[x][y][1];
				i[x][y][2] = data[x][y][2];
				
			}
		}
		return i;
	}
	public void resetData(int[][][]i){
		for(int x = 0; x != width; x+=1){
			for(int y = 0; y != height; y+=1){
				data[x][y][0] = i[x][y][0];
				data[x][y][1] = i[x][y][1];
				data[x][y][2] = i[x][y][2];
				
			}
		}
	}
	public void setName(String s) {
		name = s;
	}
	public String getName(){
		return name;
	}
	
}