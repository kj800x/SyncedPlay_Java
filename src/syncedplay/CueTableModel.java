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
    private int nextCue;
            
    CueTableModel(ArrayList<Cue> cues){
        this.cues = cues;
        this.nextCue = 0;
    }      
    
    CueTableModel(){
        this.cues = new ArrayList();
        this.nextCue = 0;
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
    void addCue(Cue cue) {
        this.cues.add(cue);
        fireTableDataChanged();
    }
    
}
