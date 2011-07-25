package com.Top_Cat.CODMW.perks;

import com.Top_Cat.CODMW.perks.tier1.tier1;
import com.Top_Cat.CODMW.perks.tier2.tier2;
import com.Top_Cat.CODMW.perks.tier3.tier3;

public enum Tiers {
    TIER1(tier1.class),
    TIER2(tier2.class),
    TIER3(tier3.class),
    ;
    
    final Class<? extends perk> k;
    
    private Tiers(Class<? extends perk> k) {
        this.k = k;
    }
    
    public boolean instanceOf(perk k2) {
        return k.isAssignableFrom(k2.getClass());
    }
}