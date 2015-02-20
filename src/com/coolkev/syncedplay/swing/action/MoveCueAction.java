/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.model.CueTableModel;
import com.coolkev.syncedplay.swing.dialogs.ImportAudioDialog;
import com.coolkev.syncedplay.swing.dialogs.MoveCueDialog;
import com.coolkev.syncedplay.swing.dialogs.SwapCueDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class MoveCueAction extends KevinBaseAction {
    String text;
    CueTableModel ctm;
    final Component parent;
    
    public MoveCueAction(String text, ImageIcon icon, String desc, Integer mnemonic, CueTableModel ctm, final Component parent) {
        super(text, icon, desc, mnemonic);
        this.ctm = ctm;
        this.parent = parent;
    }
    public MoveCueAction(String text, String desc, Integer mnemonic, CueTableModel ctm, final Component parent) {
        super(text, desc, mnemonic);
        this.ctm = ctm;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        MoveCueDialog mcd = new MoveCueDialog(ctm.getCues(), parent);
        if (mcd.showDialog() == MoveCueDialog.APPROVE_OPTION){
            int oldIndex = mcd.getPositionOfCueToMove();
            int newIndex = mcd.getDestinationPosition();
            ctm.moveCue(oldIndex, newIndex);
        }
    }   
    
}
