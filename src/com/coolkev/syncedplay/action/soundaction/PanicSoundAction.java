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
public class PanicSoundAction extends Action {
    
    public PanicSoundAction(){
        super();
    }
    
    @Override
    public String toString() {
        return "panic";
    }

    @Override
    public int handler() {
        return Action.HANDLER_SOUND_SUBSYSTEM;
    }
    
    
}
