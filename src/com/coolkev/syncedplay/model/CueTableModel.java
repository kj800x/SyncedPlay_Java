/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.model;

import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.action.cueaction.GotoCueAction;
import com.coolkev.syncedplay.util.IniFormatParser;
import com.coolkev.syncedplay.util.ActionsTextParser;
import com.coolkev.syncedplay.util.Callback;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class CueTableModel extends AbstractTableModel {

    private ArrayList<Cue> cues;
    private int nextCue;
    JTable table;
    private final ArrayList<Callback> changeCallbacks;

    public CueTableModel(ArrayList<Cue> cues) {
        this.cues = cues;
        this.nextCue = 0;
        this.changeCallbacks = new ArrayList();
    }

    public CueTableModel() {
        this.cues = new ArrayList();
        this.changeCallbacks = new ArrayList();
        this.nextCue = 0;
    }

    public void runAction(Action a) {
        if (a instanceof GotoCueAction) {
            GotoCueAction gtca = (GotoCueAction) a;
            if (0 <= (gtca.getDestCueIndex() - 1) && (gtca.getDestCueIndex() <= cues.size())) {
                System.out.println("GOTO: " + gtca.getDestCueIndex());
                setNextCueIndex(gtca.getDestCueIndex());
            }
        }
    }

    public String save() {
        StringBuilder out = new StringBuilder();
        for (Cue cue : cues) {
            out.append("[").append(cue.getDescription()).append("]\n");
            out.append(cue.getActionsText()).append("\n");
        }
        return out.toString();
    }

    public void blank() {
        this.cues = new ArrayList();
        this.nextCue = 0;
        fireTableDataChanged();
        callCallbacks();
    }

    public void load(String s) {
        if (s.trim().length() > 0) {
            Map<String, String> parsedString = IniFormatParser.parseIniFormat(s);
            ArrayList<Cue> newCues = new ArrayList<>();
            for (String key : parsedString.keySet()) {
                newCues.add(new Cue(key, ActionsTextParser.parseText(parsedString.get(key))));
            }
            setCues(newCues);
        }
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Cue";
        } else if (col == 1) {
            return "Next";
        } else if (col == 2) {
            return "Description";
        } else {
            return "!!ERROR!!";
        }
    }

    public Cue getCue(int row) {
        return cues.get(row);
    }

    public int getNextCueIndex() {
        return nextCue + 1;
    }
    
    public void setNextCueIndex(int next) {
        int oldNextCue = nextCue;
        nextCue = next - 1;
        fireTableCellUpdated(oldNextCue, 1);
        fireTableCellUpdated(nextCue, 1);
        scrollToCenter(table, nextCue, 0);
    }

    public static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
        Rectangle viewRect = viewport.getViewRect();
        rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

        int centerX = (viewRect.width - rect.width) / 2;
        int centerY = (viewRect.height - rect.height) / 2;
        if (rect.x < centerX) {
            centerX = -centerX;
        }
        if (rect.y < centerY) {
            centerY = -centerY;
        }
        rect.translate(centerX, centerY);
        viewport.scrollRectToVisible(rect);
    }

    @Override
    public int getRowCount() {
        return getCues().size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return row + 1;
        } else if (col == 1) {
            if (row == nextCue) {
                return ("*");
            } else {
                return (" ");
            }
        } else if (col == 2) {
            return cues.get(row).getDescription();
        } else {
            return "!!ERROR!!";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    
    /**
     * @return the cues
     */
    public ArrayList<Cue> getCues() {
        return cues;
    }

    /**
     * @param cues the cues to set
     */
    public void setCues(ArrayList<Cue> cues) {
        this.cues = cues;
        fireTableDataChanged();
        callCallbacks();
    }

    public void setCue(int row, Cue cue) {
        this.cues.set(row, cue);
        fireTableRowsUpdated(row, row);
        callCallbacks();
    }

    public void deleteCue(int row) {
        this.cues.remove(row);
        fireTableRowsDeleted(row, row);
        callCallbacks();
    }

    public void addCue(Cue cue) {
        addCue(cue, this.cues.size());
        callCallbacks();
    }

    public void addCue(Cue cue, int before) {
        System.out.println(before);
        System.out.println(this.cues);
        this.cues.add(before, cue);
        fireTableDataChanged();
        callCallbacks();
    }

    public void swapCues(int rowOne, int rowTwo) {
        Collections.swap(this.cues, rowOne, rowTwo);
        fireTableDataChanged();
        callCallbacks();
    }

    public void moveCue(int startIndex, int endIndex) {
        System.out.println("Moving from: " + startIndex);
        System.out.println("To: " + endIndex);
        callCallbacks();
    }

    public void setTable(JTable cuesTable) {
        this.table = cuesTable;
        callCallbacks();
    }
    
    private void callCallbacks(){
        for (Callback c: changeCallbacks){
            c.run();
        }
    }
    
    public void addStructureChangeListener(Callback c) {
        changeCallbacks.add(c);
    }
}
