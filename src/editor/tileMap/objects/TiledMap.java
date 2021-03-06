package editor.tileMap.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import database.TiledMapXML;
import editor.tileMap.Layer;
import editor.tileMap.TileSetPanel;
import gui.AAufbau;


/**
 * This class is intended to parse TilED maps. TilED is a generic tool for tile
 * map editing and can be found at:
 * 
 * http://mapeditor.org/
 * 
 * @author kevin
 */
public class TiledMap {

	/** The width of the map */
	protected int width;
	/** The height of the map */
	protected int height;
	/** The width of the tiles used on the map */
	protected int tileWidth;
	/** The height of the tiles used on the map */
	protected int tileHeight;


	/** the properties of the map */
	protected Properties props;

	/** The list of tilesets defined in the map */
	public ArrayList<TileSet> tileSets = new ArrayList<>();
	/** The list of layers defined in the map */
	public ArrayList<Layer> layers = new ArrayList<>();
	/** The list of object-groups defined in the map */
	protected ArrayList<Object> objectGroups = new ArrayList<>();
	/** Pre buffered Images*/
	public BufferedImage [] images;

	public TiledMap(File f){
		load(f);
	}
	public TiledMap(File f,SecretAccess[] s){
		load(f);
		s[0] = new SecretAccess();
	}

	public String getLayerName(int index) {
		return layers.get(index).name;
	}

	/**
	 * Get the index of the layer with given name
	 * 
	 * @param name
	 *            The name of the tile to search for
	 * @return The index of the layer or -1 if there is no layer with given name
	 */
	public int getLayerIndex(String name) {
		// int idx = 0;

		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);

