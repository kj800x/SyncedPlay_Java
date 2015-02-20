/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.util.Callback;
import com.coolkev.syncedplay.model.Cue;
import com.coolkev.syncedplay.model.CueTableModel;
import com.coolkev.syncedplay.swing.dialogs.NewCueDialog;
import com.coolkev.syncedplay.swing.dialogs.ImportAudioDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class NewCueAction extends KevinBaseAction {
    String text;
    Callback callback;
    CueTableModel ctm;
    final Component parent;
    
    public NewCueAction(String text, ImageIcon icon, String desc, Integer mnemonic, CueTableModel ctm, Callback r, final Component parent) {
        super(text, icon, desc, mnemonic);
        callback = r;
        this.ctm = ctm;
        this.parent = parent;
    }
    public NewCueAction(String text, String desc, Integer mnemonic, CueTableModel ctm, Callback r, final Component parent) {
        super(text, desc, mnemonic);
        callback = r;
        this.ctm = ctm;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        NewCueDialog ncd = new NewCueDialog(ctm.getCues(), parent);
        if (ncd.showDialog() == ImportAudioDialog.APPROVE_OPTION){
            String description = ncd.getDescription();
            Action[] actions = ncd.getActions(); 
            int beforeWhat = ncd.getPosition();
            callback.setArgs(new Cue(description, actions), beforeWhat);
            callback.run();
        }
    }   
    
}
