package editor.css;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Sp_Dasharray extends SpecialEditObject{
	float[] f;
	BasicStroke storke = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,4);
	@Override
	public ImageIcon getImage(ImageIcon i) {
		BufferedImage buff = (BufferedImage) i.getImage();
		Graphics2D g =  (Graphics2D)buff.getGraphics();
		g.setBackground(Color.white);
		g.clearRect(-1, -1, i.getIconWidth()+1, i.getIconHeight()+1);
		g.setColor(Color.black);
		g.setStroke(storke);
		g.drawLine(0, i.getIconHeight()/2, i.getIconWidth(), i.getIconHeight()/2);
		return i;
	}

	@Override
	public String toString() {
		if(f == null){
			return "-1";
		}
		String s = "" + f[0];
		for(int i = 1; i < f.length;i++){
			s += ","+ f[i];
		}
		return s;
	}

	@Override
	public void edit() {
		set(JOptionPane.showInputDialog(CSSEditor.THAT.dialog, "(Beta)", toString()));
	}

	@Override
	public SpecialEditObject set(String value) {
		setVerändert(true);
		if(value == null){
			storke = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,4);
			f = null;
			return this;
		}
		String[] s = value.split(",");
		f = new float[s.length];
		for(int n = 0; n!= s.length; n++){
			f[n] = Float.parseFloat(s[n]);
		}
		storke = new BasicStroke(4,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,4,f,0);
		return this;
	}
	public float[] getValue(){
		return f;
	}
}
