/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.action;

import com.coolkev.syncedplay.swing.dialogs.ErrorDialog;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
public class OnlineHelpAction extends KevinBaseAction {

    final Component parent;
    
    public OnlineHelpAction(String text, ImageIcon icon, String desc, Integer mnemonic, final Component parent) {
        super(text, icon, desc, mnemonic);
        this.parent = parent;
    }

    public OnlineHelpAction(String text, String desc, Integer mnemonic, final Component parent) {
        super(text, desc, mnemonic);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent q) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://redmondtheatretech.com/SyncedPlay/Help/"));
            } catch (IOException | URISyntaxException e) { /* TODO: error handling */ } 
        } else {
            ErrorDialog ed = new ErrorDialog("Navigate to http://redmondtheatretech.com/SyncedPlay/Help/ in your web browser.", parent);
            ed.showDialog();
        }   
    }

}
