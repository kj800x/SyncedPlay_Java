/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.model.CueTableModel;
import com.coolkev.syncedplay.swing.dialogs.ImportAudioDialog;
import com.coolkev.syncedplay.swing.dialogs.SwapCueDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class SwapCueAction extends KevinBaseAction {
    String text;
    CueTableModel ctm;
    final Component parent;
    
    public SwapCueAction(String text, ImageIcon icon, String desc, Integer mnemonic, CueTableModel ctm, final Component parent) {
        super(text, icon, desc, mnemonic);
        this.ctm = ctm;
        this.parent = parent;
    }
    public SwapCueAction(String text, String desc, Integer mnemonic, CueTableModel ctm, final Component parent) {
        super(text, desc, mnemonic);
        this.ctm = ctm;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        SwapCueDialog scd = new SwapCueDialog(ctm.getCues(), parent);
        if (scd.showDialog() == ImportAudioDialog.APPROVE_OPTION){
            int first = scd.getFirstPosition();
            int second = scd.getSecondPosition();
            ctm.swapCues(first, second);
        }
    }   
    
}
