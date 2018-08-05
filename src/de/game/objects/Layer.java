package de.game.objects;

import java.util.ArrayList;


@SuppressWarnings("serial")
public class Layer extends ArrayList<CObject>{
	//TODO <CObject>
	private String name = "NO NAME???";
	private boolean visible = true;
	private boolean solid = true;
	private float Faktor = 1;
	//construktoren
	public Layer(String name) {this.setName(name);}
	public Layer(String name,boolean visible) {setName(name);setVisible(visible);}
	public Layer(String name, float faktor, boolean visible, boolean solid) {
		setName(name);setFaktor(faktor);setVisible(visible);setSolid(solid);
	}
	public float getFaktor() {return Faktor;}
	public String getName() {return name;}
	public boolean isSolid() {return solid;}
	public boolean isVisible() {return visible;}
	public void setFaktor(float f) {Faktor = f;}
	public void setName(String name) {this.name = name;}
	public void setSolid(boolean solid) {this.solid = solid;}
	public void setVisible(boolean visible) {this.visible = visible;}
	

}
