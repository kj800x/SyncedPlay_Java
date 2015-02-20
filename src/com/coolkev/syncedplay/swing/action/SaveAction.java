/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.util.Callback;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kevin
 */
public class SaveAction extends KevinBaseAction {
    
    private final Callback callback;
    private final StringBuilder csd;
    final Component parent;

    public SaveAction(String text, String desc, Integer mnemonic, StringBuilder currentSaveDir, Callback r, final Component parent) {
        super(text, desc, mnemonic);
        this.callback = r;
        this.csd = currentSaveDir;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!"".equals(csd.toString())){
              callback.setArgs(csd.toString());
              callback.run();
        } else {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter syncFilter = new FileNameExtensionFilter("Synced Play Projects", "sync");
            fileChooser.setFileFilter(syncFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
              File file = fileChooser.getSelectedFile();
              if (!file.getName().endsWith(".sync")) {
                  file = new File(file.getAbsolutePath() + ".sync");
              }
              callback.setArgs(file.getAbsolutePath());
              callback.run();
            }
        }
    }
    
}