			if (layer.name.equals(name)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Gets the Image used to draw the tile at the given x and y coordinates.
	 * 
	 * @param x
	 *            The x coordinate of the tile whose image should be retrieved
	 * @param y
	 *            The y coordinate of the tile whose image should be retrieved
	 * @param layerIndex
	 *            The index of the layer on which the tile whose image should be
	 *            retrieve exists
	 * @return The image used to draw the specified tile or null if there is no
	 *         image for the specified tile.
	 */
	public Image getTileImage(int x, int y, int layerIndex) {
		Layer layer = layers.get(layerIndex);
		if(layer.data.length <= x){
			return null;
		}
		if(layer.data[x].length <= y){
			return null;
		}
		if(layer.data[x][y][1] == 0){
			return null;
		}
		int tileSetIndex = layer.data[x][y][0];
		if ((tileSetIndex >= 0) && (tileSetIndex < tileSets.size())) {
			TileSet tileSet = tileSets.get(tileSetIndex);

			int sheetX = tileSet.getTileX(layer.data[x][y][1]);
			int sheetY = tileSet.getTileY(layer.data[x][y][1]);

			return tileSet.tiles.getSprite(sheetX, sheetY);
		}

		return null;
	}

	/**
	 * Get the width of the map
	 * 
	 * @return The width of the map (in tiles)
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of the map
	 * 
	 * @return The height of the map (in tiles)
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get the height of a single tile
	 * 
	 * @return The height of a single tile (in pixels)
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Get the width of a single tile
	 * 
	 * @return The height of a single tile (in pixels)
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Get the global ID of a tile at specified location in the map
	 * 
	 * @param x
	 *            The x location of the tile
	 * @param y
	 *            The y location of the tile
	 * @param layerIndex
	 *            The index of the layer to retireve the tile from
	 * @return The global ID of the tile
	 */
	public int getTileId(int x, int y, int layerIndex) {
		Layer layer = layers.get(layerIndex);
		return layer.getTileID(x, y);
	}

	/**
	 * Set the global ID of a tile at specified location in the map
	 * 
	 * @param x
	 *            The x location of the tile
	 * @param y
	 *            The y location of the tile
	 * @param layerIndex
	 *            The index of the layer to set the new tileid
	 * @param tileid
	 *            The tileid to be set
	 */
	public void setTileId(int x, int y, int layerIndex, int tileid,int setID) {
		Layer layer = layers.get(layerIndex);
		layer.setTileID(x, y, tileid, setID);
	}

	/**
	 * Get a property given to the map. Note that this method will not perform
	 * well and should not be used as part of the default code path in the game
	 * loop.
	 * 
	 * @param propertyName
	 *            The name of the property of the map to retrieve
	 * @param def
	 *            The default value to return
	 * @return The value assigned to the property on the map (or the default
	 *         value if none is supplied)
	 */
	public String getMapProperty(String propertyName, String def) {
		if (props == null)
			return def;
		return props.getProperty(propertyName, def);
	}

	/**
	 * Get a property given to a particular layer. Note that this method will
	 * not perform well and should not be used as part of the default code path
	 * in the game loop.
	 * 
	 * @param layerIndex
	 *            The index of the layer to retrieve
	 * @param propertyName
	 *            The name of the property of this layer to retrieve
	 * @param def
	 *            The default value to return
	 * @return The value assigned to the property on the layer (or the default
	 *         value if none is supplied)
	 */
//	public String getLayerProperty(int layerIndex, String propertyName,
//			String def) {
//		Layer layer = (Layer) layers.get(layerIndex);
//		if (layer == null || layer.props == null)
//			return def;
//		return layer.props.getProperty(propertyName, def);
//	}

	/**
	 * Retrieve a count of the number of layers available
	 * 
	 * @return The number of layers available in this map
	 */
	public int getLayerCount() {
		return layers.size();
	}

	/**
	 * Load a TilED map
	 * 
	 * @param in
	 *            The input stream from which to load the map
	 * @param tileSetsLocation
	 *            The location from which we can retrieve tileset images
	 * @throws SlickException
	 *             Indicates a failure to parse the map or find a tileset
	 */
	TiledMapXML xml;
	private void load(File f) {
		if(f != null && f.exists()){
			xml = new TiledMapXML(this, new SecretAccess());
			xml.read(f);
			buffer();
		}else{
			SecretAccess access = new SecretAccess();
			access.setWidth(16);
			access.setHeight(16);
			access.setTileWidth(16);
			access.setTileHeight(16);
			Layer layer = new Layer(this,16,16);
			addLayer(layer);
			xml = new TiledMapXML(this,access);
			xml.setFile(f);
			buffer();
			
		}
		
		
		
	}

	/**Creates a Buffer image of every Layer, to prevent Lagging (not needed in OPGL but in defauft java drawings)*/
	public void buffer() {
		images = new BufferedImage[layers.size()];
		for(int i = 0; i != layers.size(); i+=1){
			BufferedImage image = new BufferedImage(getWidth()*getTileHeight(), getHeight()*getTileWidth(), BufferedImage.TYPE_4BYTE_ABGR);
			draw(image.getGraphics(), i);
			images[i] = image;
		}
	}
	/**
	 * Retrieve the number of tilesets available in this map
	 * 
	 * @return The number of tilesets available in this map
	 */
	public int getTileSetCount() {
		return tileSets.size();
	}

	/**
	 * Get a tileset at a particular index in the list of sets for this map
	 * 
	 * @param index
	 *            The index of the tileset.
	 * @return The TileSet requested
	 */
	public TileSet getTileSet(int index) {
		return tileSets.get(index);
	}

	/**
	 * Get a tileset by a given global ID
	 * 
	 * @param gid
	 *            The global ID of the tileset to retrieve
	 * @return The tileset requested or null if no tileset matches
	 */
	public TileSet getTileSetByGID(int gid) {
		for (int i = 0; i < tileSets.size(); i++) {
			TileSet set = tileSets.get(i);

			if (set.contains(gid)) {
				return set;
			}
		}

		return null;
	}

	/**
	 * Find a tile for a given global tile id
	 * 
	 * @param gid
	 *            The global tile id we're looking for
	 * @return The tileset in which that tile lives or null if the gid is not
	 *         defined
	 */
	public TileSet findTileSet(int gid) {
		for (int i = 0; i < tileSets.size(); i++) {
			TileSet set = tileSets.get(i);

			if (set.contains(gid)) {
				return set;
			}
		}

		return null;
	}

	/**
	 * Overrideable to allow other sprites to be rendered between lines of the
	 * map
	 * 
	 * @param visualY
	 *            The visual Y coordinate, i.e. 0->height
	 * @param mapY
	 *            The map Y coordinate, i.e. y->y+height
	 * @param layer
	 *            The layer being rendered
	 */
	protected void renderedLine(int visualY, int mapY, int layer) {
	}

	/**
	 * Returns the number of object-groups defined in the map.
	 * 
	 * @return Number of object-groups on the map
	 */
	public int getObjectGroupCount() {
		return objectGroups.size();
	}

	/**
	 * Returns the number of objects of a specific object-group.
	 * 
	 * @param groupID
	 *            The index of this object-group
	 * @return Number of the objects in the object-group or -1, when error
	 *         occurred.
	 */
	public int getObjectCount(int groupID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			return grp.objects.size();
		}
		return -1;
	}

