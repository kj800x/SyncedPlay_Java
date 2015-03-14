/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay.swing.tasks;

import com.coolkev.syncedplay.SyncedPlay;
import com.coolkev.syncedplay.model.CueTableModel;
import com.coolkev.syncedplay.model.SoundTableModel;
import com.coolkev.syncedplay.util.FileCopier;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class SaveTask extends SwingWorker<Void, Void> implements PropertyChangeListener {

    private final String projectFilePath;
    private final SyncedPlay sp;
    private final CueTableModel ctm;
    private final SoundTableModel stm;
    private String cs = "";
    /*
     * Main task. Executed in background thread.
     */
    private final ProgressMonitor progressMonitor;

    public SaveTask(String projectFilePath, SyncedPlay sp, CueTableModel ctm, SoundTableModel stm, ProgressMonitor progressMonitor) {
        this.projectFilePath = projectFilePath;
        this.sp = sp;
        this.ctm = ctm;
        this.stm = stm;
        this.progressMonitor = progressMonitor;
        addPropertyChangeListener(this);
    }

    @Override
    public Void doInBackground() {

        File projectFile = new File(projectFilePath);
        String projectName = projectFile.getName().replace(".sync", "");
        String projectDataDirPath = projectFile.getParentFile() + "/" + projectName + "_Data";
        File projectDataDir = new File(projectDataDirPath);
        System.out.println("Saving to :" + projectFile);
        //Create project file;
        SyncedPlay.overwriteToFile(projectFile, sp.makeProjectFileContents());
        //Make sure the directory exists, or create it if it doesn't
        if (!projectDataDir.exists()) {
            if (projectDataDir.mkdir()) {
                System.out.println("Directory Created");
            } else {
                System.out.println("Failed to create directory");
                throw new Error("Couldn't create directory");
            }
        }
        //Create Cues File
        this.cs = "Saving Cues File";
        setProgress(1);
        File cuesF = new File(projectDataDir.getAbsolutePath() + "/cues.txt");
        SyncedPlay.overwriteToFile(cuesF, ctm.save());
        //Create Sounds File
        this.cs = "Saving Sound File";
        setProgress(2);
        File soundsF = new File(projectDataDir.getAbsolutePath() + "/sounds.txt");
        SyncedPlay.overwriteToFile(soundsF, stm.save());
        //Copy all sounds to the new directory
        int i = 3;
        File[] files = stm.getFiles();
        for (File file : files) {
            this.cs = "Saving File: " + file.getName();
            setProgress(i);
            FileCopier.copyFile(file, new File(projectDataDir.getAbsolutePath() + "/" + file.getName()));
            i++;
        }
        //Update the current loaded file
        setProgress(100);

        return null;
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        //    setCursor(null); //turn off the wait cursor
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            progressMonitor.setNote(cs);
            if (progressMonitor.isCanceled() || this.isDone()) {
                if (progressMonitor.isCanceled()) {
                    this.cancel(true);
                } else {
                    done();
                }
            }
        }
    }
}
