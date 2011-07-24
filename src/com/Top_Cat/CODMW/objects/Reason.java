package com.Top_Cat.CODMW.objects;

import java.util.ArrayList;

public enum Reason {
    NONE(-1),
    FALL(0),
    KNIFE(1, true),
    BOW(2, true),
    CLAYMORE(3, true),
    DOGS(4),
    SENTRY(5),
    CHOPPER(6),
    HEADSHOT(7, true),
    FISH_SMITE(8),
    FISH(9, true),
    GRENADE(10, true),
    ;
    
    final private int id;
    final private boolean streak;
    
    private Reason(int id, boolean streak) {
        this.id = id;
        this.streak = streak;
    }
    
    private Reason(int id) {
        this(id, false);
    }

    public int getId() {
        return id;
    }
    
    public boolean getStreak() {
        return streak;
    }
    
    public static ArrayList<Reason> table = new ArrayList<Reason>(); 
    static {
        for (Reason i : Reason.values()) {
            if (i.getId() >= 0) {
                table.add(i.getId(), i);
            }
        }
    }

    public static Reason valueOf(int id) {
        return table.get(id);
    }

}