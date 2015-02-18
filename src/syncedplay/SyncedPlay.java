/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    private final SoundManager sm;
    private final CueTableModel cueTableModel;

    private final StringBuilder currentSaveDirectory = new StringBuilder();

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

    final void loadFromDirectory(String directory) {
        System.out.println("Loading from :" + directory);
        currentSaveDirectory.delete(0, currentSaveDirectory.length());
        currentSaveDirectory.append(directory);

    }

    final void saveToDirectory(String directory) {
        System.out.println("Saving to :" + directory);
        File directoryF = new File(directory);
        //Make sure the directory exists, or create it if it doesn't
        if (!directoryF.exists()) {
            if (directoryF.mkdir()) {
                System.out.println("Directory Created");
            } else {
                System.out.println("Failed to create directory");
                throw new Error("Couldn't create directory");
            }
        }
        //Create Cues File
        File cuesF = new File(directory + "/cues.txt");
        overwriteToFile(cuesF, cueTableModel.save());
        //Create Cues File
        File soundsF = new File(directory + "/sounds.txt");
        overwriteToFile(soundsF, sm.save());
        //Update the current loaded file
        currentSaveDirectory.delete(0, currentSaveDirectory.length());
        currentSaveDirectory.append(directory);
    }

    final void makeMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);

        SaveAction sa = new SaveAction("Save", "Saves a file", KeyEvent.VK_S, currentSaveDirectory, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                saveToDirectory((String) args[0]);
                //System.out.println((String) args[0]);
                //TODO do stuff
            }
        });
        JMenuItem saveMenuItem = new JMenuItem(sa);
        file.add(saveMenuItem);
        ;
        SaveAsAction saa = new SaveAsAction("Save as", "Saves a file", KeyEvent.VK_S, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                saveToDirectory((String) args[0]);
                //System.out.println((String) args[0]);
                //TODO do stuff
            }
        });
        JMenuItem saveAsMenuItem = new JMenuItem(saa);
        file.add(saveAsMenuItem);

        LoadAction la = new LoadAction("Open", "Loads a file", KeyEvent.VK_E, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                loadFromDirectory((String) args[0]);
                //sm.learnSound("knock", (File) args[0]);
                //String text = readFile((File) args[0]);
                //System.out.print(text);
            }
        });
        JMenuItem loadMenuItem = new JMenuItem(la);
        file.add(loadMenuItem);
        
        file.addSeparator();
        
        ImportSoundAction importAction = new ImportSoundAction("Import Audio", "Imports an Audio File", KeyEvent.VK_I, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                sm.learnSound((String) args[0], (File) args[1]);
            }
        });
        JMenuItem importActionMenuItem = new JMenuItem(importAction);
        file.add(importActionMenuItem);
        
        file.addSeparator();

        QuitAction qa = new QuitAction("Exit", "Closes the application", KeyEvent.VK_E);
        JMenuItem exitMenuItem = new JMenuItem(qa);
        file.add(exitMenuItem);

        menubar.add(file);
        setJMenuBar(menubar);
    }

    final void buildPopUpMenu() {
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
        cuesTable.getColumnModel().getColumn(1).setMaxWidth(20);

        cuesPane.getViewport().add(cuesTable);
        basic.add(cuesPane);

        commandPromptText = new JTextField(20);
        commandPromptText.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, commandPromptText.getPreferredSize().height));
        commandPromptText.setText("");
        basic.add(commandPromptText);
    }

    final void setUpKeyCaptures() {
        cuesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
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
                    if (e.getKeyChar() == '\t') {
                        if ((e.getModifiers() & KeyEvent.SHIFT_MASK) == 0) {
                            System.out.println("Cue Forward");
                            if (cueTableModel.getRowCount() >= cueTableModel.getNextCueIndex()) {
                                Cue cueToRun = cueTableModel.getCue(cueTableModel.getNextCueIndex() - 1);
                                System.out.println("Running Cue: " + cueToRun.getDescription());
                                if (cueTableModel.getRowCount() > cueTableModel.getNextCueIndex()) {
                                    cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() + 1);
                                }
                            }
                            /* } else {
                             System.out.println("Cue Back");
                             if (cueTableModel.getNextCueIndex() >= 1){
                             Cue cueToRun = cueTableModel.getCue(cueTableModel.getNextCueIndex());
                             System.out.println("Running Cue: "+ cueToRun.getDescription());
                             cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() + 1);
                             }*/
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
                        if (cueTableModel.getRowCount() > cueTableModel.getNextCueIndex()) {
                            cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() + 1);
                        }
                        return true;
                    } else if (e.getKeyChar() == '<') {
                        System.out.println("Step Backwards");
                        if (cueTableModel.getNextCueIndex() >= 1) {
                            cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() - 1);
                        }
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

    final static void overwriteToFile(File f, String s) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                                            new FileOutputStream(f), "utf-8"));
            writer.write(s);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }
}
