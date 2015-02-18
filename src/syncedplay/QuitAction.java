/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package syncedplay;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
class QuitAction extends KevinBaseAction {
    public QuitAction(String text, ImageIcon icon, String desc, Integer mnemonic) {
        super(text, icon, desc, mnemonic);
    }
    public QuitAction(String text, String desc, Integer mnemonic) {
        super(text, desc, mnemonic);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }   
}
