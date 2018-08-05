package editor.css;
import javax.swing.ImageIcon;

public abstract class SpecialEditObject {
	public abstract ImageIcon getImage(ImageIcon i);
	public abstract String toString();
	public abstract void edit();
	/**@value wenn null dann dafaul*/
	public abstract SpecialEditObject set(String value);
	boolean ver�ndert = true;
	public void setVer�ndert(boolean ver�ndert) {
		this.ver�ndert = ver�ndert;
	}
	public boolean isVer�ndert() {
		return ver�ndert;
	}
}
