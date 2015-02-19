/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kevin
 */
class LoadAction extends KevinBaseAction {
    String text;
    Callback callback;
    
    public LoadAction(String text, ImageIcon icon, String desc, Integer mnemonic, Callback r) {
        super(text, icon, desc, mnemonic);
        callback = r;
    }
    public LoadAction(String text, String desc, Integer mnemonic, Callback r) {
        super(text, desc, mnemonic);
        callback = r;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
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
