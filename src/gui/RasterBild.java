package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import database.SpriteXML;
import database.SpriteXML.Scene;

/**
 * //TODO Rasterbild f�r pr�ft selbt welches scene dran ist<br>
 * //TODO Rasterbild �bernimmt die einstellung von Mannager
 * 
 * @author rubber
 */
@SuppressWarnings("serial")
public class RasterBild extends javax.swing.JDialog {
	public JLabel Breite = new JLabel("error"), H�he = new JLabel("error");
	public JTextField RasterBreite, RasterH�he;
	public JTextField RasterStartX, RasterStartY;
	public JButton Farbew�hlen = new JButton("Farbe");
	public Color farbe = Color.BLACK;
	public int RasterBreiteZahl = 32, RasterH�heZahl = 32;
	public int StartX = 0, StartY = 0;
	public JButton Best�tigen;
	public JPanel Oben, Unten, Rechts;
	public JLabel Mitte;
	JScrollPane scroll;
	SpriteXML data;
	SpriteEinstellen einst;

	public RasterBild(SpriteEinstellen einst, SpriteXML data, String s) {
		super(AAufbau.f);
		this.data = data;
		this.einst = einst;
		setLayout(new BorderLayout());
		// this.addWindowListener(new WindowListener(){
		// public void windowActivated(WindowEvent arg0) {}
		// public void windowClosed(WindowEvent arg0) {dispose();}
		// public void windowClosing(WindowEvent arg0) {dispose();}
		// public void windowDeactivated(WindowEvent arg0) {}
		// public void windowDeiconified(WindowEvent arg0) {}
		// public void windowIconified(WindowEvent arg0) {}
		// public void windowOpened(WindowEvent arg0) {}});
		Oben = new JPanel();
		RasterStartX = new JTextField(2);
		RasterStartX.setText("" + StartX);
		RasterStartX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int RasterB = Integer.parseInt(RasterStartX.getText().replace(
						" ", ""));
				StartX = RasterB;
				Mitte.repaint();
			}
		});
		Oben.add(RasterStartX);
		RasterStartY = new JTextField(2);
		RasterStartY.setText("" + StartY);
		RasterStartY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int RasterB = Integer.parseInt(RasterStartY.getText().replace(
						" ", ""));
				StartY = RasterB;
				Mitte.repaint();
			}
		});
		Oben.add(RasterStartY);
		RasterBreite = new JTextField(2);
		RasterBreite.setText("" + RasterBreiteZahl);
		RasterBreite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int RasterB = Integer.parseInt(RasterBreite.getText().replace(
						" ", ""));
				if (RasterB == 0) {
					RasterBreite.setText("" + RasterBreiteZahl);
				} else {
					RasterBreiteZahl = RasterB;
					Mitte.repaint();
				}
			}
		});
		Oben.add(RasterBreite);
		RasterH�he = new JTextField(2);
		RasterH�he.setText("" + RasterH�heZahl);
		RasterH�he.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				int RasterB = Integer.parseInt(RasterH�he.getText().replace(
						" ", ""));
				if (RasterB == 0) {
					RasterH�he.setText("" + RasterH�heZahl);
				} else {
					RasterH�heZahl = RasterB;
					Mitte.repaint();
				}
			}
		});
		Oben.add(RasterH�he);
		Oben.add(Farbew�hlen);
		Farbew�hlen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// JColorChooser W�hlen = new JColorChooser();
				// if(W�hlen.getColor() == null){farbe = W�hlen.getColor();}
				if (farbe == Color.BLACK) {
					farbe = Color.WHITE;
				} else if (farbe == Color.WHITE) {
					farbe = Color.CYAN;
				} else if (farbe == Color.CYAN) {
					farbe = Color.GRAY;
				} else if (farbe == Color.GRAY) {
					farbe = Color.GREEN;
				} else if (farbe == Color.GREEN) {
					farbe = Color.PINK;
				} else if (farbe == Color.PINK) {
					farbe = Color.BLACK;
				}
				Mitte.repaint();
			}
		});
		this.add(Oben, BorderLayout.NORTH);
		Rechts = new JPanel();
		Rechts.add(H�he);
		this.add(Rechts, BorderLayout.EAST);
		Unten = new JPanel();
		Unten.add(Breite);
		Best�tigen = new JButton("          OK          ");
		Best�tigen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				finish();
			}
		});
		Unten.add(Best�tigen);
		this.add(Unten, BorderLayout.SOUTH);
		Mitte = new RasterBildMitte();
		scroll = new JScrollPane(Mitte);
		this.add(scroll, BorderLayout.CENTER);

	}

	public void setImageSize() {
		try {
			((RasterBildMitte) Mitte).start();
			Breite.setText("" + img.getWidth(null));
			H�he.setText("" + img.getHeight(null));
			this.setSize(img.getWidth(null) + 100, img.getHeight(null) + 140);
			if (this.getHeight() > 700) {
				this.setSize(this.getWidth(), 700);
			}
			if (this.getWidth() < 260) {
				this.setSize(260, this.getHeight());
			}
			if (this.getWidth() > 1200) {
				this.setSize(1200, this.getHeight());
			}
			this.setMaximumSize(this.getMaximumSize());
			Mitte.setPreferredSize(new Dimension(img.getWidth(null), img
					.getHeight(null)));
			scroll.setPreferredSize(Mitte.getPreferredSize());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Fehler: Kein Bild.");
		}
	}

	private void finish() {
		Scene s = data.getSceneByToolBar();
		s.setX(StartX % RasterBreiteZahl + ((RasterBildMitte) Mitte).KlickX
				/ RasterBreiteZahl * RasterBreiteZahl);
		s.setY(StartY % RasterH�heZahl + ((RasterBildMitte) Mitte).KlickY
				/ RasterH�heZahl * RasterH�heZahl);
		s.setWidht(RasterBreiteZahl);
		s.setHeight(RasterH�heZahl);
		einst.updataTable();
		einst.updateImage();
		new Thread(new Runnable() {
			public void run() {
				einst.updateImage();
			};
		}).start();
	}

	Image img = null;

	private class RasterBildMitte extends JLabel implements MouseListener,
			MouseMotionListener {
		public int KlickX = 1, KlickY = 1;

		RasterBildMitte() {
			super();
			start();
			this.setLayout(null);
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
		}

		public void start() {
			img = einst.imageHash.get(data.getSceneByToolBar().getImagePfad());
			this.repaint();
		}

		public void paint(Graphics g) {
			g.setColor(farbe);
			g.drawImage(img, 0, 0, this);
			int n = 0;
			while (img.getWidth(null) >= RasterBreiteZahl * n) {
				g.drawLine(StartX % RasterBreiteZahl + RasterBreiteZahl * n, 0,
						StartX % RasterBreiteZahl + RasterBreiteZahl * n,
						img.getHeight(null));
				n++;
			}
			n = 0;
			while (img.getHeight(null) >= RasterH�heZahl * n) {
				g.drawLine(0, StartY % RasterH�heZahl + RasterH�heZahl * n,
						img.getWidth(null), StartY % RasterH�heZahl
								+ RasterH�heZahl * n);
				n++;
			}
			g.setColor(Color.RED);
			g.fillRect(StartX - 5, StartY, 5, 5);
			g.fillRect(StartX, StartY - 5, 5, 5);
			g.drawRect(StartX % RasterBreiteZahl + KlickX / RasterBreiteZahl
					* RasterBreiteZahl, StartY % RasterH�heZahl + KlickY
					/ RasterH�heZahl * RasterH�heZahl, RasterBreiteZahl,
					RasterH�heZahl);
			g.dispose();
		}

		@Override
		public void mouseDragged(MouseEvent ev) {
			if (img.getWidth(null) > ev.getX())
				KlickX = ev.getX();
			if (img.getHeight(null) > ev.getY())
				KlickY = ev.getY();
			KlickX -= StartX % RasterBreiteZahl;
			KlickY -= StartY % RasterBreiteZahl;
			Breite.setText(img.getWidth(null) + ";   Maus: " + ev.getX() + ", "
					+ ev.getY());
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent ev) {
			Breite.setText(img.getWidth(null) + "; Maus: " + ev.getX() + ", "
					+ ev.getY());
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			start();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent ev) {
			if (img.getWidth(null) > ev.getX())
				KlickX = ev.getX();
			if (img.getHeight(null) > ev.getY())
				KlickY = ev.getY();
			KlickX -= StartX % RasterBreiteZahl;
			KlickY -= StartY % RasterH�heZahl;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

	}
}
