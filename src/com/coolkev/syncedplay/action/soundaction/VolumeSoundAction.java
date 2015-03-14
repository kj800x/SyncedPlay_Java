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
public class VolumeSoundAction extends Action {
    
    private final String key;
    private final float value;
    
    public VolumeSoundAction(String key, float value){
        super();
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return "volume " + key + " " + value;
    }

    @Override
    public int handler() {
        return Action.HANDLER_SOUND_SUBSYSTEM;
    }
    
    public String getKeyword(){
        return key;
    }
    
    public float getValue(){
        return value;
    }
    
}
