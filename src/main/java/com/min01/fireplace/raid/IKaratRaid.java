package com.min01.fireplace.raid;

import net.minecraft.core.BlockPos;

public interface IKaratRaid 
{
	public KaratRaid getRaidAt(BlockPos pos);
	
	public KaratRaidSaveData getRaids();
}
