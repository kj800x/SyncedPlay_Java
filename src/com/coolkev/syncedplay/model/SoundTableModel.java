/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.model;

import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.action.soundaction.LoopSoundAction;
import com.coolkev.syncedplay.action.soundaction.PanicSoundAction;
import com.coolkev.syncedplay.action.soundaction.PlaySoundAction;
import com.coolkev.syncedplay.action.soundaction.StopSoundAction;
import com.coolkev.syncedplay.action.soundaction.VolumeSoundAction;
import com.coolkev.syncedplay.util.Callback;
import com.coolkev.syncedplay.util.IniFormatParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.table.AbstractTableModel;

class AudioListener implements LineListener {
    public boolean done = false;
    public boolean isKilled = false;
    
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
                    if (isKilled) {
                        throw new InterruptedException();
                    } else {
                        wait();
                    }
                }
            }
        }

/**
 *
 * @author kevin
 */
class SoundRunnable implements Runnable {

    private boolean isKilled = false;
    private final boolean loop;
    private Clip clip;
    final public AudioListener listener;
    // The wrapper thread is unnecessary, unless it blocks on the
    // Clip finishing; see comments.
    private final File clipFile;
    private FloatControl volume;


    public SoundRunnable(File cf, boolean loop) {
        this.clipFile = cf;
        this.loop = loop;
        this.listener = new AudioListener();
    }
    
    public void setVolume(float v){
        //volume.setValue(v);
    }

    public void kill() {
        isKilled = true;
        listener.done = true;
        synchronized (listener){
            listener.notifyAll();
        }
    }

    public boolean isAlive() {
        return !(isKilled || listener.done);
    }

    @Override
    public void run() {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clipFile)) {
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            this.clip = (Clip)AudioSystem.getLine(info);
            //this.clip = AudioSystem.getClip();
            clip.addLineListener(listener);
            clip.open(audioInputStream);
            //this.volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            try {
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
                listener.waitUntilDone();
            } catch (InterruptedException ex) {
                //Ok, we're being stopped;
            } finally {
                clip.close();
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(SoundTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

public class SoundTableModel extends AbstractTableModel {

    TreeMap<String, File> keyToFile = new TreeMap();
    TreeMap<String, ArrayList<SoundRunnable>> keyToRunnable = new TreeMap();
    private final ArrayList<Callback> changeCallbacks;

    public SoundTableModel() {
        super();
        this.changeCallbacks = new ArrayList();
    }

    public void learnSound(String key, File file) {
        keyToFile.put(key, file);
        keyToRunnable.put(key, new ArrayList<SoundRunnable>());
        fireTableDataChanged();
        callCallbacks();
    }

    public void blank() {
        panic();
        cleanUpDeadThreads();
        keyToFile = new TreeMap();
        keyToRunnable = new TreeMap();
        fireTableDataChanged();
        callCallbacks();
    }

    public void load(String s, String dir) {
        if (s.trim().length() > 0){
            Map<String, String> parsedString = IniFormatParser.parseIniFormat(s);
            for (String key : parsedString.keySet()) {
                File f = new File(dir + "/" + parsedString.get(key));
                System.out.println(f.getAbsolutePath());
                learnSound(key, f);
            }
        }
    }
    
    private void setVolume(String key, float v){
        ArrayList<SoundRunnable> runnableList = keyToRunnable.get(key);
        for (SoundRunnable r : runnableList) {
            r.setVolume(v);
        }
    }

    public void runAction(Action a) {
        if (a instanceof PlaySoundAction) {
            PlaySoundAction psa = (PlaySoundAction) a;
            if (keyToFile.keySet().contains(psa.getKeyword())) {
                playSound(psa.getKeyword());
            }
        } else if (a instanceof StopSoundAction) {
            StopSoundAction ssa = (StopSoundAction) a;
            if (keyToFile.keySet().contains(ssa.getKeyword())) {
                stopSound(ssa.getKeyword());
            }
        } else if (a instanceof LoopSoundAction) {
            LoopSoundAction lsa = (LoopSoundAction) a;
            if (keyToFile.keySet().contains(lsa.getKeyword())) {
                loopSound(lsa.getKeyword());
            }
        } else if (a instanceof VolumeSoundAction) {
            VolumeSoundAction vsa = (VolumeSoundAction) a;
            if (keyToFile.keySet().contains(vsa.getKeyword())) {
                setVolume(vsa.getKeyword(), vsa.getValue());
            }
        } else if (a instanceof PanicSoundAction) {
            panic();
        }
    }

    public String save() {
        StringBuilder out = new StringBuilder();
        for (String key : keyToFile.keySet()) {
            out.append("[").append(key).append("]\n");
            out.append(keyToFile.get(key).getName()).append("\n");
        }
        return out.toString();
    }

    public static boolean isFileSupported(File f) {
        try {
            AudioSystem.getAudioInputStream(f);
            return true;
        } catch (UnsupportedAudioFileException | IOException e) {
            return false;
        }
    }

    public String keyFromRow(int row) {
        return (String) getValueAt(row, 0);
    }

    public void stopSound(final String key) {
        ArrayList<SoundRunnable> runnableList = keyToRunnable.get(key);
        for (SoundRunnable r : runnableList) {
            r.kill();
        }
        cleanUpDeadThreads();
    }

    public void panic() {
        Set<String> keys = keyToRunnable.keySet();
        for (String key : keys) {
            ArrayList<SoundRunnable> runnableList = keyToRunnable.get(key);
            for (Iterator<SoundRunnable> iterator = runnableList.iterator(); iterator.hasNext();) {
                SoundRunnable soundRunnable = iterator.next();
                if (soundRunnable.isAlive()) {
                    soundRunnable.kill();
                }
                iterator.remove();
            }
        }
    }

    public File[] getFiles() {
        return keyToFile.values().toArray(new File[keyToFile.values().size()]);
    }

    private void cleanUpDeadThreads() {
        Set<String> keys = keyToRunnable.keySet();
        for (String key : keys) {
            ArrayList<SoundRunnable> runnableList = keyToRunnable.get(key);
            for (Iterator<SoundRunnable> iterator = runnableList.iterator(); iterator.hasNext();) {
                SoundRunnable soundRunnable = iterator.next();
                if (!soundRunnable.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    public void loopSound(final String key) {
        playSound(key, true);
    }

    public void playSound(final String key) {
        playSound(key, false);
    }

    //The caller is obligated to make sure that file that it's trying to play has passed (isFileSupported)

    private void playSound(final String key, final boolean loop) {
        SoundRunnable tr = new SoundRunnable(keyToFile.get(key), loop);
        Thread thread = new Thread(tr);
        keyToRunnable.get(key).add(tr);
        thread.start();
    }

    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Keyword";
        } else if (col == 1) {
            return "File";
        } else {
            return "!!ERROR!!";
        }
    }

    @Override
    public int getRowCount() {
        return keyToFile.keySet().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return keyToFile.keySet().toArray()[row];
        } else if (col == 1) {
            return keyToFile.get(keyToFile.keySet().toArray()[row]).getName();
        } else {
            return "!!ERROR!!";
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
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
