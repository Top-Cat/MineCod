package uk.co.thomasc.codmw.perks;

import uk.co.thomasc.codmw.perks.tier1.Tier1;
import uk.co.thomasc.codmw.perks.tier2.Tier2;
import uk.co.thomasc.codmw.perks.tier3.Tier3;

public enum Tiers {
	TIER1(Tier1.class),
	TIER2(Tier2.class),
	TIER3(Tier3.class),
	;
	
	final Class<? extends Perk> k;
	
	private Tiers(Class<? extends Perk> k) {
		this.k = k;
	}
	
	public boolean instanceOf(Perk k2) {
		return k.isAssignableFrom(k2.getClass());
	}
}