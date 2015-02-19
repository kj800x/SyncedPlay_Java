/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
class ImportSoundAction extends KevinBaseAction {
    String text;
    Callback callback;
    
    public ImportSoundAction(String text, ImageIcon icon, String desc, Integer mnemonic, Callback r) {
        super(text, icon, desc, mnemonic);
        callback = r;
    }
    public ImportSoundAction(String text, String desc, Integer mnemonic, Callback r) {
        super(text, desc, mnemonic);
        callback = r;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        ImportAudioDialog iad = new ImportAudioDialog();
        if (iad.showOpenDialog() == ImportAudioDialog.APPROVE_OPTION){
            File file = iad.getFile();
            String key = iad.getKey(); 
            System.out.println(key);
            System.out.println(file);
            callback.setArgs(key, file);
            callback.run();
        }
    }   
    
}
