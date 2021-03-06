/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.swing.dialogs.AboutDialog;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class AboutBoxAction extends KevinBaseAction {
    
    final Component parent;

    public AboutBoxAction(String text, ImageIcon icon, String desc, Integer mnemonic, final Component parent) {
        super(text, icon, desc, mnemonic);
        this.parent = parent;
    }

    public AboutBoxAction(String text, String desc, Integer mnemonic, final Component parent) {
        super(text, desc, mnemonic);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent q) {
        AboutDialog ad = new AboutDialog("Synced Play", "© 2015 Kevin Johnson", "Verision 0.1", parent);
        ad.showDialog();
    }

}
