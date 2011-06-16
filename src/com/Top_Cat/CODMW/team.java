package com.Top_Cat.CODMW;

public enum team {
    GOLD("6"),
    DIAMOND("b"),
    BOTH("f");
    
	private final String colour;

    team(String c) {
        this.colour = c;
    }

    public String getColour() {
        return this.colour;
    }
}