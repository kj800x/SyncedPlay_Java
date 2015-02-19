/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class EditCueDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    private JTextArea actionsTextArea;
    private JTextField descTextField;
    
    public EditCueDialog(Cue cue) {
        super();
        initUI(cue);
    }

    public final void initUI(Cue cue) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Edit Cue");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.X_AXIS));
        descPanel.add(new JLabel("Description: "));
        descPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        descTextField = new JTextField(cue.getDescription());
        descTextField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, descTextField.getPreferredSize().height));
        descPanel.add(descTextField);
        add(descPanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.add(new JLabel("Actions: "));
        actionsPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        actionsTextArea = new JTextArea(cue.getActionsText());
        actionsPanel.add(actionsTextArea);
        add(actionsPanel);
        
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
        JButton approveOption = new JButton("Edit!");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (descTextField.getText().length() == 0){
                    ErrorDialog ed = new ErrorDialog("You must provide a description!");
                    ed.showOpenDialog();
                } else if (!ActionsTextParser.canParseText(actionsTextArea.getText())) {
                    ErrorDialog ed = new ErrorDialog("Some of the actions aren't valid!");
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
        setTitle("Edit Cue");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300,180);
    }
    
    int showOpenDialog() {
        setVisible(true);
        return closeState;
    }
    
    Cue getCue() {
        return new Cue(descTextField.getText(), ActionsTextParser.parseText(actionsTextArea.getText()));
    }
}
