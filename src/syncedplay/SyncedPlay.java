package syncedplay;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;
import static syncedplay.LoadAction.readFile;

public class SyncedPlay extends JFrame {

    SoundManager sm = new SoundManager();
    
    public SyncedPlay() {
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

    final void makePanel() {

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        JScrollPane cuesPane = new JScrollPane();
        String[] columnNames = {"id", "Description"};
        String[][] data = {{"1", "Show Start"}, {"120", "Show End"}};
        JTable cuesTable = new JTable(data, columnNames);
        TableColumn column = cuesTable.getColumnModel().getColumn(0);
        column.setMaxWidth(35);

        cuesPane.getViewport().add(cuesTable);
        basic.add(cuesPane);

        JTextField commandPromptText = new JTextField(20);
        commandPromptText.setText("kevin@kevin-mint-devel ~ $");
        basic.add(commandPromptText);
        /*
         JButton ok = new JButton("OK");
         JButton close = new JButton("Close");

         basic.add(ok);
         basic.add(Box.createRigidArea(new Dimension(5, 0)));
         basic.add(close);
         basic.add(Box.createRigidArea(new Dimension(15, 0)));
         */
        //basic.add(bottom);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));
    }
    
    final void setUpKeyCaptures(){
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
                    } else if (e.getKeyChar() == '\n') {
                        System.out.println("Command Run");
                        sm.playSound("knock");
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
