/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay.action;

/**
 *
 * @author kevin
 */
abstract public class Action {
    
    public static final int HANDLER_CUE_SUBSYSTEM = 1;
    public static final int HANDLER_SOUND_SUBSYSTEM = 2; 
    
    @Override
    public abstract String toString();
    
    public abstract int handler();
    
}
