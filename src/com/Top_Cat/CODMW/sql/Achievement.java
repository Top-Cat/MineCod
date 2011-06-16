package com.Top_Cat.CODMW.sql;

import java.util.ArrayList;

public enum Achievement {
    GET_50_KILLS(0, 1, 50, "Kill 50 enemies"),
    GET_100_KILLS(1, 1, 100, "Kill 100 enemies"),
    GET_250_KILLS(2, 1, 250, "Kill 250 enemies"),
    GET_500_KILLS(3, 1, 500, "Kill 500 enemies"),
    GET_1000_KILLS(5, 1, 1000, "Kill 100 enemies"),
    KILL_GIG(6, -1, -1, "Gigssassination"),
    WIN_5_GAMES(7, 3, 5),
    WIN_10_GAMES(8, 3, 10),
    WIN_25_GAMES(9, 3, 25),
    WIN_50_GAMES(10, 3, 50),
    WIN_100_GAMES(11, 3, 100),
    FIRE_1000_ARROWS(12, 6, 1000),
    FIRE_2500_ARROWS(13, 6, 2500),
    FIRE_5000_ARROWS(14, 6, 5000),
    FIRE_10000_ARROWS(15, 6, 10000);
    
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