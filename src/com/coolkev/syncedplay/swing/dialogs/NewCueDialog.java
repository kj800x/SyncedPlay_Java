/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.dialogs;

import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.util.ActionsTextParser;
import com.coolkev.syncedplay.model.Cue;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NewCueDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    private JTextField descTextField;
    private JTextArea actionsTextArea;
    private int insertAfter = 0;
    String[] cueNames;
    
    public NewCueDialog(ArrayList<Cue> cues, final Component parent) {
        super();
        
        cueNames = new String[cues.size() + 1];
        int i = 0;
        for (Cue cue : cues){
            cueNames[i] = "Before \"" + cue.getDescription() + "\"";
            i++;
        }
        cueNames[i] = "<At The End>";
        insertAfter = i;
        
        initUI(cues, parent);
    }

    private final void initUI(ArrayList<Cue> cues, final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Create new cue");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.X_AXIS));
        descPanel.add(new JLabel("Description: "));
        descPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        descTextField = new JTextField("");
        descTextField.setColumns(15);
        descTextField.setMinimumSize(descTextField.getPreferredSize());
        descTextField.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, descTextField.getPreferredSize().height));
        descPanel.add(descTextField);
        add(descPanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
        actionsPanel.add(new JLabel("Actions: "));
        actionsPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        actionsTextArea = new JTextArea("");
        actionsTextArea.setRows(2);
        actionsTextArea.setMinimumSize(actionsTextArea.getPreferredSize());
        actionsPanel.add(actionsTextArea);
        add(actionsPanel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel afterPanel = new JPanel();
        afterPanel.setLayout(new BoxLayout(afterPanel, BoxLayout.X_AXIS));
        afterPanel.add(new JLabel("Insert: "));
        afterPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JComboBox box = new JComboBox<>(cueNames);
        box.setSelectedItem("<At The End>");
        box.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //e.getItem();
                insertAfter = java.util.Arrays.asList(cueNames).indexOf(e.getItem());
            }
        });
        afterPanel.add(box);
        
        add(afterPanel);
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
        JButton approveOption = new JButton("Create!");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (!ActionsTextParser.canParseText(actionsTextArea.getText())){
                    ErrorDialog ed = new ErrorDialog("The actions are not valid!", parent);
                    ed.showDialog();
                } else if (descTextField.getText().length() == 0){
                    ErrorDialog ed = new ErrorDialog("You must provide a description!", parent);
                    ed.showDialog();
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
        setTitle("New Cue");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public int showDialog() {
        setVisible(true);
        return closeState;
    }
    
    public int getPosition() {
        return insertAfter;
    }
    
    public Action[] getActions() {
        return ActionsTextParser.parseText(actionsTextArea.getText());
    }
    
    public String getDescription() {
        return descTextField.getText();
    }
}
