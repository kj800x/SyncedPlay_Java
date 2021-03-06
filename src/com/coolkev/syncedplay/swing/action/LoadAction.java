/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.util.Callback;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kevin
 */
public class LoadAction extends KevinBaseAction {
    String text;
    Callback callback;
    final Component parent;
    
    public LoadAction(String text, ImageIcon icon, String desc, Integer mnemonic, Callback r, final Component parent) {
        super(text, icon, desc, mnemonic);
        callback = r;
        this.parent = parent;
    }
    public LoadAction(String text, String desc, Integer mnemonic, Callback r, final Component parent) {
        super(text, desc, mnemonic);
        callback = r;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter syncFilter = new FileNameExtensionFilter("Synced Play Projects", "sync");
        fileChooser.setFileFilter(syncFilter);
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          callback.setArgs(file.getAbsolutePath());
          callback.run();
        }
    }   
    
    static String readFile(File file) {
        StringBuffer fileBuffer;
        String fileString;
        String line;

        try {
            try (FileReader in = new FileReader(file)) {
                BufferedReader brd = new BufferedReader(in);
                fileBuffer = new StringBuffer();
                
                while ((line = brd.readLine()) != null) {
                    fileBuffer.append(line).append(
                            System.getProperty("line.separator"));
                }
            }
            fileString = fileBuffer.toString();
        } catch (IOException e) {
            return null;
        }
        return fileString;
    }
}
