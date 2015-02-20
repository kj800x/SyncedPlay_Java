/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 *
 * @author kevin
 */
public class FileCopier {

    public static void copyFile(File sourceFile, File destFile) {
        if (!sourceFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
            try {
                if (!destFile.exists()) {
                    destFile.createNewFile();
                }

                FileChannel source = null;
                FileChannel destination = null;

                try {
                    source = new FileInputStream(sourceFile).getChannel();
                    destination = new FileOutputStream(destFile).getChannel();
                    destination.transferFrom(source, 0, source.size());
                } finally {
                    if (source != null) {
                        source.close();
                    }
                    if (destination != null) {
                        destination.close();
                    }
                }
            } catch (IOException ex) {
                //Error;
            }
        }
    }

}
