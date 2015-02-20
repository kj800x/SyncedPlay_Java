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
class AboutBoxAction extends KevinBaseAction {

    public AboutBoxAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
        super(text, icon, desc, mnemonic);
    }

    public AboutBoxAction(String text, String desc, Integer mnemonic) {
        super(text, desc, mnemonic);
    }

    @Override
    public void actionPerformed(ActionEvent q) {
        ErrorDialog ed = new ErrorDialog("Synced Play. (copy) 2015 Kevin Johnson. Version Before 1");
        ed.showOpenDialog();
    }

}
