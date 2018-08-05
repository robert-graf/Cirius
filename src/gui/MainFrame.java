package gui;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import toolbars.DialogDatei;
import toolbars.Toolbar;
import database.game.DataBaseEditor;
import database.game.Ersteller;
import interfaces.FileHolder;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements WindowListener {
	public static final int SPRITEEDTIORPOSS = 1;
	public HashMap<File, JComponent> OpenFileList = new HashMap<File, JComponent>();
	public HashMap<JComponent, File> PanelToFileList = new HashMap<JComponent, File>();
	public JComponent CurrendPanel;
	JPanel trees = new JPanel();
	Toolbar toolbar = new Toolbar();
	final static String HEAD = Messages.getString("MainFrame.Titel"); //$NON-NLS-1$
	public JTabbedPane OpenFile = new JTabbedPane();

	MainFrame() {
		super(HEAD);
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds());
		setDefaultCloseOperation(0);
		JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				trees, OpenFile);
		mainSplit.setDividerLocation(Integer.parseInt(Messages
				.getString("MainFrame.DividerLoc")));
		add(mainSplit);
		trees.setLayout(new BoxLayout(trees, BoxLayout.Y_AXIS));
		new Ersteller(new File("res/")); //$NON-NLS-1$
		Tree.THAT.init(new File("res/"), trees);
		trees.add(Tree.THAT.mainPanel); //$NON-NLS-1$ //$NON-NLS-2$
		OpenFile.setTabPlacement(Integer.parseInt(Messages
				.getString("MainFrame.TabLocation")));
		setVisible(true);
		Listen();
		this.addWindowListener(this);
		setJMenuBar(Toolbar.bar);
	}

	public void addOpenFile(JComponent p, FileHolder h) {
		OpenFile.add(p.getName(), p);
		PanelToFileList.put(p, h.getFile());
		OpenFileList.put(h.getFile(), p);
		setSelectedPanel(h.getFile());
	}

	public File getFile(JComponent c) {
		return PanelToFileList.get(c);
	}

	public void setSelectedPanel(File f) {
		OpenFile.setSelectedComponent(OpenFileList.get(f));
	}

	private void Listen() {
		OpenFile.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				CurrendPanel = (JComponent) OpenFile.getSelectedComponent();
				if (CurrendPanel instanceof FileHolder) {
					setTitle(HEAD + " " + ((FileHolder) CurrendPanel).getFile()); //$NON-NLS-1$
				} else if (CurrendPanel instanceof DataBaseEditor) {
					setTitle(HEAD + " Datenbank V" //$NON-NLS-1$
							+ ((DataBaseEditor) CurrendPanel).getVersion());
				} else {
					setTitle(HEAD);
				}
				toolbar.update(CurrendPanel);
			}
		});
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (!AAufbau.f.OpenFileList.isEmpty()) {
			do {
				DialogDatei.close.doClick();
			} while ((DialogDatei.answ == 0 || DialogDatei.answ == 1)
					&& !AAufbau.f.OpenFileList.isEmpty());
		}
		if (AAufbau.f.OpenFileList.isEmpty()) {
			this.dispose();
			System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}
