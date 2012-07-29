package uk.co.thomasc.codmw.vote;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import uk.co.thomasc.codmw.Main;
import uk.co.thomasc.codmw.gamemodes.Gamemode;
import uk.co.thomasc.codmw.gamemodes.CTF;
import uk.co.thomasc.codmw.gamemodes.TDM;
import uk.co.thomasc.codmw.gamemodes.FFA;

public enum GameModes {
	CTF(CTF.class),
	TDM(TDM.class),
	FFA(FFA.class);
	
	private final Class<? extends Gamemode> k;
	
	private GameModes(Class<? extends Gamemode> k) {
		this.k = k;
	}
	
	public Gamemode createGame(Main instance) {
		try {
			Constructor<? extends Gamemode> c = k.getDeclaredConstructor(new Class[] {Main.class});
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