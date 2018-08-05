package editor.css;
import javax.swing.ImageIcon;

public abstract class SpecialEditObject {
	public abstract ImageIcon getImage(ImageIcon i);
	public abstract String toString();
	public abstract void edit();
	/**@value wenn null dann dafaul*/
	public abstract SpecialEditObject set(String value);
	boolean verändert = true;
	public void setVerändert(boolean verändert) {
		this.verändert = verändert;
	}
	public boolean isVerändert() {
		return verändert;
	}
}
