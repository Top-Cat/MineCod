package com.Top_Cat.CODMW.vote;

import java.util.HashMap;

public enum GameModes {
    CTF,
    TDM,
    FFA;
    
    private static final HashMap<String, GameModes> lookupId = new HashMap<String, GameModes>();
    
    public static GameModes getGMFromId(String name) {
       return lookupId.get(name);
    }
     
    static {
        for (GameModes gm : values()) {
            lookupId.put(gm.toString(), gm);
        }
    }
}