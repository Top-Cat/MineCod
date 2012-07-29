package uk.co.thomasc.codmw;

import org.bukkit.ChatColor;

public enum Team {
	GOLD(ChatColor.GOLD),
	DIAMOND(ChatColor.AQUA),
	BOTH(ChatColor.WHITE);

	private final String colour;

	private Team(ChatColor colour) {
		this.colour = colour.toString();
	}

	public String getColour() {
		return this.colour;
	}
}