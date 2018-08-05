package editor;

import img.Icons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import editor.tabelle.OneLine;
import editor.tools.GreifTool;

@SuppressWarnings("serial")
public class DrawSettingsPanal extends JPanel {
	public LinkedList<ConectedToggelButten> list = new LinkedList<>();

	public DrawSettingsPanal() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new ConectedToggelButten(PUNKTFANG, Icons.getImage("chess_horse",
				16)));
		add(new ConectedToggelButten(ORTHOGONAL, Icons.getImage("chess_tower",
				16)));
		add(new ConectedToggelButten(ORTHOGONAL_OFF_OTHERPOINT, Icons.getImage(
				"chess_tower", 16)));
		add(new ConectedToggelButten(EINGABEHILFE, Icons.getImage("canvas", 16)));
		add(new ConectedToggelButten(RASTER, Icons.getImage("grid", 16)));
		add(new ConectedToggelButten(PUNKTFANG_ECKE, Icons.getImage("pf_coner", 0)));
		add(new ConectedToggelButten(PUNKTFANG_MITTE, Icons.getImage("pf_center", 0)));
		add(new ConectedToggelButten(PUNKTFANG_ZENTRUM, Icons.getImage("pf_concenter", 0)));
		add(new ConectedToggelButten(PUNKTFANG_RASTER, Icons.getImage("pf_grid", 0)));
		add(new ConectedToggelButten(PUNKTFANG_TILEMAP, Icons.getImage("pf_tilemap", 0)));
		update();
	}

	public void update() {
		for (ConectedToggelButten c : list) {
			c.update();
		}
	}

	/****/
	// ------------------------------------------------
	public static OneLine<Boolean> PUNKTFANG = new OneLine<Boolean>(
			"Punktfang", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> ORTHOGONAL = new OneLine<Boolean>(
			"Senkrecht", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.ORTHOGONAL = value;
		}

		public Boolean getValue() {
			return GreifTool.ORTHOGONAL;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> ORTHOGONAL_OFF_OTHERPOINT = new OneLine<Boolean>(
			"Senkrechte eines Zweitenpunktes", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.ORTHOGONAL_OFF_OTHERPOINT = value;
		}

		public Boolean getValue() {
			return GreifTool.ORTHOGONAL_OFF_OTHERPOINT;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> EINGABEHILFE = new OneLine<Boolean>(
			"Eingabehilfe", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.EINGABEHILFE = value;
		}

		public Boolean getValue() {
			return GreifTool.EINGABEHILFE;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> RASTER = new OneLine<Boolean>(
			"Zeige Raster", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.RASTER = value;
		}

		public Boolean getValue() {
			return GreifTool.RASTER;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> PUNKTFANG_ECKE = new OneLine<Boolean>(
			"Punktfang in Ecken", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG_ECKE = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG_ECKE;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> PUNKTFANG_MITTE = new OneLine<Boolean>(
			"Punktfang in der Mitte der Linien", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG_MITTE = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG_MITTE;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> PUNKTFANG_ZENTRUM = new OneLine<Boolean>(
			"Punktfang im Zentrum", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG_ZENTRUM = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG_ZENTRUM;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	// ------------------------------------------------
	public static OneLine<Boolean> PUNKTFANG_RASTER = new OneLine<Boolean>(
			"Punktfang am Raster", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG_RASTER = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG_RASTER;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};
	public static OneLine<Boolean> PUNKTFANG_TILEMAP = new OneLine<Boolean>(
			"Punktfang am TilemapRaster", true) {

		@Override
		public void setValue(Boolean value) {
			GreifTool.PUNKTFANG_TILEMAP = value;
		}

		public Boolean getValue() {
			return GreifTool.PUNKTFANG_TILEMAP;
		}

		@Override
		public void valueChanges(Boolean value) {
			// TODO Auto-generated method stub

		};
	};

	/****/
	class ConectedToggelButten extends JToggleButton {
		private OneLine<Boolean> b;

		public ConectedToggelButten(OneLine<Boolean> bo, ImageIcon i) {
			super(i);
			setToolTipText(bo.getKey());
			this.b = bo;
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					b.setValue(!b.getValue());
				}
			});
			list.add(this);
		}

		public void update() {
			setSelected(b.getValue());
		}
	}

	public LinkedList<ConectedToggelButten> getList() {
		return list;
	}

}
