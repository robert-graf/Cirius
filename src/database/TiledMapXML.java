package database;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import editor.tileMap.Layer;
import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;
import editor.tileMap.objects.TiledMap.SecretAccess;

/**
 * @version 09.11.2013
 */
public class TiledMapXML extends XML {

	TiledMap map;
	SecretAccess access;
	public TiledMapXML(TiledMap tiledMap, SecretAccess secretAccess) {
		map = tiledMap;
		access = secretAccess;
	}

	@Override
	public void read(Element rootElement) {
		try {

			String orient = rootElement.getAttribute("orientation");
			if (!orient.equals("orthogonal")) {
				throw new SlickException("Only orthogonal maps supported, found: " + orient);
			}
			access.setWidth(Integer.parseInt(rootElement.getAttribute("width")));
			access.setHeight(Integer.parseInt(rootElement.getAttribute("height")));
			access.setTileWidth(Integer.parseInt(rootElement.getAttribute("tilewidth")));
			access.setTileHeight(Integer.parseInt(rootElement.getAttribute("tileheight")));
			

			// now read the map properties
//			Element propsElement = (Element) rootElement.getElementsByTagName(
//					"properties").item(0);
//			if (propsElement != null) {
//				NodeList properties = propsElement
//						.getElementsByTagName("property");
//				if (properties != null) {
//					props = new Properties();
//					for (int p = 0; p < properties.getLength(); p++) {
//						Element propElement = (Element) properties.item(p);
//
//						String name = propElement.getAttribute("name");
//						String value = propElement.getAttribute("value");
//						props.setProperty(name, value);
//					}
//				}
//			}
				TileSet tileSet = null;
				TileSet lastSet = null;

				NodeList setNodes = rootElement.getElementsByTagName("tileset");
				for (int i = 0; i < setNodes.getLength(); i++) {
					Element current = (Element) setNodes.item(i);

					tileSet = new TileSet(current);
					tileSet.index = i;

					if (lastSet != null) {
						lastSet.setLimit(tileSet.firstGID - 1);
					}
					lastSet = tileSet;
					
					map.addtileSet(tileSet);
				}
			

			NodeList layerNodes = rootElement.getElementsByTagName("layer");
			for (int i = 0; i < layerNodes.getLength(); i++) {
				Element current = (Element) layerNodes.item(i);
				Layer layer = new Layer(map, current);
				layer.index = i;

				map.addLayer(layer);
			}

//			// acquire object-groups
//			NodeList objectGroupNodes = rootElement
//					.getElementsByTagName("objectgroup");

//			for (int i = 0; i < objectGroupNodes.getLength(); i++) {
//				Element current = (Element) objectGroupNodes.item(i);
//				ObjectGroup objectGroup = new ObjectGroup(current);
//				objectGroup.index = i;
//
//				objectGroups.add(objectGroup);
//			}
		} catch (Exception e) {
			Log.error(e);
			try {
				throw new SlickException("Failed to parse tilemap", e);
			} catch (SlickException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
		rootElement.setAttribute("orientation", "orthogonal");
		rootElement.setAttribute("width", "" + map.getWidth());
		rootElement.setAttribute("height", ""+ map.getHeight());
		rootElement.setAttribute("tilewidth", "" + map.getTileWidth());
		rootElement.setAttribute("tileheight", "" + map.getTileHeight());
		int GIDCont = 1;
		for(int i = 0; i != map.getTileSetCount(); i+=1){
			TileSet ts = map.getTileSet(i);
			Element e = xmlDoc.createElement("tileset");
			ts.firstGID = GIDCont;
			GIDCont += ts.getHorizontalCount()*ts.getVerticalCount();
			e.setAttribute("firstgid", ts.firstGID+"");
			e.setAttribute("source", ts.getFile().getPath());
			rootElement.appendChild(e);
		}
		ArrayList<Layer> list = access.getLayers();
		for(Layer l : list){
			l.save(rootElement,xmlDoc,true);
		}
//			
//

//

	}

	@Override
	public String getXMLName() {
		return "tiledMap";
	}

	@Override
	public void fileDoesNotExist() {
		
	}

}
