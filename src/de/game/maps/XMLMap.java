package de.game.maps;

import java.io.File;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import de.game.database.GlobalObject;
import de.game.database.MapXML;
import de.game.game.Event;
import de.game.game.Main;
import de.game.objects.CObject;
import de.game.objects.Layer;

public class XMLMap extends BasicGameState {
	ArrayList<Layer> list = new ArrayList<Layer>();
	int id = 0;
	ScriptEngine engine;
	GlobalObject ob;
	Event event = new de.game.game.Event();
	public XMLMap(File f, int id) throws Exception {
		MapXML xml;
		this.id = id;
		if(f != null){
			xml = new MapXML(list,f);
			ob = xml.getGlobalObject();
		}else{
			System.err.println("Map is empty.");
		}
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		try{
		try {
			engine = new ScriptEngineManager().getEngineByName( "JavaScript" );
			engine.put("map",this);
			engine.put("Event", event);
			
			if(ob != null){
				engine.eval(ob.getFullScript());
				engine.eval("" +
						"function $$$_render(){" +
						"$$$_currendRenderObject.renderFunction($$$_currendRenderObject,$$$_currendGraphicObject);" +
						"};\n" +
						"function $$$_update(){" +
							"for (var i=0; i<$$$_currendRenderObject.updateFunctions.size(); i++) {" +
								"var fkt = $$$_currendRenderObject.updateFunctions.get(i);" +
								"fkt(delta, frameIndex,$$$_currendRenderObject,container,game,Input);" +
							"}" +
						"};");
				
			}
			
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		for (Layer layer : list) {
			for (CObject o : layer) {
				o.init();
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
			Main.kill();
			throw ex;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
	try{
		g.setColor(Color.white);
	
//		g.setAntiAlias(true);
		
		g.translate(CObject.getVerschiebungX(), CObject.getVerschiebungY());
		for (int i = list.size() - 1; i != -1; i--){
			Layer layer = list.get(i);
			if(!layer.isVisible())continue;
			for (int j = layer.size() - 1; j != -1; j--) {
//				if(j %2==0){
//					g.setColor(Color.yellow);
//				}else{
				g.setColor(Color.white);
//				}
				layer.get(j).draw(g,engine);
			}
		}
	}catch(Exception ex){
		ex.printStackTrace();
		Main.kill();
		throw ex;
	}
	}
	int index = 0;
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	try{
		engine.put("Input", container.getInput());
		engine.put("delta", delta);
		engine.put("container", container);
		engine.put("game", game);
		engine.put("frameIndex", index);
		
		CObject.updateScroll(delta);
		for (Layer layer : list) {
			for (CObject o : layer) {
				o.updateMain(container,game,container.getInput(),delta,list,index,layer.isSolid(),engine);
				
			}
		}
		
		Default.update(container, game, delta);
		index++;
	}catch(Exception ex){
		ex.printStackTrace();
		Main.kill();
		throw ex;
	}
	}

	@Override
	public int getID() {
		return id;
	}
	public Layer createLayer(String name,int i){
		Layer l = new Layer(name);
		if(i >= list.size()){
			i = list.size();
		}
		list.add(i,l);
		return l;
	}
	public void spawnCObject(int index,CObject c){
		list.get(index).add(c);
	}
	public void spawnCObject(Layer l,CObject c){
		l.add(c);
	}
	public Layer getLayerByName(String s){
		for (int i = list.size() - 1; i != -1; i--) {
			if(list.get(i).getName().equals(s)){
				return list.get(i);
			}
		}
		return null;
	}
	public CObject getObjectByName(String s){
		for (int i = list.size() - 1; i != -1; i--) {
			for (int j = list.get(i).size() - 1; j != -1; j--) {
				if(list.get(i).get(j).getName().equals(s)){
					return list.get(i).get(j);
				}
			}
		}
		return null;
	}
}
