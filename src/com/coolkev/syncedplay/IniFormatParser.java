/*
 * Copyright 2015 Kevin Johnson.
 * All rights reserved.
 */
package com.coolkev.syncedplay;

import java.util.LinkedHashMap;

/**
 *
 * @author kevin
 */
class IniFormatParser {
    static public LinkedHashMap<String, String> parseIniFormat(String s){
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
        StringBuilder sectionHead = new StringBuilder();
        StringBuilder sectionBody = new StringBuilder();
        String[] lines = s.split("\n");
        for (String line : lines){
            line = line.trim();
            if (line.startsWith("[") && line.endsWith("]")){
                if (!(sectionHead.toString().isEmpty() && sectionBody.toString().isEmpty())){
                    out.put(sectionHead.toString(), sectionBody.toString());
                    sectionBody.delete(0, sectionBody.length());
                    sectionHead.delete(0, sectionHead.length());
                }
               sectionHead.append(line.subSequence(1, line.length()-1));
            } else {
               sectionBody.append(line);
            }
        }
        out.put(sectionHead.toString(), sectionBody.toString());
        return out;
    }
}
