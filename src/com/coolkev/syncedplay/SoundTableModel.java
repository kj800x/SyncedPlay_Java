/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author kevin
 */
public class SoundTableModel extends AbstractTableModel {
    
    TreeMap<String, File> keyToFile = new TreeMap();

    void learnSound(String key, File file) {
        keyToFile.put(key, file);
        fireTableDataChanged();
    }
    
    String save() {
        StringBuilder out = new StringBuilder();
        for (String key : keyToFile.keySet()){
            out.append("[").append(key).append("]\n");
            out.append(keyToFile.get(key).getName()).append("\n");
        }
        return out.toString();
    }

    static boolean isFileSupported(File f){
        try {
            AudioSystem.getAudioInputStream(f);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }
    
    
    //The caller is obligated to make sure that file that it's trying to play has passed (isFileSupported)
    void playSound(final String key) {
        Thread thread = new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            @Override
            public void run() {
                File clipFile = keyToFile.get(key);
                class AudioListener implements LineListener {

                    private boolean done = false;

                    @Override
                    public synchronized void update(LineEvent event) {
                        LineEvent.Type eventType = event.getType();
                        if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE) {
                            done = true;
                            notifyAll();
                        }
                    }

                    public synchronized void waitUntilDone() throws InterruptedException {
                        while (!done) {
                            wait();
                        }
                    }

                }
                AudioListener listener = new AudioListener();
                try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile)) {
                    Clip clip = AudioSystem.getClip();
                    clip.addLineListener(listener);
                    clip.open(audioInputStream);
                    try {
                        clip.start();
                        listener.waitUntilDone();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SoundTableModel.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        clip.close();
                    }
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                    Logger.getLogger(SoundTableModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }
    
    SoundTableModel(){
        super();
    }
    
    @Override
    public String getColumnName(int col) {
        if (col == 0){
            return "Keyword";
        } else if (col == 1){
            return "File";
        } else {
            return "!!ERROR!!";
        }
    }
    @Override
    public int getRowCount() { return keyToFile.keySet().size(); }
    @Override
    public int getColumnCount() { return 2; }
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0){
            return keyToFile.keySet().toArray()[row];
        } else if (col == 1){
            return keyToFile.get(keyToFile.keySet().toArray()[row]).getName();
        } else {
            return "!!ERROR!!";
        }
    }
    @Override
    public boolean isCellEditable(int row, int col)
        { return false; }
    
}
