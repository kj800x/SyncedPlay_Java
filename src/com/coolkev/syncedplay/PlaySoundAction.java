/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay;

/**
 *
 * @author kevin
 */
class PlaySoundAction extends Action {
    
    private final String key;
    
    public PlaySoundAction(String key){
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
