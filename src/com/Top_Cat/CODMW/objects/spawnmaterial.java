package com.Top_Cat.CODMW.objects;

public class spawnmaterial {
    
    private int ammount, slot;
    
    public spawnmaterial(int a, int s) {
        ammount = a;
        slot = s;
    }
    
    public int getSlot() {
        return slot;
    }
    
    public int getAmmount() {
        return ammount;
    }
    
    public void setSlot(int s) {
        slot = s;
    }
    
    public void setAmmount(int a) {
        ammount = a;
    }
    
}