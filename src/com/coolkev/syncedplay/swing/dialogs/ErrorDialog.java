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

public class ErrorDialog extends JDialog {
    
    public ErrorDialog(String errorText, final Component parent) {
        super();
        initUI(errorText, parent);
    }

    private final void initUI(String errorText, final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = new JLabel(errorText);
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });
        add(close);
        
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle("Error");
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
