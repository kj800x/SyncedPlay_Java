/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import static com.coolkev.syncedplay.SyncedPlay.overwriteToFile;
import com.coolkev.syncedplay.swing.tasks.SaveTask;
import com.coolkev.syncedplay.model.CueTableModel;
import com.coolkev.syncedplay.model.Cue;
import com.coolkev.syncedplay.model.SoundTableModel;
import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.util.Callback;
import com.coolkev.syncedplay.swing.action.QuitAction;
import com.coolkev.syncedplay.swing.action.SwapCueAction;
import com.coolkev.syncedplay.swing.action.SaveAction;
import com.coolkev.syncedplay.swing.action.ImportSoundAction;
import com.coolkev.syncedplay.swing.action.NewCueAction;
import com.coolkev.syncedplay.swing.action.AboutBoxAction;
import com.coolkev.syncedplay.swing.action.ManualAction;
import com.coolkev.syncedplay.swing.action.SaveAsAction;
import com.coolkev.syncedplay.swing.action.LoadAction;
import com.coolkev.syncedplay.util.FileCopier;
import com.coolkev.syncedplay.util.ActionsTextParser;
import com.coolkev.syncedplay.swing.dialogs.ErrorDialog;
import com.coolkev.syncedplay.swing.dialogs.EditCueDialog;
import com.coolkev.syncedplay.swing.dialogs.ConfirmDialog;
import com.coolkev.syncedplay.swing.dialogs.ProgressDialog;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.NoSuchElementException;
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
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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
        
        /*UIManager.put("nimbusLightBackground", new Color(10,10,10));
        UIManager.put("nimbusBase", new Color(0,0,0));
        UIManager.put("nimbusBlueGrey", new Color(0,0,0));
        UIManager.put("control", new Color(0,0,0));
        UIManager.put("text", new Color(255,255,255));*/
        
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            System.out.println(info.getName());
            if ("Nimbus".equals(info.getName())) {
                System.out.println("set LAF to "+ info.getName());
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    System.out.println("Couldn't find Nimbus");
                    //Logger.getLogger(SyncedPlay.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    static final String loadFromFile(File file){
        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException | NoSuchElementException ex) {
        }
        return "";
    }
    
    final void loadFromDirectory(String projectFilePath) {
        
        File projectFile = new File(projectFilePath);
        String projectName = projectFile.getName().replace(".sync", "");
        String projectDataDirPath = projectFile.getParentFile() + "/" + projectName + "_Data";
        File projectDataDir = new File(projectDataDirPath);
        if (!projectDataDir.exists()){
            ErrorDialog ed = new ErrorDialog("Project_Data folder doesn't exist. Loading failed!", this);
            ed.showDialog();
            return;
        }
        System.out.println("Loading from :" + projectFile);
        cueTableModel.blank();
        soundTableModel.blank();
        File cuesF = new File(projectDataDir.getAbsolutePath() + "/cues.txt");
        if (cuesF.canRead()){
            String cuesData = loadFromFile(cuesF);
            cueTableModel.load(cuesData);
        } else {
            ErrorDialog ed = new ErrorDialog("Couldn't load the cue file!", this);
            ed.showDialog();
        }
        File soundsF = new File(projectDataDir.getAbsolutePath() + "/sounds.txt");
        if (soundsF.canRead()){
            String soundsData = loadFromFile(soundsF);
            soundTableModel.load(soundsData, projectDataDir.getAbsolutePath());
        } else {
            ErrorDialog ed = new ErrorDialog("Couldn't load the sound file!", this);
            ed.showDialog();
        }
        currentSaveDirectory.delete(0, currentSaveDirectory.length());
        currentSaveDirectory.append(projectFile.getAbsolutePath());
    }
    
    public final String makeProjectFileContents(){
        return "This file is a placeholder file. It means nothing.";
    }

    final void saveToDirectory(String projectFilePath) {
        
        ProgressMonitor progressMonitor = new ProgressMonitor(null,
                "Saving",
                "", 0, 2+soundTableModel.getRowCount());
        progressMonitor.setProgress(0);
        SaveTask task = new SaveTask(projectFilePath, this, cueTableModel, soundTableModel, progressMonitor);
        task.execute();
        
        currentSaveDirectory.delete(0, currentSaveDirectory.length());
        currentSaveDirectory.append(new File(projectFilePath).getAbsolutePath());
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
        }, this);
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
        }, this);
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
        }, this);
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
        }, this);
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
        }, this);
        JMenuItem newCueActionMenuItem = new JMenuItem(newCueAction);
        cues.add(newCueActionMenuItem);
        
        /*MoveCueAction moveCueAction = new MoveCueAction("Move Cue", "Move a cue", KeyEvent.VK_M, cueTableModel, this);
        JMenuItem moveCueActionMenuItem = new JMenuItem(moveCueAction);
        cues.add(moveCueActionMenuItem);*/
        
        SwapCueAction swapCueAction = new SwapCueAction("Swap Cue", "Swaps two cues", KeyEvent.VK_S, cueTableModel, this);
        JMenuItem swapCueActionMenuItem = new JMenuItem(swapCueAction);
        cues.add(swapCueActionMenuItem);
        
        
        menubar.add(cues);
        
        menubar.add(Box.createHorizontalGlue());
        
        JMenu help = new JMenu("Help");
        ManualAction manualAction = new ManualAction("Manual", "Brings up the manual", KeyEvent.VK_F1, this);
        JMenuItem manualActionMenuItem = new JMenuItem(manualAction);
        help.add(manualActionMenuItem);
        AboutBoxAction aboutBoxAction = new AboutBoxAction("About", "Loads the About Page", KeyEvent.VK_A, this);
        JMenuItem aboutBoxActionMenuItem = new JMenuItem(aboutBoxAction);
        help.add(aboutBoxActionMenuItem);
        
        menubar.add(help);
        
        setJMenuBar(menubar);
    }

    final void runAction(Action a) {
        switch (a.handler()) {
            case Action.HANDLER_CUE_SUBSYSTEM:
                cueTableModel.runAction(a);
                break;
            case Action.HANDLER_SOUND_SUBSYSTEM:
                soundTableModel.runAction(a);
                break;
        }
    }

    final void buildPopUpMenu() {
        cuesTablePMenu = new JPopupMenu();
        final Container mainWindow = this;
        JMenuItem editCue = new JMenuItem("Edit");
        editCue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = cuesTable.rowAtPoint(contextClickPoint);
                EditCueDialog ecd = new EditCueDialog(cueTableModel.getCue(row), mainWindow);
                if (ecd.showDialog() == EditCueDialog.APPROVE_OPTION) {
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
                ConfirmDialog confirmDialog = new ConfirmDialog("Are you sure you want to delete this cue?", mainWindow);
                if (confirmDialog.showDialog() == EditCueDialog.APPROVE_OPTION) {
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
        JMenuItem loopSound = new JMenuItem("Loop");
        loopSound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Point pt = ((JPopupMenu) ((JMenuItem) e.getSource()).getParent()).getLocation(); //TODO: This is supposed to get the screen location, of the upper right corner of the popup menu, but this doesn't work.
                int row = soundsTable.rowAtPoint(contextClickPoint);
                soundTableModel.loopSound(soundTableModel.keyFromRow(row));
            }
        });
        soundsTablePMenu.add(loopSound);
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
        cueTableModel.setTable(cuesTable);
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

    public final static void overwriteToFile(File f, String s) {
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
