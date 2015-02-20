/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.util.Callback;
import com.coolkev.syncedplay.swing.dialogs.ImportAudioDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class ImportSoundAction extends KevinBaseAction {
    String text;
    Callback callback;
    final Component parent;
    
    public ImportSoundAction(String text, ImageIcon icon, String desc, Integer mnemonic, Callback r, final Component parent) {
        super(text, icon, desc, mnemonic);
        callback = r;
        this.parent = parent;
    }
    public ImportSoundAction(String text, String desc, Integer mnemonic, Callback r, final Component parent) {
        super(text, desc, mnemonic);
        callback = r;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ImportAudioDialog iad = new ImportAudioDialog(parent);
        if (iad.showDialog() == ImportAudioDialog.APPROVE_OPTION){
            File file = iad.getFile();
            String key = iad.getKey(); 
            System.out.println(key);
            System.out.println(file);
            callback.setArgs(key, file);
            callback.run();
        }
    }   
    
}
