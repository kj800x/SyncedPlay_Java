/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay.util;

import com.coolkev.syncedplay.action.Action;
import com.coolkev.syncedplay.action.soundaction.PlaySoundAction;
import com.coolkev.syncedplay.action.soundaction.LoopSoundAction;
import com.coolkev.syncedplay.action.soundaction.PanicSoundAction;
import com.coolkev.syncedplay.action.soundaction.StopSoundAction;
import java.util.ArrayList;



/**
 *
 * @author kevin
 */
public class ActionsTextParser {
    
    static private class ParserException extends Exception {

    }
    
    private static Action parseOneAction(String text) throws ParserException {       
        String[] words = text.split(" ");
        switch (words[0].toLowerCase()) {
            case "play":
                //Format: [play, KEYWORD]
                if (words.length == 2) { 
                    return new PlaySoundAction(words[1]);
                } else {
                    throw new ParserException();
                }
            case "panic":
                //Format: [panic]
                return new PanicSoundAction();
            case "loop":
                //Format: [loop, KEYWORD]
                if (words.length == 2) { 
                    return new LoopSoundAction(words[1]);
                } else {
                    throw new ParserException();
                }
            case "stop":
                //Format: [stop, KEYWORD]
                if (words.length == 2) { 
                    return new StopSoundAction(words[1]);
                } else {
                    throw new ParserException();
                }
        }
        //If we couldn't find a match
        throw new ParserException();
    }
    
    private static Action[] trulyParseText(String text) throws ParserException {
        ArrayList<Action> out = new ArrayList<>();
        String[] lines = text.split("\n");
        for (String line : lines){
            line = line.trim();
            if (!line.isEmpty()){
                out.add(parseOneAction(line));
            }
        }
        return out.toArray(new Action[out.size()]);
    }
    
    public static boolean canParseText(String text){
        try {
            trulyParseText(text);
            return true;
        } catch (ParserException ex) {
            return false;
        }
    }
    public static Action[] parseText(String text){
        try {
            return trulyParseText(text);
        } catch (ParserException ex) {
            return null;
        }
    }
}
