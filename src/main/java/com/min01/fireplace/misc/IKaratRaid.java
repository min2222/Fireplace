package com.min01.fireplace.misc;

import com.min01.fireplace.raid.KaratRaid;
import com.min01.fireplace.raid.KaratRaidSaveData;

import net.minecraft.core.BlockPos;

public interface IKaratRaid 
{
	public KaratRaid getKaratRaidAt(BlockPos pos);
	
	public KaratRaidSaveData getKaratRaids();
}
