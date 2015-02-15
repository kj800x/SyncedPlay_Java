package syncedplay;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author kevin
 */
class LoadAction extends KevinBaseAction {
    String text;
    Callback callback;
    
    public LoadAction(String text, ImageIcon icon, String desc, Integer mnemonic, Callback r) {
        super(text, icon, desc, mnemonic);
        callback = r;
    }
    public LoadAction(String text, String desc, Integer mnemonic, Callback r) {
        super(text, desc, mnemonic);
        callback = r;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileopen = new JFileChooser();
        FileFilter cueFilter = new FileNameExtensionFilter("Cue Files", "cue");
        fileopen.addChoosableFileFilter(cueFilter);
        FileFilter soundFilter = new FileNameExtensionFilter("Sound Files", "mp3");
        fileopen.addChoosableFileFilter(soundFilter);
        JPanel panel = new JPanel();
        int ret = fileopen.showDialog(panel, "Open file");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            callback.setArgs(file);
            callback.run();
        }
    }   
    
    static String readFile(File file) {

        StringBuffer fileBuffer = null;
        String fileString = null;
        String line = null;

        try {
            FileReader in = new FileReader(file);
            BufferedReader brd = new BufferedReader(in);
            fileBuffer = new StringBuffer();

            while ((line = brd.readLine()) != null) {
                fileBuffer.append(line).append(
                        System.getProperty("line.separator"));
            }

            in.close();
            fileString = fileBuffer.toString();
        } catch (IOException e) {
            return null;
        }
        return fileString;
    }

}
