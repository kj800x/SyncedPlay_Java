package syncedplay;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class SyncedPlay extends JFrame {

    private SoundManager sm;
    private CueTableModel cueTableModel;
    
    private Point contextClickPoint;
    private JTextField commandPromptText;
    private JTable cuesTable;
    private JPopupMenu tablePMenu;

    
    public SyncedPlay() {
        this.cueTableModel = new CueTableModel();
        this.sm = new SoundManager();
        initUI();
    }

    final void setLaF() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            throw new ClassNotFoundException();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            try {
                // Set System L&F
                UIManager.setLookAndFeel(
                        //"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        //UIManager.getCrossPlatformLookAndFeelClassName());
                        UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException z) {
                // handle exception
            }
        }
    }

    final void makeMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        QuitAction qa = new QuitAction("Exit", "Closes the application", KeyEvent.VK_E);
        JMenuItem exitMenuItem = new JMenuItem(qa);
        file.add(exitMenuItem);

        LoadAction la = new LoadAction("Open", "Loads a file", KeyEvent.VK_E, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                sm.learnSound("knock", (File) args[0]);
                //String text = readFile((File) args[0]);
                //System.out.print(text);
            }
        });
        JMenuItem loadMenuItem = new JMenuItem(la);
        file.add(loadMenuItem);

        menubar.add(file);
        setJMenuBar(menubar);
    }
    
    final void buildPopUpMenu(){
        tablePMenu = new JPopupMenu();
        
        JMenuItem editCue = new JMenuItem("Edit Cue");
        editCue.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                //Point pt = ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getLocation(); //TODO: This is supposed to get the screen location, of the upper right corner of the popup menu, but this doesn't work.
                int row = cuesTable.rowAtPoint(contextClickPoint);
                //System.out.println(pt);
                System.out.println("Editing: " + cueTableModel.getCue(row).getDescription());
            }
        });

        tablePMenu.add(editCue);
    }

    final void makePanel() {
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        JScrollPane cuesPane = new JScrollPane();
        
        ArrayList<Cue> cues = new ArrayList();
        cues.add(new Cue("One"));
        cues.add(new Cue("Two"));
        cues.add(new Cue("Three"));
        cueTableModel.setCues(cues);
        cuesTable = new JTable(cueTableModel);
        cuesTable.getColumnModel().getColumn(0).setMaxWidth(35);

        cuesPane.getViewport().add(cuesTable);
        basic.add(cuesPane);

        commandPromptText = new JTextField(20);
        commandPromptText.setText("");
        basic.add(commandPromptText);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    final void setUpKeyCaptures(){
        cuesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    String description = cueTableModel.getCue(row).getDescription();
                    System.out.println(description);
                }
                if (me.getButton() == MouseEvent.BUTTON3) {
                    tablePMenu.show(me.getComponent(), me.getX(), me.getY());
                    contextClickPoint = p;
                }
            }
        });
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(final KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_TYPED) {
                    if (e.getKeyChar() == '\t'){
                        if ((e.getModifiers() & KeyEvent.SHIFT_MASK) == 0){
                            System.out.println("Cue Forward");
                        } else {
                            System.out.println("Cue Back");
                        }
                        return true;
                    } else if (e.getKeyChar() == '\n') {
                        System.out.println("Command Run");
                        cueTableModel.addCue(new Cue(commandPromptText.getText()));
                        /*try {
                            sm.playSound("knock");
                        } catch (UnsupportedAudioFileException ex) {
                            System.out.println("File is unsupported.");
                        }*/
                        return true;
                    } else if (e.getKeyChar() == '>') {
                        System.out.println("Step Forward");
                        return true;
                    } else if (e.getKeyChar() == '<') {
                        System.out.println("Step Backwards");
                        return true;
                    }
                }
                //Continue Propagation:
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    public final void initUI() {
        setLaF();

        makeMenuBar();

        makePanel();
        
        setUpKeyCaptures();
        
        buildPopUpMenu();
        
        setTitle("Synced Play");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SyncedPlay mainWindow = new SyncedPlay();
                mainWindow.setVisible(true);
            }
        });
    }
}
