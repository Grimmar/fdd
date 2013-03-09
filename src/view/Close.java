package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Algorithm;
import model.CloseAlgorithm;

public class Close {

	private JFrame frame;
	private JLabel fileLabel;
	private JButton start;
	private JButton stop;
	private JTextField support;
	private JTextArea rules;
	private JFileChooser jfc;
	private Algorithm model;

	public enum Item {
		OPEN_FILE(new JMenuItem("Ouvrir un fichier")), CLOSE_FILE(
				new JMenuItem("Fermer le fichier")), SAVE_RESULTS(
				new JMenuItem("Enregistrer les résultats")), CLOSE(
				new JMenuItem("Fermer"));

		private JMenuItem item;

		private Item(JMenuItem item) {
			this.item = item;
		}

		public JMenuItem getItem() {
			return item;
		}
	}

	public enum Menu {
		FILE(new JMenu("Fichier"));

		private JMenu menu;

		private Menu(JMenu menu) {
			this.menu = menu;
		}

		public JMenu getMenu() {
			return menu;
		}

		public static final Map<Menu, Item[]> MENU_STRUCT;
		static {
			MENU_STRUCT = new EnumMap<Menu, Item[]>(Menu.class);
			MENU_STRUCT.put(Menu.FILE, new Item[] { Item.OPEN_FILE,
					Item.CLOSE_FILE, Item.SAVE_RESULTS, null, Item.CLOSE });
		}
	}

	public Close() {
		createModel();
		createView();
		placeComponents();
		createAndInstallMenuBar();
		createController();
	}

	private void createModel() {
		model = new CloseAlgorithm();
	}

	private void createView() {
		frame = new JFrame("Fouille de données: Algorithme Close");
		fileLabel = new JLabel();
		start = new JButton("Démarrer");
		stop = new JButton("Arrêter");
		support = new JTextField("1.0");
		rules = new JTextArea(30, 40);
		jfc = new JFileChooser() {
			private static final long serialVersionUID = -11708675517620289L;

			public boolean accept(File file) {
				return (file.getName().toLowerCase().endsWith(".txt") || file
						.isDirectory());
			}
		};
	}

	private void createAndInstallMenuBar() {
		JMenuBar menubar = new JMenuBar();
		for (Menu m : Menu.MENU_STRUCT.keySet()) {
			for (Item item : Menu.MENU_STRUCT.get(m)) {
				if (item == null) {
					m.getMenu().addSeparator();
				} else {
					m.getMenu().add(item.getItem());
				}
			}
			menubar.add(m.getMenu());
		}
		frame.setJMenuBar(menubar);
	}

	private void placeComponents() {
		JPanel p = new JPanel(new BorderLayout());
		{
			JPanel q = new JPanel(new BorderLayout());
			{
				JPanel r = new JPanel(new GridLayout(3, 2));
				{
					r.add(new JLabel("Support"));
					r.add(support);
					r.add(start);
					r.add(stop);
				}
				q.add(r, BorderLayout.NORTH);
				q.add(new JPanel(), BorderLayout.CENTER);
			}
			p.add(q, BorderLayout.WEST);
			q = new JPanel(new GridLayout(1, 0));
			{
				q.add(new JScrollPane(rules));
			}
			p.add(q, BorderLayout.CENTER);
			q = new JPanel(null);
			{
				q.setLayout(new BoxLayout(q, BoxLayout.LINE_AXIS));
				q.add(new JLabel("File: "));
				q.add(fileLabel);
			}
			p.add(q, BorderLayout.SOUTH);
		}
		frame.setContentPane(p);
	}

	private void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start.setEnabled(false);
		stop.setEnabled(false);
		Item.CLOSE_FILE.getItem().setEnabled(false);
		Item.SAVE_RESULTS.getItem().setEnabled(false);
		rules.setEditable(false);
		Item.OPEN_FILE.getItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = jfc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = jfc.getSelectedFile();
					model.setFile(f);
					fileLabel.setText(f.getName());
					Item.CLOSE_FILE.getItem().setEnabled(true);
					if (!support.getText().isEmpty() && model.getFile() != null) {
						start.setEnabled(true);
					} else {
						start.setEnabled(false);
					}
				}
			}
		});

		Item.CLOSE_FILE.getItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.closeFile();
				fileLabel.setText("");
				Item.CLOSE_FILE.getItem().setEnabled(false);
			}
		});

		Item.SAVE_RESULTS.getItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		Item.CLOSE.getItem().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Item.OPEN_FILE.getItem().setEnabled(false);
			}
		});

		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Item.OPEN_FILE.getItem().setEnabled(true);
			}
		});

		support.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (!support.getText().isEmpty() && model.getFile() != null) {
					start.setEnabled(true);
				} else {
					start.setEnabled(false);
				}
			}

			@Override
			public void keyPressed(KeyEvent arg0) {

			}
		});
	}

	public void display() {
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.pack();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Close().display();
			}
		});
	}

}
