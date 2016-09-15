package org.generallib.nms.chunk;

public interface BlockFilter{
	public boolean allow(int blockID, byte data);
}