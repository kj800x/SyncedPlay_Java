/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.dialogs;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class AboutDialog extends JDialog {
    
    public AboutDialog(String name, String copy, String version, final Component parent) {
        super();
        initUI(name, copy, version, parent);
    }

    private void initUI(String name, String copy, String version, final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(0.5f);
        add(nameLabel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel copyLabel = new JLabel(copy);
        copyLabel.setAlignmentX(0.5f);
        add(copyLabel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel versionLabel = new JLabel(version);
        versionLabel.setAlignmentX(0.5f);
        add(versionLabel);
        
        add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        close.setAlignmentX(0.5f);
        add(close);
        
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("About");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        System.out.println(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public void showDialog() {
        setVisible(true);
    }
}