	/**
	 * Return the name of a specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The name of an object or null, when error occurred
	 */
	public String getObjectName(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.name;
			}
		}
		return null;
	}

	/**
	 * Return the type of an specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The type of an object or null, when error occurred
	 */
	public String getObjectType(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.type;
			}
		}
		return null;
	}

	/**
	 * Returns the x-coordinate of a specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The x-coordinate of an object, or -1, when error occurred
	 */
	public int getObjectX(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.x;
			}
		}
		return -1;
	}

	/**
	 * Returns the y-coordinate of a specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The y-coordinate of an object, or -1, when error occurred
	 */
	public int getObjectY(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.y;
			}
		}
		return -1;
	}

	/**
	 * Returns the width of a specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The width of an object, or -1, when error occurred
	 */
	public int getObjectWidth(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.width;
			}
		}
		return -1;
	}

	/**
	 * Returns the height of a specific object from a specific group.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The height of an object, or -1, when error occurred
	 */
	public int getObjectHeight(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);
				return object.height;
			}
		}
		return -1;
	}

	/**
	 * Retrieve the image source property for a given object
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @return The image source reference or null if one isn't defined
	 */
	public String getObjectImage(int groupID, int objectID) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);

				if (object == null) {
					return null;
				}

				return object.image;
			}
		}

		return null;
	}

	/**
	 * Looks for a property with the given name and returns it's value. If no
	 * property is found, def is returned.
	 * 
	 * @param groupID
	 *            Index of a group
	 * @param objectID
	 *            Index of an object
	 * @param propertyName
	 *            Name of a property
	 * @param def
	 *            default value to return, if no property is found
	 * @return The value of the property with the given name or def, if there is
	 *         no property with that name.
	 */
	public String getObjectProperty(int groupID, int objectID,
			String propertyName, String def) {
		if (groupID >= 0 && groupID < objectGroups.size()) {
			ObjectGroup grp = (ObjectGroup) objectGroups.get(groupID);
			if (objectID >= 0 && objectID < grp.objects.size()) {
				GroupObject object = (GroupObject) grp.objects.get(objectID);

				if (object == null) {
					return def;
				}
				if (object.props == null) {
					return def;
				}

				return object.props.getProperty(propertyName, def);
			}
		}
		return def;
	}

	/**
	 * A group of objects on the map (objects layer)
	 * 
	 * @author kulpae
	 */
	protected class ObjectGroup {
		/** The index of this group */
		public int index;
		/** The name of this group - read from the XML */
		public String name;
		/** The Objects of this group */
		public ArrayList<GroupObject> objects;
		/** The width of this layer */
		public int width;
		/** The height of this layer */
		public int height;

		/** the properties of this group */
		public Properties props;

		/**
		 * Create a new group based on the XML definition
		 * 
		 * @param element
		 *            The XML element describing the layer
		 * @throws SlickException
		 *             Indicates a failure to parse the XML group
		 */
		public ObjectGroup(Element element){
			name = element.getAttribute("name");
			width = Integer.parseInt(element.getAttribute("width"));
			height = Integer.parseInt(element.getAttribute("height"));
			objects = new ArrayList<>();

			// now read the layer properties
			Element propsElement = (Element) element.getElementsByTagName(
					"properties").item(0);
			if (propsElement != null) {
				NodeList properties = propsElement
						.getElementsByTagName("property");
				if (properties != null) {
					props = new Properties();
					for (int p = 0; p < properties.getLength(); p++) {
						Element propElement = (Element) properties.item(p);

						String name = propElement.getAttribute("name");
						String value = propElement.getAttribute("value");
						props.setProperty(name, value);
					}
				}
			}

			NodeList objectNodes = element.getElementsByTagName("object");
			for (int i = 0; i < objectNodes.getLength(); i++) {
				Element objElement = (Element) objectNodes.item(i);
				GroupObject object = new GroupObject(objElement);
				object.index = i;
				objects.add(object);
			}
		}
	}

	/**
	 * An object from a object-group on the map
	 * 
	 * @author kulpae
	 */
	protected class GroupObject {
		/** The index of this object */
		public int index;
		/** The name of this object - read from the XML */
		public String name;
		/** The type of this object - read from the XML */
		public String type;
		/** The x-coordinate of this object */
		public int x;
		/** The y-coordinate of this object */
		public int y;
		/** The width of this object */
		public int width;
		/** The height of this object */
		public int height;
		/** The image source */
		private String image;

		/** the properties of this group */
		public Properties props;

		/**
		 * Create a new group based on the XML definition
		 * 
		 * @param element
		 *            The XML element describing the layer
		 * @throws SlickException
		 *             Indicates a failure to parse the XML group
		 */
		public GroupObject(Element element){
			name = element.getAttribute("name");
			type = element.getAttribute("type");
			x = Integer.parseInt(element.getAttribute("x"));
			y = Integer.parseInt(element.getAttribute("y"));
			width = Integer.parseInt(element.getAttribute("width"));
			height = Integer.parseInt(element.getAttribute("height"));

			Element imageElement = (Element) element.getElementsByTagName(
					"image").item(0);
			if (imageElement != null) {
				image = imageElement.getAttribute("source");
			}

			// now read the layer properties
			Element propsElement = (Element) element.getElementsByTagName(
					"properties").item(0);
			if (propsElement != null) {
				NodeList properties = propsElement
						.getElementsByTagName("property");
				if (properties != null) {
					props = new Properties();
					for (int p = 0; p < properties.getLength(); p++) {
						Element propElement = (Element) properties.item(p);

						String name = propElement.getAttribute("name");
						String value = propElement.getAttribute("value");
						props.setProperty(name, value);
					}
				}
			}
		}
	}
	public void draw(Graphics2D g1, int px, int py, int LayerIndex) {
		g1.drawImage(images[LayerIndex],px,py,null);

	}
	@Deprecated
	public void draw(Graphics2D g1, int px, int py, int LayerIndex,
			Rectangle sichtfeld, int Raster) {
//		for (int x = 0; x != getWidth(); x++) {
//			for (int y = 0; y != getHeight(); y++) {
//				Image i = getTileImage(x, y, LayerIndex);
//				if (i != null
////						&& sichtfeld.contains((x) * (getTileWidth() + Raster)
////								+ px, (y) * (getTileHeight() + Raster) + py)
//								) {
//					g1.drawImage(i, px + (x) * (getTileWidth() + Raster), py
//							+ (y) * (getTileHeight() + Raster), null);
//				}
//			}
//		}
		g1.drawImage(images[LayerIndex],px,py,null);
	}
	private void draw(Graphics g1, int LayerIndex) {
		draw(g1, LayerIndex,0);
	}
	private void draw(Graphics g1, int LayerIndex,int Raster) {
		for (int x = 0; x != getWidth(); x++) {
			for (int y = 0; y != getHeight(); y++) {
				Image i = getTileImage(x, y, LayerIndex);
				if (i != null
//						&& sichtfeld.contains((x) * (getTileWidth() + Raster)
//								+ px, (y) * (getTileHeight() + Raster) + py)
								) {
					g1.drawImage(i, (x) * (getTileWidth() + Raster),(y) * (getTileHeight() + Raster), null);
				}
			}
		}
	}
	
	public class SecretAccess{
		public void setWidth(int w) {
			width = w;
		}

		public void setHeight(int h) {
			height = h;
		}

		public void setTileWidth(int tw) {
			tileWidth = tw;
		}

		public void setTileHeight(int th) {
			tileHeight = th;
		}

		public ArrayList<Layer> getLayers() {
			return layers;
		}

		public void changeSize(int w, int h) {
			setHeight(h);
			setWidth(w);
			for(Layer l : layers){
				int data[][][] = l.data;
				l.data = new int[w][h][3];
				
				for(int x = 0; x != w && x != l.width;x+= 1){
					for(int y = 0; y != h && y != l.height;y+= 1){
						for(int z = 0; z != 3; z += 1){
							l.data[x][y][z] = data[x][y][z];
						}
					}
				}
				for(int x = l.width; x < w;x+= 1){
					for(int y = 0; y < h ;y+= 1){
						l.data[x][y][0] = -1;
						l.data[x][y][1] = 0;
						l.data[x][y][2] = 0;
					}
				}
				for(int x = 0; x < w;x+= 1){
					for(int y = l.height; y < h ;y+= 1){
						l.data[x][y][0] = -1;
						l.data[x][y][1] = 0;
						l.data[x][y][2] = 0;
					}
				}
				l.height = h;
				l.width = w;
			}
			
		}
	}

	public void addtileSet(TileSet tileSet) {
		
		tileSets.add(tileSet);
		
	}

	public void addLayer(Layer layer) {
		layers.add(layer);
		buffer();
	}
	public void addLayer(Layer l, int index) {
		layers.add(index, l);
		buffer();
	}
	public void save() {
		xml.write();
	}
	public int containsTileSet(File f) {
		int i = 0;
		for(TileSet set : tileSets){
			if(set.f.equals(f)){
				return i;
			}
			i+=1;
		}
		return -1;
	}
	public void clearUnusedTileSet(TileSetPanel panel) {
		int[] i = new int[tileSets.size()];
		int empty = 0;
		for(int id = 0; id != layers.size(); id++){
			Layer l = layers.get(id);
			for(int x = 0; x != l.width; x +=1){
				for(int y = 0; y != l.height; y +=1){
					if(l.data[x][y][0] == -1){
						empty+=1;
					}else{
						i[l.data[x][y][0]]+=1;
					}
				}
			}
		}
		String s = "Auswertung:\n";
		for(int y = 0; y != i.length; y +=1){
			String name = tileSets.get(y).name;
			if(i[y] == 0){
				s+= "<html><font color=#ff0000>";
			}
			s+= "Nr. " + y + " \t " + name + " \t Benutzt: " + i[y];
			if(i[y] == 0){
				s+= "</font></html>";
			}
			s+="\n";
		}
		s+= empty + " Felder leer\n";
		int answ = JOptionPane.showConfirmDialog(AAufbau.f, s, "Unbenutzte Tilesets entfernen?", JOptionPane.YES_NO_OPTION);
		if(answ != 0)return;
		for(int y = i.length-1; y != -1; y -=1){
			if(i[y] == 0){
				panel.tilesChooser.setSelectedIndex(0);
				panel.tilesChooser.removeItemAt(y);
				tileSets.remove(y);
			}
		}
	}
	
	
}
