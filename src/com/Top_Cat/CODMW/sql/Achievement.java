package com.Top_Cat.CODMW.sql;

import java.util.ArrayList;

public enum Achievement {
    KILL_50(0, 1, 50, "Kill 50 enemies"),
    KILL_100(1, 1, 100, "Kill 100 enemies"),
    KILL_250(2, 1, 250, "Kill 250 enemies"),
    KILL_500(3, 1, 500, "Kill 500 enemies"),
    KILL_1000(5, 1, 1000, "Kill 100 enemies"),
    KILL_GIG(6, -1, -1, "Gigssassination"),
    WIN_5(7, 3, 5, ""),
    WIN_10(8, 3, 10, ""),
    WIN_25(9, 3, 25, ""),
    WIN_50(10, 3, 50, ""),
    WIN_100(11, 3, 100, ""),
    FIRE_1000(12, 6, 1000, ""),
    FIRE_2500(13, 6, 2500, ""),
    FIRE_5000(14, 6, 5000, ""),
    FIRE_10000(15, 6, 10000, ""),
    LOGIN_5(16, 3, 5, ""),
    LOGIN_10(17, 3, 10, ""),
    LOGIN_25(18, 3, 25, ""),
    LOGIN_50(19, 3, 50, ""),
    LOGIN_100(20, 3, 100, "");;
    
    private final int id;
    private final int statid;
    private final int c;
    private final int points;
    private final String name;

    Achievement(int id, int statid, int c, String name) {
        this(id, statid, c, name, 20);
    }
    
    Achievement(int id, int statid, int c, String name, int points) {
        this.id = id;
        this.statid = statid;
        this.c = c;
        this.points = points;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }
    
    public int getStatId() {
        return this.statid;
    }
    
    public int getCount() {
        return this.c;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    private static ArrayList<Achievement> table = new ArrayList<Achievement>(); 
    static {
        for (Achievement i : Achievement.values()) {
            table.add(i.getId(), i);
        }
    }

    public static Achievement valueOf(int id) {
        return table.get(id);
    }
}