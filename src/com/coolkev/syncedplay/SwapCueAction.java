/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
class SwapCueAction extends KevinBaseAction {
    String text;
    CueTableModel ctm;
    
    public SwapCueAction(String text, ImageIcon icon, String desc, Integer mnemonic, CueTableModel ctm) {
        super(text, icon, desc, mnemonic);
        this.ctm = ctm;
    }
    public SwapCueAction(String text, String desc, Integer mnemonic, CueTableModel ctm) {
        super(text, desc, mnemonic);
        this.ctm = ctm;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        SwapCueDialog scd = new SwapCueDialog(ctm.getCues());
        if (scd.showOpenDialog() == ImportAudioDialog.APPROVE_OPTION){
            int first = scd.getFirstPosition();
            int second = scd.getSecondPosition();
            ctm.swapCues(first, second);
        }
    }   
    
}
