package gui;

import img.Icons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import database.game.GameDatabase;

public enum Tree {
	THAT();
	JPanel mainPanel = new JPanel();
	JTree tree;
	File f;
	String suche;
	DefaultMutableTreeNode Stamm;
	static File selected;
	static FileFilter filterFolder = new FileFilter() {
		
		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			return f.isDirectory();
		}
	};
	static FileFilter filterDatabase = new FileFilter() {
		
		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			return f.getName().endsWith("game." +GameDatabase.getXMLNameStatic());
		}
	};
	public void init(final File f, JPanel trees2) {
		this.f = f;
		f.mkdirs();
		
		//.database erstellen
		File list[] = f.listFiles(filterFolder);
		
		for(File file : list){
			if(file.listFiles(filterDatabase).length == 0){
				try {
					new File(file.getPath() + "/game." + GameDatabase.getXMLNameStatic()).createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		mainPanel.setLayout(new BorderLayout());
		Stamm = new DefaultMutableTreeNode(f.getName());
		try {
			watcher = FileSystems.getDefault().newWatchService();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		initWatchDir();
		regFolder(f);
		durchsucheOrdner(Stamm, f.listFiles());
		tree = new JTree(Stamm);
		tree.addMouseListener(new DragListener());
		tree.setBackground(new Color(200, 200, 200));
		tree.setBorder(null);
		tree.setCellRenderer(new Render());
		tree.expandRow(0);
		int cont = tree.getRowCount();
		while (0 != cont) {
			tree.expandRow(cont);
			cont--;
		}
		mainPanel.add(new JScrollPane(tree));
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		String[] name = { Messages.getString("NeueDatei.Folder"),
				NeueDatei.THAT.sprite.getName(), NeueDatei.THAT.rpg.getName(),
				NeueDatei.THAT.bild.getName() };
		Icon[] Bilder = { Icons.getImage("folder_add", 16), Render.sprite,
				Render.img, Render.map };
		cont = 0;
		while (cont != name.length) {
			JButton button = new JButton(Bilder[cont]);
			button.setToolTipText(name[cont] + " neu erstellen");
			button.setName(name[cont]);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						NeueDatei.THAT.Neu(
								((Component) e.getSource()).getName(),
								tree.getLeadSelectionPath());
					} catch (Exception ex) {
						NeueDatei.THAT.Neu(
								((Component) e.getSource()).getName(), null);
						System.err.println(ex);
					}
				}
			});
			p.add(button);
			cont++;
		}
		mainPanel.add(p, BorderLayout.NORTH);
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2 && selected != null) {
					Öffnen.Open(selected, e);
				}
			}
		});
		tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						TreePath path = e.getNewLeadSelectionPath();
						String s = null;
						try {
							if (path == null) {
								return;
							}
							s = path.toString().replace("[", "")
									.replaceFirst(f.getName(), "")
									.replace("]", "").replace(", ", "\\");
						} catch (NullPointerException e1) {
							e1.printStackTrace();
							return;
						}
						// System.out.println(new File(f + "\\" + s) );
						selected = new File(f.getPath() + "\\" + s);
						// TODO
					}
				});
		
	}

	public static File getSelectedFile() {
		return selected;
	}

	private void durchsucheOrdner(DefaultMutableTreeNode stamm, File[] files) {
		for (File f : files) {
			if(f.getName().contains("!")){
				continue;
			}
					if (f.isDirectory()) {
						DefaultMutableTreeNode Ordner = new DefaultMutableTreeNode(
								f.getName());
						durchsucheOrdner(Ordner, f.listFiles());
						stamm.add(Ordner);
						regFolder(f);
					} else {
						stamm.add(new DefaultMutableTreeNode(f.getName()));
					}
				
		}
	}
	HashMap<File,WatchKey> keys = new HashMap<>();
	private static WatchService watcher;
	
	public void initWatchDir(){
		try{		
		new Thread(){
			public void run() {
				while(THAT.mainPanel != null){
					boolean update = false;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Object[] keyarry = keys.keySet().toArray();
					for(int n = 0; keyarry.length != n ; n+=1){
						File f = (File)keyarry[n];
						WatchKey key = keys.get(f);
					for(WatchEvent<?> event : key.pollEvents()){
						  @SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>)event;
					      Path filename = ev.context();
					      DefaultMutableTreeNode node = null;
					   if(filename != null){
						  String s[] = (f.getPath().replace("\\", "/") + "/"+filename).split("/");
						  String fileString = null;
						  boolean start = false;
						  
						  for(String string : s){
							  if(start){
								  fileString += "/" + string;  
								boolean found = false;
								@SuppressWarnings("unchecked")
								Enumeration<DefaultMutableTreeNode> children = node.children();
								while(children.hasMoreElements()){
									DefaultMutableTreeNode child = children.nextElement();
									if(child.toString().equals(string)){
										found = true;
										
										if(!new File(fileString).exists()){
											child.removeFromParent();
											update = true;
										}
										node =  child;
										break;
									}
								}
								if(found == false){
									DefaultMutableTreeNode child = new DefaultMutableTreeNode(string);
									node.add(child);
									node = child;
									tree.expandPath(new TreePath(node));
									update = true;
									if(new File(fileString).isDirectory()){
										durchsucheOrdner(node,new File(fileString).listFiles());
									}
									
								}
							  }
							  if(!start && string.equals(getFile().getName())){
								  start = true;
								  node = Stamm;
								  fileString = Stamm.toString();
								 
							  }
						  }
						  
					   }
					}
					}
				if(update){
					TreePath[] rows = new TreePath[tree.getRowCount()];
					
					for(int i = 0; i<tree.getRowCount(); i+=1){
						if(tree.isExpanded(i)){
							rows[i] = tree.getPathForRow(i);
						}
					}					
					((DefaultTreeModel)tree.getModel()).nodeStructureChanged(Stamm);
					for(TreePath p : rows){
						if(p == null)continue;
						tree.expandPath(p);
					}
				}
				}
				
			};
		}.start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void regFolder(File f){
		try{
		if(f.isDirectory()){
		Path dir =f.toPath();
		final WatchKey key = dir.register(watcher,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE//,
//				StandardWatchEventKinds.ENTRY_MODIFY
				);
		keys.put(f,key);
		for(File file : f.listFiles()){
			regFolder(file);
		}
		}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("serial")
	static public class Render extends javax.swing.tree.DefaultTreeCellRenderer {
		static public ImageIcon main, map, sprite, img, tmx, Ordner, OrdnerOffen,
				database, xts, pesx;

		protected Render() {
			main = Icons.getImage("package", 16);
			map = Icons.getImage("map", 16);
			sprite = Icons.getImage("user_catwomen", 16);
			img = Icons.getImage("file_extension_jpg", 16);
			tmx = Icons.getImage("table", 16);
			OrdnerOffen = Icons.getImage("folder", 16);
			Ordner = Icons.getImage("folder_key", 16);
			database = Icons.getImage("database", 16);
			xts = Icons.getImage("small_tiles", 16);
			pesx = Icons.getImage("rain", 16);
			// = Icons.getImage("", 16);
			// = Icons.getImage("", 16);

		};

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean isLeaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					isLeaf, row, hasFocus);
			setOpaque(true);
			setBackground(tree.getBackground());
			// treenode holen
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (node.isRoot()) {
				setIcon((Icon) main);
				return this;
			}
			if (node.toString().contains(".")) {
				if (node.toString().toLowerCase().endsWith(".rpg")) {
					setIcon((Icon) map);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".das")) {
					setIcon((Icon) sprite);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".png")) {
					setIcon((Icon) img);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".tmx")) {
					setIcon((Icon) tmx);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".xts")) {
					setIcon((Icon) xts);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".pesx")) {
					setIcon((Icon) pesx);
					return this;
				}
				if (node.toString().toLowerCase().endsWith(".database")) {
					setIcon((Icon) database);
					return this;
				}
			}

			if (node.toString().contains("map")) {
				setIcon((Icon) map);
				return this;
			}
			if (node.toString().contains("sprite")) {
				setIcon((Icon) sprite);
				return this;
			}
			if (node.toString().contains("pic")) {
				setIcon((Icon) img);
				return this;
			}
			if (node.toString().contains("particel")) {
				setIcon((Icon) pesx);
				return this;
			}
			if (expanded) {
				setIcon(OrdnerOffen);
				return this;
			}

			setIcon(Ordner);
			return this;
		}
	}

	private class DragListener implements MouseListener {
		String s;
		boolean b = false;

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			b = false;
		}

		public void mouseExited(MouseEvent e) {
			b = true;
		}

		public void mousePressed(MouseEvent e) {

			try {
				s = "res"
						+ tree.getPathForRow(
								tree.getRowForLocation(e.getX(), e.getY()))
								.toString().replace("[", "")
								.replaceFirst(f.getName(), "").replace("]", "")
								.replace(", ", "\\");
				;
			} catch (Exception e1) {
			}
		}

		public void mouseReleased(MouseEvent e) {
			boolean open = true;
			if (b) {
				if (s == null) {
				} else if (AAufbau.f.CurrendPanel instanceof Drop) {
					open = ((Drop) AAufbau.f.CurrendPanel)
							.dropEvent(new File(s));
				}
				if (open) {
					Öffnen.Open(selected, e);
				}
			}// }
		}
	}
	public File getFile() {
		return f;
	}
}