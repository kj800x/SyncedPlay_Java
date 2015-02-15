package syncedplay;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;


public class SyncedPlay extends JFrame {

    public SyncedPlay() {
        initUI();
    }

    public final void initUI() {
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            throw new ClassNotFoundException();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            try {
            // Set System L&F
                UIManager.setLookAndFeel(
                    //"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    //UIManager.getCrossPlatformLookAndFeelClassName());
                    UIManager.getSystemLookAndFeelClassName());
            } 
            catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException z) {
               // handle exception
            }
        }

        
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        JScrollPane cuesPane = new JScrollPane();
        String[] columnNames = {"id", "Description"};
        String[][] data = {{"1", "Show Start"}, {"120", "Show End"}};
        JTable cuesTable = new JTable(data, columnNames);
        TableColumn column = cuesTable.getColumnModel().getColumn(0);
        column.setMaxWidth(35);

        cuesPane.getViewport().add(cuesTable);
        basic.add(cuesPane);

        JTextField commandPromptText = new JTextField(20);
        commandPromptText.setText("kevin@kevin-mint-devel ~ $");
        basic.add(commandPromptText);
        /*
        JButton ok = new JButton("OK");
        JButton close = new JButton("Close");

        basic.add(ok);
        basic.add(Box.createRigidArea(new Dimension(5, 0)));
        basic.add(close);
        basic.add(Box.createRigidArea(new Dimension(15, 0)));
*/
        //basic.add(bottom);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));

        setTitle("Synced Play");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SyncedPlay mainWindow = new SyncedPlay();
                mainWindow.setVisible(true);
            }
        });
    }
}