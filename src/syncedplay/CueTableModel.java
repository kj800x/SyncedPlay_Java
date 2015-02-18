/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syncedplay;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class CueTableModel extends AbstractTableModel {
    
    private ArrayList<Cue> cues;
            
    CueTableModel(ArrayList<Cue> cues){
        this.cues = cues;
    }      
    
    CueTableModel(){
        this.cues = new ArrayList();
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
    Cue getCue(int row){
        return cues.get(row);
    }
    @Override
    public int getRowCount() { return getCues().size(); }
    @Override
    public int getColumnCount() { return 2; }
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0){
            return row;
        } else if (col == 1){
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
    void addCue(Cue cue) {
        this.cues.add(cue);
        fireTableDataChanged();
    }
    
}
