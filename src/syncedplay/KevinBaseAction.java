/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package syncedplay;

import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

/**
 *
 * @author kevin
 */
abstract class KevinBaseAction extends javax.swing.AbstractAction {
    public KevinBaseAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
    }
    
    public KevinBaseAction(String text,
                           String desc, Integer mnemonic) {
        super(text);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
    }
}
