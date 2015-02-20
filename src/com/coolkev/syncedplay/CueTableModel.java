/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class CueTableModel extends AbstractTableModel {
    
    private ArrayList<Cue> cues;
    private int nextCue;
            
    CueTableModel(ArrayList<Cue> cues){
        this.cues = cues;
        this.nextCue = 0;
    }      
    
    CueTableModel(){
        this.cues = new ArrayList();
        this.nextCue = 0;
    }
    
    String save() {
        StringBuilder out = new StringBuilder();
        for (Cue cue : cues){
            out.append("[").append(cue.getDescription()).append("]\n");
            out.append(cue.getActionsText()).append("\n");
        }
        return out.toString();
    }
    
    public void blank(){
        this.cues = new ArrayList();
        this.nextCue = 0;
        fireTableDataChanged();
    }
    
    public void load(String s){
        Map<String, String> parsedString = IniFormatParser.parseIniFormat(s);
        ArrayList<Cue> newCues = new ArrayList<>();
        for (String key : parsedString.keySet()){
            newCues.add(new Cue(key, ActionsTextParser.parseText(parsedString.get(key))));
        }
        setCues(newCues);
    }
    
    @Override
    public String getColumnName(int col) {
        if (col == 0){
            return "Cue";
        } else if (col == 1){
            return "Next";
        } else if (col == 2){
            return "Description";
        } else {
            return "!!ERROR!!";
        }
    }
    Cue getCue(int row){
        return cues.get(row);
    }
    int getNextCueIndex(){
        return nextCue + 1;
    }
    void setNextCueIndex(int next){
        nextCue = next - 1;
        fireTableDataChanged();
    }
    @Override
    public int getRowCount() { return getCues().size(); }
    @Override
    public int getColumnCount() { return 3; }
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0){
            return row;
        } else if (col == 1){
            if (row == nextCue){
                return ("*");
            } else {
                return (" ");
            }
        } else if (col == 2){
            return cues.get(row).getDescription();
        } else {
            return "!!ERROR!!";
        }
    }
    @Override
    public boolean isCellEditable(int row, int col)
        { return false; }

    /**
     * @return the cues
     */
    ArrayList<Cue> getCues() {
        return cues;
    }

    /**
     * @param cues the cues to set
     */
    void setCues(ArrayList<Cue> cues) {
        this.cues = cues;
        fireTableDataChanged();
    }
    void setCue(int row, Cue cue) {
        this.cues.set(row, cue);
        fireTableDataChanged();
    }
    
    void deleteCue(int row) {
        this.cues.remove(row);
        fireTableDataChanged();
    }
    
    void addCue(Cue cue) {
        addCue(cue, this.cues.size());
    }

    void addCue(Cue cue, int before) {
        this.cues.add(before, cue);
        fireTableDataChanged();
    }
    
    void swapCues(int rowOne, int rowTwo){
        Collections.swap(this.cues, rowOne, rowTwo);
        fireTableDataChanged();
    }
}
