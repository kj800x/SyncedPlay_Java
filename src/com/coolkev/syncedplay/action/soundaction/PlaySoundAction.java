/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay.action.soundaction;

import com.coolkev.syncedplay.action.Action;

/**
 *
 * @author kevin
 */
public class PlaySoundAction extends Action {
    
    private final String key;
    
    public PlaySoundAction(String key){
        super();
        this.key = key;
    }
    
    @Override
    public String toString() {
        return "play " + key;
    }

    @Override
    public int handler() {
        return Action.HANDLER_SOUND_SUBSYSTEM;
    }
    
    public String getKeyword(){
        return key;
    }
    
    
}
