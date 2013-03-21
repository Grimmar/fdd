package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.Algorithm;
import model.CloseAlgorithm;
import model.Rule;

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
        new JMenuItem("Fermer")), HELP_MENU(new JMenuItem("À propos"));
        private JMenuItem item;
        
        private Item(JMenuItem item) {
            this.item = item;
        }
        
        public JMenuItem getItem() {
            return item;
        }
    }
    
    public enum Menu {
        
        FILE(new JMenu("Fichier")), HELP(new JMenu("Aide"));
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
            MENU_STRUCT.put(Menu.FILE, new Item[]{Item.OPEN_FILE,
                        Item.CLOSE_FILE, Item.SAVE_RESULTS, null, Item.CLOSE});
            MENU_STRUCT.put(Menu.HELP, new Item[]{Item.HELP_MENU});
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
            
            @Override
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
                JPanel r = new JPanel(new GridLayout(2, 2, 5, 10));
                {
                    r.setBorder(BorderFactory.createTitledBorder("Algorithme"));
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
                q.setBorder(BorderFactory.createTitledBorder("Résultats"));
                q.add(new JScrollPane(rules));
            }
            p.add(q, BorderLayout.CENTER);
            q = new JPanel(null);
            {
                q.setLayout(new BoxLayout(q, BoxLayout.LINE_AXIS));
                q.setBorder(BorderFactory.createRaisedBevelBorder());
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
                int returnVal = jfc.showSaveDialog(frame);
                
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f = jfc.getSelectedFile();
                    BufferedWriter br = null;
                    try {
                        br = new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(f)));
                        List<Rule> rulesList = model.getRules();
                        for (Rule r : rulesList) {
                            br.write(r.toString());
                            br.newLine();
                        }
                        rulesList = model.getApproximativeRules();
                        for (Rule r : rulesList) {
                            br.write(r.toString());
                            br.newLine();
                        }
                        
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Une erreur a eu lieu lors de la sauvegarde.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        try {
                            br.close();
                        } catch (IOException ex) {
                            
                        }
                    }
                }
            }
        });
        
        Item.CLOSE.getItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        Item.HELP_MENU.getItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String msg = new String("Réalisé par Quentin Bisson et David Charpentier\nMaster 1 Université de Rouen\ndans le cadre du cours de fouille de données");
                JOptionPane.showMessageDialog(frame,
                                    msg,
                                    "A propos",
                                    JOptionPane.NO_OPTION);
      
            }
        });
        
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rules.setText("");
                
                if (model.getFile() != null) {
                    try {
                        float f = Float.parseFloat(support.getText());
                        model.setMinSupport(f);
                        
                        if (f < 0.0 || f > 1.0) {
                            JOptionPane.showMessageDialog(frame,
                                    "Le support doit être compris entre 0 et 1.",
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            Item.OPEN_FILE.getItem().setEnabled(false);
                            rules.setText("--------- Démarrage de l'algorithme ---------\n\n");
                            model.start();
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Le support doit être un nombre flottant",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                
            }
        });
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.stop();
                Item.OPEN_FILE.getItem().setEnabled(true);
            }
        });
        
        model.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (model.isStopped()) {
                    Item.OPEN_FILE.getItem().setEnabled(true);
                    StringBuilder sb = new StringBuilder("Règles exactes\n");
                    List<Rule> rulesList = model.getRules();
                    for (Rule r : rulesList) {
                        sb.append(r).append("\n");
                    }
                    sb.append("\nRègles approximatives\n");
                    rulesList = model.getApproximativeRules();
                    for (Rule r : rulesList) {
                        sb.append(r).append("\n");
                    }
                    sb.append("\n--------- Fin de l'algorithme ---------");
                    rules.append(sb.toString());
                    Item.SAVE_RESULTS.getItem().setEnabled(true);
                }
                
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
