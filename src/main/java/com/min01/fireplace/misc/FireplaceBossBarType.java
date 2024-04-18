package com.min01.fireplace.misc;

public enum FireplaceBossBarType
{
	NONE(0, 0),
	KARAT_RAID(0, 5);
	
	public int yOffset;
	public int yOffset2;
	
	private FireplaceBossBarType(int yOffset, int yOffset2)
	{
		this.yOffset = yOffset;
		this.yOffset2 = yOffset2;
	}
}
