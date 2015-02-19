/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
class NewCueAction extends KevinBaseAction {
    String text;
    Callback callback;
    CueTableModel ctm;
    
    public NewCueAction(String text, ImageIcon icon, String desc, Integer mnemonic, CueTableModel ctm, Callback r) {
        super(text, icon, desc, mnemonic);
        callback = r;
        this.ctm = ctm;
    }
    public NewCueAction(String text, String desc, Integer mnemonic, CueTableModel ctm, Callback r) {
        super(text, desc, mnemonic);
        callback = r;
        this.ctm = ctm;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        NewCueDialog ncd = new NewCueDialog(ctm.getCues());
        if (ncd.showOpenDialog() == ImportAudioDialog.APPROVE_OPTION){
            String description = ncd.getDescription();
            Action[] actions = ncd.getActions(); 
            int beforeWhat = ncd.getPosition();
            callback.setArgs(new Cue(description, actions), beforeWhat);
            callback.run();
        }
    }   
    
}
