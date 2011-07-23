package com.Top_Cat.CODMW.sql;

import java.util.ArrayList;

public enum Stat {
    OTHER(-1),
    POINTS(0),
    KILLS(1),
    DEATHS(2),
    WINS(3),
    LOSSES(4),
    LOGIN(5),
    ARROWS_FIRED(6),
    KNIFE_KILLS(7),
    BOW_KILLS(8),
    CLAYMORE_KILLS(9),
    DOG_KILLS(10),
    SENTRY_KILLS(11),
    CHOPPER_KILLS(12),
    KNIFE_DEATHS(13),
    BOW_DEATHS(14),
    CLAYMORE_DEATHS(15),
    DOG_DEATHS(16),
    SENTRY_DEATHS(17),
    CHOPPER_DEATHS(18),
    FALL_DEATHS(19),
    MAX_STREAK(20),
    CLAYMORES_USED(21),
    APPLES_USED(22),
    DOGS_USED(23),
    SENTRIES_PLACED(24),
    CHOPPERS_USED(25),
    AMMO_PICKED_UP(26),
    ASSISTS(27),
    LAST_DEATH(28),
    LAST_KILL(29),
    TOP_SCORE(30),
    MAX_POINTS(31),
    MAX_KILLS(32),
    MAX_DEATHS(33),
    SENTRIES_DESTROYED(34),
    CLAYMORES_ACHIEVED(35),
    APPLES_ACHIEVED(36),
    DOGS_ACHIEVED(37),
    SENTRIES_ACHIEVED(38),
    CHOPPERS_ACHIEVED(39),
    INVULNERABLE_KILLS(40),
    HEADSHOTS(41),
    FLAG_PICKUPS(42),
    FLAG_RETURNS(43),
    FLAG_CAPTURES(44),
    FURTHEST_KILL(45),
    FURTHEST_HEADSHOT(46),
    LIFE_REGENS(47),
    BLOCKS_MOVED(48),
    ITEMS_THROWN(49),
    FISH_KILLS(50),
    FISH_DEATHS(51),
    PORK_USED(52),
    PORK_ACHIEVED(53),
    GRENADE_KILLS(54),
    GRENADE_DEATHS(55),
    GRENADES_THROWN(56),
    UARROWS_USED(57),
    UARROWS_ACHIEVED(58),
    FALL_DAMAGE(59),
    CAREPACKAGES_ACHIEVED(60),
    CAREPACKAGES_USED(61),
    ;
    
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
            if (i.getId() >= 0) {
                table.add(i.getId(), i);
            }
        }
    }

    public static Stat valueOf(int id) {
        return table.get(id);
    }
}