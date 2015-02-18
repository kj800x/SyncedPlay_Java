/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package syncedplay;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.*;

/**
 *
 * @author kevin
 */
class SoundManager {

    HashMap<String, File> keyToFile = new HashMap();

    void learnSound(String key, File file) {
        keyToFile.put(key, file);
    }

    void playSound(final String key) throws UnsupportedAudioFileException {

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
                        Type eventType = event.getType();
                        if (eventType == Type.STOP || eventType == Type.CLOSE) {
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
                        Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        clip.close();
                    }
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
                    Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
    }
}
