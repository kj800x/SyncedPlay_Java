/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syncedplay;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author kevin
 */
public class SaveAsAction extends KevinBaseAction {
    
    private final Callback callback;

    public SaveAsAction(String text, String desc, Integer mnemonic, Callback r) {
        super(text, desc, mnemonic);
        this.callback = r;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          callback.setArgs(file.getAbsolutePath());
          callback.run();
        }
    }
    
    
    
}
