/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay.action.cueaction;

import com.coolkev.syncedplay.action.Action;

/**
 *
 * @author kevin
 */
public class GotoCueAction extends Action {
    
    private final int i;
    
    public GotoCueAction(int i){
        super();
        this.i = i;
    }
    
    @Override
    public String toString() {
        return "goto " + i;
    }

    @Override
    public int handler() {
        return Action.HANDLER_CUE_SUBSYSTEM;
    }
    
    public int getDestCueIndex(){
        return i;
    }
    
    
}
