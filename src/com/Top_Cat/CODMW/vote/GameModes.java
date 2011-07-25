package com.Top_Cat.CODMW.vote;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.Top_Cat.CODMW.main;
import com.Top_Cat.CODMW.gamemodes.gamemode;
import com.Top_Cat.CODMW.gamemodes.CTF;
import com.Top_Cat.CODMW.gamemodes.TDM;
import com.Top_Cat.CODMW.gamemodes.FFA;

public enum GameModes {
    CTF(CTF.class),
    TDM(TDM.class),
    FFA(FFA.class);
    
    private final Class<? extends gamemode> k;
    
    private GameModes(Class<? extends gamemode> k) {
        this.k = k;
    }
    
    public gamemode createGame(main instance) {
        try {
            Constructor<? extends gamemode> c = k.getDeclaredConstructor(new Class[] {main.class});
            return c.newInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
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