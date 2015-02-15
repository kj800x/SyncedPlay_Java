package syncedplay;

import java.awt.Dimension;
import javax.swing.BorderFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class SyncedPlay extends JFrame {

    public SyncedPlay() {
        initUI();
    }

    public final void initUI() {

        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        //JPanel bottom = new JPanel();
        //bottom.setAlignmentX(1f);
        //bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        JScrollPane cuesPane = new JScrollPane();
        JTextArea cuesArea = new JTextArea();

        cuesArea.setLineWrap(true);
        cuesArea.setText(" 1: Cues");
        cuesArea.setWrapStyleWord(true);
        cuesArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        cuesPane.getViewport().add(cuesArea);
        basic.add(cuesPane);
        
        JScrollPane commandPane = new JScrollPane();
        JTextArea commandArea = new JTextArea();

        commandArea.setLineWrap(true);
        commandArea.setText("Command Line");
        commandArea.setWrapStyleWord(true);
        commandArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        commandPane.getViewport().add(commandArea);
        basic.add(commandPane);
        
        JButton ok = new JButton("OK");
        JButton close = new JButton("Close");

        basic.add(ok);
        basic.add(Box.createRigidArea(new Dimension(5, 0)));
        basic.add(close);
        basic.add(Box.createRigidArea(new Dimension(15, 0)));

        //basic.add(bottom);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));

        setTitle("Synced Play");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SyncedPlay mainWindow = new SyncedPlay();
                mainWindow.setVisible(true);
            }
        });
    }
}