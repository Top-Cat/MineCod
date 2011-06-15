package com.Top_Cat.CODMW.sql;

import java.util.ArrayList;

public enum Stat {
	POINTS(0),
	KILLS(1),
	DEATHS(2),
	WINS(3),
	LOSSES(4),
	LOGIN(5);
	
	private final int id;

    Stat(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    
    private static ArrayList<Stat> table = new ArrayList<Stat>(); 
    static {
        for (Stat i : Stat.values()) {
        	table.add(i.getId(), i);
        }
    }

    public static Stat valueOf(int id) {
        return table.get(id);
    }
}