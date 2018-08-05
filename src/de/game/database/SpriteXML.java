package de.game.database;


import java.io.File;

import org.newdawn.slick.Image;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.game.objects.CSprite;

public class SpriteXML {
	public static void init(Element rootElement,CSprite s) throws Exception{
		NodeList nlist = rootElement.getElementsByTagName("Animation");
		//Animationsloop
				for(int n = 0; n!=nlist.getLength();n++){
					Element ani = (Element)nlist.item(n);
					//Scene
					NodeList scen = ani.getElementsByTagName("Scene");
					Image[] image = new Image[scen.getLength()];
					int[] time = new int[scen.getLength()];
					for(int m = 0;m!=scen.getLength();m++){
						Element oneScene = (Element)scen.item(m);
						time[m] = Integer.parseInt(oneScene.getAttribute("time"));
						if(oneScene.hasAttribute("x")){
							image[m] = new Image(oneScene.getAttribute("image")).getSubImage(
									Integer.parseInt(oneScene.getAttribute("x")),
									Integer.parseInt(oneScene.getAttribute("y")),
									Integer.parseInt(oneScene.getAttribute("width")),
									Integer.parseInt(oneScene.getAttribute("height")));
					
						}else{
							image[m] = new Image(oneScene.getAttribute("image").replace("\\", "/"));
						}
						
					}
					s.addAnimation(image,time);
				}
	}

	
}
