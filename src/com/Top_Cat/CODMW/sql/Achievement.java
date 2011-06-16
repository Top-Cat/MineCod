package com.Top_Cat.CODMW.sql;

import java.util.ArrayList;

public enum Achievement {
    KILL_50(0, Stat.KILLS, 50, "Criminal"),
    KILL_100(1, Stat.KILLS, 100, "Assassin"),
    KILL_250(2, Stat.KILLS, 250, "Murderer"),
    KILL_500(3, Stat.KILLS, 500, "Serial Killer"),
    KILL_1000(4, Stat.KILLS, 1000, "Worse than Hitler"),
    KILL_GIG(5, Stat.OTHER, -1, "Gigssassination"),
    WIN_5(6, Stat.WINS, 5, "Winner"),
    WIN_10(7, Stat.WINS, 10, "Victor"),
    WIN_25(8, Stat.WINS, 25, "Champion"),
    WIN_50(9, Stat.WINS, 50, "Conquerer"),
    WIN_100(10, Stat.WINS, 100, "No lifer"),
    FIRE_1000(11, Stat.ARROWS_FIRED, 1000, "Bad aim"),
    FIRE_2500(12, Stat.ARROWS_FIRED, 2500, "Worse aim"),
    FIRE_5000(13, Stat.ARROWS_FIRED, 5000, "Even worse aim"),
    FIRE_10000(14, Stat.ARROWS_FIRED, 10000, "Gig-level aim"),
    CLAYMORE_GET_5(15, Stat.CLAYMORES_ACHIEVED, 5, "Sparklers"),
    CLAYMORE_GET_10(16, Stat.CLAYMORES_ACHIEVED, 10, "Catherine Wheels"),
    CLAYMORE_GET_25(17, Stat.CLAYMORES_ACHIEVED, 25, "Fire Fountain"),
    CLAYMORE_GET_50(18, Stat.CLAYMORES_ACHIEVED, 50, "Rockets"),
    CLAYMORE_GET_100(19, Stat.CLAYMORES_ACHIEVED, 100, "4th of July"),
    APPLE_GET_5(20, Stat.APPLES_ACHIEVED, 5, "5 a day"),
    APPLE_GET_10(21, Stat.APPLES_ACHIEVED, 10, "Healthy"),
    APPLE_GET_25(22, Stat.APPLES_ACHIEVED, 25, "Fruit salad"),
    APPLE_GET_50(23, Stat.APPLES_ACHIEVED, 50, "Adam and/or Eve"),
    APPLE_GET_100(24, Stat.APPLES_ACHIEVED, 100, "Granny Smith"),
    DOG_GET_5(25, Stat.DOGS_ACHIEVED, 5, "Puddles the Dog"),
    DOG_GET_10(26, Stat.DOGS_ACHIEVED, 10, "Rufus the Dog"),
    DOG_GET_25(27, Stat.DOGS_ACHIEVED, 25, "Max the Dog"),
    DOG_GET_50(28, Stat.DOGS_ACHIEVED, 50, "Butch the Dog"),
    DOG_GET_100(29, Stat.DOGS_ACHIEVED, 100, "Brian Blessed"),
    SENTRY_GET_5(30, Stat.SENTRIES_ACHIEVED, 5, "Hello?"),
    SENTRY_GET_10(31, Stat.SENTRIES_ACHIEVED, 10, "Who's there?"),
    SENTRY_GET_25(32, Stat.SENTRIES_ACHIEVED, 25, "Activating!"),
    SENTRY_GET_50(33, Stat.SENTRIES_ACHIEVED, 50, "I see you!"),
    SENTRY_GET_100(34, Stat.SENTRIES_ACHIEVED, 100, "Are you still there?"),
    CHOPPER_GET_5(35, Stat.CHOPPERS_ACHIEVED, 5, "Calling the chopper!"),
    CHOPPER_GET_10(36, Stat.CHOPPERS_ACHIEVED, 10, "Helicopter on the way!"),
    CHOPPER_GET_25(37, Stat.CHOPPERS_ACHIEVED, 25, "ETA 2 minutes!"),
    CHOPPER_GET_50(38, Stat.CHOPPERS_ACHIEVED, 50, "STANDBY, MY HAWKMEN!"),
    CHOPPER_GET_100(39, Stat.CHOPPERS_ACHIEVED, 100, "Get to the chopper!"),
    CLAYMORE_KILL_5(40, Stat.CLAYMORE_KILLS, 5, "Vandal"),
    CLAYMORE_KILL_10(41, Stat.CLAYMORE_KILLS, 10, "Bomber"),
    CLAYMORE_KILL_25(42, Stat.CLAYMORE_KILLS, 25, "Demolistionist"),
    CLAYMORE_KILL_50(43, Stat.CLAYMORE_KILLS, 50, "Eradicator"),
    CLAYMORE_KILL_100(44, Stat.CLAYMORE_KILLS, 100, "Bin Laden"),
    APPLE_KILL_5(45, Stat.INVULNERABLE_KILLS, 5, "Invulnerable"),
    APPLE_KILL_10(46, Stat.INVULNERABLE_KILLS, 10, "Steel skin"),
    APPLE_KILL_25(47, Stat.INVULNERABLE_KILLS, 25, "Can't touch this!"),
    APPLE_KILL_50(48, Stat.INVULNERABLE_KILLS, 50, "Diamond skin"),
    APPLE_KILL_100(49, Stat.INVULNERABLE_KILLS, 100, "Invincible"),
    DOG_KILL_5(50, Stat.DOG_KILLS, 5, "Man's best friend!"),
    DOG_KILL_10(51, Stat.DOG_KILLS, 10, "New tricks!"),
    DOG_KILL_25(52, Stat.DOG_KILLS, 25, "Giving the dog Steve's bones!"),
    DOG_KILL_50(53, Stat.DOG_KILLS, 50, "The dog's dinner!"),
    DOG_KILL_100(54, Stat.DOG_KILLS, 100, "His bite is worse than his bark!"),
    SENTRY_KILL_5(55, Stat.SENTRY_KILLS, 5, "Activating."),
    SENTRY_KILL_10(56, Stat.SENTRY_KILLS, 10, "Target acquired."),
    SENTRY_KILL_25(57, Stat.SENTRY_KILLS, 25, "Dispensing product!"),
    SENTRY_KILL_50(58, Stat.SENTRY_KILLS, 50, "Firing!"),
    SENTRY_KILL_100(59, Stat.SENTRY_KILLS, 100, "Sentry mode activated!"),
    CHOPPER_KILL_5(60, Stat.CHOPPER_KILLS, 5, "Enemy down!"),
    CHOPPER_KILL_10(61, Stat.CHOPPER_KILLS, 10, "Target neutralised."),
    CHOPPER_KILL_25(62, Stat.CHOPPER_KILLS, 25, "Mass murder"),
    CHOPPER_KILL_50(63, Stat.CHOPPER_KILLS, 50, "Overpowered!"),
    CHOPPER_KILL_100(64, Stat.CHOPPER_KILLS, 100, "Ragequit"),
    KILL_NOTCH(65, Stat.OTHER, -1, "Kill Notch");
    
    private final int id;
    private final Stat s;
    private final int c;
    private final int points;
    private final String name;
    
    Achievement(int id, Stat s, int c, String name, int points) {
        this.id = id;
        this.s = s;
        this.c = c;
        this.points = points;
        this.name = name;
    }
    
    Achievement(int id, Stat s, int c, String name) {
        this(id, s, c, name, 20);
    }

    public int getId() {
        return this.id;
    }
    
    public Stat getStat() {
        return this.s;
    }
    
    public int getCount() {
        return this.c;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static ArrayList<Achievement> table = new ArrayList<Achievement>(); 
    static {
        for (Achievement i : Achievement.values()) {
            table.add(i.getId(), i);
        }
    }

    public static Achievement valueOf(int id) {
        return table.get(id);
    }
}