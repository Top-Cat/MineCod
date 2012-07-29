package uk.co.thomasc.codmw.objects;

import org.bukkit.block.Block;

public class Door {
	
	Block b1, b2;
	
	public Door(Block base) {
		b1 = base;
		b2 = base.getRelative(0, 1, 0);
	}
	
	public void open() {
		if (b1.getData() == 4 || b1.getData() == 6) {
			b1.setData((byte) (b1.getData() - 4));
			b2.setData((byte) (b2.getData() - 4));
		} else if (b1.getData() == 1 || b1.getData() == 3) {
			b1.setData((byte) (b1.getData() + 4));
			b2.setData((byte) (b2.getData() + 4));
		}
	}
	
	public void close() {
		if (b1.getData() == 5 || b1.getData() == 7) {
			b1.setData((byte) (b1.getData() - 4));
			b2.setData((byte) (b2.getData() - 4));
		} else if (b1.getData() == 0 || b1.getData() == 2) {
			b1.setData((byte) (b1.getData() + 4));
			b2.setData((byte) (b2.getData() + 4));
		}
	}
	
}