/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.dialogs;

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

public class SwapCueDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    String[] cueNames;
    private int firstPosition;
    private int secondPosition;
    private final int defaultPosition;
    
    public SwapCueDialog(ArrayList<Cue> cues, final Component parent) {
        super();
        
        cueNames = new String[cues.size() + 1];
        int i = 0;
        for (Cue cue : cues){
            cueNames[i] = cue.getDescription();
            i++;
        }
        cueNames[i] = "<Select a Cue>";
        secondPosition = i;
        firstPosition = i;
        defaultPosition = i;
        
        initUI(parent);
    }

    private void initUI(final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //getContentPane()
        //        .setBorder(new EmptyBorder(10, 10, 10, 10) );

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Swap cues");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.X_AXIS));
        firstPanel.add(new JLabel("Swap cue: "));
        firstPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JComboBox firstBox = new JComboBox<>(cueNames);
        firstBox.setSelectedItem("<Select a Cue>");
        firstBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //e.getItem();
                firstPosition = java.util.Arrays.asList(cueNames).indexOf(e.getItem());
            }
        });
        firstPanel.add(firstBox);
        
        add(firstPanel);
        
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
        secondPanel.add(new JLabel("with cue: "));
        secondPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JComboBox secondBox = new JComboBox<>(cueNames);
        secondBox.setSelectedItem("<Select a Cue>");
        secondBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //e.getItem();
                secondPosition = java.util.Arrays.asList(cueNames).indexOf(e.getItem());
            }
        });
        secondPanel.add(secondBox);
        
        add(secondPanel);
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
        JButton approveOption = new JButton("Swap!");
        approveOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (firstPosition == defaultPosition || secondPosition == defaultPosition){
                    ErrorDialog ed = new ErrorDialog("You must select two cues!", parent);
                    ed.showDialog();
                } else if (firstPosition == secondPosition){
                    ErrorDialog ed = new ErrorDialog("You must select two DIFFERENT cues!", parent);
                    ed.showDialog();
                } else {
                    closeState = APPROVE_OPTION;
                    dispose();
                }
            }
        });
        buttonsPanel.add(approveOption);
        add(buttonsPanel);
        

        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle("Swap Cues");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public int showDialog() {
        setVisible(true);
        return closeState;
    }
    
    public int getSecondPosition() {
        return firstPosition;
    }
    
    public int getFirstPosition() {
        return secondPosition;
    }
}
