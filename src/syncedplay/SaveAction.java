/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package syncedplay;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author kevin
 */
public class SaveAction extends KevinBaseAction {
    
    private final Callback callback;
    private final StringBuilder csd;

    public SaveAction(String text, String desc, Integer mnemonic, StringBuilder currentSaveDir, Callback r) {
        super(text, desc, mnemonic);
        this.callback = r;
        this.csd = currentSaveDir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!"".equals(csd.toString())){
              callback.setArgs(csd.toString());
              callback.run();
        } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
              File file = fileChooser.getSelectedFile();
              callback.setArgs(file.getAbsolutePath());
              callback.run();
            }
        }
    }
    
    
    
}
