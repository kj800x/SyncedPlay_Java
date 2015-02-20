/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
class OnlineHelpAction extends KevinBaseAction {

    public OnlineHelpAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
        super(text, icon, desc, mnemonic);
    }

    public OnlineHelpAction(String text, String desc, Integer mnemonic) {
        super(text, desc, mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent q) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://redmondtheatretech.com/SyncedPlay/Help/"));
            } catch (IOException | URISyntaxException e) { /* TODO: error handling */ } 
        } else {
            ErrorDialog ed = new ErrorDialog("Navigate to http://redmondtheatretech.com/SyncedPlay/Help/ in your web browser.");
            ed.showOpenDialog();
        }   
    }

}
