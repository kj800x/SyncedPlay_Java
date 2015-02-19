/*
 * Copyright 2015 Kevin Johnson
 * All rights reserved.
 */
package com.coolkev.syncedplay;

/**
 *
 * @author kevin
 */
class Cue {
    private Action[] actions = {};
    private String description;

    public Cue(String description){
        this.description = description;
    }
    public Cue(String description, Action[] actions){
        this.description = description;
        this.actions = actions;
    }
    
    /**
     * @return the actions
     */
    public Action[] getActions() {
        return actions;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(Action[] actions) {
        this.actions = actions;
    }
    
    String getActionsText() {
        StringBuilder out = new StringBuilder();
        for (Action action : actions){
            out.append(action.toString()).append("\n");
        }
        return out.toString();
    }
    
    
}
