/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syncedplay;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class CueTableModel extends AbstractTableModel {
    
    private Cue[] cues = {};
            
    CueTableModel(Cue[] cues){
        this.cues = cues;
    }      
    
    CueTableModel(){
    }
    
    @Override
    public String getColumnName(int col) {
        if (col == 0){
            return "Cue";
        } else if (col == 1){
            return "Description";
        } else {
            return "!!ERROR!!";
        }
    }
    @Override
    public int getRowCount() { return getCues().length; }
    @Override
    public int getColumnCount() { return 2; }
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0){
            return row;
        } else if (col == 1){
            return getCues()[col].getDescription();
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
    Cue[] getCues() {
        return cues;
    }

    /**
     * @param cues the cues to set
     */
    void setCues(Cue[] cues) {
        this.cues = cues;
    }
    
}
