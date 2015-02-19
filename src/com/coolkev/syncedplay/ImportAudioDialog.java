/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

class ImportAudioDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    private File selectedFile;
    
    private JLabel fileNameLabel;
    private JTextField keyTextField;
    
    public ImportAudioDialog() {
        super();
        initUI();
    }

    public final void initUI() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Import Audio File");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePanel.add(new JLabel("File:"));
        filePanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JButton fileSelectButton = new JButton("Select New File");
        fileSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                FileFilter WAVaudioFilter = new FileNameExtensionFilter("WAV Audio Files", "wav");
                FileFilter AUaudioFilter = new FileNameExtensionFilter("AU Audio Files", "au");
                fileChooser.setFileFilter(AUaudioFilter);
                fileChooser.setFileFilter(WAVaudioFilter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                  File file = fileChooser.getSelectedFile();
                  if (SoundTableModel.isFileSupported(file)){
                    selectedFile = file;
                    fileNameLabel.setText(file.getName());
                  } else {
                    ErrorDialog ed = new ErrorDialog("The file is not in a known format!");
                    ed.showOpenDialog();
                  }
                }
            }
        });
        filePanel.add(fileSelectButton);
        filePanel.add(Box.createRigidArea(new Dimension(7, 0)));
        fileNameLabel = new JLabel("No File Selected");
        filePanel.add(fileNameLabel);
        add(filePanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.X_AXIS));
        keyPanel.add(new JLabel("Keyword:"));
        keyPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        keyTextField = new JTextField("");
        keyTextField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, keyTextField.getPreferredSize().height));
        keyPanel.add(keyTextField);
        add(keyPanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                closeState = CANCEL_OPTION;
                dispose();
            }
        });
        buttonsPanel.add(close);
        JButton approveOption = new JButton("Import!");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (selectedFile == null){
                    ErrorDialog ed = new ErrorDialog("You must select a file!");
                    ed.showOpenDialog();
                } else if (keyTextField.getText().length() == 0){
                    ErrorDialog ed = new ErrorDialog("You must provide a keyword!");
                    ed.showOpenDialog();
                } else {
                    closeState = APPROVE_OPTION;
                    dispose();
                }
            }
        });
        buttonsPanel.add(approveOption);
        buttonsPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        add(buttonsPanel);
        

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Import Sound");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,180);
    }
    
    int showOpenDialog() {
        setVisible(true);
        return closeState;
    }
    
    File getFile() {
        return selectedFile;
    }
    
    String getKey() {
        return keyTextField.getText();
    }
}
