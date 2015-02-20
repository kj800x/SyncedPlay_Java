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

public class MoveCueDialog extends JDialog {
    
    public static int CANCEL_OPTION = 0;
    public static int APPROVE_OPTION = 1;
    private int closeState = 0;
    
    String[] selectableCueNames;
    String[] destinationCuePlaces;
    private int selectedCueIndex;
    private int destinationIndex;
    private final int defaultPosition;
    
    public MoveCueDialog(ArrayList<Cue> cues, final Component parent) {
        super();
        
        selectableCueNames = new String[cues.size() + 1];
        destinationCuePlaces = new String[cues.size() + 1];
        int i = 0;
        for (Cue cue : cues){
            selectableCueNames[i] = cue.getDescription();
            destinationCuePlaces[i] = "Before \"" + cue.getDescription() + "\"";
            i++;
        }
        selectableCueNames[i] = "<Select a Cue>";
        destinationCuePlaces[i] = "<To The End>";
        selectedCueIndex = i;
        destinationIndex = i;
        defaultPosition = i;
        
        initUI(parent);
    }

    private final void initUI(final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //getContentPane()
        //        .setBorder(new EmptyBorder(10, 10, 10, 10) );

        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel label = new JLabel("Move cue");
        label.setAlignmentX(0.5f);
        add(label);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel firstPanel = new JPanel();
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.X_AXIS));
        firstPanel.add(new JLabel("Move cue: "));
        firstPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JComboBox firstBox = new JComboBox<>(selectableCueNames);
        firstBox.setSelectedItem("<Select a Cue>");
        firstBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //e.getItem();
                selectedCueIndex = java.util.Arrays.asList(selectableCueNames).indexOf(e.getItem());
            }
        });
        firstPanel.add(firstBox);
        
        add(firstPanel);
        
        JPanel secondPanel = new JPanel();
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.X_AXIS));
        secondPanel.add(new JLabel("with cue: "));
        secondPanel.add(Box.createRigidArea(new Dimension(7, 0)));
        JComboBox secondBox = new JComboBox<>(destinationCuePlaces);
        secondBox.setSelectedItem("<To The End>");
        secondBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //e.getItem();
                destinationIndex = java.util.Arrays.asList(destinationCuePlaces).indexOf(e.getItem());
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
                if (selectedCueIndex == defaultPosition){
                    ErrorDialog ed = new ErrorDialog("You must select a cue!", parent);
                    ed.showDialog();
                } else if (selectedCueIndex == destinationIndex){
                    ErrorDialog ed = new ErrorDialog("It's pointless to move something to the place it already was!", parent);
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
        setTitle("Move Cue");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public int showDialog() {
        setVisible(true);
        return closeState;
    }
    
    public int getPositionOfCueToMove() {
        return selectedCueIndex;
    }
    
    public int getDestinationPosition() {
        return destinationIndex;
    }
}
