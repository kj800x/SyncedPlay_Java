/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay;

/**
 *
 * @author kevin
 */
class StopSoundAction extends Action {
    
    private final String key;
    
    public StopSoundAction(String key){
        this.key = key;
    }
    
    @Override
    public String toString() {
        return "stop " + key;
    }

    @Override
    public int handler() {
        return Action.HANDLER_SOUND_SUBSYSTEM;
    }
    
    public String getKeyword(){
        return key;
    }
    
    
}
