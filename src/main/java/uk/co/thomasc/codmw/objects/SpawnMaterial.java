package uk.co.thomasc.codmw.objects;

public class SpawnMaterial {
	
	private int ammount, slot;
	
	public SpawnMaterial(int a, int s) {
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