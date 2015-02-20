/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private final CueTableModel cueTableModel;
    private final SoundTableModel soundTableModel;

    private final StringBuilder currentSaveDirectory = new StringBuilder();

    private Point contextClickPoint;
    private JTextField commandPromptText;
    private JTable cuesTable;
    private JTable soundsTable;
    private JPopupMenu cuesTablePMenu;
    private JPopupMenu soundsTablePMenu;

    public SyncedPlay() {
        this.cueTableModel = new CueTableModel();
        this.soundTableModel = new SoundTableModel();
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

    static final String loadFromFile(File file){
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException ex) {}
        return "";
    }
    
    final void loadFromDirectory(String directory) {
        System.out.println("Loading from :" + directory);
        cueTableModel.blank();
        soundTableModel.blank();
        File cuesF = new File(directory + "/cues.txt");
        if (cuesF.canRead()){
            String cuesData = loadFromFile(cuesF);
            cueTableModel.load(cuesData);
        } else {
            ErrorDialog ed = new ErrorDialog("Couldn't load the cue file!");
            ed.showOpenDialog();
        }
        File soundsF = new File(directory + "/sounds.txt");
        if (soundsF.canRead()){
            String soundsData = loadFromFile(soundsF);
            soundTableModel.load(soundsData, directory);
        } else {
            ErrorDialog ed = new ErrorDialog("Couldn't load the sound file!");
            ed.showOpenDialog();
        }
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
        //Create Sounds File
        File soundsF = new File(directory + "/sounds.txt");
        overwriteToFile(soundsF, soundTableModel.save());
        //Copy all sounds to the new directory
        File[] files = soundTableModel.getFiles();
        for (File file : files){
            FileCopier.copyFile(file, new File(directory + "/" + file.getName()));
        }
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

        QuitAction qa = new QuitAction("Exit", "Closes the application", KeyEvent.VK_E);
        JMenuItem exitMenuItem = new JMenuItem(qa);
        file.add(exitMenuItem);

        menubar.add(file);
        
        JMenu sounds = new JMenu("Sounds");
  
        ImportSoundAction importAction = new ImportSoundAction("Import Audio", "Imports an Audio File", KeyEvent.VK_I, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                soundTableModel.learnSound((String) args[0], (File) args[1]);
            }
        });
        JMenuItem importActionMenuItem = new JMenuItem(importAction);
        sounds.add(importActionMenuItem);
        menubar.add(sounds);
        
        JMenu cues = new JMenu("Cues");
        NewCueAction newCueAction = new NewCueAction("New Cue", "Creates a new cue", KeyEvent.VK_C, cueTableModel, new Callback() {
            @Override
            public void run() {
                Object[] args = getArgs();
                cueTableModel.addCue((Cue) args[0], (int) args[1]);
//                soundTableModel.learnSound((String) args[0], (File) args[1]);
            }
        });
        JMenuItem newCueActionMenuItem = new JMenuItem(newCueAction);
        cues.add(newCueActionMenuItem);
        SwapCueAction swapCueAction = new SwapCueAction("Swap Cue", "Swaps two cues", KeyEvent.VK_S, cueTableModel);
        JMenuItem swapCueActionMenuItem = new JMenuItem(swapCueAction);
        cues.add(swapCueActionMenuItem);
        
        menubar.add(cues);
        
        setJMenuBar(menubar);
    }

    final void runAction(Action a) {
        switch (a.handler()) {
//            case Action.HANDLER_CUE_SUBSYSTEM:
//                cueTableModel.runAction(a);
//                break;
            case Action.HANDLER_SOUND_SUBSYSTEM:
                soundTableModel.runAction(a);
                break;
        }
    }

    final void buildPopUpMenu() {
        cuesTablePMenu = new JPopupMenu();

        JMenuItem editCue = new JMenuItem("Edit");
        editCue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = cuesTable.rowAtPoint(contextClickPoint);
                EditCueDialog ecd = new EditCueDialog(cueTableModel.getCue(row));
                if (ecd.showOpenDialog() == EditCueDialog.APPROVE_OPTION) {
                    cueTableModel.setCue(row, ecd.getCue());
                }
            }
        });
        cuesTablePMenu.add(editCue);
        
        JMenuItem deleteCue = new JMenuItem("Delete");
        deleteCue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = cuesTable.rowAtPoint(contextClickPoint);
                ConfirmDialog confirmDialog = new ConfirmDialog("Are you sure you want to delete this cue?");
                if (confirmDialog.showOpenDialog() == EditCueDialog.APPROVE_OPTION) {
                    System.out.println("Deleting " + row);
                    cueTableModel.deleteCue(row);
                }
            }
        });
        cuesTablePMenu.add(deleteCue);

        soundsTablePMenu = new JPopupMenu();
        JMenuItem playSound = new JMenuItem("Play");
        playSound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Point pt = ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getLocation(); //TODO: This is supposed to get the screen location, of the upper right corner of the popup menu, but this doesn't work.
                int row = soundsTable.rowAtPoint(contextClickPoint);
                soundTableModel.playSound(soundTableModel.keyFromRow(row));
            }
        });
        soundsTablePMenu.add(playSound);
        JMenuItem stopSound = new JMenuItem("Stop");
        stopSound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Point pt = ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getLocation(); //TODO: This is supposed to get the screen location, of the upper right corner of the popup menu, but this doesn't work.
                int row = soundsTable.rowAtPoint(contextClickPoint);
                soundTableModel.stopSound(soundTableModel.keyFromRow(row));
            }
        });
        soundsTablePMenu.add(stopSound);
    }

    final void makePanel() {
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        JScrollPane soundsPane = new JScrollPane();
        //soundTableModel.setSounds(cues);
        soundsTable = new JTable(soundTableModel);
        //soundsTable.getColumnModel().getColumn(0).setMaxWidth(35);
        //soundsTable.getColumnModel().getColumn(1).setMaxWidth(20);
        soundsPane.getViewport().add(soundsTable);
        basic.add(soundsPane);

        JScrollPane cuesPane = new JScrollPane();
        ArrayList<Cue> cues = new ArrayList();
        //cues.add(new Cue("One"));
        //cues.add(new Cue("Two"));
        //cues.add(new Cue("Three"));
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
                    cuesTablePMenu.show(me.getComponent(), me.getX(), me.getY());
                    contextClickPoint = p;
                }
            }
        });
        soundsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    String key = soundTableModel.keyFromRow(row);
                    soundTableModel.playSound(key);
                }
                if (me.getButton() == MouseEvent.BUTTON3) {
                    soundsTablePMenu.show(me.getComponent(), me.getX(), me.getY());
                    contextClickPoint = p;
                }
            }
        });
        KeyListener kl = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\t') {
                    if ((e.getModifiers() & KeyEvent.SHIFT_MASK) == 0) {
                        System.out.println("Cue Forward");
                        if (cueTableModel.getRowCount() >= cueTableModel.getNextCueIndex()) {
                            Cue cueToRun = cueTableModel.getCue(cueTableModel.getNextCueIndex() - 1);
                            System.out.println("Running Cue: " + cueToRun.getDescription());
                            if (cueTableModel.getRowCount() > cueTableModel.getNextCueIndex()) {
                                cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() + 1);
                            }
                            Action[] actions = cueToRun.getActions();
                            for (Action action : actions) {
                                runAction(action);
                            }
                        }
                    }
                    e.consume();
                } else if (e.getKeyChar() == '\n') {
                    System.out.println("Command Run");
                    if (ActionsTextParser.canParseText(commandPromptText.getText())) {
                        Action[] actions = ActionsTextParser.parseText(commandPromptText.getText());
                        for (Action action : actions) {
                            runAction(action);
                        }
                        commandPromptText.setText("");
                    } else {
                        System.out.println("Cannot run command");
                    }
                    //cueTableModel.addCue(new Cue());
                    e.consume();
                } else if (e.getKeyChar() == '!') {
                    System.out.println("PANIC");
                    soundTableModel.panic();
                    e.consume();
                } else if (e.getKeyChar() == '>') {
                    System.out.println("Step Forward");
                    if (cueTableModel.getRowCount() > cueTableModel.getNextCueIndex()) {
                        cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() + 1);
                    }
                    e.consume();
                } else if (e.getKeyChar() == '<') {
                    System.out.println("Step Backwards");
                    if (cueTableModel.getNextCueIndex() >= 2) {
                        cueTableModel.setNextCueIndex(cueTableModel.getNextCueIndex() - 1);
                    }
                    e.consume();
                }
            }
        };
        commandPromptText.addKeyListener(kl);
        soundsTable.addKeyListener(kl);
        cuesTable.addKeyListener(kl);
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
