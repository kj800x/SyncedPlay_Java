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
import javax.swing.JProgressBar;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ProgressDialog extends JDialog {
    
    final int numberOfStages;
    int currentStage = 0;
    String currentMessage = "Reticulating Splines";
    private JProgressBar progressBar;
    private JLabel stageDescriptionLabel;
    
    public ProgressDialog(String title, int stages, final Component parent) {
        super();
        numberOfStages = stages;
        initUI(title, parent);
    }
    
    private void updateUItoStage(){
        stageDescriptionLabel.setText(currentMessage);
        progressBar.setValue(currentStage);
    }

    public void setCurrentStage(int stageState, String stageMessage){
        currentStage = stageState;
        currentMessage = stageMessage;
        System.out.println(currentMessage);
        updateUItoStage();
    }
    
    private void initUI(String title, final Component parent) {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(title);
        nameLabel.setAlignmentX(0.5f);
        add(nameLabel);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        progressBar = new JProgressBar(0, numberOfStages);
        
        add(Box.createRigidArea(new Dimension(0, 10)));
        
        stageDescriptionLabel = new JLabel();
        stageDescriptionLabel.setAlignmentX(0.5f);
        add(stageDescriptionLabel);
        
        updateUItoStage();
        
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pack();
    }
    
    public void showDialog() {
        setVisible(true);
    }
    
    public void beDone() {
        setVisible(false);
    }
    
}
