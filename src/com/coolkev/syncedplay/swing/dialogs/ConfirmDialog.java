/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConfirmDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    public ConfirmDialog(String message, final Component parent) {
        super();
        initUI(message, parent);
    }

    private void initUI(String message, final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel(message);
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        JButton close = new JButton("Cancel");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                closeState = CANCEL_OPTION;
                dispose();
            }
        });
        buttonsPanel.add(close);
        JButton approveOption = new JButton("Ok");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                closeState = APPROVE_OPTION;
                dispose();
            }
        });
        buttonsPanel.add(approveOption);
        add(buttonsPanel);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Confirm Dialog");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public int showDialog() {
        setVisible(true);
        return closeState;
    }
}
